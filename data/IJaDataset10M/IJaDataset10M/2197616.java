package net.sourceforge.smartconversion.api.exception;

/**
 * Thrown when a type is not valid in some context (e.g. needs to be complex or primitive but 
 * it's the opposite).
 *
 * @author Ovidiu Dolha
 */
public class InvalidTypeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidTypeException() {
        super();
    }

    public InvalidTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTypeException(String message) {
        super(message);
    }

    public InvalidTypeException(Throwable cause) {
        super(cause);
    }
}
