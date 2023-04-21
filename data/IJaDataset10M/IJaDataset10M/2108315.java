package jfun.yan;

/**
 * Represents any exception that's used internally by the framework
 * and should not be wrapped.
 * <p>
 * @author Ben Yu
 * Dec 13, 2005 5:54:50 PM
 */
public class InternalException extends YanException {

    public InternalException() {
    }

    public InternalException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalException(String arg0) {
        super(arg0);
    }

    public InternalException(Throwable cause) {
        super(cause);
    }
}
