package titanic.basic;

/**
 * This class represents a 2D vector
 * and provides basic operations of vector algebra.
 * @author danon
 */
public class Vector2D {

    private float x, y;

    public Vector2D() {
        x = 0;
        y = 0;
    }

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D v) {
        x = v.getX();
        y = v.getY();
    }

    /**
     * @return the x
     */
    public float getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public float getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Multiply current vector by a scalar float value c.
     * @return The result of multiplication.
     */
    public Vector2D multiply(float c) {
        return new Vector2D(getX() * c, getY() * c);
    }

    /**
     * Scalar multiplication of this vector and <code>v</code>
     * @return The result of the multiplication.
     */
    public float multiply(Vector2D v) {
        return v.getX() * getX() + v.getY() * getY();
    }

    /**
     * Summ of specified vector v and this vector.
     * @return Result of the summ
     */
    public Vector2D add(Vector2D v) {
        return new Vector2D(getX() + v.getX(), getY() + v.getY());
    }
}
