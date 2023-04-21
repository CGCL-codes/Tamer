package gpsmate.visuals.heightprofile;

import gpsmate.geodata.UtmPoint;

/**
 * HeightProfilePoint
 * 
 * @author longdistancewalker
 * 
 */
public class HeightProfilePoint extends UtmPoint {

    /**
   * Indicates the distance in meters from this point to the first point of the
   * track it belongs to.
   */
    private double distanceToStart;

    /**
   * @param northing
   * @param easting
   * @param utmZone
   */
    public HeightProfilePoint(double northing, double easting, String utmZone) {
        super(northing, easting, utmZone);
        this.distanceToStart = 0.0;
    }

    /**
   * @param northing
   * @param easting
   * @param utmZone
   */
    public HeightProfilePoint(UtmPoint other, double distanceToStart) {
        super(other);
        this.distanceToStart = distanceToStart;
    }

    /**
   * @param northing
   * @param easting
   * @param utmZone
   */
    public HeightProfilePoint(double northing, double easting, String utmZone, double elevation) {
        super(northing, easting, utmZone, elevation);
        this.distanceToStart = 0.0;
    }

    /**
   * @param northing
   * @param easting
   * @param utmZone
   */
    public HeightProfilePoint(double northing, double easting, String utmZone, double elevation, double distanceToStart) {
        super(northing, easting, utmZone, elevation);
        this.distanceToStart = distanceToStart;
    }

    /**
   * @return the distanceToStart
   */
    public double getDistanceToStart() {
        return distanceToStart;
    }

    /**
   * @param distanceToStart
   *          the distanceToStart to set
   */
    public void setDistanceToStart(double distanceToStart) {
        this.distanceToStart = distanceToStart;
    }
}
