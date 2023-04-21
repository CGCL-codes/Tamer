package de.bioutils.blast.printer;

public class NoColumnsException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8035588757633784919L;

    NoColumnsException() {
    }

    NoColumnsException(String message) {
        super(message);
    }

    NoColumnsException(Throwable cause) {
        super(cause);
    }

    NoColumnsException(String message, Throwable cause) {
        super(message, cause);
    }
}
