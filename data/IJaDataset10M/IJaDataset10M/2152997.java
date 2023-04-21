package JCL;

/**
 * Generic JCL exception for the Java Constraint Library.
 *
 * @author Erik Bruchez
 */
public class JCLException extends RuntimeException {

    public JCLException() {
    }

    public JCLException(String message) {
        super(message);
    }
}
