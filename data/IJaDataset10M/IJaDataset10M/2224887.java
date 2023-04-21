package org.dyn4j.geometry;

import org.dyn4j.Epsilon;
import org.dyn4j.resources.Messages;

/**
 * Represents a line {@link Segment}.
 * @author William Bittle
 * @version 3.0.2
 * @since 1.0.0
 */
public class Segment extends Wound implements Convex, Shape, Transformable {

    /** The segment length */
    protected double length;

    /**
	 * Full constructor.
	 * @param point1 the first point
	 * @param point2 the second point
	 * @throws NullPointerException if point1 or point2 is null
	 * @throws IllegalArgumentException if point1 == point2
	 */
    public Segment(Vector2 point1, Vector2 point2) {
        super();
        if (point1 == null) throw new NullPointerException(Messages.getString("geometry.segment.nullPoint1"));
        if (point2 == null) throw new NullPointerException(Messages.getString("geometry.segment.nullPoint2"));
        if (point1.equals(point2)) {
            throw new IllegalArgumentException(Messages.getString("geometry.segment.samePoint"));
        }
        this.vertices = new Vector2[2];
        this.vertices[0] = point1;
        this.vertices[1] = point2;
        this.normals = new Vector2[2];
        this.normals[0] = point1.to(point2).right();
        this.normals[0].normalize();
        this.normals[1] = point1.to(point2).left();
        this.normals[1].normalize();
        this.center = Geometry.getAverageCenter(this.vertices);
        this.length = point1.distance(point2);
        this.radius = this.length * 0.5;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Segment[").append(super.toString()).append("|Length=").append(this.length).append("|UserData=").append(this.userData).append("]");
        return sb.toString();
    }

    /**
	 * Returns point1 in local coordinates.
	 * @return {@link Vector2}
	 */
    public Vector2 getPoint1() {
        return this.vertices[0];
    }

    /**
	 * Returns point2 in local coordinates.
	 * @return {@link Vector2}
	 */
    public Vector2 getPoint2() {
        return this.vertices[1];
    }

    /**
	 * Returns the length of the line {@link Segment}.
	 * @return double
	 */
    public double getLength() {
        return this.length;
    }

    /**
	 * Determines where the point is relative to the given line.
	 * <pre>
	 * Set L = linePoint2 - linePoint1
	 * Set P = point - linePoint1
	 * location = L.cross(P)
	 * </pre>
	 * Returns 0 if the point lies on the line created from the line segment.<br />
	 * Assuming a right handed coordinate system:<br />
	 * Returns < 0 if the point lies on the right side of the line<br />
	 * Returns > 0 if the point lies on the left side of the line
	 * <p>
	 * Assumes all points are in world space.
	 * @param point the point
	 * @param linePoint1 the first point of the line
	 * @param linePoint2 the second point of the line
	 * @return double
	 */
    public static double getLocation(Vector2 point, Vector2 linePoint1, Vector2 linePoint2) {
        return (linePoint2.x - linePoint1.x) * (point.y - linePoint1.y) - (point.x - linePoint1.x) * (linePoint2.y - linePoint1.y);
    }

    /**
	 * Returns the point on the given line closest to the given point.
	 * <p>
	 * Project the point onto the line:
	 * <pre>
	 * V<sub>line</sub> = P<sub>1</sub> - P<sub>0</sub>
	 * V<sub>point</sub> = P<sub>0</sub> - P
	 * P<sub>closest</sub> = V<sub>point</sub>.project(V<sub>line</sub>)
	 * </pre>
	 * Assumes all points are in world space.
	 * @see Vector2#project(Vector2)
	 * @param point the point
	 * @param linePoint1 the first point of the line
	 * @param linePoint2 the second point of the line
	 * @return {@link Vector2}
	 */
    public static Vector2 getPointOnLineClosestToPoint(Vector2 point, Vector2 linePoint1, Vector2 linePoint2) {
        Vector2 p1ToP = point.difference(linePoint1);
        Vector2 line = linePoint2.difference(linePoint1);
        double ab2 = line.dot(line);
        if (ab2 <= Epsilon.E) return linePoint1.copy();
        double ap_ab = p1ToP.dot(line);
        double t = ap_ab / ab2;
        return line.multiply(t).add(linePoint1);
    }

