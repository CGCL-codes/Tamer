package cv;

/**
 *
 * @author sean
 */
public class IncorrectImageTypeException extends Exception {

    public IncorrectImageTypeException() {
        super("Incorrect image type supplied!");
    }

    public IncorrectImageTypeException(String msg) {
        super(msg);
    }
}
