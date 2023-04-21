package apjava.bank;

import java.util.*;

/**
 * A bank. Can hold accounts and have customer interactions.
 * @author Ben Cohen
 *
 */
public class Bank {

    private String name;

    private double balance;

    private long transactions;

    private Month currentMonth;

    /**
	 * Fee Types
	 * @author Ben Cohen
	 *
	 */
    public enum Fee {

        CHECK, TRANS, SAVINGS
    }

    private TreeMap<Fee, Integer> fees;

    private double interest;

    private String options;

    private TreeMap<Customer, BankAccount> accounts;

    private Scanner sc;

    /**
	 * Create a bank
	 * @param name the bank name
	 * @param transactionFee the transaction fee
	 * @param checkFee the check fee
	 * @param interestRate the interest rate
	 * @param sc the scanner to use
	 */
    public Bank(String name, int transactionFee, int checkFee, double interestRate, Scanner sc) {
        this.name = name;
        this.balance = 0;
        this.transactions = 0;
        this.currentMonth = new Month();
        fees = new TreeMap<Fee, Integer>();
        fees.put(Fee.CHECK, checkFee);
        fees.put(Fee.TRANS, transactionFee);
        fees.put(Fee.SAVINGS, transactionFee);
        this.interest = interestRate;
        accounts = new TreeMap<Customer, BankAccount>();
        this.sc = sc;
    }

    /**
	 * Visit a bank, with options to help, open an account, report on the bank, perform customer interactions,
	 * show the trans fee, check fee, and interest rate, and move to a new month.
	 */
    public void visit() {
        BankAccount a;
        System.out.println("Welcome to " + name);
        while (true) {
            a = null;
            System.out.println("Choose an option:");
            System.out.print("help open report customer ");
            System.out.println("transactionFee checkFee interestRate newMonth exit");
            options = sc.next().toLowerCase();
            System.out.println(options);
            if (options.equals("help")) {
                System.out.print("help open report customer ");
                System.out.println("transactionFee checkFee interestRate newMonth exit");
            } else if (options.equals("open")) {
                openNewAccount();
            } else if (options.equals("report")) {
                report();
            } else if (options.equals("customer")) {
                a = whichAccount(null);
                if (a != null) processTransactionsForAccount(a);
            } else if (options.equals("transactionfee")) {
                System.out.println(options + " " + fees.get(Fee.TRANS));
            } else if (options.equals("checkfee")) {
                System.out.println(options + " " + fees.get(Fee.CHECK));
            } else if (options.equals("interestrate")) {
                System.out.println(options + " " + interest);
            } else if (options.equals("newmonth")) {
                newMonth();
            } else if (options.equals("exit")) {
                report();
                break;
            }
        }
    }

    /**
	 * Prompts user for data to make a new account of type fee, checking, or saving, prompts for
	 * name and initial value.
	 */
    private void openNewAccount() {
        Customer c;
        BankAccount b;
        int iv;
        System.out.println("What is the customer's name?");
        c = new Customer(sc.next());
        System.out.println(c);
        System.out.println("What type of account?");
        System.out.println("[F]ee, [C]hecking, [S]avings");
        options = sc.next();
        System.out.println(options);
        System.out.println("What initial value?");
        try {
            if (options.toLowerCase().startsWith("f")) {
                iv = sc.nextInt();
                b = new FeeAccount(iv, this, c);
                System.out.println(iv);
            } else if (options.toLowerCase().startsWith("c")) {
                iv = sc.nextInt();
                b = new CheckingAccount(iv, this, c);
                System.out.println(iv);
            } else if (options.toLowerCase().startsWith("s")) {
                iv = sc.nextInt();
                b = new SavingsAccount(iv, this, c);
                System.out.println(iv);
            } else {
                System.out.println("Not a valid account option");
                System.out.println("Account creation failed");
                return;
            }
        } catch (InvalidAmountException e) {
            System.out.println(e);
            System.out.println("Account creation failed");
            return;
        }
        accounts.put(c, b);
    }

