package jmodnews.scoring;

/**
 * Exception thrown when a flag action string cannot be parsed.
 * @author Michael Schierl <schierlm@gmx.de>
 */
public class FlagActionException extends RuntimeException {

    /**
	 * @param message
	 */
    public FlagActionException(String message) {
        super(message);
    }
}
