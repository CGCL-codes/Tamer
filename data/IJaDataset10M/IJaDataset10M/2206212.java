package org.mars_sim.msp.core.mars;

import org.mars_sim.msp.core.Coordinates;
import org.mars_sim.msp.core.time.MarsClock;
import java.io.Serializable;

/** 
 * The OrbitInfo class keeps track of the orbital position of Mars 
 */
public class OrbitInfo implements Serializable {

    private static final double ORBIT_PERIOD = 59355072D;

    private static final double ECCENTRICITY = .093D;

    private static final double TILT = .4396D;

    private static final double SOLAR_DAY = 88775.244D;

    private static final double ORBIT_AREA = 9.5340749D;

    private double orbitTime;

    private double theta;

    private double radius;

    private Coordinates sunDirection;

    /** Constructs an OrbitInfo object */
    public OrbitInfo() {
        orbitTime = 0D;
        theta = 0D;
        radius = 1.665732D;
        sunDirection = new Coordinates((Math.PI / 2D) + TILT, Math.PI);
    }

    /** 
     * Adds time to the orbit
     * @param millisols time added (millisols)
     */
    public void addTime(double millisols) {
        double seconds = MarsClock.convertMillisolsToSeconds(millisols);
        orbitTime += seconds;
        while (orbitTime > ORBIT_PERIOD) orbitTime -= ORBIT_PERIOD;
        double area = ORBIT_AREA * orbitTime / ORBIT_PERIOD;
        double areaTemp = 0D;
        if (area > (ORBIT_AREA / 2D)) areaTemp = area - (ORBIT_AREA / 2D); else areaTemp = (ORBIT_AREA / 2D) - area;
        theta = Math.abs(2D * Math.atan(1.097757562D * Math.tan(.329512059D * areaTemp)));
        if (area < (ORBIT_AREA / 2D)) theta = 0D - theta;
        theta += Math.PI;
        if (theta >= (2 * Math.PI)) theta -= (2 * Math.PI);
        radius = 1.510818924D / (1 + (ECCENTRICITY * Math.cos(theta)));
        double sunTheta = sunDirection.getTheta();
        sunTheta -= (2D * Math.PI) * (seconds / SOLAR_DAY);
        while (sunTheta < 0D) sunTheta += 2D * Math.PI;
        sunDirection.setTheta(sunTheta);
        double sunPhi = (Math.PI / 2D) + (Math.sin(theta + (Math.PI / 2D)) * TILT);
        sunDirection.setPhi(sunPhi);
    }

    /** Returns the theta angle of Mars's orbit.
     *  Angle is clockwise starting at aphelion.
     *  @return the theta angle of Mars's orbit
     */
    public double getTheta() {
        return theta;
    }

    /** Returns the radius of Mars's orbit in A.U.
     * @return the radius of Mars's orbit
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Gets the current distance to the Sun.
     * @return distance in Astronomical Units (A.U.)
     */
    public double getDistanceToSun() {
        return radius;
    }

    /** 
     * Gets the Sun's angle from a given phi (latitude).
     * @param phi location in radians (0 - PI).
     * @return angle in radians (0 - PI).
     */
    public double getSunAngleFromPhi(double phi) {
        return Math.abs(phi - sunDirection.getPhi());
    }

    /** The point on the surface of Mars perpendicular to the Sun as Mars rotates. 
     *  @return the surface point on Mars perpendicular to the sun
     */
    public Coordinates getSunDirection() {
        return sunDirection;
    }

    /**
     * Prepare object for garbage collection.
     */
    public void destroy() {
        sunDirection = null;
    }
}
