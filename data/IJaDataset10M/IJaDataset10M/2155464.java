package com.vividsolutions.jump.geom.precision;

/**
 * Reduces the precision of a number
 * by rounding it off after scaling by a given scale factor.
 */
public class NumberPrecisionReducer {

    /**
   * Computes the scale factor for a given number of decimal places.
   * A negative value for decimalPlaces indicates the scale factor
   * should be divided rather than multiplied. The negative sign
   * is carried through to the computed scale factor.
   * @param decimalPlaces
   * @return
   */
    public static double scaleFactorForDecimalPlaces(int decimalPlaces) {
        int power = Math.abs(decimalPlaces);
        int sign = decimalPlaces >= 0 ? 1 : -1;
        double scaleFactor = 1.0;
        for (int i = 1; i <= power; i++) {
            scaleFactor *= 10.0;
        }
        return scaleFactor * sign;
    }

    private double scaleFactor = 0.0;

    private boolean multiplyByScaleFactor = true;

    public NumberPrecisionReducer() {
    }

    /**
   * A negative value for scaleFactor indicates
   * that the precision reduction will eliminate significant digits
   * to the left of the decimal point.
   * (I.e. the scale factor
   * will be divided rather than multiplied).
   * A zero value for scaleFactor will result in no precision reduction being performed.
   * A scale factor is normally an integer value.
   *
   * @param scaleFactor
   */
    public NumberPrecisionReducer(double scaleFactor) {
        setScaleFactor(scaleFactor);
    }

    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = Math.abs(scaleFactor);
        multiplyByScaleFactor = scaleFactor >= 0;
    }

    public double reducePrecision(double d) {
        if (scaleFactor == 0.0) return d;
        if (multiplyByScaleFactor) {
            double scaled = d * scaleFactor;
            return Math.floor(scaled + 0.5) / scaleFactor;
        } else {
            double scaled = d / scaleFactor;
            return Math.floor(scaled + 0.5) * scaleFactor;
        }
    }
}
