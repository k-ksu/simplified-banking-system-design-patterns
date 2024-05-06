
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Locale;
import java.math.RoundingMode;
import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) throws Exception {
        BankingSystem bankingSystem = BankingSystem.getInstance();

        Scanner scanner = new Scanner(System.in);

        int numOperations = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        for (int i = 0; i < numOperations; i++) {
            String line = scanner.nextLine();
            String[] tokens = line.split("\\s+");

            String command = tokens[0];
            switch (command) {
                case "Create":
                    String accountType = tokens[2].substring(0);
                    String accountName = tokens[3];
                    float initialDeposit = Float.parseFloat(tokens[4].substring(0));
                    AccountType type = AccountType.valueOf(accountType.toUpperCase());
                    Account account = new Account(type, accountName, initialDeposit);
                    bankingSystem.addAccount(account);
                    account.makeTransaction(account, initialDeposit, TransactionType.INITIALDEPOSIT);
                    float value0 = initialDeposit;
                    String formattedValue0 = String.format(Locale.ENGLISH, "%.3f", value0);

                    System.out.print("A new " + accountType + " account created for " + accountName +
                            " with an initial balance of $" + formattedValue0);
                    System.out.println(".");
                    break;
                case "Deposit":
                    String depositAccountName = tokens[1].substring(0);
                    float depositAmount = Float.parseFloat(tokens[2].substring(0));
                    if (bankingSystem.doesExist(depositAccountName)) {
                        account = bankingSystem.getAccount(depositAccountName);
                        if (account.deposit(account, depositAmount)){
                            float value1 = depositAmount;
                            String formattedValue1 = String.format(Locale.ENGLISH, "%.3f", value1);

                            float value2 = bankingSystem.getAccount(depositAccountName).getAccountDeposit();
                            String formattedValue2 = String.format(Locale.ENGLISH, "%.3f", value2);

                            System.out.println(depositAccountName + " successfully deposited $" + formattedValue1 + ". New Balance: $" + formattedValue2 + ".");
                        }
                    }
                    else{
                        System.out.println("Error: Account " + depositAccountName + " does not exist.");
                    }
                    break;
                case "Withdraw":
                    String withdrawAccountName = tokens[1].substring(0);
                    float withdrawalAmount = Float.parseFloat(tokens[2].substring(0));
                    if (bankingSystem.doesExist(withdrawAccountName)) {
                        Account withdrawAccount = bankingSystem.getAccount(withdrawAccountName);
                        if (withdrawAccount.withdraw(withdrawAccount, withdrawalAmount)) {
                            float value3 = withdrawAccount.calculateTransactionFee(withdrawalAmount);
                            BigDecimal bd = new BigDecimal(value3).setScale(4, RoundingMode.HALF_UP);
                            float newNum3 = bd.floatValue();
                            String formattedValue3 = String.format(Locale.ENGLISH, "%.3f", newNum3);

                            float value4 = withdrawAccount.getAccountDeposit();
                            String formattedValue4 = String.format(Locale.ENGLISH, "%.3f", value4);


                            float value5 = withdrawalAmount - value3;
                            BigDecimal bd5 = new BigDecimal(value5).setScale(4, RoundingMode.HALF_UP);
                            float newNum5 = bd5.floatValue();
                            String formattedValue5 = String.format(Locale.ENGLISH, "%.3f", newNum5);

                            System.out.println(withdrawAccountName + " successfully withdrew $" + formattedValue3 + ". New Balance: $"
                                    + formattedValue4 + ". Transaction Fee: $" + formattedValue5 + " (" + withdrawAccount.getFee() * 100 + "%) in the system.");
                        }
                    }
                    else {
                        System.out.println("Error: Account " + withdrawAccountName + " does not exist.");
                    }
                    break;
                case "Transfer":
                    String fromAccountName = tokens[1].substring(0);
                    String toAccountName = tokens[2];
                    float transferAmount = Float.parseFloat(tokens[3].substring(0));
                    if (bankingSystem.doesExist(fromAccountName) && bankingSystem.doesExist(toAccountName)) {
                        Account fromAccount = bankingSystem.getAccount(fromAccountName);
                        Account toAccount = bankingSystem.getAccount(toAccountName);
                        if (fromAccount.transaction(toAccount, transferAmount)) {

                            float value6 = fromAccount.calculateTransactionFee(transferAmount);
                            String formattedValue6 = String.format(Locale.ENGLISH, "%.3f", value6);

                            float value7 = fromAccount.getAccountDeposit();
                            String formattedValue7 = String.format(Locale.ENGLISH, "%.3f", value7);


                            float value8 = transferAmount - value6;
                            BigDecimal bd = new BigDecimal(value8).setScale(4, RoundingMode.HALF_UP);
                            float newNum8 = bd.floatValue();
                            String formattedValue8 = String.format(Locale.ENGLISH, "%.3f", newNum8);

                            System.out.println(fromAccountName + " successfully transferred $" + formattedValue6
                                    + " to " + toAccountName + ". New Balance: $" + formattedValue7 +
                                    ". Transaction Fee: $" + formattedValue8 + " (" + fromAccount.getFee() * 100 + "%) in the system.");
                        }
                    } else {
                        if (!bankingSystem.doesExist(fromAccountName) && !bankingSystem.doesExist(toAccountName)){
                            System.out.println("Error: Account " + fromAccountName + " does not exist.");
                        }

                        else{
                            if (!bankingSystem.doesExist(fromAccountName)) {
                                System.out.println("Error: Account " + fromAccountName + " does not exist.");
                            }

                            else if (!bankingSystem.doesExist(toAccountName)) {
                                System.out.println("Error: Account " + toAccountName + " does not exist.");
                            }
                        }
                    }
                    break;
                case "View":
                    String viewAccountName = tokens[1].substring(0);
                    if (bankingSystem.doesExist(viewAccountName)) {

                        Account viewAccount = bankingSystem.getAccount(viewAccountName);
                        viewAccount.showDetails();                    }
                    else{
                        System.out.println("Error: Account " + viewAccountName + " does not exist.");
                    }

                    break;
                case "Deactivate":
                    String deactivateAccountName = tokens[1].substring(0);
                    if (bankingSystem.doesExist(deactivateAccountName)) {

                        Account deactivateAccount = bankingSystem.getAccount(deactivateAccountName);
                        deactivateAccount.deactivate();                   }
                    else{
                        System.out.println("Error: Account " + deactivateAccountName + " does not exist.");
                    }
                    break;
                case "Activate":
                    String activateAccountName = tokens[1].substring(0);
                    if (bankingSystem.doesExist(activateAccountName)) {

                        Account activateAccount = bankingSystem.getAccount(activateAccountName);
                        activateAccount.activate();
                    }
                    else{
                        System.out.println("Error: Account " + activateAccountName + " does not exist.");
                    }
                    break;
                default:
                    System.out.println("Invalid command:" + command);
            }
        }
        scanner.close();
//        part of code to trigger the overdraft protection feature, just in case of necessity to demonstrate
//        // Create a new account with initial deposit of $1000
//        Account checkingAccount = new Account(AccountType.CHECKING, "John's Checking", 1000);
//
//        // Apply overdraft protection feature to the account
//        OverdraftProtection overdraftProtection = new OverdraftProtection();
//        overdraftProtection.applyFeature(checkingAccount);
//
//        // Attempt to withdraw an amount greater than the available balance
//        float withdrawalAmount = 1500;
//        System.out.println("Attempting to withdraw $" + withdrawalAmount + " from " + checkingAccount.getAccountName() + ":");
//        checkingAccount.withdraw(null, withdrawalAmount); // Pass null for toAccount since it's not relevant for this scenario
//
//        // Check the account details to see the result
//        checkingAccount.showDetails();
    }
}


