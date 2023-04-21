package net.sf.flophase.core.exception;

/**
 * This exception is thrown if there is an error while trying to find the handler delegate.
 */
public class FindDelegateException extends Exception {

    /**
     * Serialization identifier
     */
    private static final long serialVersionUID = -2392662601856638662L;

    /**
     * Creates a new FindDelegateException instance.
     * 
     * @param message The error message.
     */
    public FindDelegateException(String message) {
        super(message);
    }
}
