package net.sourceforge.cbmi;

public class CBMIException extends Exception {

    public CBMIException(String message, Throwable cause) {
        super(message, cause);
    }

    public CBMIException(String message) {
        super(message);
    }

    public CBMIException(Throwable cause) {
        super(cause);
    }
}
