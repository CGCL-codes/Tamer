package barsuift.simLife.j3d.util;

import javax.vecmath.Point3f;

public final class BarycentreHelper {

    private BarycentreHelper() {
    }

    /**
     * Return a point on the line between startPoint and endPoint, located at the given distance from the startPoint.
     * <p>
     * The returned point is always on the line, between the 2 given points. If distance is too low (negative), then the
     * start point is returned. If distance is too high, then the end point is returned.
     * </p>
     * 
     * @param startPoint
     * @param endPoint
     * @param distance
     * @return
     */
    public static Point3f getBarycentre(Point3f startPoint, Point3f endPoint, float distance) {
        float maxDistance = startPoint.distance(endPoint);
        float distanceToUse = computeDistanceToUse(maxDistance, distance);
        float ratio = distanceToUse / maxDistance;
        Point3f result = new Point3f();
        result.interpolate(startPoint, endPoint, ratio);
        return result;
    }

    /**
     * Bounds the given distance between 0 and maxDistance
     * 
     * @param maxDistance
     * @param distance
     * @return
     */
    static float computeDistanceToUse(float maxDistance, float distance) {
        if (distance < 0) {
            return 0;
        }
        if (distance > maxDistance) {
            return maxDistance;
        }
        return distance;
    }
}
