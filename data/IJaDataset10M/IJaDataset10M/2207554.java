package com.gwtent.client.ui.validate;

public class ValidateException extends RuntimeException {

    /**
	 * 
	 * James Luo 2007-12-26 上午10:40:27
	 */
    private static final long serialVersionUID = 1L;

    public ValidateException() {
        super();
    }

    /**
	 * Constructs a new runtime exception with the specified detail message. The
	 * cause is not initialized, and may subsequently be initialized by a call
	 * to {@link #initCause}.
	 * 
	 * @param message
	 *            the detail message. The detail message is saved for later
	 *            retrieval by the {@link #getMessage()} method.
	 */
    public ValidateException(String message) {
        super(message);
    }
}
