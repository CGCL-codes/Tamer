package gumbo.core.exception;

/**
 * Thrown when a resource (system or user) is found but cannot be accessed, such
 * as a file that cannot be read or written.
 * @author jonb
 */
public class ResourceAccessException extends RuntimeException {

    public ResourceAccessException() {
        super();
    }

    public ResourceAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceAccessException(String message) {
        super(message);
    }

    public ResourceAccessException(Throwable cause) {
        super(cause);
    }

    /**
	 * Gets the default message for this exception.
	 * @return The message.  Never null.
	 */
    public static String getDefaultMessage() {
        return "Resource was found but is not accessible.";
    }

    private static final long serialVersionUID = 1L;
}
