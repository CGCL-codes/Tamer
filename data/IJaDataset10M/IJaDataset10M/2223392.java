package net.aa3sd.SMT.geometry;

import org.apache.log4j.Logger;

/**
 * A distance on a bearing.
 * 
 * @author Paul J. Morris
 *
 */
public class Vector {

    private static final Logger log = Logger.getLogger(Vector.class);

    private double distance;

    private double bearing;

    /** 
	 * Default constructor, sets distance and bearing to zero.
	 */
    public Vector() {
        distance = 0d;
        bearing = 0d;
    }

    /**
	 * @param distance in meters
	 * @param bearing in degrees
	 */
    public Vector(double distance, double bearing) {
        super();
        this.distance = distance;
        this.bearing = bearing;
    }

    /**
	 * @return the bearing
	 */
    public double getBearing() {
        return bearing;
    }

    /**
	 * @param bearing the bearing to set
	 */
    public void setBearing(double bearing) {
        this.bearing = bearing;
    }

    /**
	 * @return the distance
	 */
    public double getDistance() {
        return distance;
    }

    /**
	 * @param distance the distance to set
	 */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj != null && obj.getClass() == this.getClass()) {
            if (((Vector) obj).getBearing() == this.bearing) {
                if ((int) ((Vector) obj).getDistance() == (int) this.distance) {
                    result = true;
                }
            }
        }
        return result;
    }
}
