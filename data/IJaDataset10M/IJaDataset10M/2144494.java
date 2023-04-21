package skycastle.util.colorgradient.pointbased;

import skycastle.util.ParameterChecker;
import skycastle.util.ColorUtils;

/**
 * A value and its associated color for a PointBasedColorGradient.
 *
 * @author Hans H�ggstr�m
 */
public final class ColorGradientPoint implements Comparable<ValuePoint>, ValuePoint {

    private final float myValue;

    private final float myRed;

    private final float myGreen;

    private final float myBlue;

    private final float myAlpha;

    public ColorGradientPoint(final float value, final float red, final float green, final float blue, final float alpha) {
        ParameterChecker.checkZeroToOneInclusive(red, "red");
        ParameterChecker.checkZeroToOneInclusive(green, "green");
        ParameterChecker.checkZeroToOneInclusive(blue, "blue");
        ParameterChecker.checkZeroToOneInclusive(alpha, "alpha");
        myValue = value;
        myRed = red;
        myGreen = green;
        myBlue = blue;
        myAlpha = alpha;
    }

    public ColorGradientPoint(final double value, final double red, final double green, final double blue, final double alpha) {
        this((float) value, (float) red, (float) green, (float) blue, (float) alpha);
    }

    public int compareTo(final ValuePoint otherPoint) {
        final float otherValue = otherPoint.getValue();
        if (myValue < otherValue) {
            return -1;
        } else if (myValue > otherValue) {
            return 1;
        } else {
            return 0;
        }
    }

    public float getValue() {
        return myValue;
    }

    public float getRed() {
        return myRed;
    }

    public float getGreen() {
        return myGreen;
    }

    public float getBlue() {
        return myBlue;
    }

    public float getAlpha() {
        return myAlpha;
    }

    public int getRgbaColor() {
        return ColorUtils.createPackedRgbFromFloats(getRed(), getGreen(), getBlue(), getAlpha());
    }
}
