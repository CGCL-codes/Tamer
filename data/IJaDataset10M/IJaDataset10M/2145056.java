package nz.ac.waikato.jdsl.core.api;

/** 
 * A BoundaryViolationException indicates that a Container's edges
 * were trespassed somehow: off the end, over the top, beyond the
 * bottom, etc.
 * 
 * @author David Ellis
 * @author based on a previous version by Mark Handy
 * @version JDSL 2.1.1 
 */
public class BoundaryViolationException extends CoreException {

    public BoundaryViolationException(String message) {
        super(message);
    }

    public BoundaryViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BoundaryViolationException(Throwable cause) {
        super(cause);
    }
}
