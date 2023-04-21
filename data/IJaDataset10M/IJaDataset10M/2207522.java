package com.excelsior.portfolio.position;

/**
 * The Class CannotCalculateMaximumLossException.
 */
public class CannotCalculateMaximumLossException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2658657724870940426L;

    /**
	 * Instantiates a new cannot calculate maximum loss exception.
	 */
    public CannotCalculateMaximumLossException() {
    }

    /**
	 * Instantiates a new cannot calculate maximum loss exception.
	 * 
	 * @param message the message
	 */
    public CannotCalculateMaximumLossException(String message) {
        super(message);
    }

    /**
	 * Instantiates a new cannot calculate maximum loss exception.
	 * 
	 * @param cause the cause
	 */
    public CannotCalculateMaximumLossException(Throwable cause) {
        super(cause);
    }

    /**
	 * Instantiates a new cannot calculate maximum loss exception.
	 * 
	 * @param message the message
	 * @param cause the cause
	 */
    public CannotCalculateMaximumLossException(String message, Throwable cause) {
        super(message, cause);
    }
}
