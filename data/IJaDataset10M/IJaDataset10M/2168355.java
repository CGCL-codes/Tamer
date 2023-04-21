package apjava.bank;

/**
 * Exception for if an account does not have enough in it to withdraw or
 * transfer the specified amount.
 * @author Ben Cohen
 *
 */
public class InsufficientFundsException extends BankException {

    private static final long serialVersionUID = 1L;

    private BankAccount account;

    private double amount;

    /**
	 * Construct the exception with a message, the account with insufficient
	 * funds, and the amount.
	 * @param message the message
	 * @param account the account that caused the exception
	 * @param amount the amount of the shortage
	 */
    public InsufficientFundsException(String message, BankAccount account, double amount) {
        super(message);
        this.account = account;
        this.amount = amount;
    }

    /**
	 * @return the account that caused the exception
	 */
    public BankAccount getAccount() {
        return account;
    }

    /**
	 * @param account the account to set
	 */
    public void setAccount(BankAccount account) {
        this.account = account;
    }

    /**
	 * @return the amount of the shortage
	 */
    public double getAmount() {
        return amount;
    }

    /**
	 * @param amount the amount of the shortage
	 */
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