    /**
	 * Process customer transactions: help, deposit, withdraw, transfer, balance, cash a check, or quit.
	 * @param acct the account to perform the actions on
	 */
    private void processTransactionsForAccount(BankAccount acct) {
        BankAccount acct2;
        int tv;
        while (true) {
            System.out.println("What kind of transaction would you like to process?");
            System.out.print("[h]elp [d]eposit [w]ithdraw [t]ransfer [b]alance ");
            if (acct instanceof CheckingAccount) System.out.print("[c]ash check ");
            System.out.println("[q]uit");
            options = sc.next();
            System.out.println(options);
            switch(options.toCharArray()[0]) {
                case 'd':
                    System.out.println("How much?");
                    if (sc.hasNextInt()) try {
                        tv = sc.nextInt();
                        acct.deposit(tv);
                        System.out.println(tv);
                    } catch (InvalidAmountException e) {
                        System.out.println(e.getMessage());
                    } catch (InsufficientFundsException e) {
                        System.out.println("Insufficient funds to process deposit fee");
                    }
                    ;
                    break;
                case 'w':
                    System.out.println("How much?");
                    if (sc.hasNextInt()) try {
                        tv = sc.nextInt();
                        acct.withdraw(tv);
                        System.out.println(tv);
                    } catch (BankException e) {
                        System.out.println(e);
                    }
                    ;
                    break;
                case 't':
                    acct2 = whichAccount(acct);
                    if (acct2 == null) break;
                    System.out.println("How much?");
                    int amount = sc.nextInt();
                    System.out.println(amount);
                    try {
                        acct.withdraw(amount);
                    } catch (InvalidAmountException e) {
                        System.out.println(e);
                        break;
                    } catch (InsufficientFundsException e) {
                        System.out.println(e);
                        break;
                    }
                    try {
                        acct2.incrementBalance(amount);
                    } catch (InsufficientFundsException e) {
                    }
                    break;
                case 'b':
                    try {
                        System.out.println("Balance is: " + acct.requestBalance());
                    } catch (InsufficientFundsException e) {
                        System.out.println("Insufficient funds to cover balance checking fee");
                    }
                    break;
                case 'c':
                    if (acct instanceof CheckingAccount) {
                        System.out.println("How much?");
                        if (sc.hasNextInt()) try {
                            tv = sc.nextInt();
                            ((CheckingAccount) acct).honorCheck(tv);
                            System.out.println(tv);
                        } catch (BankException e) {
                            System.out.println(e.getMessage());
                        }
                        ;
                    } else {
                        System.out.println("Account 2 is not a checking account");
                    }
                    break;
                case 'q':
                    return;
                case 'h':
                default:
            }
        }
    }

    /**
	 * Prompts the user to choose an account.
	 * @param acct account to exclude from the selection ie for transfers that can't among the same account
	 * @return the account selected
	 */
    private BankAccount whichAccount(BankAccount acct) {
        System.out.println("What is the name on the account?");
        options = sc.next();
        System.out.println(options);
        for (Customer c : accounts.keySet()) {
            if (c.getName().toLowerCase().equals(options) && accounts.get(c) != acct) {
                System.out.println("Customer " + c + "? [y/N]");
                options = sc.next();
                System.out.println(options);
                if (options.toLowerCase().equals("y")) return accounts.get(c);
            }
        }
        System.out.println("Customer not found.");
        return null;
    }

    /**
	 * Moves to a new month. Moves each account, then changes the bank's month property
	 */
    private void newMonth() {
        for (BankAccount a : accounts.values()) a.newMonth();
        currentMonth.next();
    }

    /**
	 * Print each account, its status, and history, then the bank data.
	 */
    private void report() {
        for (BankAccount a : accounts.values()) {
            System.out.println(a);
            a.printHistory();
        }
        System.out.println("Bank Totals");
        System.out.println("Name: " + name);
        System.out.println("Balance: " + balance);
        System.out.println("Transactions: " + transactions);
        System.out.println("Accounts: " + accounts.size());
        System.out.println("Current Month: " + currentMonth);
    }

    /**
	 * Change the balance of the bank.
	 * @param amount amount to change the balance
	 * @param a the account that called the function
	 */
    public void incrementBalance(double amount, BankAccount a) {
        if (accounts.values().contains(a)) this.balance += amount;
    }

    /**
	 * @return the transFee
	 */
    public int getTransFee() {
        return fees.get(Fee.TRANS);
    }

    /**
	 * @param transFee the transFee to set
	 */
    public void setTransFee(int transFee) {
        fees.put(Fee.TRANS, transFee);
    }

    /**
	 * @return the checkFee
	 */
    public int getCheckFee() {
        return fees.get(Fee.CHECK);
    }

    /**
	 * @param checkFee the checkFee to set
	 */
    public void setCheckFee(int checkFee) {
        fees.put(Fee.CHECK, checkFee);
    }

    /**
	 * @return the savings fee
	 */
    public int getSavingsFee() {
        return fees.get(Fee.SAVINGS);
    }

    /**
	 * @param checkFee the savings fee to set
	 */
    public void setSavingsFee(int checkFee) {
        fees.put(Fee.SAVINGS, checkFee);
    }

    /**
	 * @return the interest
	 */
    public double getInterest() {
        return interest;
    }

    /**
	 * @param interest the interest to set
	 */
    public void setInterest(double interest) {
        this.interest = interest;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return the fees
	 */
    public TreeMap<Fee, Integer> getFees() {
        return fees;
    }

    /**
	 * @param fees the fees to set
	 */
    public void setFees(TreeMap<Fee, Integer> fees) {
        this.fees = fees;
    }

    /**
	 * @return the balance
	 */
    public double getBalance() {
        return balance;
    }

    /**
	 * @return the transactions
	 */
    public long getTransactions() {
        return transactions;
    }

    /**
	 * @return the currentMonth
	 */
    public Month getCurrentMonth() {
        return currentMonth;
    }

    /**
	 * @return the sc
	 */
    public Scanner getScanner() {
        return sc;
    }

    /**
	 * @param sc the sc to set
	 */
    public void setScanner(Scanner sc) {
        this.sc = sc;
    }

    public static void main(String[] args) throws java.io.FileNotFoundException {
        Bank b = new Bank("Bank", 5, 5, .02, new Scanner(new java.io.File(args[0] + " " + args[1])));
        b.visit();
        b.setScanner(new Scanner(System.in));
        b.visit();
    }
}
