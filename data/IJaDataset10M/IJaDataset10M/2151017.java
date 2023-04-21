package fr.scenerenderer.objects3d;

import java.util.List;
import fr.scenerenderer.objects3d.boundingvolumes.BoundingVolume;
import fr.scenerenderer.Intersection;
import fr.scenerenderer.Ray;
import fr.scenerenderer.geometry.Point3D;
import fr.scenerenderer.geometry.Vector3D;
import fr.scenerenderer.RGBColor;

public abstract class Object3D implements Transformable {

    public static final float EPSILON = 0.0001f;

    public static final float NO_INTERSECTION = -1f;

    protected BoundingVolume boundingVolume;

    /**
     * Get accessor for boundingVolume
     * @return  value of boundingVolume
     */
    public BoundingVolume getBoundingVolume() {
        return this.boundingVolume;
    }

    /**
     * Set accessor for boundingVolume
     * @param value the value to set in boundingVolume
     */
    public void setBoundingVolume(BoundingVolume value) {
        this.boundingVolume = value;
    }

    protected Object3D() {
    }

    public abstract Intersection searchIntersection(final Ray ray);

    public abstract float searchIntersectionDistance(final Ray ray);

    public abstract boolean isIntersected(final Ray ray);

    public abstract List<Intersection> searchIntersections(final Ray ray);

    public abstract boolean isInside(final Point3D point);

    public abstract Vector3D getNormal(final Point3D point);

    public abstract RGBColor getColor(final Point3D point);

    public String toString() {
        return this.getClass().getName();
    }

    public abstract void generatePolyhedron();

    public abstract void generateBoundingVolume();

    public boolean boundingVolumeIsIntersected(final Ray ray) {
        return this.getBoundingVolume().isIntersected(ray);
    }

    public void generateBoundingSphere(final Point3D[] points) {
    }

    public void searchNearestIntersection(final Intersection[] intersections, final Point3D point) {
    }
}
