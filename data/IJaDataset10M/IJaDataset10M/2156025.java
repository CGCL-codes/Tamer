package netdev.base;

import java.io.Serializable;

public class Coords implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7081645135014822954L;

    double x;

    double y;

    double z;

    public double distanceTo(Coords other) {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2) + Math.pow(z - other.z, 2));
    }
}
