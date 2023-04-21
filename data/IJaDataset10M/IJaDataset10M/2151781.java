package com.jmbaai.bombsight.tech.physics;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globes.Globe;
import com.jmbaai.bombsight.tech.math.MathUtils;
import com.jmbaai.bombsight.tech.math.Quantitative;

/**
 * An immutable geocentric position expressed as latitude,
 * longitude, and radius. Latitude is an angle with zero at the
 * equator, increasing north, with range -90 to 90 deg. Longitude is an angle
 * with zero at the prime meridian, increasing east, with a range -180 to <180
 * deg. Radius is the geocentric distance from the origin (globe center).
 * @author Jon Barrilleaux of JMB and Associates, Inc.
 */
public class GeocentricPosition implements Quantitative {

    public GeocentricPosition(Angle latitude, Angle longitude, double radius) {
        if (latitude == null) throw new IllegalArgumentException();
        if (longitude == null) throw new IllegalArgumentException();
        if (radius < 0) throw new IllegalArgumentException();
        Angle lat = MathUtils.normalizeLatitude(latitude);
        Angle lon = MathUtils.normalizeLongitude(latitude, longitude);
        _latitude = lat;
        _longitude = lon;
        _radius = radius;
    }

    public GeocentricPosition(Position pos) {
        this(pos.getLatitude(), pos.getLongitude(), pos.getElevation());
    }

    /**
	 * Gets the normalized and constrained latitude angle.
	 * @return Never null.
	 */
    public Angle getLatitude() {
        return _latitude;
    }

    /**
	 * Gets the normalized and constrained longitude angle.
	 * @return Never null.
	 */
    public Angle getLongitude() {
        return _longitude;
    }

    /**
	 * Gets the radial distance from the origin (globe center).
	 * @return The result.
	 */
    public double getRadius() {
        return _radius;
    }

    /**
	 * Gets the geodetic latitude of this geocentric position.
	 * @param globe Temp input globe.  Never null.
	 * @return The result.
	 */
    public Angle getGeodeticLatitude(Globe globe) {
        if (globe == null) throw new IllegalArgumentException();
        return MathUtils.toGeodeticLatitude(globe, _latitude);
    }

    @Override
    public String toString(String format) {
        return "[" + String.format("lat=" + format, _latitude.getDegrees()) + " " + String.format("lon=" + format, _longitude.getDegrees()) + " " + String.format("rad=" + format, _radius) + "]";
    }

    @Override
    public boolean equalsValue(Object obj, double tolerance) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final GeocentricPosition other = (GeocentricPosition) obj;
        double deltaDeg = Math.abs(tolerance * 360.0);
        if (Math.abs(_latitude.getDegrees() - other._latitude.getDegrees()) > deltaDeg) return false;
        if (Math.abs(_longitude.getDegrees() - other._longitude.getDegrees()) > deltaDeg) return false;
        if (Math.abs(_radius - other._radius) > tolerance) return false;
        return true;
    }

    @Override
    public String toString() {
        return toString("%f");
    }

    private final Angle _latitude;

    private final Angle _longitude;

    private final double _radius;

    /**
	 * Zero latitude, longitude, radius.
	 */
    public static final GeocentricPosition ZERO = new GeocentricPosition(Angle.ZERO, Angle.ZERO, 0.0);
}
