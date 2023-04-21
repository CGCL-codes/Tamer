package influx.biz.exception;

import influx.exception.InfluxRuntimeException;

/**
 * Common application exception
 * 
 * @author whoover
 */
public class ApplicationException extends InfluxRuntimeException {

    private static final long serialVersionUID = 1L;

    /**
	 * Message/Cause constructor
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
    public ApplicationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
	 * Cause constructor
	 * 
	 * @param cause
	 *            the cause
	 */
    public ApplicationException(final Throwable cause) {
        super(cause);
    }
}
