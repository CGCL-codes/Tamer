package hla.rti1516e.exceptions;

/**
 * Public exception class CallNotAllowedFromWithinCallback.
 */
public final class CallNotAllowedFromWithinCallback extends RTIexception {

    public CallNotAllowedFromWithinCallback(String msg) {
        super(msg);
    }

    public CallNotAllowedFromWithinCallback(String message, Throwable cause) {
        super(message, cause);
    }
}
