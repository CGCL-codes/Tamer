package hla.rti1516e.exceptions;

/**
 * Public exception class ErrorReadingMIM
 */
public final class ErrorReadingMIM extends RTIexception {

    public ErrorReadingMIM(String msg) {
        super(msg);
    }

    public ErrorReadingMIM(String message, Throwable cause) {
        super(message, cause);
    }
}
