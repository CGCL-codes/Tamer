package org.tolven.exeption;

/**
 * Thrown when a persistent session is not found.
 * 
 * @author Joseph Isaac
 *
 */
public class TolvenSessionNotFoundException extends RuntimeException {

    public TolvenSessionNotFoundException() {
    }

    public TolvenSessionNotFoundException(String message) {
        super(message);
    }

    public TolvenSessionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TolvenSessionNotFoundException(Throwable cause) {
        super(cause);
    }
}
