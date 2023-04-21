package javax.enterprise.deploy.spi.exceptions;

/**
 * This exception is to report bad target designators.
 */
public class TargetException extends Exception {

    /**
	 * Creates an new TargetException object.
	 * 
	 * @param s
	 *            a string indicating what was wrong with the target.
	 */
    public TargetException(String s) {
        super(s);
    }
}
