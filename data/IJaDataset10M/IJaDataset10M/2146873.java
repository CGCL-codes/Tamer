package lu.albert.ovum.exceptions;

/**
 * @todo Javadoc
 *
 * @author Michel Albert <michel@albert.lu>
 *
 */
public class InvalidClassException extends Exception {

    /** @todo Javadoc */
    private static final long serialVersionUID = 3256999947634029107L;

    /**
    * @todo Javadoc
    *
    * @param message
    */
    public InvalidClassException(String message) {
        super(message);
    }
}
