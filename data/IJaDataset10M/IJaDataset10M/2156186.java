package com.jme3.voxel;

import java.io.Serializable;
import com.jme3.math.Vector3f;

/**
 * 3-dimensional vector of integers
 * 
 * @author Marius Dransfeld
 * 
 */
public final class Vector3i implements Serializable, Cloneable {

    private static final long serialVersionUID = 5562717088677334483L;

    public int x;

    public int y;

    public int z;

    /**
	 * creates a new vector
	 * 
	 * @param x
	 *            x component
	 * @param y
	 *            y component
	 * @param z
	 *            z component
	 */
    public Vector3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
	 * creates a zero vector
	 */
    public Vector3i() {
        x = 0;
        y = 0;
        z = 0;
    }

    /**
	 * copy construktor
	 */
    public Vector3i(Vector3f vec) {
        x = (int) vec.x;
        y = (int) vec.y;
        z = (int) vec.z;
    }

    public Vector3i(Vector3i vec) {
        x = vec.x;
        y = vec.y;
        z = vec.z;
    }

    public Vector3i(float x, float y, float z) {
        this.x = (int) x;
        this.y = (int) y;
        this.z = (int) z;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }

    public static Vector3i fromString(String s) {
        final String[] split = s.substring(1, s.length() - 1).split(" ");
        return new Vector3i(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Vector3i)) {
            return false;
        }
        final Vector3i vec = (Vector3i) other;
        return vec.x == x && vec.y == y && vec.z == z;
    }

    @Override
    public int hashCode() {
        int hash = 37;
        hash += 37 * hash + (x);
        hash += 37 * hash + (y);
        hash += 37 * hash + (z);
        return hash;
    }

    /**
	 * TODO docs
	 * 
	 * @param other
	 * @return
	 */
    public Vector3i addLocal(Vector3i other) {
        x += other.x;
        y += other.y;
        z += other.z;
        return this;
    }

    /**
	 * TODO docs
	 * 
	 * @param other
	 * @return
	 */
    public Vector3i subLocal(Vector3i other) {
        x -= other.x;
        y -= other.y;
        z -= other.z;
        return this;
    }

    /**
	 * TODO docs
	 * 
	 * @param skalar
	 * @return
	 */
    public Vector3i multLocal(int skalar) {
        x *= skalar;
        y *= skalar;
        z *= skalar;
        return this;
    }

    /**
	 * TODO docs
	 * 
	 * @param skalar
	 * @return
	 */
    public Vector3i divLocal(int skalar) {
        x /= skalar;
        y /= skalar;
        z /= skalar;
        return this;
    }

    /**
	 * TODO docs
	 * 
	 * @param other
	 * @return
	 */
    public Vector3i add(Vector3i other) {
        return new Vector3i(x + other.x, y + other.y, z + other.z);
    }

    /**
	 * TODO docs
	 * 
	 * @param other
	 * @return
	 */
    public Vector3i sub(Vector3i other) {
        return new Vector3i(x - other.x, y - other.y, z - other.z);
    }

    /**
	 * TODO docs
	 * 
	 * @param scalar
	 * @return
	 */
    public Vector3i mult(int scalar) {
        return new Vector3i(x * scalar, y * scalar, z * scalar);
    }

    /**
	 * TODO docs
	 * 
	 * @param scalar
	 * @return
	 */
    public Vector3i div(int scalar) {
        return new Vector3i(x / scalar, y / scalar, z / scalar);
    }

    @Override
    public Vector3i clone() {
        return new Vector3i(x, y, z);
    }

    /**
	 * creates a float vector
	 * 
	 * @return float vector
	 */
    public Vector3f toFloat() {
        return new Vector3f(x, y, z);
    }
}
