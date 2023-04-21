package au.edu.jcu.haldbus.exceptions;

/**
 * This type of exception is internal to the HAL java package.
 * 
 * @author gilles
 */
public class InvalidMethodCall extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidMethodCall() {
        super();
    }

    public InvalidMethodCall(String message) {
        super(message);
    }

    public InvalidMethodCall(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMethodCall(Throwable cause) {
        super(cause);
    }
}
