package unclej.framework;

/**
 * Thrown when a target can not be found or constructed for any reason.
 * @author scottv
 */
public class TargetNotFoundException extends RuntimeException {

    public TargetNotFoundException(String message) {
        super(message);
    }

    public TargetNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
