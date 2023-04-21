package joelib2.gui.render3D.math.geometry;

/**
 * A geometric vector class that provides vector computations: length, normalize, dot and cross.
 * note: Vector is a coordinate independent concept. Translating
 *   a vector in 3D (or 2D) space yields the same vector.
 *
 * @.author   Zhidong Xie (zxie@tripos.com)
 * @.author     wegnerj
 * @.license    GPL
 * @.cvsversion    $Revision: 1.6 $, $Date: 2005/02/17 16:48:33 $
 */
public class GeoVector3D {

    /**
     * X component of the vector
     */
    protected double x = 0.0;

    /**
     * Y component of the vector
     */
    protected double y = 0.0;

    /**
     * Z component of the vector
     */
    protected double z = 0.0;

    /**
     * Default constructor, all component are 0.0
     */
    public GeoVector3D() {
    }

    /**
     * copy constructor
     *
     * @param gv  the vector to be copied
     */
    public GeoVector3D(GeoVector3D gv) {
        x = gv.x;
        y = gv.y;
        z = gv.z;
    }

    /**
     * constructor
     *
     * @param p1  start point of the vector
     * @param p2  end   point of the vector
     */
    public GeoVector3D(Point3D p1, Point3D p2) {
        x = p2.getX() - p1.getX();
        y = p2.getY() - p1.getY();
        z = p2.getZ() - p1.getZ();
    }

    /**
     * Full constructor
     *
     * @param x  Description of the Parameter
     * @param y  Description of the Parameter
     * @param z  Description of the Parameter
     */
    public GeoVector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * rotat the vector by a factor
     *
     * add a vector to this vector
     *
     * @param gv      Description of the Parameter
     */
    public void add(GeoVector3D gv) {
        x += gv.x;
        y += gv.y;
        z += gv.z;
    }

    /**
     * cross product with another vector
     *
     * @param gv  the other vector to cross
     * @return    (GeoVector3D) this cross gv
     */
    public GeoVector3D cross(GeoVector3D gv) {
        double iComponent = (y * gv.z) - (gv.y * z);
        double jComponent = (z * gv.x) - (gv.z * x);
        double kComponent = (x * gv.y) - (gv.x * y);
        return new GeoVector3D(iComponent, jComponent, kComponent);
    }

    /**
     * dot product with another vector
     *
     * @param gv  the other vector to dot
     * @return    the (double) this dot gv
     */
    public double dot(GeoVector3D gv) {
        return ((x * gv.x) + (y * gv.y) + (z * gv.z));
    }

    /**
     * Return component x
     *
     * @return   The x value
     */
    public double getX() {
        return x;
    }

    /**
     * Return component y
     *
     * @return   The y value
     */
    public double getY() {
        return y;
    }

    /**
     * Return component z
     *
     * @return   The z value
     */
    public double getZ() {
        return z;
    }

    /**
     * calculate the length of the vector
     *
     * @return   Description of the Return Value
     */
    public double length() {
        return Math.sqrt((x * x) + (y * y) + (z * z));
    }

    /**
     * subtract a vector from this vector
     *
     * @param gv  the other vector to subtract
     */
    public void minus(GeoVector3D gv) {
        x -= gv.x;
        y -= gv.y;
        z -= gv.z;
    }

    /**
     * change the vector's length so that it becomes a unit vector
     */
    public void normalize() {
        double len = length();
        if (len > Double.MIN_VALUE) {
            x /= len;
            y /= len;
            z /= len;
        } else {
            x = 0.0;
            y = 0.0;
            z = 0.0;
        }
    }

    /**
     * scale the vector by a factor
     *
     * @param factor  the scaling factor
     */
    public void scale(double factor) {
        x *= factor;
        y *= factor;
        z *= factor;
    }

    /**
     * Set component x
     *
     * @param x  x component
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Set component y
     *
     * @param y  y component
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Set component z
     *
     * @param z  z component
     */
    public void setZ(double z) {
        this.z = z;
    }
}
