package ch.jester.system.exceptions;

/**
 * Exception welcher geworfen werden kann, wenn auf die
 * Startnummern der PlayerCard zugegriffen wird, diese aber noch nicht gesetzt wurde.
 *
 */
public class NoStartingNumbersException extends Exception {

    private static final long serialVersionUID = 8567273103944087862L;

    public NoStartingNumbersException() {
        super("The PlayerCards have no starting numbers!");
    }

    public NoStartingNumbersException(String message) {
        super(message);
    }
}
