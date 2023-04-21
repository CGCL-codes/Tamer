package org.dyn4j.geometry;

import org.dyn4j.resources.Messages;

/**
 * Represents a {@link Rectangle} (either axis aligned or oriented).
 * <p>
 * A {@link Rectangle} cannot have a width or height of zero.
 * @author William Bittle
 * @version 3.0.2
 * @since 1.0.0
 */
public class Rectangle extends Polygon implements Shape, Transformable {

    /** The {@link Rectangle}'s width */
    protected double width;

    /** The {@link Rectangle}'s height */
    protected double height;

    /**
	 * Full constructor.
	 * <p>
	 * The center of the {@link Rectangle} is (0, 0).
	 * @param width the width
	 * @param height the height
	 * @throws IllegalArgumentException if width or height is less than or equal to zero
	 */
    public Rectangle(double width, double height) {
        if (width <= 0.0) throw new IllegalArgumentException(Messages.getString("geometry.rectangle.invalidWidth"));
        if (height <= 0.0) throw new IllegalArgumentException(Messages.getString("geometry.rectangle.invalidHeight"));
        this.vertices = new Vector2[] { new Vector2(-width * 0.5, -height * 0.5), new Vector2(width * 0.5, -height * 0.5), new Vector2(width * 0.5, height * 0.5), new Vector2(-width * 0.5, height * 0.5) };
        this.normals = new Vector2[] { new Vector2(0.0, -1.0), new Vector2(1.0, 0.0), new Vector2(0.0, 1.0), new Vector2(-1.0, 0.0) };
        this.center = Geometry.getAverageCenter(this.vertices);
        this.radius = this.center.distance(this.vertices[0]);
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Rectangle[").append(super.toString()).append("|Width=").append(this.width).append("|Height=").append(this.height).append("]");
        return sb.toString();
    }

    /**
	 * Returns the height.
	 * @return double
	 */
    public double getHeight() {
        return this.height;
    }

    /**
	 * Returns the width.
	 * @return double
	 */
    public double getWidth() {
        return this.width;
    }

    /**
	 * Returns the rotation about the local center in radians.
	 * @return double the rotation in radians
	 * @since 3.0.1
	 */
    public double getRotation() {
        return this.normals[1].getAngleBetween(Vector2.X_AXIS);
    }

    @Override
    public Vector2[] getAxes(Vector2[] foci, Transform transform) {
        int fociSize = foci != null ? foci.length : 0;
        Vector2[] axes = new Vector2[2 + fociSize];
        int n = 0;
        axes[n++] = transform.getTransformedR(this.normals[1]);
        axes[n++] = transform.getTransformedR(this.normals[2]);
        for (int i = 0; i < fociSize; i++) {
            Vector2 focus = foci[i];
            Vector2 closest = transform.getTransformed(this.vertices[0]);
            double d = focus.distanceSquared(closest);
            for (int j = 1; j < 4; j++) {
                Vector2 vertex = this.vertices[j];
                vertex = transform.getTransformed(vertex);
                double dt = focus.distanceSquared(vertex);
                if (dt < d) {
                    closest = vertex;
                    d = dt;
                }
            }
            Vector2 axis = focus.to(closest);
            axis.normalize();
            axes[n++] = axis;
        }
        return axes;
    }

    @Override
    public boolean contains(Vector2 point, Transform transform) {
        Vector2 p = transform.getInverseTransformed(point);
        Vector2 c = this.center;
        Vector2 p1 = this.vertices[0];
        Vector2 p2 = this.vertices[1];
        Vector2 p4 = this.vertices[3];
        double widthSquared = p1.distanceSquared(p2);
        double heightSquared = p1.distanceSquared(p4);
        Vector2 projectAxis0 = p1.to(p2);
        Vector2 projectAxis1 = p1.to(p4);
        Vector2 toPoint = c.to(p);
        if (toPoint.project(projectAxis0).getMagnitudeSquared() <= (widthSquared * 0.25)) {
            if (toPoint.project(projectAxis1).getMagnitudeSquared() <= (heightSquared * 0.25)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Interval project(Vector2 axis, Transform transform) {
        Vector2 center = transform.getTransformed(this.center);
        Vector2 projectAxis0 = transform.getTransformedR(this.normals[1]);
        Vector2 projectAxis1 = transform.getTransformedR(this.normals[2]);
        double c = center.dot(axis);
        double e = (this.width * 0.5) * Math.abs(projectAxis0.dot(axis)) + (this.height * 0.5) * Math.abs(projectAxis1.dot(axis));
        return new Interval(c - e, c + e);
    }

    /**
	 * Creates a {@link Mass} object using the geometric properties of
	 * this {@link Rectangle} and the given density.
	 * <pre>
	 * m = d * h * w
	 * I = m * (h<sup>2</sup> + w<sup>2</sup>) / 12
	 * </pre>
	 * @param density the density in kg/m<sup>2</sup>
	 * @return {@link Mass} the {@link Mass} of this {@link Rectangle}
	 */
    @Override
    public Mass createMass(double density) {
        double height = this.height;
        double width = this.width;
        double mass = density * height * width;
        double inertia = mass * (height * height + width * width) / 12.0;
        return new Mass(this.center, mass, inertia);
    }
}
