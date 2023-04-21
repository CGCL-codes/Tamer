package com.elogico.xbound;

/**
 * Thrown when a problem with xbound configuration. 
 * This error will typically be thrown when a class specified cannot be found or instantiated.
 *  * 
 * @version <tt>$Revision: 1.1 $</tt>
 * @author <a href="mailto:marco@elogico.com">Marco Sarti</a>
 */
public class XBoundConfigurationError extends Error {

    /**
     * Create a new XBoundConfigurationError with no detail mesage.
     */
    public XBoundConfigurationError() {
        super();
    }

    /**
     * Create a new XBoundConfigurationError with the String specified as an error message.
     *
     * @param msg The error message for the exception.
     */
    public XBoundConfigurationError(String msg) {
        super(msg);
    }

    /**
     * Create a new XBoundConfigurationError with a given Exception base cause of the error.
     * 
     * @param msg The error message for the exception.
     * @param e  The exception to be encapsulated in a XBoundConfigurationError
     */
    public XBoundConfigurationError(String msg, Throwable e) {
        super(msg, e);
    }

    /**
     * Create a new XBoundConfigurationError with a given Exception base cause of the error.
     * 
     * @param e  The exception to be encapsulated in a XBoundConfigurationError
     */
    public XBoundConfigurationError(Throwable e) {
        super(e);
    }
}
