package exceptions;

/**
 * May be thrown when there is no such player involved in the game.
 * @author George Harnish
 * @version 0.1
 */
public class NoSuchPlayerException extends RuntimeException {

    /**
     * Constructs an instance of <code>NoSuchPlayerException</code> with the 
     * specified detail message.
     * @param msg the detail message.
     */
    public NoSuchPlayerException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>NoSuchPlayerException</code> with no message.
     */
    public NoSuchPlayerException() {
    }
}
