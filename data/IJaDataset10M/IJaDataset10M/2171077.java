package neo.geo;

import static java.lang.Math.abs;

/**
 * Specialized class representing angular measurements along Earth spin axis
 * aka <b>North:South direction</b>.
 * <pre>Note: Latitudes ranges from -90 degrees to 90 degrees at poles
 * with zero degrees at Equatorial line.</pre>
 *
 * @author dekassegui@gmail.com
*/
public class Latitude extends Ordinate {

    /** Standard constructor just storing the latitude value. */
    public Latitude(double x) {
        super(x);
        value = value % 90;
    }

    /** Constructor translating data from DMS format to floating point. */
    public Latitude(int d, int m, double s) {
        super(d, m, s);
        value = value % 90;
    }

    /**
   * Constructor translating data from DMS format to floating point
   * signed in conformity to given cardinal point suffix i.e.
   * negative if suffix is "S" (south) else positive.
  */
    public Latitude(int d, int m, double s, String suffix) {
        super(d, m, s);
        value = value % 90;
        value = (suffix.matches("(?i:S|N)") ? -1 : 1) * abs(value);
    }

    /**
   * Returns a friendly string representation of this ordinate in DMS format
   * plus related cardinal point suffix.
  */
    public String asString() {
        return String.format("%s %c", toDMS(value), (abs(value) > EPS && value < 0 ? 'S' : 'N'));
    }
}
