package name.levering.ryan.sparql.logic.function;

/**
 * A specific type of external funtion exception that can happen during any
 * casting operation.
 * 
 * @author Ryan Levering
 * @version 1.1
 */
public class IllegalCastException extends ExternalFunctionException {

    /**
     * Generated serial id for exception serialization.
     */
    private static final long serialVersionUID = 111597921296642360L;

    /**
     * Creates a new illegal cast exception, with a message describing why the
     * value couldn't be cast to another type.
     * 
     * @param message the message describing the illegal cast operation
     */
    public IllegalCastException(String message) {
        super(message);
    }
}
