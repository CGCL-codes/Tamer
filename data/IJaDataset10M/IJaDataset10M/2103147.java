package com.xenonsoft.bridgetown.aop;

/**
 * A root unchecked exception for a failures that are 
 * interception related, for instance if a proxy bean
 * builder or a method interceptor cannot weave
 * the correct behaviour in and around a targeted method.
 *
 * @author Peter Pilgrim, 27-Oct-2004 08:59:37
 * @version $Id: InterceptionWeaveException.java,v 1.2 2005/02/23 01:28:14 peter_pilgrim Exp $
 */
public class InterceptionWeaveException extends RuntimeException {

    /**
     * Default constructor
     */
    public InterceptionWeaveException() {
        super();
    }

    /**
     * Standard constructor
     * @param message the message to report
     */
    public InterceptionWeaveException(String message) {
        super(message);
    }

    /**
     * Standard constructor
     * @param cause the root cause
     */
    public InterceptionWeaveException(Throwable cause) {
        super(cause);
    }

    /**
     * Standard constructor
     * @param message the message to report
     * @param cause the root cause
     */
    public InterceptionWeaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
