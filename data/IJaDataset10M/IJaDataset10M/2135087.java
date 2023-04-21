package eu.popeye.middleware.usermanagement.exception;

public class BaseProfileNotInitializedException extends Exception {

    static final long serialVersionUID = 1;

    /**
	 * Creates a new instance of BaseProfileNotInitializedException using the default message.
	 */
    public BaseProfileNotInitializedException() {
        super("The BaseProfile was not initialized before it was attempted to use it!");
    }

    /**
	 * Creates a new instance of BaseProfileNotInitializedException using the given message.
	 */
    public BaseProfileNotInitializedException(String message) {
        super(message);
    }

    public BaseProfileNotInitializedException(Throwable cause) {
        super(cause);
    }

    public BaseProfileNotInitializedException(String message, Throwable cause) {
        super(message, cause);
    }
}
