package com.sun.org.apache.xerces.internal.xni;

/**
 * This exception is the base exception of all XNI exceptions. It
 * can be constructed with an error message or used to wrap another
 * exception object.
 * <p>
 * <strong>Note:</strong> By extending the Java 
 * <code>RuntimeException</code>, XNI handlers and components are 
 * not required to catch XNI exceptions but may explicitly catch
 * them, if so desired.
 *
 * @author Andy Clark, IBM
 *
 * @version $Id: XNIException.java,v 1.2.6.1 2005/09/06 08:53:57 neerajbj Exp $
 */
public class XNIException extends RuntimeException {

    /** Serialization version. */
    static final long serialVersionUID = 9019819772686063775L;

    /** The wrapped exception. */
    private Exception fException;

    /**
     * Constructs an XNI exception with a message. 
     *
     * @param message The exception message.
     */
    public XNIException(String message) {
        super(message);
    }

    /**
     * Constructs an XNI exception with a wrapped exception. 
     *
     * @param exception The wrapped exception.
     */
    public XNIException(Exception exception) {
        super(exception.getMessage());
        fException = exception;
    }

    /**
     * Constructs an XNI exception with a message and wrapped exception. 
     *
     * @param message The exception message.
     * @param exception The wrapped exception.
     */
    public XNIException(String message, Exception exception) {
        super(message);
        fException = exception;
    }

    /** Returns the wrapped exception. */
    public Exception getException() {
        return fException;
    }
}
