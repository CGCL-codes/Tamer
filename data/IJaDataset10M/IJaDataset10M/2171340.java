package vegetation3d.math;

/**
 * Data class for vector in Euclidean space R^3
 * @author janczar
 */
public class Vector3d {

    private double x, y, z;

    /**
     * Creates new vector of coordinates (0,0,0).
     */
    public Vector3d() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }

    /**
     * Creates new vector as copy of another vector.
     * @param v Vector to copy
     */
    public Vector3d(Vector3d v) {
        this.x = v.getX();
        this.y = v.getY();
        this.z = v.getZ();
    }

    /**
     * Creates new vector of given coordinates
     * @param x X coordinate of new vector
     * @param y Y coordinate of new vector
     * @param z Z coordinate of new vector
     */
    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns X coordinate of this vector.
     * @return X coordinate of vector
     */
    public double getX() {
        return x;
    }

    /**
     * Sets X coordinate of this vector.
     * @param x X coordinate of vector
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Returns Y coordinate of this vector.
     * @return Y coordinate of vector
     */
    public double getY() {
        return y;
    }

    /**
     * Sets Y coordinate of this vector.
     * @param y Y coordinate of vector
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Returns Z coordinate of this vector.
     * @return Z coordinate of vector
     */
    public double getZ() {
        return z;
    }

    /**
     * Sets Z coordinate of this vector.
     * @param z Z coordinate of vector
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * Returns length of this vector.
     * @return Length of vector
     */
    public double getLength() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Sets coordinates of this vector as copy of another vector.
     * @param v Vector to copy
     */
    public void set(Vector3d v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    /**
     * Rotates this vector by specified angle around axis defined by given vector.
     * @param axis Axis vector
     * @param angle Angle in radians
     */
    public void rotateAndSet(Vector3d axis, double angle) {
        Matrix4 matrix = Transformation.getRotationMatrix(axis.getX(), axis.getY(), axis.getZ(), angle);
        Transformation.transformVector(this, matrix);
        correct();
    }

    /**
     * Normalizes this vector (divides every coordinate so length of vector
     * is exactly 1.0 and it's direction is not changed).
     */
    public void normalize() {
        double length = Math.sqrt(x * x + y * y + z * z);
        x = x / length;
        y = y / length;
        z = z / length;
    }

    /**
     * Multiplies vector v by number and returns result as new vector.
     * @param v Vector to multiply
     * @param n Number to multiply by
     * @return New vector w that w = v*n
     */
    public static Vector3d mul(Vector3d v, double n) {
        return new Vector3d(v.x * n, v.y * n, v.z * n);
    }

    /**
     * Multiplies current vector by number.
     * @param n Number to multiply by
     */
    public void mulAndSet(double n) {
        x = x * n;
        y = y * n;
        z = z * n;
    }

    /**
     * Returns new vector as cross product of vectors v1 and v2.
     * @param v1 Vector 1
     * @param v2 Vector 2
     * @return Cross product of v1 and v2
     */
    public static Vector3d cross(Vector3d v1, Vector3d v2) {
        double x = v1.getY() * v2.getZ() - v1.getZ() * v2.getY();
        double y = v1.getZ() * v2.getX() - v1.getX() * v2.getZ();
        double z = v1.getX() * v2.getY() - v1.getY() * v2.getX();
        return new Vector3d(x, y, z);
    }

    /**
     * Returns dot product of vectors v1 and v2.
     * @param v1 Vector 1
     * @param v2 Vector 2
     * @return Dot product of v1 and v2
     */
    public static double dot(Vector3d v1, Vector3d v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();
    }

    /**
     * Sets this vector as result of multiplication of vectors v1 and v2
     * @param v1 Vector 1
     * @param v2 Vector 2
     */
    public void mulAndSet(Vector3d v1, Vector3d v2) {
        double nx = v1.getY() * v2.getZ() - v1.getZ() * v2.getY();
        double ny = v1.getZ() * v2.getX() - v1.getX() * v2.getZ();
        double nz = v1.getX() * v2.getY() - v1.getY() * v2.getX();
        x = nx;
        y = ny;
        z = nz;
        correct();
    }

    /**
     * Makes correction of all coordinates to avoid calculation errors like
     * 0.999999999999999998 instead of 1.0
     */
    public void correct() {
        x = x * 1048576.0;
        x = Math.rint(x);
        x = x / 1048576.0;
        y = y * 1048576.0;
        y = Math.rint(y);
        y = y / 1048576.0;
        z = z * 1048576.0;
        z = Math.rint(z);
        z = z / 1048576.0;
    }
}
