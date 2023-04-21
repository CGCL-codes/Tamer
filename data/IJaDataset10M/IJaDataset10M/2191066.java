package net.sf.jLockit;

/**
 * Thrown when an exception occurs during the lock/unlock process 
 * of a record
 * 
 * @author sseaman
 * @since 1.0.0
 * @version 1.0.0
 */
public class LockException extends RuntimeException {

    private static final long serialVersionUID = 1l;

    /**
	 * Creates a new exception
	 * 
	 * @param _msg The reason for the exception
	 */
    public LockException(String _msg) {
        super(_msg);
    }

    /**
	 * Creates a new exception
	 * 
	 * @param _msg The reason for the exception
	 * @param _e The underlying exception that may have occurred
	 */
    public LockException(String _msg, Exception _e) {
        super(_msg, _e);
    }
}
