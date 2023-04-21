package it.postecom.ambienti.exception;

/**
 * @author Giovanni
 *
 */
public class FatalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FatalException() {
        super();
    }

    public FatalException(String message) {
        super(message);
    }

    public FatalException(String message, Throwable cause) {
        super(message, cause);
    }

    public FatalException(Throwable cause) {
        super(cause);
    }
}
