package org.omg.tacsit.common.math;

/**
 * A representation of the angular distance between two intersecting lines.
 * <p>
 * An Angle can only be constructed by the fromDegrees and fromRadians method.  Since angular  measurements are 
 * typically stored as doubles (for both radians and degrees), this approach was adopted to make it clear which 
 * measurement was intended when constructing an angle.
 * <p>
 * Instances of Angle are immutable.
 * 
 * @author Matthew Child
 */
public class Angle {

    /**
   * An angle with no angular distance (radians and degrees are 0).
   */
    public static final Angle ZERO = Angle.fromRadians(0);

    private static final char DEGREE_SYMBOL = '°';

    private static final double DEGREES_IN_A_CIRCLE = 360.0;

    private double radians;

    private double degrees;

    private Angle(double radians, double degrees) {
        this.radians = radians;
        this.degrees = degrees;
    }

    /**
   * Constructs a new Angle from a radian angular measurement.
   * @param radians The radians of the angle.
   * @return A new Angle which has the associated radian measurement.
   */
    public static Angle fromRadians(double radians) {
        return new Angle(radians, Math.toDegrees(radians));
    }

    /**
   * Constructs a new Angle from a degree angular measurement.
   * @param degrees The degrees of the angle.
   * @return A new Angle which has the associated degree measurement.
   */
    public static Angle fromDegrees(double degrees) {
        return new Angle(Math.toRadians(degrees), degrees);
    }

    /**
   * Constructs a new Angle based on a circle radius, and an arc length.
   * @param circleRadius The radius of the circle.  May not be zero.
   * @param arcLength The arc length along the circle's circumference.
   * @return A new Angle which has the specified arc length for a circle which has the given circleRadius.
   */
    public static Angle fromArcLength(Distance circleRadius, Distance arcLength) {
        if (circleRadius.isZero()) {
            throw new IllegalArgumentException("circleRadius may not be zero");
        }
        double arcRadians = arcLength.dividedBy(circleRadius);
        return fromRadians(arcRadians);
    }

    /**
   * Gets the value of this Angle as expressed in degrees.
   * @return The degree value angular measurement.
   */
    public double getDegrees() {
        return degrees;
    }

    /**
   * Gets the value of this Angle, as expressed in radians.
   * @return The radian value angular measurement.
   */
    public double getRadians() {
        return radians;
    }

    /**
   * Returns a new Angle which represents the difference of this angular distance and the parameter (this - that).
   * <p>
   * Neither this angle nor the parameter are modified.
   * @param angle The angle to subtract.
   * @return A new Angle which represents (this - that).
   */
    public Angle subtract(Angle angle) {
        double newDegrees = degrees - angle.degrees;
        return Angle.fromDegrees(normalizeDegrees(newDegrees));
    }

    /**
   * Creates a new Angle which is the normalized version of this Angle.  
   * <p>
   * The normalized Angle is defined as 0 <= A < 360
   * @return A normalized Angle which is equivalent to this Angle.
   */
    public Angle normalize() {
        double normalizedDegrees = normalizeDegrees(this.degrees);
        return fromDegrees(normalizedDegrees);
    }

    private static double normalizeDegrees(double degrees) {
        double normalized = degrees % DEGREES_IN_A_CIRCLE;
        return (normalized >= 0) ? normalized : (normalized + DEGREES_IN_A_CIRCLE);
    }

    @Override
    public String toString() {
        return String.valueOf(degrees) + DEGREE_SYMBOL;
    }
}
