package tgreiner.amy.go.engine;

/**
 * Thrown to indicate an illegal move.
 *
 * @author <a href="mailto:thorsten.greiner@googlemail.com">Thorsten Greiner</a>
 */
public class IllegalMoveException extends Exception {

    /**
     * Create an IllegalMoveException without a detail message.
     */
    public IllegalMoveException() {
        super();
    }

    /**
     * Create an IllegalMoveException with the supplied detail message.
     *
     * @param message the message
     */
    public IllegalMoveException(final String message) {
        super(message);
    }
}
