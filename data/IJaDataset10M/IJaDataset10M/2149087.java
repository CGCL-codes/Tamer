package entity;

/**
 * <p>
 * Signal that a password has expired and needs to be changed before principal
 * can be authorized for any other operation.
 * </p>
 */
public class ExpiredPasswordException extends Exception {

    /**
     * <p>
     * Instantiate a new <code>ExpiredPasswordException</code>, utilizing the
     * specified username.
     * </p>
     * 
     * @param username
     *            Username whose password has expired
     */
    public ExpiredPasswordException(String username) {
        super("Password for " + username + " has expired.");
    }

    /**
     * <p>
     * Instantiate a new <code>ExpiredPasswordException</code>.
     * </p>
     * 
     */
    public ExpiredPasswordException() {
        super("Your password has expired.");
    }
}
