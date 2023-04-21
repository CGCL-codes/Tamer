package org.chargecar.gpx;

/**
 * @author Chris Bartley (bartley@cmu.edu)
 */
public interface DistanceCalculator {

    /**
    * Returns the distance between the given {@link GPSCoordinate}s in meters.  Returns 0 if either (or both)
    * {@link GPSCoordinate} is <code>null</code>.
    */
    double compute2DDistance(final GPSCoordinate t1, final GPSCoordinate t2);
}
