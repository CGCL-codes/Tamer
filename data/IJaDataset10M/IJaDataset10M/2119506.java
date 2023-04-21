package org.opensourcephysics.numerics;

/**
 * A 3-element vector that is represented by double-precision floating point
 * x,y,z coordinates.
 */
public class Vec3D {

    public double x, y, z;

    /**
   * Constructs and initializes a Vector3d from the specified xyz coordinates.
   * @param x the x coordinate
   * @param y the y coordinate
   * @param z the z coordinate
   */
    public Vec3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
   * Constructs and initializes a Vector3d from the array of length 3.
   * @param v the array of length 3 containing xyz in order
   */
    public Vec3D(double[] v) {
        x = v[0];
        y = v[1];
        z = v[2];
    }

    /**
   * Constructs and initializes a Vector3d from the specified Vector3d.
   * @param v1 the Vector3d containing the initialization x y z data
   */
    public Vec3D(Vec3D v1) {
        x = v1.x;
        y = v1.y;
        z = v1.z;
    }

    /**
   * Constructs and initializes it to (0,0,0).
   */
    public Vec3D() {
        this(0, 0, 0);
    }

    /**
   *  Sets this vector to the vector subtraction of vectors v1 and v2.
   *  @param v1 the first vector
   *  @param v2 the second vector
   */
    public final void subtract(Vec3D v1, Vec3D v2) {
        this.x = v1.x - v2.x;
        this.y = v1.y - v2.y;
        this.z = v1.z - v2.z;
    }

    /**
   *  Sets this vector to the vector addition of vectors v1 and v2.
   *  @param v1 the first vector
   *  @param v2 the second vector
   */
    public final void add(Vec3D v1, Vec3D v2) {
        this.x = v1.x + v2.x;
        this.y = v1.y + v2.y;
        this.z = v1.z + v2.z;
    }

    /**
   *  Sets this vector to the vector cross product of vectors v1 and v2.
   *  @param v1 the first vector
   *  @param v2 the second vector
   */
    public void cross(Vec3D v1, Vec3D v2) {
        double x, y;
        x = v1.y * v2.z - v1.z * v2.y;
        y = v2.x * v1.z - v2.z * v1.x;
        this.z = v1.x * v2.y - v1.y * v2.x;
        this.x = x;
        this.y = y;
    }

    /**
   *  Sets this vector to the multiplication of vector v1 and a scalar number
   *  @param v1 the vector
   *  @param number to multiply v1
   */
    public void multiply(Vec3D v1, double number) {
        this.x = v1.x * number;
        this.y = v1.y * number;
        this.z = v1.z * number;
    }

    /**
   * Normalizes this vector in place.
   */
    public final void normalize() {
        double norm = this.x * this.x + this.y * this.y + this.z * this.z;
        if (norm < Util.defaultNumericalPrecision) {
            return;
        }
        if (norm == 1) {
            return;
        }
        norm = 1 / Math.sqrt(norm);
        this.x *= norm;
        this.y *= norm;
        this.z *= norm;
    }

    /**
   * Returns the dot product of this vector and vector v1.
   * @param v1 the other vector
   * @return the dot product of this and v1
   */
    public final double dot(Vec3D v1) {
        return (this.x * v1.x + this.y * v1.y + this.z * v1.z);
    }

    /**
   * Returns the squared magnitude of this vector.
   * @return the squared magnitude
   */
    public final double magnitudeSquared() {
        return (this.x * this.x + this.y * this.y + this.z * this.z);
    }

    /**
   * Returns the magnitude of this vector.
   * @return the magnitude
   */
    public final double magnitude() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    /**
   *    Returns the angle in radians between this vector and the vector
   *    parameter; the return value is constrained to the range [0,PI].
   *    @param v1    the other vector
   *    @return   the angle in radians in the range [0,PI]
   */
    public final double angle(Vec3D v1) {
        double vDot = this.dot(v1) / (this.magnitude() * v1.magnitude());
        if (vDot < -1.0) {
            vDot = -1.0;
        }
        if (vDot > 1.0) {
            vDot = 1.0;
        }
        return ((double) (Math.acos(vDot)));
    }
}
