package com.mapquest.spatialbase.geometry;

/**
 * Common set of operations available on 2d shapes.
 * @author tlaurenzomq
 *
 */
public interface Geometry2D {

    /**
	 * Translate according to the vector
	 * @param v
	 */
    public void translate(Vector2D v);

    /**
	 * Does the shape contain the given point
	 * @param p
	 * @return
	 */
    public boolean contains(Point2D p);

    /**
	 * Return the area
	 * @return
	 */
    public double area();

    /**
	 * Return the centroid
	 * @return
	 */
    public Point2D centroid();

    /**
	 * Returns information about how this shape intersects the given rectangle
	 * @param r
	 * @return
	 */
    public IntersectCase intersect(Rectangle r);
}
