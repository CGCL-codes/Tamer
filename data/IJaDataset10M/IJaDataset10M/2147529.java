package com.volantis.vdp.configuration.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public class ConfigurationException extends Exception {

    private static final long serialVersionUID = 1124780806618649735L;

    /**
	 * The throwable that caused this throwable to get thrown.
	 */
    Throwable cause;

    /**
	 * @param exception cause
	 */
    public ConfigurationException(String msg) {
        super(msg);
    }

    /**
	 * @param msg: exception cause message
	 * @param cause: exception to be wrapped
	 */
    public ConfigurationException(String msg, Throwable cause) {
        super(msg);
        this.cause = cause;
    }

    public Throwable getCause() {
        return cause;
    }

    public void printStackTrace() {
        super.printStackTrace();
        if (cause != null) {
            System.out.println("Caused by:");
            cause.printStackTrace();
        }
    }

    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        if (cause != null) {
            s.println("Caused by:");
            cause.printStackTrace(s);
        }
    }

    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
        if (cause != null) {
            s.println("Caused by:");
            cause.printStackTrace(s);
        }
    }

    public String getMessage() {
        if (cause == null) {
            return super.getMessage();
        } else {
            return super.getMessage() + " (see below for lower-level details)";
        }
    }
}
