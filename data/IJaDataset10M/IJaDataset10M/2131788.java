package ch.epfl.lbd.trajectories;

import ch.epfl.lbd.database.spatial.GPSPoint;
import ch.epfl.lbd.database.spatial.LatLngPoint;

/**
 * its a stop event taken by a Moving Object
 * **/
public class Stop extends Episode {

    protected double mergingTolerance = 10;

    public Stop() {
        super();
        geom = new LatLngPoint();
    }

    public Stop(GPSPoint point) {
        super(point);
        geom = new LatLngPoint(point.getLat(), point.getLng());
    }

    @Override
    public boolean merge(Episode other) {
        if (other.getClass().equals(Stop.class)) {
            LatLngPoint point = (LatLngPoint) this.geom;
            if (other.getGeometry().distance(this.getGeometry()) < this.mergingTolerance) {
                double newLat = (point.getLat() + other.geom.getCentroid().getLat()) / 2;
                double newLng = (point.getLng() + other.geom.getCentroid().getLng()) / 2;
                geom = new LatLngPoint(newLat, newLng);
                return true;
            }
            logger.debug("The Stop is too far from the actual Stop");
            return false;
        }
        logger.error("Trying to Merge Stop and another Episode type");
        return false;
    }
}
