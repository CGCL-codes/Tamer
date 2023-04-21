package org.eclipse.core.databinding;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * An unchecked exception indicating a binding problem.
 * 
 * @since 1.0
 */
public class BindingException extends RuntimeException {

    private static final long serialVersionUID = -4092828452936724217L;

    private Throwable cause;

    /**
	 * Creates a new BindingException with the given message.
	 * 
	 * @param message
	 */
    public BindingException(String message) {
        super(message);
    }

    /**
	 * Creates a new BindingException with the given message and cause.
	 * 
	 * @param message
	 * @param cause
	 */
    public BindingException(String message, Throwable cause) {
        super(message);
        this.cause = cause;
    }

    public void printStackTrace(PrintStream err) {
        super.printStackTrace(err);
        if (cause != null) {
            err.println("caused by:");
            cause.printStackTrace(err);
        }
    }

    public void printStackTrace(PrintWriter err) {
        super.printStackTrace(err);
        if (cause != null) {
            err.println("caused by:");
            cause.printStackTrace(err);
        }
    }
}
