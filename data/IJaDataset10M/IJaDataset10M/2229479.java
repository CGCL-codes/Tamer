package neembuu;

/**
 *
 * @author Shashank Tulsyan
 */
public class LoginException extends Exception {

    private Account account;

    public LoginException() {
        this(null);
    }

    public LoginException(String message) {
        this(message, null);
    }

    public LoginException(String message, Account account) {
        super(message);
        this.account = account;
    }
}
