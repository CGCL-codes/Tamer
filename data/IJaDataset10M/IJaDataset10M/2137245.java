package de.ufinke.cubaja.util;

/**
 * Exception thrown when there is no enum corresponding to a given value.
 * @author Uwe Finke
 */
public class NoSuchEnumException extends Exception {

    /**
   * Constructor.
   * @param message
   */
    public NoSuchEnumException(String message) {
        super(message);
    }
}
