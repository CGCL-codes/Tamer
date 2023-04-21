package gov.lanl.adore.djatoka;

/**
 * Default Exception Handler for djatoka API
 * @author Ryan Chute
 *
 */
public class DjatokaException extends Exception {

    private static final long serialVersionUID = 1L;

    public DjatokaException(String message) {
        super(message);
    }

    public DjatokaException(String message, Throwable cause) {
        super(message, cause);
    }

    public DjatokaException(Throwable cause) {
        super(cause);
    }
}
