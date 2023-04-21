package net.sourceforge.happybank.exception;

/**
 * Exception thrown when an invalid account number is entered.
 *
 * @author Kevin A. Lee
 * @email kevin.lee@buildmeister.com
 */
public class AccountDoesNotExistException extends BankException {

    /**
     * Generated serialization identifier.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     *
     * @param s
     *            title of the exception
     */
    public AccountDoesNotExistException(final String s) {
        super(s);
    }

    /**
     * Return the key of the exception.
     *
     * @return string containing the exception key
     */
    public final String getMessageKey() {
        return "error.accountDoesNotExistException";
    }
}
