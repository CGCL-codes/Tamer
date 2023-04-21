package hla.rti1516e.exceptions;

/**
 * Public exception class InvalidInteractionClassHandle
 */
public final class InvalidInteractionClassHandle extends RTIexception {

    public InvalidInteractionClassHandle(String msg) {
        super(msg);
    }

    public InvalidInteractionClassHandle(String message, Throwable cause) {
        super(message, cause);
    }
}
