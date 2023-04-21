package org.posterita.exceptions;

public class QuantityNotAvailableException extends OperationException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String message;

    public QuantityNotAvailableException() {
    }

    public QuantityNotAvailableException(String exception) {
        super(exception);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
