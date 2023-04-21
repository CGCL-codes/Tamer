package net.jgf.core;

/**
 * This checked exception is thrown when an operation is called on a service or
 * component that doesn't implement that operation.
 * 
 * @author jjmontes
 */
public class UnsupportedOperationException extends JgfRuntimeException {

    /**
     * Id for serialization
     */
    private static final long serialVersionUID = -8971616437376909601L;

    /**
     * Builds a new UnsupportedOperationException with the message specified.
     */
    public UnsupportedOperationException(String message) {
        super(message);
    }

    /**
     * Builds a new UnsupportedOperationException with the given message and the given
     * nested exception.
     */
    public UnsupportedOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
