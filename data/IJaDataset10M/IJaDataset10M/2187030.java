package net.sourceforge.smartconversion.api.exception;

/**
 * Thrown when a type is not found.
 *
 * @author Ovidiu Dolha
 */
public class TypeNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public TypeNotFoundException() {
        super();
    }

    public TypeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TypeNotFoundException(String message) {
        super(message);
    }

    public TypeNotFoundException(Throwable cause) {
        super(cause);
    }
}
