package org.argouml.persistence;

import java.io.PrintStream;
import java.io.PrintWriter;
import org.xml.sax.SAXException;

/**
 * An exception to be thrown during failure of a opening
 * and reading some storage medium.
 * @author Bob Tarling
 */
public class OpenException extends PersistenceException {

    /**
     * The constructor.
     *
     * @param message the message to show
     */
    public OpenException(String message) {
        super(message);
    }

    /**
     * The constructor.
     *
     * @param message the message to show
     * @param cause the cause for the exception
     */
    public OpenException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * The constructor.
     *
     * @param cause the cause for the exception
     */
    public OpenException(Throwable cause) {
        super(cause);
    }

    public void printStackTrace() {
        super.printStackTrace();
        if (getCause() instanceof SAXException && ((SAXException) getCause()).getException() != null) {
            ((SAXException) getCause()).getException().printStackTrace();
        }
    }

    public void printStackTrace(PrintStream ps) {
        super.printStackTrace(ps);
        if (getCause() instanceof SAXException && ((SAXException) getCause()).getException() != null) {
            ((SAXException) getCause()).getException().printStackTrace(ps);
        }
    }

    public void printStackTrace(PrintWriter pw) {
        super.printStackTrace(pw);
        if (getCause() instanceof SAXException && ((SAXException) getCause()).getException() != null) {
            ((SAXException) getCause()).getException().printStackTrace(pw);
        }
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -4787911270548948677L;
}
