package com.xenoage.util.math;

import java.util.ArrayList;
import java.util.List;
import com.xenoage.util.enums.VSide;

/**
 * This class contains the convex hull over a given list
 * of coordinates.
 * 
 * Either the top part or the bottom part of the hull is computed.
 * This is useful for computing forbidden areas for slurs for example.
 * 
 * It is assumed, that the points are sorted from left to right,
 * and that no points have the same x-coordinate. The y-axis
 * rises from bottom to top.
 * 
 * Here is an example for a top hull (<code>x</code> are the
 * given points):
 * 
 * <pre>
 *   x-------------x
 *  /           x   \
 * x       x        x\
 *    x               x
 * </pre>
 * 
 * @author Andreas Wenger
 */
public class ConvexHull {

    private enum ExtremumType {

        Min, Max
    }

    ;

    private final ArrayList<Point2f> points;

    private final VSide side;

    private ConvexHull(VSide side, ArrayList<Point2f> points) {
        this.side = side;
        this.points = points;
    }

    /**
	 * Computes the top or bottom convex hull of the given points.
	 * @param points  all points, x-coordinates sorted ascending
	 * @return  the points of the top hull
	 */
    public static ConvexHull create(List<Point2f> points, VSide side) {
        if (points.size() < 2) throw new IllegalArgumentException("At least two points must be given");
        ArrayList<Point2f> ret = new ArrayList<Point2f>();
        ret.add(points.get(0));
        int lastHullPointIndex = 0;
        while (lastHullPointIndex < points.size() - 1) {
            lastHullPointIndex = getExtremumGradientIndex(points, lastHullPointIndex, (side == VSide.Top ? ExtremumType.Max : ExtremumType.Min));
            ret.add(points.get(lastHullPointIndex));
        }
        ret.trimToSize();
        return new ConvexHull(side, ret);
    }

    /**
	 * Returns the index of the point with the most extreme gradient relative to the
	 * point with the given index.
	 * @param points        all points, x-coordinates sorted ascending
	 * @param fromIndex     reference point
	 * @param extremumType  1 to find the maximum, -1 to find the minimum
	 * @return  the index of the point with the highest gradient
	 */
    private static int getExtremumGradientIndex(List<Point2f> points, int fromIndex, ExtremumType extremumType) {
        if (points.size() < 2) throw new IllegalArgumentException("At least two points must be given");
        float maxGradient = Float.NEGATIVE_INFINITY;
        int maxGradientIndex = fromIndex + 1;
        for (int i = fromIndex + 1; i < points.size(); i++) {
            float gradient = (extremumType == ExtremumType.Min ? -1 : 1) * (points.get(i).y - points.get(fromIndex).y) / (points.get(i).x - points.get(fromIndex).x);
            if (gradient > maxGradient) {
                maxGradient = gradient;
                maxGradientIndex = i;
            }
        }
        return maxGradientIndex;
    }

    public VSide getSide() {
        return side;
    }

    public ArrayList<Point2f> getPoints() {
        return points;
    }
}
