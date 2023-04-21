package net.sourceforge.ftgl.util;

/**
 * Implements an Vector in 3D space.
 * 
 * @author Ralf Petring
 * @author funsheep
 */
public class Vector3d implements Cloneable {

    /** The tolerance we accept when prooving length against a value (e.g. 1.0f). */
    public static final double TOLERANCE = 1E-6f;

    /** x component*/
    public double x;

    /** y component*/
    public double y;

    /** z component*/
    public double z;

    /**
	 * Constructs a new vector.
	 * 
	 * @param x the first coordinate
	 * @param y the second coordinate
	 * @param z the third coordinate
	 */
    public Vector3d(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
	 * Constructs a new vector.
	 * @param vector new vector's coordinates
	 */
    public Vector3d(final double[] vector) {
        this(vector[0], vector[1], vector[2]);
    }

    /**
	 * Constructs a new vector whith (0,0,0) as coordinates.
	 */
    public Vector3d() {
        this(0, 0, 0);
    }

    /**
	 * Copyconstructor.
	 * 
	 * @param v the vector to be copied
	 */
    public Vector3d(final Vector3d v) {
        this(v.x, v.y, v.z);
    }

    /**
	 * {@inheritDoc}
	 */
    public boolean equals(Object o) {
        if (o instanceof Vector3d) return this.equals((Vector3d) o);
        return false;
    }

    /**
	 * Compares this vector with the given vector.
	 * 
	 * @param v the vector to be used for compare
	 * @return true if this object is the same as the obj argument, false otherwise
	 */
    public boolean equals(Vector3d v) {
        if (v == null) return false;
        return (this.x == v.x && this.y == v.y && this.z == v.z);
    }

    /**
	 * {@inheritDoc}
	 */
    public int hashCode() {
        int i1 = Float.floatToIntBits((float) this.x);
        int i2 = Float.floatToIntBits((float) this.y);
        int i3 = Float.floatToIntBits((float) this.z);
        return i1 ^ ((i2 << 8) | (i2 >>> 24)) ^ ((i3 << 16) | (i3 >>> 16));
    }

    /**
	 * Adds the given double values to the coordinates of this vector.
	 * 
	 * @param dx the value to be added to the x coordinate
	 * @param dy the value to be added to the y coordinate
	 * @param dz the value to be added to the z coordinate
	 * @return this
	 */
    public Vector3d add(final double dx, final double dy, final double dz) {
        this.x += dx;
        this.y += dy;
        this.z += dz;
        return this;
    }

    /**
	 * Adds a given vector to this vector.
	 * 
	 * @param v the vector to be added
	 * @return this
	 */
    public Vector3d add(final Vector3d v) {
        return this.add(v.x, v.y, v.z);
    }

    /**
	 * Adds to this vector the given one with the given scale.
	 * @param v A vector to add.
	 * @param d The length of the vector, to be added.
	 * @return this
	 */
    public Vector3d addScaled(final Vector3d v, final double d) {
        return this.add(v.x * d, v.y * d, v.z * d);
    }

    /**
	 * Substracts a given vector from this vector.
	 * 
	 * @param v the vector to be substracted
	 * @return this
	 */
    public Vector3d sub(final Vector3d v) {
        return this.sub(v.x, v.y, v.z);
    }

    /**
	 * Substracts the given double values from the coordinates of this vector.
	 * 
	 * @param dx the value to be substracted from the x coordinate
	 * @param dy the value to be substracted from the y coordinate
	 * @param dz the value to be substracted from the z coordinate
	 * @return this
	 */
    public Vector3d sub(final double dx, final double dy, final double dz) {
        this.x -= dx;
        this.y -= dy;
        this.z -= dz;
        return this;
    }

    /**
	 * Multiplies the coordinates of this vector with the given double values.
	 * 
	 * @param dx the value to be multiplied the x coordinate
	 * @param dy the value to be multiplied the y coordinate
	 * @param dz the value to be multiplied the z coordinate
	 * @return this
	 */
    public Vector3d scale(final double dx, final double dy, final double dz) {
        this.x *= dx;
        this.y *= dy;
        this.z *= dz;
        return this;
    }

    /**
	 * Multiplies all coordinates of this vector with the given double value.
	 * 
	 * @param d the value to be multiplied with all coordinates
	 * @return this
	 */
    public Vector3d scale(final double d) {
        return this.scale(d, d, d);
    }

    /**
	 * Calculates the dotprodukt of this vector and the given vector.
	 * 
	 * @param v the vector to be used for calculation
	 * @return the dotproduct
	 */
    public final double dot(final Vector3d v) {
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }

    /**
	 * Normalizes this vector.
	 * 
	 * @return this
	 */
    public Vector3d normalize() {
        double l = 1.0d / this.length();
        if (l == 0) return this;
        this.x *= l;
        this.y *= l;
        this.z *= l;
        return this;
    }

    /**
	 * Calculates the angle between this vector and the given vector.
	 * 
	 * @param v the vector to be used for calculation
	 * @return the angle between this and v
	 */
    public double angle(final Vector3d v) {
        double dls = dot(v) / (this.length() * v.length());
        if (dls < -1.0d) dls = -1.0d; else if (dls > 1.0d) dls = 1.0d;
        return Math.acos(dls);
    }

    /**
	 * Sets all coordinates of this vector to zero.
	 * 
	 * @return this
	 */
    public Vector3d setZero() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        return this;
    }

