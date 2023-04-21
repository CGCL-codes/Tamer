package org.dinopolis.gpstool.util.angle;

/***
 * A latitude angle. Positive latitudes are North, while negative
 * latitudes are South. This class has no direct OpenGIS equivalent.
 *
 * @version 1.0
 * @author Martin Desruisseaux
 *
 * @see Longitude
 * @see AngleFormat
 */
public final class Latitude extends Angle {

    /***
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -4496748683919618976L;

    /***
     * Minimum legal value for latitude (-90?).
     */
    public static final double MIN_VALUE = -90;

    /***
     * Maximum legal value for latitude (+90?).
     */
    public static final double MAX_VALUE = +90;

    /***
     * Contruct a new latitude with the specified value.
     *
     * @param theta Angle in degrees.
     */
    public Latitude(final double theta) {
        super(theta);
    }

    /***
     * Constructs a newly allocated <code>Latitude</code> object that
     * represents the latitude value represented by the string.   The
     * string should represents an angle in either fractional degrees
     * (e.g. 45.5?) or degrees with minutes and seconds (e.g. 45?30').
     * The hemisphere (N or S) is optional (default to North).
     *
     * @param  source A string to be converted to a <code>Latitude</code>.
     * @throws NumberFormatException if the string does not contain a parsable latitude.
     */
    public Latitude(final String source) throws NumberFormatException {
        super(source);
    }
}
