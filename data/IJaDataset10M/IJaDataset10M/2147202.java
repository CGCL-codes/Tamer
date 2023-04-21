package net.sourceforge.pmd.eclipse;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * An exception to encapsulate exceptions thrown by various plug-in components
 * 
 * @author Philippe Herlin
 * @version $Revision: 2343 $
 * 
 * $Log$
 * Revision 1.1.2.1  2003/10/29 13:22:33  phherlin
 * Fix JDK1.3 runtime problem (Thanks to Eduard Naum)
 *
 * Revision 1.1  2003/10/14 21:56:47  phherlin
 * Creation
 *
 */
public class PMDEclipseException extends Exception {

    private Throwable cause;

    /**
     * Default Constructor
     */
    public PMDEclipseException() {
        super();
    }

    /**
     * Constructor with a message
     * @param message exception message
     */
    public PMDEclipseException(String message) {
        super(message);
    }

    /**
     * Constructor with a message and a root exception
     * @param message exception message
     * @param cause root exception
     */
    public PMDEclipseException(String message, Throwable cause) {
        super(message);
        setCause(cause);
    }

    /**
     * Constructor with a root exception
     * @param cause a root exception
     */
    public PMDEclipseException(Throwable cause) {
        setCause(cause);
    }

    /**
     * @return
     */
    public Throwable getCause() {
        return cause;
    }

    /**
     * @param throwable
     */
    public void setCause(Throwable throwable) {
        cause = throwable;
    }

    /**
     * @see java.lang.Throwable#printStackTrace()
     */
    public void printStackTrace() {
        super.printStackTrace();
        if (getCause() != null) {
            System.err.println("Caused by:");
            getCause().printStackTrace();
        }
    }

    /**
     * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
     */
    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        if (getCause() != null) {
            s.println("Caused by:");
            getCause().printStackTrace(s);
        }
    }

    /**
     * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter)
     */
    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
        if (getCause() != null) {
            s.println("Caused by:");
            getCause().printStackTrace(s);
        }
    }
}
