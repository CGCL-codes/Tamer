package org.restlet.ext.jaxrs.internal.exceptions;

/**
 * This class is the super class for the most exception classes in this
 * implementation.
 * 
 * @author Stephan Koops
 */
public abstract class JaxRsException extends Exception {

    private static final long serialVersionUID = -7662465289573982489L;

    /**
     * For subclasses only
     */
    JaxRsException() {
        super();
    }

    /**
     * @param message
     */
    public JaxRsException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public JaxRsException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public JaxRsException(Throwable cause) {
        super(cause);
    }
}
