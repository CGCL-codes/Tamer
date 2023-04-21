package de.uniba.kinf.cityguide.exceptions;

/**
 * Exception thrown when there was an error concerning the tour.
 * Happens e.g. when there were some problems generating, enlarging or shortening a tour.
 * @author bhofmann
 *
 */
public class BadTourException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public BadTourException(String message) {
        super(message);
    }

    public BadTourException() {
        super();
    }
}