    /**
	 * Returns the point on this line segment closest to the given point.
	 * <p>
	 * If the point closest to the given point on the line created by this
	 * line segment is not on the line segment then either of the segments
	 * end points will be returned.
	 * <p>
	 * Assumes all points are in world space.
	 * @see Segment#getPointOnLineClosestToPoint(Vector2, Vector2, Vector2)
	 * @param point the point
	 * @param linePoint1 the first point of the line
	 * @param linePoint2 the second point of the line
	 * @return {@link Vector2}
	 */
    public static Vector2 getPointOnSegmentClosestToPoint(Vector2 point, Vector2 linePoint1, Vector2 linePoint2) {
        Vector2 p1ToP = point.difference(linePoint1);
        Vector2 line = linePoint2.difference(linePoint1);
        double ab2 = line.dot(line);
        double ap_ab = p1ToP.dot(line);
        if (ab2 <= Epsilon.E) return linePoint1.copy();
        double t = ap_ab / ab2;
        t = Interval.clamp(t, 0.0, 1.0);
        return line.multiply(t).add(linePoint1);
    }

    @Override
    public Vector2[] getAxes(Vector2[] foci, Transform transform) {
        int size = foci != null ? foci.length : 0;
        Vector2[] axes = new Vector2[2 + size];
        int n = 0;
        Vector2 p1 = transform.getTransformed(this.vertices[0]);
        Vector2 p2 = transform.getTransformed(this.vertices[1]);
        axes[n++] = transform.getTransformedR(this.normals[1]);
        axes[n++] = transform.getTransformedR(this.normals[0].getLeftHandOrthogonalVector());
        Vector2 axis;
        for (int i = 0; i < size; i++) {
            Vector2 f = foci[i];
            if (p1.distanceSquared(f) < p2.distanceSquared(f)) {
                axis = p1.to(f);
            } else {
                axis = p2.to(f);
            }
            axis.normalize();
            axes[n++] = axis;
        }
        return axes;
    }

    @Override
    public Vector2[] getFoci(Transform transform) {
        return null;
    }