// Define the AccountType enum
enum AccountType {
    SAVINGS {
        @Override
        public String toString() {
            return "Savings";
        }
    },
    CHECKING {
        @Override
        public String toString() {
            return "Checking";
        }
    },
    BUSINESS {
        @Override
        public String toString() {
            return "Business";
        }
    };
}

enum TransactionType {
    TRANSFER, WITHDRAW, DEPOSIT, INITIALDEPOSIT
}

// Define the Transaction class
class Transaction {
    TransactionType transactionType;
    Account fromAccount;
    Account toAccount;
    float transferAmount;

    public Transaction(Account fromAccount, Account toAccount, float transferAmount, TransactionType transactionType) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.transferAmount = transferAmount;
        this.transactionType = transactionType;
    }
}

// Define the TransactionFeeCalculator interface
interface TransactionFeeCalculator {
    float FEE_RATE = 0.0f; // Default value, overridden in subclasses

    float calculateTransactionFee(float amount);
    float getFee();
}

// Concrete Strategy for Savings Account
class SavingsAccountFeeCalculator implements TransactionFeeCalculator {

    public static final float FEE_RATE = 0.015f;
    @Override
    public float calculateTransactionFee(float amount) {
        return amount * (1 - FEE_RATE);
    }

    @Override
    public float getFee(){
        return FEE_RATE;
    }
}

