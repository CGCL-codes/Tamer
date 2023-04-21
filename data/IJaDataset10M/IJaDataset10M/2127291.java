package uipp.exceptions;

/**
 * Email address invalid
 * 
 * @author Jindrich Basek (basekjin@fel.cvut.cz, CTU Prague, FEE)
 */
public class EmailValidationException extends ValidationException {

    /**
     * 
     */
    private static final long serialVersionUID = -5197193682329077111L;

    /**
     * Creates a new instance of <code>EmailValidationException</code> without detail message.
     */
    public EmailValidationException() {
    }

    /**
     * Constructs an instance of <code>EmailValidationException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public EmailValidationException(String msg) {
        super(msg);
    }
}
