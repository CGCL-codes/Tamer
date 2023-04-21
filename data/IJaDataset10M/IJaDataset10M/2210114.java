package org.chess.quasimodo.errors;

public class InvalidFENException extends AppException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8744752909500568724L;

    public InvalidFENException() {
        super();
    }

    public InvalidFENException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidFENException(String message) {
        super(message);
    }

    public InvalidFENException(Throwable cause) {
        super(cause);
    }
}
