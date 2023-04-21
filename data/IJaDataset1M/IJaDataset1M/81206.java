package net.sourceforge.etsysync.utils.event.exceptions.unchecked;

public class GeneralOAuthException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public GeneralOAuthException(String message) {
        super(message);
    }

    public GeneralOAuthException(String message, Exception exception) {
        super(message, exception);
    }
}
