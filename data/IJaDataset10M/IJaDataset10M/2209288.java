package javax.security.auth.login;

/**
 * This is the base class for various credential-related exceptions.
 * @since 1.5
 */
public class CredentialException extends LoginException {

    private static final long serialVersionUID = -4772893876810601859L;

    /**
   * Create a new exception object.
   */
    public CredentialException() {
    }

    /**
   * Create a new exception with the given detail message.
   * @param message the detail message
   */
    public CredentialException(String message) {
        super(message);
    }
}
