package net.martinimix.dao;

/**
 * Thrown to indicate an unrecoverable exception occurred during the
 * invocation of a business action.
 * 
 * @author Scott Rossillo
 *
 */
public class BusinessActionInvocationException extends BusinessActionException {

    private static final long serialVersionUID = 1L;

    public BusinessActionInvocationException(String message, Throwable t) {
        super(message, t);
    }

    public BusinessActionInvocationException(String message) {
        super(message);
    }

    public BusinessActionInvocationException(Throwable t) {
        super(t);
    }
}
