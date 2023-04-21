package homura.hde.core.scene.intersection;

import homura.hde.util.maths.FastMath;
import homura.hde.util.maths.Ray;
import homura.hde.util.maths.Vector3f;

/**
 * <code>Distance</code> provides functional methods for determining the
 * distances between one object and another. These methods are static to allow
 * for easy calling.
 *
 * @author Mark Powell
 * @version $Id: Distance.java,v 1.12 2006/01/13 19:39:50 renanse Exp $
 */
public class Distance {

    private Distance() {
    }

    /**
     * <code>distance</code> calculates the distance between two points.
     *
     * @param point1 the first point to test.
     * @param point2 the second point to test.
     * @return the distance between the two points.
     */
    public static float distance(Vector3f point1, Vector3f point2) {
        return FastMath.sqrt(distanceSquared(point1, point2));
    }

    /**
     * <code>distanceSquared</code> returns the distance between two points,
     * with the distance squared. This allows for faster comparisons if relation
     * is important but actual distance is not.
     *
     * @param p1 the first point to test.
     * @param p2 the second point to test.
     * @return the distance squared between two points.
     */
    public static float distanceSquared(Vector3f p1, Vector3f p2) {
        return ((p1.x - p2.x) * (p1.x - p2.x)) + ((p1.y - p2.y) * (p1.y - p2.y)) + ((p1.z - p2.z) * (p1.z - p2.z));
    }

    /**
     * <code>distance</code> calculates the distance between a point and a ray.
     *
     * @param point the point to test.
     * @param ray   the ray to test.
     * @return the distance between the point and the ray.
     */
    public static float distance(Vector3f point, Ray ray) {
        Vector3f diff = point.subtract(ray.getOrigin());
        float t = diff.dot(ray.getDirection());
        if (t <= 0.0) {
            t = 0.0f;
        } else {
            t /= ray.getDirection().lengthSquared();
            diff.subtractLocal(ray.getDirection().mult(t));
        }
        return diff.length();
    }
}
