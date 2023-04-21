package wizworld.navigate.math;

/** Math functions
 * @author (c) Stephen Denham 2002-2011
 * @version 0.1
 * @version 0.2 - Add round(x, m)
 */
public final class Maths {

    /** Hexadecimal radix */
    public static final int HEX_RADIX = 16;

    /** Interpolate value using linear regression
   * @param x0 First x-coordinate
   * @param x1 Second y-coordinate
   * @param y0 First x-coordinate
   * @param y1 Second y-coordinate
   * @param x x value
   * @return y value
   */
    public static double interpolate(double x0, double x1, double y0, double y1, double x) {
        if (y1 != y0) {
            double m = (y1 - y0) / (x1 - x0);
            double c = y0 - m * x0;
            return m * x + c;
        } else {
            return y0;
        }
    }

    /** Round to specified increment
   1. Divide x by m, let the result be y;
   2. Round y to an integer value, call it q;
   3. Multiply q by m to obtain the rounded value z.
   @param x	Value to round
   @param m Increment
   @return Rounded value
  */
    public static double round(double x, double m) {
        double y = x / m;
        long q = Math.round(y);
        return q * m;
    }
}
