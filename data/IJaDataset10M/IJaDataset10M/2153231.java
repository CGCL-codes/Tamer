package it.tukano.jps.math;

/**
 * Operations of 4D points
 */
public class Points4D {

    /**
     * Factory method
     * @return a new instance of Points4D
     */
    public static Points4D newInstance() {
        return new Points4D();
    }

    /**
     * Default no arg constructor
     */
    protected Points4D() {
    }

    /**
     * Extends a 3d point in 4d space.
     * @param point the point to extend
     * @return the 4d extension of the 3d point
     */
    public NTuple4 extend(NTuple3 point) {
        return new NTuple4(point.getX(), point.getY(), point.getZ(), 1);
    }
}
