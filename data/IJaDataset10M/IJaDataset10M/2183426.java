package org.jaxen.saxpath;

import java.io.PrintStream;
import java.io.PrintWriter;

/** Base of all SAXPath exceptions.
 *
 *  @author bob mcwhirter (bob@werken.com)
 */
public class SAXPathException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 4826444568928720706L;

    private static double javaVersion = 1.4;

    static {
        try {
            String versionString = System.getProperty("java.version");
            versionString = versionString.substring(0, 3);
            javaVersion = Double.valueOf(versionString).doubleValue();
        } catch (Exception ex) {
        }
    }

    /** Create a new SAXPathException with a given message.
     *
     *  @param message the error message
     */
    public SAXPathException(String message) {
        super(message);
    }

    /** Create a new SAXPathException based on another exception
     *
     *  @param cause the error source
     */
    public SAXPathException(Throwable cause) {
        super(cause.getMessage());
        initCause(cause);
    }

    /**
     * Create a new SAXPathException with the specified detail message
     * and root cause.
     * 
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public SAXPathException(String message, Throwable cause) {
        super(message);
        initCause(cause);
    }

    private Throwable cause;

    private boolean causeSet = false;

    /**
     * Returns the exception that caused this exception.
     * This is necessary to implement Java 1.4 chained exception 
     * functionality in a Java 1.3-compatible way.
     * 
     * @return the exception that caused this exception
     */
    public Throwable getCause() {
        return cause;
    }

    /**
     * Sets the exception that caused this exception.
     * This is necessary to implement Java 1.4 chained exception 
     * functionality in a Java 1.3-compatible way.
     * 
     * @param cause the exception wrapped in this runtime exception
     * 
     * @return this exception
     */
    public Throwable initCause(Throwable cause) {
        if (causeSet) throw new IllegalStateException("Cause cannot be reset");
        if (cause == this) throw new IllegalArgumentException("Exception cannot be its own cause");
        causeSet = true;
        this.cause = cause;
        return this;
    }

    /** Print this exception's stack trace, followed by the
     *  source exception's trace, if any.
     *
     * @param s the stream on which to print the stack trace
     */
    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        if (javaVersion < 1.4 && getCause() != null) {
            s.print("Caused by: ");
            getCause().printStackTrace(s);
        }
    }

    /** Print this exception's stack trace, followed by the
     *  source exception's stack trace, if any.
     *
     * @param s the writer on which to print the stack trace
     */
    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
        if (javaVersion < 1.4 && getCause() != null) {
            s.print("Caused by: ");
            getCause().printStackTrace(s);
        }
    }
}
