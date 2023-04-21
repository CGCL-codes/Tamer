package org.otfeed;

import org.otfeed.event.OTError;

/**
 * Runtime Exception that may hold OTError object describing an error condition
 * reported by Opentick server.
 */
public final class OTException extends RuntimeException {

    private static final long serialVersionUID = 2189306294577039319L;

    private final OTError error;

    /**
	 * Creates empty exception.
	 */
    public OTException() {
        error = null;
    }

    /**
	 * Creates exception with text description.
	 * 
	 * @param reason description of the reason.
	 */
    public OTException(String reason) {
        super(reason);
        error = null;
    }

    /**
	 * Creates exception by wrapping some other exception.
	 * 
	 * @param wrapped exception that caused the problem.
	 */
    public OTException(Exception wrapped) {
        super(wrapped);
        error = null;
    }

    /**
	 * Creates exception by wrapping OTError object.
	 * 
	 * @param error describes error condition reported by Opentick server.
	 */
    public OTException(OTError error) {
        super(error.toString());
        this.error = error;
    }

    /**
	 * Returns wrapped OTError object (may be null).
	 * 
	 * @return error object.
	 */
    public OTError getError() {
        return error;
    }

    /**
	 * {@inheritDoc}
	 */
    public String toString() {
        if (error == null) return super.toString();
        return "OtfeedException: error=" + error;
    }
}
