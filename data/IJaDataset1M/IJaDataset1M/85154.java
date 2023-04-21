package dk.i2m.converge.ws.soap;

/**
 * Exception thrown if an unknown {@link MediaItemStatus} was requested.
 *
 * @author Allan Lykke Christensen
 */
public class InvalidMediaItemStatusException extends Exception {

    public InvalidMediaItemStatusException(Throwable cause) {
        super(cause);
    }

    public InvalidMediaItemStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMediaItemStatusException(String message) {
        super(message);
    }

    public InvalidMediaItemStatusException() {
        super();
    }
}
