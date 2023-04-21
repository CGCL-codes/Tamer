package us.wthr.jdem846.gis.projections;

import us.wthr.jdem846.gis.exceptions.MapProjectionException;
import us.wthr.jdem846.logging.Log;
import us.wthr.jdem846.logging.Logging;

public class WagnerVIProjection extends AbstractBaseProjection {

    @SuppressWarnings("unused")
    private static Log log = Logging.getLog(WagnerVIProjection.class);

    public WagnerVIProjection() {
    }

    public WagnerVIProjection(double north, double south, double east, double west, double width, double height) {
        super(north, south, east, west, width, height);
    }

    @Override
    public void project(double latitude, double longitude, double elevation, MapPoint point) throws MapProjectionException {
        point.column = longitude * Math.sqrt(1 - 3 * Math.pow(latitude / Math.PI, 2));
        point.row = latitude;
    }
}
