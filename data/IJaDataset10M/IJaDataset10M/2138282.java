package royere.cwi.util;

import java.text.DecimalFormat;

/**
* A simple 3D vector class. Nothing fancy, just the usual.
*/
public class Vector3D implements java.io.Serializable {

    /** Formatter for toString() */
    protected static DecimalFormat df = new DecimalFormat();

    static {
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);
    }

    public double x;

    public double y;

    public double z;

    public Vector3D(double X, double Y, double Z) {
        x = X;
        y = Y;
        z = Z;
    }

    public Vector3D(double X, double Y) {
        x = X;
        y = Y;
        z = 0.0;
    }

    public Vector3D() {
        x = 0.0;
        y = 0.0;
        z = 0.0;
    }

    public Vector3D(Vector3D v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public void setValue(double X, double Y, double Z) {
        x = X;
        y = Y;
        z = Z;
    }

    public void setValue(double X, double Y) {
        x = X;
        y = Y;
    }

    public void setValue(Vector3D v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    /**
    * Accessor method for coordinates of the vector.
    */
    public double getX() {
        return x;
    }

    /**
    * Accessor method for coordinates of the vector.
    */
    public double getY() {
        return y;
    }

    /**
    * Accessor method for coordinates of the vector.
    */
    public double getZ() {
        return z;
    }

    /**
    * Add a vector to the current
    */
    public void add(Vector3D v) {
        x += v.x;
        y += v.y;
        z += v.z;
    }

    /**
    * Substract a vector from the current
    */
    public void sub(Vector3D v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
    }

    /**
    * Create a sum vector
    */
    public static Vector3D add(Vector3D v1, Vector3D v2) {
        return new Vector3D(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }

    /**
    * Create a difference vector
    */
    public static Vector3D sub(Vector3D v1, Vector3D v2) {
        return new Vector3D(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    /**
    * Quadratic distance from a vector
    */
    public double quadDistance(Vector3D v) {
        return (v.x - x) * (v.x - x) + (v.y - y) * (v.y - y) + (v.z - z) * (v.z - z);
    }

    /**
    * Distance from a vector
    */
    public double distance(Vector3D v) {
        return Math.sqrt(quadDistance(v));
    }

    /**
    * Distance from a vector
    */
    public double distance(double X, double Y, double Z) {
        return Math.sqrt((x - X) * (x - X) + (y - Y) * (y - Y) + (z - Z) * (z - Z));
    }

    /**
    * Quadratic norm of the vector
    */
    public double quadNorm() {
        return x * x + y * y + z * z;
    }

    /**
    * Norm of the vector
    */
    public double norm() {
        return Math.sqrt(quadNorm());
    }

    /**
    * Multiply with a scalar
    */
    public void mult(double d) {
        x *= d;
        y *= d;
        z *= d;
    }

    /**
    * Scalar multiplication with a vector
    */
    public double mult(Vector3D v) {
        return x * v.x + y * v.y + z * v.z;
    }

    /**
    * Scalar multiplication of two vectors
    */
    public static double mult(Vector3D v1, Vector3D v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    /**
     * Returns a point halfway between this point and another point.
     */
    public Vector3D midpoint(Vector3D other) {
        double midX = (this.x + other.x) / 2.0;
        double midY = (this.y + other.y) / 2.0;
        double midZ = (this.z + other.z) / 2.0;
        Vector3D midpoint = new Vector3D(midX, midY, midZ);
        return midpoint;
    }

    /**
    * Clone the vector
    */
    public Vector3D duplicate() {
        return new Vector3D(x, y, z);
    }

    /**
    * Clone with the signature defined by Java
    */
    public Object clone() {
        return duplicate();
    }

    /**
    * Return a unit Vector in the same direction
    */
    public Vector3D unit() {
        return newWithLength(1.0);
    }

    /**
    * Return a vector in the same direction and of a specific length
    */
    public Vector3D newWithLength(double d) {
        if (x == 0.0 && y == 0.0 && z == 0.0) {
            return new Vector3D();
        } else {
            Vector3D retval = duplicate();
            retval.mult(d / norm());
            return retval;
        }
    }

    /**
    * Equality. Overrides an inherited fuction.
    * @param theOther
    */
    public boolean equals(Object theOther) {
        if (theOther instanceof Vector3D) {
            Vector3D v = (Vector3D) theOther;
            return x == v.x && y == v.y && z == v.z;
        } else {
            return super.equals(theOther);
        }
    }

    /**
    * Override the inherited function. Handy for debug
    */
    public String toString() {
        return "(x,y,z)=(" + df.format(x) + "," + df.format(y) + "," + df.format(z) + ")";
    }
}
