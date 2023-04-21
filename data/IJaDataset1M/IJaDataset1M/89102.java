package net.sourceforge.pebble;

/**
 * Superclass for all Pebble specific exceptions.
 *
 * @author Simon Brown
 */
public class PebbleException extends Exception {

    public PebbleException() {
    }

    public PebbleException(String message) {
        super(message);
    }

    public PebbleException(Throwable cause) {
        super(cause);
    }
}
