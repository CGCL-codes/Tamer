package skycastle.function;

import skycastle.util.MathUtils;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * An one dimensional function, created by interpolating between a number of guidepoints.
 *
 * @author Hans H�ggstr�m
 */
public class InterpolatedFunction1D implements Function1D, Serializable {

    private List<Point2D> myGuidePoints = new ArrayList<Point2D>(5);

    private static final Comparator<Point2D> GUIDE_POINT_COMPARATOR = new Comparator<Point2D>() {

        /**
         * @return a negative integer, zero, or a positive integer as the
         *         first argument is less than, equal to, or greater than the
         *         second.
         */
        public int compare(final Point2D o1, final Point2D o2) {
            int result = 0;
            if (o1.getX() < o2.getX()) {
                result = -1;
            } else if (o1.getX() > o2.getX()) {
                result = 1;
            }
            return result;
        }
    };

    private static final long serialVersionUID = -1830419044766310767L;

    public InterpolatedFunction1D() {
    }

    @SuppressWarnings({ "MethodWithMultipleReturnPoints" })
    public float getValueAt(float sampleDiameter, float x) {
        double value = 0;
        Point2D previousPoint = null;
        for (Point2D guidePoint : myGuidePoints) {
            if (x <= guidePoint.getX()) {
                if (previousPoint == null) {
                    return (float) guidePoint.getY();
                } else {
                    return MathUtils.interpolate(x, (float) previousPoint.getX(), (float) guidePoint.getX(), (float) previousPoint.getY(), (float) guidePoint.getY());
                }
            }
            value = guidePoint.getY();
            previousPoint = guidePoint;
        }
        return (float) value;
    }

    /**
     * Adds a new guidepoint.
     */
    public void addGuidePoint(float x, float value) {
        addGuidePoint(new Point2D.Float(x, value));
    }

    /**
     * Adds a new guidepoint.
     *
     * @param point2D the guide point to add.  Not added if it is null or already added.
     */
    public void addGuidePoint(Point2D point2D) {
        if (point2D != null && !myGuidePoints.contains(point2D)) {
            myGuidePoints.add(new Point2D.Float((float) point2D.getX(), (float) point2D.getY()));
            Collections.sort(myGuidePoints, GUIDE_POINT_COMPARATOR);
        }
    }

    /**
     * Removes a guidepoint from the list of guidepoints, if found.
     *
     * @param point2D the guide point to remove.  Ignored if it is null.
     */
    public void removeGuidePoint(Point2D point2D) {
        if (point2D != null) {
            myGuidePoints.remove(point2D);
        }
    }

    /**
     * @return the list of guide points defining the function.
     */
    public List<Point2D> getGuidePoints() {
        return Collections.unmodifiableList(myGuidePoints);
    }
}
