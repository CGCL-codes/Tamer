package xmage.math;

/** Class representing double-precision infinite line in 3D. */
public class Line3d {

    /** Point that lies on line. */
    public final Point3d a = new Point3d();

    /** Another point that lies on line. */
    public final Point3d b = new Point3d(0.0, 0.0, 1.0);

    /**
	 * Create new line.
	 *
	 * <p>
	 * Default line is specified by points <code>(0.0, 0.0, 0.0)</code>
	 * and <code>(0.0, 0.0, 1.0)</code>.
	 * </p>
	 */
    public Line3d() {
    }

    /**
	 * Create new line by specifying two points lying on it.
	 *
	 * @param a first point lying on line
	 * @param b second point lying on line
	 */
    public Line3d(Point3d a, Point3d b) {
        this(a.x, a.y, a.z, b.x, b.y, b.z);
    }

    /**
	 * Create new line by specifying two points lying on it.
	 *
	 * @param ax X coordinate of first point lying on line
	 * @param ay Y coordinate of first point lying on line
	 * @param az Z coordinate of first point lying on line
	 * @param bx X coordinate of second point lying on line
	 * @param by Y coordinate of second point lying on line
	 * @param bz Z coordinate of second point lying on line
	 */
    public Line3d(double ax, double ay, double az, double bx, double by, double bz) {
        a.set(ax, ay, az);
        b.set(bx, by, bz);
    }

    /**
	 * Find point on line nearest to specified point.
	 *
	 * @param x point X coordinate 
	 * @param y point Y coordinate
	 * @param z point Z coordinate
	 * @return point on line nearest to specified point
	 */
    public Point3d nearest(double x, double y, double z) {
        return nearest(x, y, z, new Point3d());
    }

    /**
	 * Find point on line nearest to specified point.
	 *
	 * @param point point
	 * @return point on line nearest to specified point
	 */
    public Point3d nearest(Point3d point) {
        return nearest(point.x, point.y, point.z, new Point3d());
    }

    /**
	 * Find point on line nearest to specified point.
	 *
	 * @param x point X coordinate
	 * @param y point Y coordinate
	 * @param z point Z coordinate
	 * @param nearest point in which computed nearest point will be stored;
	 *        this is the object which will be returned from the method
	 * @return point on line nearest to specified point
	 */
    public Point3d nearest(double x, double y, double z, Point3d nearest) {
        return nearest.set(b).sub(a).mul(nearestRatio(x, y, z)).add(a);
    }

    /**
	 * Find point on line nearest to specified point.
	 *
	 * @param point point
	 * @param nearest point in which computed nearest point will be stored;
	 *        this is the object which will be returned from the method
	 * @return point on line nearest to specified point
	 */
    public Point3d nearest(Point3d point, Point3d nearest) {
        return nearest(point.x, point.y, point.z, nearest);
    }

    /**
	 * Return position of point relative to two points line is built upon.
	 * 
	 * @param x point X coordinate
	 * @param y point Y coordinate
	 * @param z point Z coordinate
	 * @return position of point relative to two points line is built upon
	 */
    protected double nearestRatio(double x, double y, double z) {
        double xx = (x - a.x) * (b.x - a.x);
        double yy = (y - a.y) * (b.y - a.y);
        double zz = (z - a.z) * (b.z - a.z);
        double d = Math.abs(b.distanceSq(a));
        if (d == 0.0) {
            throw new IllegalArgumentException("Line points cannot coincide");
        }
        return (xx + yy + zz) / d;
    }

    /**
	 * Return common string representation of this class.
	 *
	 * @return string representation of this class
	 */
    @Override
    public String toString() {
        return "[a=" + a + " b=" + b + "]";
    }
}