// Concrete Strategy for Checking Account
class CheckingAccountFeeCalculator implements TransactionFeeCalculator {
    public static final float FEE_RATE = 0.02f;

    @Override
    public float calculateTransactionFee(float amount) {
        return amount * (1 - FEE_RATE);
    }

    @Override
    public float getFee(){
        return FEE_RATE;
    }
}

// Concrete Strategy for Business Account
class BusinessAccountFeeCalculator implements TransactionFeeCalculator {
    public static final float FEE_RATE = 0.025f;

    @Override
    public float calculateTransactionFee(float amount) {
        return amount * (1 - FEE_RATE);
    }

    @Override
    public float getFee(){
        return FEE_RATE;
    }
}

// Define the State interface
interface State {

    boolean deposit(Account account, Account toAccount, float amount);

    boolean withdraw(Account account, Account toAccount, float amount);
    boolean transaction(Account account, Account toAccount, float amount);
    boolean activate(Account account);
    boolean deactivate(Account account);
    String getStateString();

    boolean overdraft(Account account, double amount);

}

// Concrete State for Active Account
class ActiveState implements State {
    @Override
    public boolean deposit(Account account, Account toAccount, float amount) {
        account.makeTransaction(toAccount, amount, TransactionType.DEPOSIT);
        return true;
    }

    @Override
    public boolean withdraw(Account account, Account toAccount, float amount) {
        if (account.getAccountDeposit() >= amount) {
            account.makeTransaction(toAccount, amount, TransactionType.WITHDRAW);
            return true;
        } else {
            System.out.println("Error: Insufficient funds for " + account.getAccountName() + ".");
            return false;
        }
    }


    public boolean transaction(Account account, Account toAccount, float amount) {
        if (account.getAccountDeposit() >= amount) {
            account.makeTransaction(toAccount, amount, TransactionType.TRANSFER);
            return true;
        } else {
            System.out.println("Error: Insufficient funds for " + account.getAccountName() + ".");
        }
        return false;
    }

    @Override
    public boolean activate(Account account) {
        System.out.println("Error: Account " + account.getAccountName() + " is already activated.");
        return false;
    }

    @Override
    public boolean deactivate(Account account) {
        account.setState(new InactiveState());
        System.out.println(account.getAccountName() + "'s account is now deactivated.");
        return true;
    }

    @Override
    public String getStateString() {
        return "Active";
    }

    @Override
    public boolean overdraft(Account account, double amount) {
        // Active accounts do not allow overdraft, so return false
        return false;
    }
}

// Concrete State for Inactive Account
// Concrete State for Active Account
class InactiveState implements State {
    @Override
    public boolean deposit(Account account, Account toAccount, float amount) {
        account.makeTransaction(toAccount, amount, TransactionType.DEPOSIT);
        return true;
    }

    @Override
    public boolean withdraw(Account account, Account toAccount, float amount) {
        System.out.println("Error: Account " + account.getAccountName() + " is inactive.");
        return false;
    }


    public boolean transaction(Account account, Account toAccount, float amount) {
        System.out.println("Error: Account " + account.getAccountName() + " is inactive.");
        return false;
    }


