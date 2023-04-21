package jpfm;

/**
 *
 * @author Shashank Tulsyan
 */
public final class IncorrectImplementationException extends Exception {

    public IncorrectImplementationException() {
    }

    public IncorrectImplementationException(String message) {
        super(message);
    }

    public IncorrectImplementationException(String message, Throwable cause) {
        super(message, cause);
    }
}
