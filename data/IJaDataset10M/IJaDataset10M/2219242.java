package free.jin;

/**
 * The exception thrown when login fails.
 */
public class LoginException extends Exception {

    /**
   * Creates a new <code>LoginException</code>.
   */
    public LoginException() {
    }

    /**
   * Creates a new <code>LoginException</code> with the specified error message.
   */
    public LoginException(String message) {
        super(message);
    }
}
