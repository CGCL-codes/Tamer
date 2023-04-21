package org.dinopolis.gpstool.util;

import java.lang.IllegalStateException;
import org.dinopolis.gpstool.util.angle.Angle;
import org.dinopolis.gpstool.util.angle.AngleFormat;
import org.dinopolis.gpstool.util.angle.Latitude;
import org.dinopolis.gpstool.util.angle.Longitude;

/**
 * This class serves as a helper class to allow all modules to use the
 * same format for speed, angles (latitude, longitude), distances, ...
 *
 * @author Christof Dallermassl
 * @version $Revision: 777 $
 */
public class UnitHelper {

    public static final int UNIT_SYSTEM_METRIC = 1;

    public static final int UNIT_SYSTEM_MILES = 2;

    public static final int UNIT_SYSTEM_NAUTIC = 3;

    public static final double KM2MILES = 0.62137119;

    public static final double KM2NAUTIC = 0.54;

    public static final double METER2FEET = 3.280839895;

    protected int unit_system_ = UNIT_SYSTEM_METRIC;

    protected String angle_format_;

    protected String heading_format_;

    protected AngleFormat angle_formatter_;

    protected AngleFormat heading_formatter_;

    /**
 * Empty constructor.
 */
    public UnitHelper() {
    }

    /**
 * Constructor setting the unit system to use (METRIC, MILES, NAUTIC).
 *
 * @param unit_system the unit system to use.
 * @throws IllegalArgumentException if the unit system is not one of
 * the allowed values.
 */
    public UnitHelper(int unit_system) throws IllegalArgumentException {
        this();
        setUnitSystem(unit_system);
    }

    /**
 * Set the unit system to use (METRIC, MILES, NAUTIC).
 *
 * @param unit_system the unit system to use.
 * @throws IllegalArgumentException if the unit system is not one of
 * the allowed values.
 */
    public void setUnitSystem(int unit_system) throws IllegalArgumentException {
        if ((unit_system < 1) || (unit_system > 3)) throw new IllegalArgumentException("Unit System must be one of 'UNIT_SYSTEM_METRIC','UNIT_SYSTEM_MILES', or 'UNIT_SYSTEM_NAUTIC'");
        unit_system_ = unit_system;
    }

    /**
 * Returns the unit system used (METRIC, MILES, NAUTIC).
 *
 * @return the unit system used (METRIC, MILES, NAUTIC).
 */
    public int getUnitSystem() {
        return (unit_system_);
    }

    /**
 * Set the format for angles (latitude, longitude) to use.
 *
 * @param angle_format the angle format to use.
 * @see org.dinopolis.gpstool.util.angle.AngleFormat
 */
    public void setAngleFormat(String angle_format) {
        angle_format_ = angle_format;
        angle_formatter_ = new AngleFormat(angle_format_);
    }

    /**
 * Returns the format for angles (latitude, longitude).
 *
 * @return the format for angles (latitude, longitude).
 * @see org.dinopolis.gpstool.util.angle.AngleFormat
 */
    public String getAngleFormat() {
        return (angle_format_);
    }

    /**
 * Set the format for heading to use.
 *
 * @param heading_format the heading format to use.
 * @see org.dinopolis.gpstool.util.angle.AngleFormat
 */
    public void setHeadingFormat(String heading_format) {
        heading_format_ = heading_format;
        heading_formatter_ = new AngleFormat(heading_format_);
    }

    /**
 * Returns the format for heading.
 *
 * @return the format for heading.
 * @see org.dinopolis.gpstool.util.angle.AngleFormat
 */
    public String getHeadingFormat() {
        return (heading_format_);
    }

    /**
 * Formats the given latitude by the use of the angle format.
 *
 * @param latitude the latitude to format
 * @return the formatted string for the given value.
 * @see org.dinopolis.gpstool.util.angle.AngleFormat
 * @see #setAngleFormat(String)
 */
    public String formatLatitude(double latitude) {
        return (angle_formatter_.format(new Latitude(latitude)));
    }

    /**
 * Formats the given longitude by the use of the angle format.
 *
 * @param longitude the longitude to format
 * @return the formatted string for the given value.
 * @see org.dinopolis.gpstool.util.angle.AngleFormat
 * @see #setAngleFormat(String)
 */
    public String formatLongitude(double longitude) {
        return (angle_formatter_.format(new Longitude(longitude)));
    }

    /**
 * Formats the given angle (latitude or longitude) by the use of the
 * angle format.
 *
 * @param angle the angle to format
 * @return the formatted string for the given value.
 * @see org.dinopolis.gpstool.util.angle.AngleFormat
 * @see #setAngleFormat(String)
 */
    public String formatAngle(double angle) {
        return (angle_formatter_.format(new Angle(angle)));
    }

    /**
 * Formats the given heading by the use of the heading format.
 *
 * @param heading_in_deg the heading in degrees.
 * @return the formatted string for the given value.
 * @see org.dinopolis.gpstool.util.angle.AngleFormat
 * @see #setAngleFormat(String)
 */
    public String formatHeading(double heading_in_deg) {
        return (heading_formatter_.format(new Angle(heading_in_deg)));
    }

    /**
 * Formats the given speed given in kilomters per hour using the
 * previously set unit system and the correct unit (e.g. "10.0" for
 * UNIT_SYSTEM_MILES returns "6.2mph").
 *
 * @param speed_in_kmh the speed in kilometers per hour.
 * @return the formatted string for the given value.
 */
    public String formatSpeed(double speed_in_kmh) {
        return (getValueString(getSpeed(speed_in_kmh)) + getSpeedUnit());
    }

