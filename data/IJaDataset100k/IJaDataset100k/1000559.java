package de.banh.bibo.exceptions;

public class CannotSearchEntityException extends BiboException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6612079491340459662L;

    public CannotSearchEntityException() {
        super();
    }

    public CannotSearchEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotSearchEntityException(String message) {
        super(message);
    }

    public CannotSearchEntityException(Throwable cause) {
        super(cause);
    }
}
