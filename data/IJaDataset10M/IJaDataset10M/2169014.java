package com.thegbomb.sphere;

/**
 * Represents a plane intersecting the origin.
 * @author Gary Jackson
 */
public class Plane {

    private Point normal;

    /**
     * Creates a new instance of Plane given two points on the unit spehere and the origin. This assumes CCW order from a to b to produce the plane normal vector.
     * @param a first point on the unit sphere
     * @param b second point on the unit sphere
     * @throws SphereException when the given points are identical, yielding no single plane
     */
    public Plane(Point a, Point b) {
        if (a.equals(b)) throw new SphereException("points are identical");
        this.normal = a.normal(b);
    }

    /**
     * Creates a new instance of Plane given the normal vector for the plane.
     * @param normal normal vector for the new plane
     */
    public Plane(Point normal) {
        this.normal = normal;
    }

    /**
     * Get the normal vector for this Plane.
     * @return the normal vector for this plane
     */
    public Point getNormal() {
        return this.normal;
    }

    /**
     * Returns true if the planes are equal, ignoring the direction of the normal vector.
     * @param c Any plane
     * @return whether or not the planes are strictly equal, ignoring the direction of the normal vector
     */
    public boolean equals(Plane c) {
        Point cNorm = c.getNormal();
        return this.normal.equals(cNorm) || this.normal.isAntipodal(cNorm);
    }

    public int hashCode() {
        return this.normal.toString().hashCode() ^ this.normal.antipode().hashCode();
    }

    /**
     * Returns true if the planes are equal within some given error, ignoring the direction of the normal vector.
     * @param c any plane
     * @param epsilon allowed error -- Planes are considered equal if any part of the normal vectors are within epsilon of each other
     * @return whether or not the planes are strictly equal, ignoring the direction of the normal vector
     */
    public boolean equals(Plane c, double epsilon) {
        Point cNorm = c.getNormal();
        return this.normal.equals(cNorm, epsilon) || this.normal.isAntipodal(cNorm, epsilon);
    }

    /**
     * Computes the dihedral angle between this plane and a given plane
     * @param c any plane
     * @return the dihedral angle between this plane and c
     */
    public double angle(Plane c) {
        return this.normal.distance(c.getNormal());
    }
}
