package net.sf.sit.exception;

/**
 * A runtime (unchecked) wrapper exception that wraps exceptions thrown from
 * invoking a provider method.
 */
public class SitInvocationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
	 * Full constructor.
	 * 
	 * @param operation
	 *            Operation invoked
	 * @param cause
	 *            Cause exception thrown from the operation
	 */
    public SitInvocationException(String operation, Throwable cause) {
        super("Error invoking service operation " + operation + ": " + cause, cause);
    }
}
