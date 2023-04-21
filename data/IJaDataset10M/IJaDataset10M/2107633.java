package checkers3d.presentation;

/**
 * Class for managing a 2D vector as floats.
 * 
 * @author Ruben Acuna
 */
public class Vector {

    /**
     * The vector's x component.
     */
    public float x;

    /**
     * The vector's y component.
     */
    public float y;

    /**
     * Class consturctor that creates a vector of value 0,0.
     */
    public Vector() {
        this(0f, 0f);
    }

    /**
     * Class consturctor that creates a vector with a given value.
     */
    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns a new instance of this vector that has been normalized.
     *
     * @return Normalized copy.
     */
    public Vector getNormalizedCopy() {
        Vector normalized = new Vector();
        float length = getLength();
        normalized.x = x / length;
        normalized.y = y / length;
        return normalized;
    }

    /**
     * Returns a new instance of this vector that has been scaled.
     * 
     * @param scalar Scalar value.
     * @return Returns a new instance of this vector that has been scaled.
     */
    public Vector getScaledCopy(float scalar) {
        Vector scaled = new Vector();
        scaled.x = x * scalar;
        scaled.y = y * scalar;
        return scaled;
    }

    /**
     * Returns the length of this vector.
     *
     * @return Vector length.
     */
    public float getLength() {
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Modifies this vector by adding the values of another vector's components
     * to it.
     *
     * @param dxdy Vector with components for translation.
     */
    public void translate(Vector dxdy) {
        x += dxdy.x;
        y += dxdy.y;
    }

    /**
     * Returns a new vector instance with the same components as this one.
     *
     * @return Clone of vector.
     */
    @Override
    public Vector clone() {
        Vector copy = new Vector();
        copy.x = x;
        copy.y = y;
        return copy;
    }

    /**
     * Returns a string representation of this object containing it's class name
     * and vector components.
     *
     * @return String representation.
     */
    @Override
    public String toString() {
        return getClass().getName() + " -> x: " + x + " y: " + y;
    }
}
