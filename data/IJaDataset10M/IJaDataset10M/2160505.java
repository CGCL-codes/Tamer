package com.thegbomb.sphere;

/**
 *
 * @author Gary Jackson
 */
public class RotatedPoint extends Point {

    private Point original;

    /**
     * Creates a new instance of RotatedPoint
     */
    public RotatedPoint(Point rotated, Point original) {
        super(rotated.getTheta(), rotated.getPhi());
        this.original = original;
    }

    public RotatedPoint(Cartesian rotated, Point original) {
        super(rotated);
        this.original = original;
    }

    public Point getOriginal() {
        return this.original;
    }
}
