package alice.cartago;

/**
 * Exception raised when using
 * a feature that is not supported
 * by the specific kind of artifact.
 * 
 * @author aricci
 *
 */
public class FeatureNotSupportedException extends CartagoException {

    public FeatureNotSupportedException(String descr) {
        super(descr);
    }
}
