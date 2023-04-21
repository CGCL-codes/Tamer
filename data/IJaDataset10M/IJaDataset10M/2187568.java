package integrationtestclasses.bankaccount;

/**
 *  @author Per Otto Bergum Christensen
 */
public class BankAccount {

    private int balance;

    public BankAccount(int initialBalance) {
        this.balance = initialBalance;
    }

    public int withdraw(int amount) {
        if (amount < 0) {
            throw new IllegalStateException("amount cannot be negative");
        }
        int newBalance = balance - amount;
        if (newBalance < 0) {
            throw new IllegalStateException("Account cannot be overdrawn");
        }
        balance = newBalance;
        return amount;
    }

    public int balance() {
        return balance;
    }

    public void deposit(int amount) {
        if (amount < 0) {
            throw new IllegalStateException("amount cannot be negative");
        }
        balance = balance + amount;
    }
}
