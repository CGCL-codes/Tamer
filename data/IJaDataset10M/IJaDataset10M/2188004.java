package name.nanek.gdwprototype.shared.exceptions;

/**
 * An exception specifically generated via the game. 
 * A useful distinction to make so that we know if we should ask the user to check their network connection or not.
 * Many errors are simply due to losing network connection during the game.
 * 
 * @author Lance Nanek
 *
 */
public class GameException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public GameException() {
    }

    public GameException(String message, Throwable cause) {
        super(message, cause);
    }

    public GameException(String message) {
        super(message);
    }

    public GameException(Throwable cause) {
        super(cause);
    }
}