    @Override
    public boolean activate(Account account) {
        account.setState(new ActiveState());
        System.out.println(account.getAccountName() + "'s account is now activated.");
        return true;
    }

    @Override
    public boolean deactivate(Account account) {
        System.out.println("Error: Account " + account.getAccountName() + " is already deactivated.");
        return false;
    }

    @Override
    public String getStateString() {
        return "Inactive";
    }

    @Override
    public boolean overdraft(Account account, double amount) {
        // Implement overdraft handling logic for inactive accounts
        System.out.println("Error: Account " + account.getAccountName() + " cannot be overdrawn while inactive.");
        return false;
    }

}

// Concrete State for Overdrawn Account
abstract class OverdrawnState implements State {
    @Override
    public boolean deposit(Account account, Account toAccount, float amount) {
        account.makeTransaction(toAccount, amount, TransactionType.DEPOSIT);
        return true;
    }

    @Override
    public boolean withdraw(Account account, Account toAccount, float amount) {
        return false;
    }


    public boolean transaction(Account account, Account toAccount, float amount) {
        return false;
    }


    @Override
    public boolean activate(Account account) {
        System.out.println("");
        return false;
    }

    @Override
    public boolean deactivate(Account account) {
        account.setState(new InactiveState());
        return true;
    }

    @Override
    public String getStateString() {
        return "Overdrawn";
    }
}

// Define the AccountFeature interface
interface AccountFeature {
    void applyFeature(Account account);
    void removeFeature(Account account);
}

class OverdraftProtection implements AccountFeature {
    @Override
    public void applyFeature(Account account) {
        // Apply overdraft protection by setting the account state to Overdrawn
        account.setState(new OverdrawnState() {
            @Override
            public boolean overdraft(Account account, double amount) {
                if (account.getAccountDeposit() >= amount) {
                    System.out.println("Overdraft protection triggered for account " + account.getAccountName() + ".");
                    return true;
                } else {
                    System.out.println("Error: Account " + account.getAccountName() + " cannot be overdrawn.");
                    return false;
                }
            }

        });
    }

    @Override
    public void removeFeature(Account account) {
        // Remove overdraft protection by setting the account state back to Active or Inactive
        if (account.getInitialDeposit() > 0) {
            account.setState(new ActiveState());
        } else {
            account.setState(new InactiveState());
        }
    }
}

// Account class with Strategy pattern integration
class Account {
    private AccountType accountType;
    private String accountName;

    private float initialDeposit;
    private List<Transaction> history;
    private State state;
    private TransactionFeeCalculator feeCalculator;
    private List<AccountFeature> features = new ArrayList<>();

    public Account(AccountType accountType, String accountName, float initialDeposit) {
        this.accountType = accountType;
        this.accountName = accountName;
        this.initialDeposit = initialDeposit;
        this.history = new ArrayList<>();
        this.state = new ActiveState();
        this.feeCalculator = createFeeCalculator(accountType);
    }

    // Factory method to create a transaction fee calculator based on account type
    private TransactionFeeCalculator createFeeCalculator(AccountType accountType) {
        switch (accountType) {
            case SAVINGS:
                return new SavingsAccountFeeCalculator();
            case CHECKING:
                return new CheckingAccountFeeCalculator();
            case BUSINESS:
                return new BusinessAccountFeeCalculator();
            default:
                throw new IllegalArgumentException("Unsupported account type");
        }
    }

    public void addFeature(AccountFeature feature) {
        features.add(feature);
        feature.applyFeature(this);
    }

    public void removeFeature(AccountFeature feature) {
        features.remove(feature);
        feature.removeFeature(this);
    }

    // Calculate transaction fee using the current strategy
    public float calculateTransactionFee(float amount) {
        return feeCalculator.calculateTransactionFee

                (amount);
    }

    public float getFee() {
        return feeCalculator.getFee();
    }

    // Methods for deposit and withdraw operations

    public float getAccountDeposit() {
        return initialDeposit;
    }

    // Method to set the state dynamically


