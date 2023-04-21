package DE.FhG.IGD.gps;

import java.lang.IllegalArgumentException;

/**
 * Thrown to indicate that the application attempted to convert a
 * <code>String</code> into an <code>AngularDistance</code>, but that the
 * <code>String</code> does not have the appropriate format.
 *
 * @author Dennis Bartussek <dennis.bartussek@igd.fhg.de>
 */
public class AngularDistanceFormatException extends IllegalArgumentException {

    /**
     * Constructs a new <code>AngularDistanceFormatException</code> with no
     * detail message.
     */
    public AngularDistanceFormatException() {
        super();
    }

    /**
     * Constructs a new <code>AngularDistanceFormatException</code> with the
     * specified detail message.
     *
     * @param message the detail message
     */
    public AngularDistanceFormatException(String message) {
        super(message);
    }
}
