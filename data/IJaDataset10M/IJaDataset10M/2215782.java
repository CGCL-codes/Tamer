package hla.rti1516e.exceptions;

/**
 * Public exception class ConnectionFailed.
 */
public final class ConnectionFailed extends RTIexception {

    public ConnectionFailed(String msg) {
        super(msg);
    }

    public ConnectionFailed(String message, Throwable cause) {
        super(message, cause);
    }
}
