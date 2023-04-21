package com.vividsolutions.jts.awt;

import java.awt.geom.Point2D;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * Transforms a geometry {@link Coordinate} into a Java2D Point,
 * possibly with a mathematical transformation of the ordinate values.
 * Transformation from a model coordinate system to a view coordinate system 
 * can be efficiently performed by supplying an appropriate transformation.
 * 
 * @author Martin Davis
 */
public interface PointTransformation {

    /**
	 * Transforms a {@link Coordinate} into a Java2D Point.
	 * 
	 * @param src the source Coordinate 
	 * @param dest the destination Point
	 */
    public void transform(Coordinate src, Point2D dest);
}
