package com.excelsior.portfolio;

/**
 * The Class CannotClosePortfolioException.
 */
public class CannotClosePortfolioException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2593816536740787923L;

    /**
	 * Instantiates a new cannot close portfolio exception.
	 */
    public CannotClosePortfolioException() {
    }

    /**
	 * Instantiates a new cannot close portfolio exception.
	 * 
	 * @param message the message
	 */
    public CannotClosePortfolioException(String message) {
        super(message);
    }

    /**
	 * Instantiates a new cannot close portfolio exception.
	 * 
	 * @param cause the cause
	 */
    public CannotClosePortfolioException(Throwable cause) {
        super(cause);
    }

    /**
	 * Instantiates a new cannot close portfolio exception.
	 * 
	 * @param message the message
	 * @param cause the cause
	 */
    public CannotClosePortfolioException(String message, Throwable cause) {
        super(message, cause);
    }
}
