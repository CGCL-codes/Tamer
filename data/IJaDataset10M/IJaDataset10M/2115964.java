package net.sf.evemsp.io;

/**
 * Thrown when there is a problem processing a support call.
 * 
 * @author Jaabaa
 */
public class EveApiSupportException extends Exception {

    private Exception cause;

    public EveApiSupportException(Exception cause) {
        super(cause.toString());
        this.cause = cause;
    }

    public EveApiSupportException(String message) {
        super(message);
    }

    public Exception getCause() {
        return cause;
    }

    public void printStackTrace() {
        if (cause != null) {
            cause.printStackTrace();
            return;
        }
        super.printStackTrace();
    }

    public String getMessage() {
        if (cause != null) return cause.getMessage();
        return super.getMessage();
    }

    public String toString() {
        if (cause != null) return cause.toString();
        return super.toString();
    }
}