    /**
	 * Copies all values from the given vector to this vector.
	 * 
	 * @param v the vector to be copied
	 * @return this
	 */
    public Vector3d set(final Vector3d v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        return this;
    }

    /**
	 * Sets the vector's components to the first 3 values
	 * in argument <code>array</code>.
	 * @param array array which contains the new values
	 * @return this
	 */
    public Vector3d set(final double[] array) {
        this.x = array[0];
        this.y = array[1];
        this.z = array[2];
        return this;
    }

    /**
	 * Negates all coordinates of this vector.
	 * 
	 * @return this
	 */
    public Vector3d negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        return this;
    }

    /**
	 * Returns the squared length of this vector.
	 * 
	 * @return the squared length of this vector
	 */
    public final double lengthSquared() {
        return (this.x * this.x + this.y * this.y + this.z * this.z);
    }

    /**
	 * Returns the length of this vector.
	 * 
	 * @return the length of this vector
	 */
    public final double length() {
        return Math.sqrt(this.lengthSquared());
    }

    /**
	 * Calculates the vector cross product of this vector and the given vector.
	 * 
	 * @param v the second vector
	 * @return this
	 */
    public final Vector3d cross(final Vector3d v) {
        final double a = this.y * v.z - this.z * v.y;
        final double b = v.x * this.z - v.z * this.x;
        final double c = this.x * v.y - this.y * v.x;
        this.x = a;
        this.y = b;
        this.z = c;
        return this;
    }

    /**
	 * Calculates the vector cross product of the two given vectors.
	 * <br><code>a x b = c</code> c is returned.
	 * @param a The first vector.
	 * @param b The second vector.
	 * @return A new vector, holding the cross product of a x b.
	 */
    public static final Vector3d cross(final Vector3d a, final Vector3d b) {
        return new Vector3d(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x);
    }

    /**
	 * {@inheritDoc}
	 */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("the roof is on fire", e);
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public String toString() {
        return "[" + this.x + "," + this.y + "," + this.z + "]";
    }

    /**
	 * Copies all components of this vector into the first 3 array components.
	 * @param dest array to be filled - may be <code>null</code>
	 * @return argument <code>dest</code> or a new array filled with this
	 * instances components.
	 */
    public double[] toArray(double[] dest) {
        if (dest == null) dest = new double[3];
        dest[0] = this.x;
        dest[1] = this.y;
        dest[2] = this.z;
        return dest;
    }
}
