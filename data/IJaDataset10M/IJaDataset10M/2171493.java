package cmjTracer.bvh;

import cmjTracer.math.Vec3;
import cmjTracer.raytracer.basic.Ray;
import cmjTracer.raytracer.objects.SceneObject;

/** 
 * An infinite bounding volume is simply a bounding volume containing all points.
 * @author Jens-Fabian Goetzmann
 * @version 0.2
 * @since 0.2
 *
 */
public class InfiniteBoundingVolume implements BoundingVolume {

    public InfiniteBoundingVolume() {
    }

    public boolean contains(Vec3 point) {
        return true;
    }

    public void enclose(SceneObject object) {
    }

    public void enclose(SceneObject[] objects) {
    }

    public void enclose(BoundingVolume volume) {
    }

    public void enclose(BoundingVolume[] volumes) {
    }

    public void enlarge(BoundingVolume volume) {
    }

    public Vec3 getMax() {
        return new Vec3(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public Vec3 getMin() {
        return new Vec3(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
    }

    public boolean occludes(Ray ray) {
        return true;
    }

    public double getVolume() {
        return Double.POSITIVE_INFINITY;
    }
}