    public void makeTransaction(Account toAccount, float amount, TransactionType transactionType) {
        Transaction currentTransaction = new Transaction(this, toAccount, amount, transactionType);
        float finalAmount = this.calculateTransactionFee(amount);
        switch (transactionType) {
            case TRANSFER:
                this.initialDeposit -= amount;
                this.history.add(currentTransaction);
                toAccount.initialDeposit += finalAmount;
                break;
            // More case statements as needed
            case WITHDRAW:
                this.initialDeposit -= amount;
                this.history.add(currentTransaction);
                break;
            case DEPOSIT:
                this.initialDeposit += amount;
                //this.history.add(currentTransaction);
            case INITIALDEPOSIT:
                this.history.add(currentTransaction);
        }
    }

    public String showHistory() {
        System.out.print("[");
        float value10 = this.history.get(0).transferAmount;
        String formattedValue10 = String.format(Locale.ENGLISH, "%.3f", value10);
        switch (this.history.get(0).transactionType) {
            case INITIALDEPOSIT:
                System.out.printf("Initial Deposit $" + formattedValue10);
                break;
            case TRANSFER:
                System.out.printf("Transfer $" + formattedValue10);
                break;
            // More case statements as needed
            case WITHDRAW:
                System.out.printf("Withdrawal $" + formattedValue10);
                break;
            case DEPOSIT:
                System.out.printf("Deposit $" + formattedValue10);
        }

        int k = this.history.size();
        for (int i = 1; i < k; i++) {
            float value = this.history.get(i).transferAmount;
            String formattedValue = String.format(Locale.ENGLISH, "%.3f", value);
            switch (this.history.get(i).transactionType) {
                case INITIALDEPOSIT:
                    System.out.printf(", Initial Deposit $" + formattedValue);
                    break;
                case TRANSFER:
                    System.out.printf(", Transfer $" + formattedValue);
                    break;
                // More case statements as needed
                case WITHDRAW:
                    System.out.printf(", Withdrawal $" + formattedValue);
                    break;
                case DEPOSIT:
                    System.out.printf(", Deposit $" + formattedValue);
            }
        }
        System.out.println("].");
        return "";
    }

    public void showDetails() {

        String formattedValue = String.format(Locale.ENGLISH, "%.3f", this.initialDeposit);
        System.out.print(this.accountName + "'s Account: Type: " + this.accountType + ", Balance: $" +
                formattedValue + ", State: " + this.getState().getStateString() + ", Transactions: ");
        showHistory();
    }

    public boolean deposit(Account toAccount, float amount) {
        return state.deposit(this, toAccount, amount);
    }

    public boolean withdraw(Account toAccount, float amount) {
        return state.withdraw(this, toAccount, amount);
    }

    public boolean transaction(Account toAccount, float amount) {
        return state.transaction(this, toAccount, amount);
    }

    public boolean activate() {
        return state.activate(this);
    }

    public boolean deactivate() {
        return state.deactivate(this);
    }

    // Getters and setters
    public String getAccountName() {
        return accountName;
    }

    public float getInitialDeposit() {
        return initialDeposit;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }
}



class BankingSystem {
    // Singleton instance
    private static BankingSystem instance;

    // Map to store accounts by account name
    List<Account> accounts = new ArrayList<>();

    // Private constructor to prevent instantiation
    public BankingSystem() {
        accounts = new ArrayList<>();
    }

    // Method to get the singleton instance
    public static synchronized BankingSystem getInstance() {
        if (instance == null) {
            instance = new BankingSystem();
        }
        return instance;
    }

    // Method to add an account to the banking system
    public void addAccount(Account account) {
        accounts.add(account);
    }

    // Method to get accounts by account name
    public Account getAccount(String accountName) {
        for (Account account : accounts){
            if (account.getAccountName().equals(accountName)){
                return account;
            }
        }
        System.out.println("Error: Account " + accountName + " does not exist");
        return null;
    }

    public boolean doesExist(String accountName) {
        for (Account account : accounts){
            if (account.getAccountName().equals(accountName)){
                return true;
            }
        }
        return false;
    }
}