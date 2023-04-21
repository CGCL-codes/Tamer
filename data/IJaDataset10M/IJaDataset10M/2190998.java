package uk.org.ogsadai.activity.io;

import java.io.IOException;

/**
 * Raised by OGSA-DAI input and output stream implementations when a termination
 * signal is detected.
 * 
 * @author The OGSA-DAI Project Team
 */
public class TerminatedIOException extends IOException {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007.";

    /**
     * Creates a new exception.
     * 
     * @param message
     *            exception message
     */
    public TerminatedIOException(String message) {
        super(message);
    }

    /**
     * Creates a new exception.
     * 
     * @param cause
     *            cause of the exception
     */
    public TerminatedIOException(Throwable cause) {
        super();
        initCause(cause);
    }
}
