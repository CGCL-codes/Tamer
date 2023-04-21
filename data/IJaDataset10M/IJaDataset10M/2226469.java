package org.jdesktop.swingx.mapviewer.util;

import org.jdesktop.swingx.mapviewer.*;

/**
 * A utility class of methods that help when dealing
 * with standard Mercator projections.
 * @author joshua.marinacci@sun.com
 */
public final class MercatorUtils {

    /** Creates a new instance of MercatorUtils */
    private MercatorUtils() {
    }

    public static int longToX(double longitudeDegrees, double radius) {
        double longitude = Math.toRadians(longitudeDegrees);
        return (int) (radius * longitude);
    }

    public static int latToY(double latitudeDegrees, double radius) {
        double latitude = Math.toRadians(latitudeDegrees);
        double y = radius / 2.0 * Math.log((1.0 + Math.sin(latitude)) / (1.0 - Math.sin(latitude)));
        return (int) y;
    }

    public static double xToLong(int x, double radius) {
        double longRadians = x / radius;
        double longDegrees = Math.toDegrees(longRadians);
        int rotations = (int) Math.floor((longDegrees + 180) / 360);
        double longitude = longDegrees - (rotations * 360);
        return longitude;
    }

    public static double yToLat(int y, double radius) {
        double latitude = (Math.PI / 2) - (2 * Math.atan(Math.exp(-1.0 * y / radius)));
        return Math.toDegrees(latitude);
    }
}
