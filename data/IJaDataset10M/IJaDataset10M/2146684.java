package org.apache.jetspeed.services.security;

/**
 * This exception is thrown when the requested user principal was not found.
 *
 * @author <a href="mailto:david@bluesunrise.com">David Sean Taylor</a>
 * @version $Id: UnknownUserException.java,v 1.3 2004/02/23 03:58:11 jford Exp $
 */
public class UnknownUserException extends UserException {

    /**
     * Constructs an UnknownUserException with no detail message. A detail
     * message is a String that describes this particular exception.
     */
    public UnknownUserException() {
        super();
    }

    /**
     * Constructs an UnknownUserException with the specified detail message.
     * A detail message is a String that describes this particular
     * exception.
     *
     * <p>
     *
     * @param msg the detail message.  
     */
    public UnknownUserException(String msg) {
        super(msg);
    }

    /**
     * Construct a nested exception.
     *
     * @param msg The detail message.
     * @param nested the exception or error that caused this exception 
     *               to be thrown.
     */
    public UnknownUserException(String msg, Throwable nested) {
        super(msg, nested);
    }
}
