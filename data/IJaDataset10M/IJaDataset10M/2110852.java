package org.argouml.model.mdr;

/**
 * Exception for external reference problems with XMI files.  This is an
 * internal MDR exception which extends RuntimeException so that it can be
 * thrown from MDR internals.
 * 
 * @author Tom Morris
 */
public class XmiReferenceException extends RuntimeException {

    private String reference;

    /**
     * Construct an XmiReferenceException with the given message.
     * 
     * @param message the message
     */
    public XmiReferenceException(String message) {
        super(message);
    }

    /**
     * Construct an XmiReferenceException for the given reference
     * 
     * @param href the reference that caused the error
     * @param cause the nested exception if available
     */
    public XmiReferenceException(String href, Throwable cause) {
        super(href, cause);
        reference = href;
    }

    /**
     * @return the external reference (href) that caused the exception
     */
    public String getReference() {
        return reference;
    }

    /**
     * Construct an exception with a causing exception.
     *
     * @param c the cause of the exception
     */
    public XmiReferenceException(Throwable c) {
        super(c);
    }
}
