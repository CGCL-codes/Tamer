package de.schwarzrot.app.errors;

public class IncompleteClassException extends ApplicationException {

    private static final long serialVersionUID = 713L;

    public IncompleteClassException() {
        super();
    }

    public IncompleteClassException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncompleteClassException(String message) {
        super(message);
    }

    public IncompleteClassException(Throwable cause) {
        super(cause);
    }
}
