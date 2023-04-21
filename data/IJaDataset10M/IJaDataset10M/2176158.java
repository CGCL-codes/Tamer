package net.martinimix.dao;

public class BusinessActionException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    protected BusinessActionException(String message, Throwable t) {
        super(message, t);
    }

    protected BusinessActionException(String message) {
        super(message);
    }

    protected BusinessActionException(Throwable t) {
        super(t);
    }
}
