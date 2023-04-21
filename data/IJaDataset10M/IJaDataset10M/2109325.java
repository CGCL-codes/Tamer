package jshomeorg.simplytrain.service;

/**
 * Rotatepoint, point with rotation angle - typically in radians
 * @author js
 */
public class rotatepoint extends point {

    double rot;

    /**
	 * Create rotatepoint
	 * @param _x X
	 * @param _y Y
	 * @param r angle in radians
	 */
    public rotatepoint(int _x, int _y, double r) {
        super(_x, _y);
        rot = r;
    }

    /**
	 * Create rotatepoint
	 * @param p point
	 * @param r angle in radians
	 */
    public rotatepoint(point p, double r) {
        super(p);
        rot = r;
    }

    /**
	 * Copy rotatepoint
	 * @param _t Copy
	 */
    public rotatepoint(rotatepoint _t) {
        super(_t);
        rot = _t.rot;
    }

    /**
	 * Angle
	 * @return angle in radians!
	 */
    public double getRotate() {
        return rot;
    }
}
