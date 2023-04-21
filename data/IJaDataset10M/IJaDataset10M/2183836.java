package net.sourceforge.etsysync.utils.event.exceptions.unchecked;

public class InvalidEtsyApiUrlException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidEtsyApiUrlException(Throwable cause) {
        super(cause);
    }

    public InvalidEtsyApiUrlException(String message, Throwable cause) {
        super(message, cause);
    }
}
