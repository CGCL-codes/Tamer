package exceptions;

/**
 * May be thrown when there is no more room for a player to join the game.
 * @author George Harnish
 * @version 0.1
 */
public class GameIsFullException extends RuntimeException {

    /**
     * Constructs an instance of <code>GameIsFullException</code> with the 
     * specified detail message.
     * @param msg the detail message.
     */
    public GameIsFullException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>GameIsFullException</code> with no message.
     */
    public GameIsFullException() {
    }
}
