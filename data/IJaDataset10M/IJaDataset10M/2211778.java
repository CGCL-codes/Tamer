package util;

public interface BoundingVolume {

    /**
     * returns the closest intersection point, null if there is none.
     */
    public Point3 getIntersection(Point3 eye, Point3 direction);

    /**
     *
     */
    public Point3 getCenterPoint();

    /**
     * @return the intersection status of the parameter : 
     * DISJOINT,OVERLAP,INSIDE or DONT_KNOW
     */
    public int intersection(BoundingVolume p);

    public static final int OUTSIDE = 0;

    public static final int OVERLAP = 1;

    public static final int INSIDE = 2;

    public static final int DONT_KNOW = -1;

    public static final BoundingVolume NULL = new NullBoundingVolume();
}
