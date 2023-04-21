package com.sun.ebxml.registry;

/**
 * Signals that a Registry exception has occured. It contains no members other than the standard reason String.  
 * 
 * @author     Nicholas Kassem 
 * @author     Mark Hapner  
 * @author     Rajiv Mordani
 *
 */
public class RegistryException extends Exception {

    private Exception exception;

    /**
     * Construct a RegistryException with no reason.
     * 
     * 
     * @see
     */
    public RegistryException() {
        super();
        this.exception = null;
    }

    /**
     * Construct a RegistryException with a reason .
     * 
     * 
     * @param reason
     *
     * @see
     */
    public RegistryException(String reason) {
        super(reason);
        this.exception = null;
    }

    /**
     * Construct a RegistryException with the embedded exception and the reason for.
     * 
     * 
     * @param reason
     * @param exception
     *
     * @see
     */
    public RegistryException(String reason, Exception exception) {
        super(reason);
        this.exception = exception;
    }

    /**
     * Construct a RegistryException with the embedded exception.
     * 
     * 
     * @param exception
     *
     * @see
     */
    public RegistryException(Exception exception) {
        super();
        this.exception = exception;
    }

    /**
     * Return a detail message for this exception.
     * 
     * 
     * @return
     *
     * @see
     */
    public String getMessage() {
        String message = super.getMessage();
        if (message == null && exception != null) {
            return exception.getMessage();
        } else {
            return message;
        }
    }

    /**
     * Return the embedded exception, if any.
     * 
     * 
     * @return
     *
     * @see
     */
    public Exception getException() {
        return exception;
    }
}