    @Override
    public boolean contains(Vector2 point, Transform transform) {
        Vector2 p = transform.getInverseTransformed(point);
        Vector2 p1 = this.vertices[0];
        Vector2 p2 = this.vertices[1];
        double value = Segment.getLocation(p, p1, p2);
        if (Math.abs(value) <= Epsilon.E) {
            double distSqrd = p1.distanceSquared(p2);
            if (p.distanceSquared(p1) <= distSqrd && p.distanceSquared(p2) <= distSqrd) {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
	 * Returns true if the given point is inside this {@link Shape}.
	 * <p>
	 * If the given point lies on an edge the point is considered
	 * to be inside the {@link Shape}.
	 * <p>
	 * The given point is assumed to be in world space.
	 * <p>
	 * If the radius is greater than zero then the point is tested to be
	 * within the shape expanded radially by the radius.
	 * @param point world space point
	 * @param transform {@link Transform} for this {@link Shape}
	 * @param radius the expansion radius; in the range [0, &infin;]
	 * @return boolean
	 */
    public boolean contains(Vector2 point, Transform transform, double radius) {
        if (radius <= 0) {
            return contains(point, transform);
        } else {
            Vector2 p = transform.getInverseTransformed(point);
            if (this.vertices[0].distanceSquared(p) <= radius * radius) {
                return true;
            } else if (this.vertices[1].distanceSquared(p) <= radius * radius) {
                return true;
            } else {
                Vector2 l = this.vertices[0].to(this.vertices[1]);
                Vector2 p1 = this.vertices[0].to(p);
                Vector2 p2 = this.vertices[1].to(p);
                if (l.dot(p1) > 0 && -l.dot(p2) > 0) {
                    double dist = p1.project(l.getRightHandOrthogonalVector()).getMagnitudeSquared();
                    if (dist <= radius * radius) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Interval project(Vector2 n, Transform transform) {
        double v = 0.0;
        Vector2 p1 = transform.getTransformed(this.vertices[0]);
        Vector2 p2 = transform.getTransformed(this.vertices[1]);
        double min = n.dot(p1);
        double max = min;
        v = n.dot(p2);
        if (v < min) {
            min = v;
        } else if (v > max) {
            max = v;
        }
        return new Interval(min, max);
    }

    @Override
    public Vector2 getFarthestPoint(Vector2 n, Transform transform) {
        Vector2 p1 = transform.getTransformed(this.vertices[0]);
        Vector2 p2 = transform.getTransformed(this.vertices[1]);
        double dot1 = n.dot(p1);
        double dot2 = n.dot(p2);
        if (dot1 >= dot2) {
            return p1;
        } else {
            return p2;
        }
    }

    /**
	 * Returns the feature farthest in the direction of n.
	 * <p>
	 * For a {@link Segment} it's always the {@link Segment} itself.
	 * @param n the direction
	 * @param transform the local to world space {@link Transform} of this {@link Convex} {@link Shape}
	 * @return {@link Edge}
	 */
    @Override
    public Edge getFarthestFeature(Vector2 n, Transform transform) {
        Vector2 max = null;
        Vector2 p1 = transform.getTransformed(this.vertices[0]);
        Vector2 p2 = transform.getTransformed(this.vertices[1]);
        double dot1 = n.dot(p1);
        double dot2 = n.dot(p2);
        int index = 0;
        if (dot1 >= dot2) {
            max = p1;
            index = 0;
        } else {
            max = p2;
            index = 1;
        }
        Vertex vp1 = new Vertex(p1, 0);
        Vertex vp2 = new Vertex(p2, 1);
        Vertex vm = new Vertex(max, index);
        if (p1.to(p2).right().dot(n) > 0) {
            return new Edge(vp2, vp1, vm, p2.to(p1), 0);
        } else {
            return new Edge(vp1, vp2, vm, p1.to(p2), 0);
        }
    }

    @Override
    public void rotate(double theta, double x, double y) {
        super.rotate(theta, x, y);
        this.vertices[0].rotate(theta, x, y);
        this.vertices[1].rotate(theta, x, y);
        this.normals[0].rotate(theta, x, y);
        this.normals[1].rotate(theta, x, y);
    }

    @Override
    public void translate(double x, double y) {
        super.translate(x, y);
        this.vertices[0].add(x, y);
        this.vertices[1].add(x, y);
    }

    /**
	 * Creates a {@link Mass} object using the geometric properties of
	 * this {@link Segment} and the given density.
	 * <pre>
	 * m = d * length
	 * I = l<sup>2</sup> * m / 12
	 * </pre>
	 * @param density the density in kg/m<sup>2</sup>
	 * @return {@link Mass} the {@link Mass} of this {@link Segment}
	 */
    @Override
    public Mass createMass(double density) {
        double length = this.length;
        double mass = density * length;
        double inertia = 1.0 / 12.0 * length * length * mass;
        return new Mass(this.center, mass, inertia);
    }

    @Override
    public AABB createAABB(Transform transform) {
        double vx = 0.0;
        double vy = 0.0;
        Vector2 p = transform.getTransformed(this.vertices[0]);
        double minX = Vector2.X_AXIS.dot(p);
        double maxX = minX;
        double minY = Vector2.Y_AXIS.dot(p);
        double maxY = minY;
        p = transform.getTransformed(this.vertices[1]);
        vx = Vector2.X_AXIS.dot(p);
        vy = Vector2.Y_AXIS.dot(p);
        minX = Math.min(minX, vx);
        maxX = Math.max(maxX, vx);
        minY = Math.min(minY, vy);
        maxY = Math.max(maxY, vy);
        return new AABB(minX, minY, maxX, maxY);
    }
}
