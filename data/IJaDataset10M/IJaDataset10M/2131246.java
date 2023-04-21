package javax.security.auth.login;

/**
 * An exception that signals that an attempt was made to login to an account
 * that has expired.
 */
public class AccountExpiredException extends AccountException {

    private static final long serialVersionUID = -6064064890162661560L;

    public AccountExpiredException() {
    }

    public AccountExpiredException(String message) {
        super(message);
    }
}
