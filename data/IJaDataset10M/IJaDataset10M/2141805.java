package org.ikasan.framework.initiator;

/**
 * Exception resulting from an operation invoked on this initiator.
 * 
 * @author Ikasan Development Team
 *
 */
public class InitiatorOperationException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 303376919145399965L;

    /**
     * Constructor
     * 
     * @param message
     * @param exception
     */
    public InitiatorOperationException(String message, Exception exception) {
        super(message, exception);
    }
}
