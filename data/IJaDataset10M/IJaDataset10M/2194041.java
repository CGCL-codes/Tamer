package checkers3d.logic;

/**
 * Point3D represents a point in 3D space using x, y, and z coordinates.
 *
 * @author      Sean Keel
 */
public class Point3D {

    public int x;

    public int y;

    public int z;

    public Point3D() {
        this(0, 0, 0);
    }

    /**
     * The class constructor which creates a point at 0,0,0.
     *
     * @param x The value of the point along the x axis.
     * @param y The value of the point along the y axis.
     * @param z The value of the point along the z axis.
     */
    public Point3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Transforms this Point3D by adding another Point3D's
     * coordinates to this one.
     *
     * @param transformer A Point3D that is used to transform the orginal location.
     * @return  A Point3D of this class's transformed point.
     */
    public Point3D transform(Point3D transformer) {
        return new Point3D(x + transformer.x, y + transformer.y, z + transformer.z);
    }

    /**
     * Returns a string of information about this class containing
     * the class name.
     *
     * @return  Returns a string representation of this class.
     */
    @Override
    public String toString() {
        return this.getClass().toString() + " - x:" + x + " y:" + y + " z:" + z;
    }
}
