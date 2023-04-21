package theba.visualizer;

/**
 * Implements a point in the 3D space which has a reference to another point.
 */
public class Point {

    public int x, y, z;

    public Point next;

    /**
	 * Constructs a new point on origo.
	 */
    public Point() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        next = null;
    }

    /**
	 * Constructs a new point with the given coordinates.
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param z
	 *            the z coordinate
	 */
    public Point(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        next = null;
    }

    /**
	 * Constructs a new point with the given coordinates and a reference to the
	 * next point.
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param z
	 *            the z coordinate
	 */
    public Point(int x, int y, int z, Point next) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.next = next;
    }
}