    /**
 * Formats the given distance given in kilometers using the previously
 * set unit system and the correct unit (e.g. "10.0" for
 * UNIT_SYSTEM_MILES returns "6.2miles").
 *
 * @param distance_in_km the distance in km
 * @return the formatted string for the given value.
 */
    public String formatDistance(double distance_in_km) {
        return (getValueString(getDistance(distance_in_km)) + getDistanceUnit());
    }

    /**
 * Formats the given alitude in meters using the previously
 * set unit system and the correct unit (e.g. "10.0" for
 * UNIT_SYSTEM_MILES returns "32ft").
 *
 * @param altitude_in_m altitude in meters.
 * @return the formatted string for the given value.
 */
    public String formatAltitude(double altitude_in_m) {
        return (getValueString(getAltitude(altitude_in_m)) + getAltitudeUnit());
    }

    /**
 * Converts the given altitude in meters using the previously set unit
 * system (e.g. "10.0" for UNIT_SYSTEM_MILES
 * returns "32.808398").
 *
 * @param altitude_in_m altitude in meters.
 * @return the converted value depending on the unit system.
 */
    public double getAltitude(double altitude_in_m) {
        switch(unit_system_) {
            case UNIT_SYSTEM_METRIC:
                return (altitude_in_m);
            case UNIT_SYSTEM_MILES:
            case UNIT_SYSTEM_NAUTIC:
                return (altitude_in_m * METER2FEET);
        }
        throw new IllegalStateException("Illegal Unit System");
    }

    /**
 * Converts the given distance in kilomters using the previously set
 * unit system (e.g. "10.0" for UNIT_SYSTEM_MILES returns
 * "6.2137119").
 *
 * @param distance_in_km distance in km.
 * @return the converted value depending on the unit system.
 */
    public double getDistance(double distance_in_km) {
        switch(unit_system_) {
            case UNIT_SYSTEM_METRIC:
                return (distance_in_km);
            case UNIT_SYSTEM_MILES:
                return (distance_in_km * KM2MILES);
            case UNIT_SYSTEM_NAUTIC:
                return (distance_in_km * KM2NAUTIC);
        }
        throw new IllegalStateException("Illegal Unit System");
    }

    /** Converts the given number to a string. The number of digits after
 * the comma depends on the value (no digits if higher than 100.0, one
 * digit if less than 100.0).
 *
 * @param value the value to convert
 * @return the converted value.
 */
    public String getValueString(double value) {
        if (value > 100.0) return (String.valueOf((int) Math.round(value)));
        return (String.valueOf((int) (Math.round(value * 10)) / 10.0));
    }

    /**
 * Converts the given speed in kilomters per hour using the previously
 * set unit system (e.g. "10.0" for UNIT_SYSTEM_MILES returns
 * "6.2137119").
 *
 * @param speed_in_kmh speed in kmh
 * @return the converted value depending on the unit system.
 */
    public double getSpeed(double speed_in_kmh) {
        return (getDistance(speed_in_kmh));
    }

    /**
 * Returns the unit for speed for the unit system previously set
 * ("kmh","mph","knots").
 *
 * @return the the unit for speed for the unit system previously set
 * ("kmh","mph","knots").
 */
    public String getSpeedUnit() {
        switch(unit_system_) {
            case UNIT_SYSTEM_METRIC:
                return ("km/h");
            case UNIT_SYSTEM_MILES:
                return ("mph");
            case UNIT_SYSTEM_NAUTIC:
                return ("knots");
        }
        throw new IllegalStateException("Illegal Unit System");
    }

    /**
 * Returns the unit for altitude for the unit system previously set
 * ("m","ft","ft").
 *
 * @return  the unit for altitude for the unit system previously set
 * ("m","ft","ft").
 */
    public String getAltitudeUnit() {
        switch(unit_system_) {
            case UNIT_SYSTEM_METRIC:
                return ("m");
            case UNIT_SYSTEM_MILES:
            case UNIT_SYSTEM_NAUTIC:
                return ("ft");
        }
        throw new IllegalStateException("Illegal Unit System");
    }

    /**
 * Returns the unit for distance for the unit system previously set
 * ("km","mi","nmi").
 *
 * @return the unit for distance for the unit system previously set
 * ("km","mi","nmi").
 */
    public String getDistanceUnit() {
        switch(unit_system_) {
            case UNIT_SYSTEM_METRIC:
                return ("km");
            case UNIT_SYSTEM_MILES:
                return ("mi");
            case UNIT_SYSTEM_NAUTIC:
                return ("nmi");
        }
        throw new IllegalStateException("Illegal Unit System");
    }

    /**
 * Returns the factor between kilometers and the chosen unit for
 * distances or speed (miles, nautic).
 *
 * @return the factor between kilometers and the chosen unit for
 * distances or speed (miles, nautic).
 */
    public double getDistanceOrSpeedFactor() {
        switch(unit_system_) {
            case UNIT_SYSTEM_METRIC:
                return (1.0);
            case UNIT_SYSTEM_MILES:
                return (KM2MILES);
            case UNIT_SYSTEM_NAUTIC:
                return (KM2NAUTIC);
        }
        throw new IllegalStateException("Illegal Unit System");
    }
}
