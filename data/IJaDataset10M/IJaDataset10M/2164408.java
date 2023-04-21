package it.tukano.jps.math;

/**
 * 3D points functions.
 */
public class Points3D {

    /**
     * Factory method. Creates a new instanceof Points3D
     * @return a new instance of Points3D
     */
    public static Points3D newInstance() {
        return new Points3D();
    }

    private final NTuple3 ORIGIN = new NTuple3(0, 0, 0);

    /**
     * Default no arg constructor
     */
    protected Points3D() {
    }

    /**
     * Return the origin of the axes
     * @return the origin of the axes
     * <p><i>Memory consistency effects:</i>access to final immutable field</p>
     */
    public NTuple3 getOrigin() {
        return ORIGIN;
    }

    /**
     * Returns the distance between two points.
     * @param p0 the first point
     * @param p1 the second point
     * @return the distance between p0 and p1
     */
    public Number distance(NTuple3 p0, NTuple3 p1) {
        final double dx = p1.getX().doubleValue() - p0.getX().doubleValue();
        final double dy = p1.getY().doubleValue() - p0.getY().doubleValue();
        final double dz = p1.getZ().doubleValue() - p0.getZ().doubleValue();
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2) + Math.pow(dz, 2));
    }

    /**
     * Rotates a point using a quaternion
     * @param p the point to rotate
     * @param q the rotation expressed as a quaternion
     * @param JPSDefaults.quaternions() the quaternions functions to use
     * @return a new point expressing p rotated by q
     */
    public NTuple3 rotate(NTuple3 p, NTuple4 q) {
        NTuple4 conjQ = JPSDefaults.quaternions().conjugate(q);
        NTuple4 q1 = new NTuple4(p.getX(), p.getY(), p.getZ(), 0);
        NTuple4 r = JPSDefaults.quaternions().multiply(JPSDefaults.quaternions().multiply(q, q1), conjQ);
        return new NTuple3(r.getX(), r.getY(), r.getZ());
    }

    /**
     * Adds to points.
     * @param a the first point
     * @param b the second point
     * @return the point c = a + b
     */
    public NTuple3 add(NTuple3 a, NTuple3 b) {
        double x = a.getX().doubleValue() + b.getX().doubleValue();
        double y = a.getY().doubleValue() + b.getY().doubleValue();
        double z = a.getZ().doubleValue() + b.getZ().doubleValue();
        return new NTuple3(x, y, z);
    }

    /**
     * Returns a string representation of a point
     * @param point a NTuple3 expressing a point
     * @return a string representing the given point
     */
    public String toString(NTuple3 point) {
        return String.format("Point(%s, %s, %s)", point.getX(), point.getY(), point.getZ());
    }
}
