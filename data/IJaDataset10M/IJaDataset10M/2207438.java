package Utilities;

/**
 *
 * @author  David
 */
public class Vector2D {

    public double x;

    public double z;

    /** Creates a new instance of 3DVector */
    public Vector2D(double newX, double newZ) {
        x = newX;
        z = newZ;
    }

    public void Normalise() {
        double length;
        length = Math.sqrt(x * x + z * z);
        if (length != 0) {
            x /= length;
            z /= length;
        } else {
            x = 0;
            z = 0;
        }
        return;
    }
}
