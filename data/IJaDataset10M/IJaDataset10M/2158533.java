package org.nightlabs.io;

import java.io.IOException;

/**
 * Exception thrown when the end of a stream is reached.
 */
public class EndOfStreamException extends IOException {

    /**
	 * The serial version of this class.
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Create a new EndOfStreamException.
	 */
    public EndOfStreamException() {
        super();
    }

    /**
	 * Create a new EndOfStreamException.
	 * @param  message the detail message (which is saved for later retrieval
	 *         by the {@link #getMessage()} method).
	 */
    public EndOfStreamException(String message) {
        super(message);
    }

    /**
	 * Create a new EndOfStreamException.
	 * @param  message the detail message (which is saved for later retrieval
	 *         by the {@link #getMessage()} method).
	 * @param  cause the cause (which is saved for later retrieval by the
	 *         {@link #getCause()} method).  (A <tt>null</tt> value is
	 *         permitted, and indicates that the cause is nonexistent or
	 *         unknown.)
	 */
    public EndOfStreamException(String message, Throwable cause) {
        super(message);
        initCause(cause);
    }

    /**
	 * Create a new EndOfStreamException.
	 * @param  cause the cause (which is saved for later retrieval by the
	 *         {@link #getCause()} method).  (A <tt>null</tt> value is
	 *         permitted, and indicates that the cause is nonexistent or
	 *         unknown.)
	 */
    public EndOfStreamException(Throwable cause) {
        super();
        initCause(cause);
    }
}
