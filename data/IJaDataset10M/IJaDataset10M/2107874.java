package org.xito.dazzle.animation.timing.interpolation;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import org.xito.dazzle.animation.timing.*;

/**
 * This class interpolates fractional values using Bezier splines.  The anchor
 * points  * for the spline are assumed to be (0, 0) and (1, 1).  Control points
 * should all be in the range [0, 1].
 * <p>
 * For more information on how splines are used to interpolate, refer to the
 * SMIL specification at http://w3c.org.
 * <p>
 * This class provides one simple built-in facility for non-linear
 * interpolation.  Applications are free to define their own Interpolator
 * implementation and use that instead when particular non-linear
 * effects are desired.
 *
 * @author Chet
 */
public final class SplineInterpolator implements Interpolator {

    private float x1, y1, x2, y2;

    private ArrayList lengths = new ArrayList();

    /**
     * Creates a new instance of SplineInterpolator with the control points
     * defined by (x1, y1) and (x2, y2).  The anchor points are implicitly
     * defined as (0, 0) and (1, 1).
     * 
     * @throws IllegalArgumentException This exception is thrown when values
     * beyond the allowed [0,1] range are passed in
     */
    public SplineInterpolator(float x1, float y1, float x2, float y2) {
        if (x1 < 0 || x1 > 1.0f || y1 < 0 || y1 > 1.0f || x2 < 0 || x2 > 1.0f || y2 < 0 || y2 > 1.0f) {
            throw new IllegalArgumentException("Control points must be in " + "the range [0, 1]:");
        }
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        float prevX = 0.0f;
        float prevY = 0.0f;
        float prevLength = 0.0f;
        for (float t = 0.01f; t <= 1.0f; t += .01f) {
            Point2D.Float xy = getXY(t);
            float length = prevLength + (float) Math.sqrt((xy.x - prevX) * (xy.x - prevX) + (xy.y - prevY) * (xy.y - prevY));
            LengthItem lengthItem = new LengthItem(length, t);
            lengths.add(lengthItem);
            prevLength = length;
            prevX = xy.x;
            prevY = xy.y;
        }
        for (int i = 0; i < lengths.size(); ++i) {
            LengthItem lengthItem = (LengthItem) lengths.get(i);
            lengthItem.setFraction(prevLength);
        }
    }

    /**
     * Calculates the XY point for a given t value.
     *
     * The general spline equation is:
     *   x = b0*x0 + b1*x1 + b2*x2 + b3*x3
     *   y = b0*y0 + b1*y1 + b2*y2 + b3*y3
     * where:
     *   b0 = (1-t)^3
     *   b1 = 3 * t * (1-t)^2
     *   b2 = 3 * t^2 * (1-t)
     *   b3 = t^3
     * We know that (x0,y0) == (0,0) and (x1,y1) == (1,1) for our splines,
     * so this simplifies to:
     *   x = b1*x1 + b2*x2 + b3
     *   y = b1*x1 + b2*x2 + b3
     * @param t parametric value for spline calculation
     */
    private Point2D.Float getXY(float t) {
        Point2D.Float xy;
        float invT = (1 - t);
        float b1 = 3 * t * (invT * invT);
        float b2 = 3 * (t * t) * invT;
        float b3 = t * t * t;
        xy = new Point2D.Float((b1 * x1) + (b2 * x2) + b3, (b1 * y1) + (b2 * y2) + b3);
        return xy;
    }

    /**
     * Utility function: When we are evaluating the spline, we only care
     * about the Y values.  See {@link getXY getXY} for the details.
     */
    private float getY(float t) {
        Point2D.Float xy;
        float invT = (1 - t);
        float b1 = 3 * t * (invT * invT);
        float b2 = 3 * (t * t) * invT;
        float b3 = t * t * t;
        return (b1 * y1) + (b2 * y2) + b3;
    }

    /**
     * Given a fraction of time along the spline (which we can interpret
     * as the length along a spline), return the interpolated value of the
     * spline.  We first calculate the t value for the length (by doing
     * a lookup in our array of previousloy calculated values and then
     * linearly interpolating between the nearest values) and then
     * calculate the Y value for this t.
     * @param lengthFraction Fraction of time in a given time interval.
     * @return interpolated fraction between 0 and 1
     */
    public float interpolate(float lengthFraction) {
        float interpolatedT = 1.0f;
        float prevT = 0.0f;
        float prevLength = 0.0f;
        for (int i = 0; i < lengths.size(); ++i) {
            LengthItem lengthItem = (LengthItem) lengths.get(i);
            float fraction = lengthItem.getFraction();
            float t = lengthItem.getT();
            if (lengthFraction <= fraction) {
                float proportion = (lengthFraction - prevLength) / (fraction - prevLength);
                interpolatedT = prevT + proportion * (t - prevT);
                return getY(interpolatedT);
            }
            prevLength = fraction;
            prevT = t;
        }
        return getY(interpolatedT);
    }
}

/**
 * Struct used to store information about length values.  Specifically,
 * each item stores the "length" (which can be thought of as the time
 * elapsed along the spline path), the "t" value at this length (used to
 * calculate the (x,y) point along the spline), and the "fraction" which
 * is equal to the length divided by the total absolute length of the spline.
 * After we calculate all LengthItems for a give spline, we have a list
 * of entries which can return the t values for fractional lengths from 
 * 0 to 1.
 */
class LengthItem {

    float length;

    float t;

    float fraction;

    LengthItem(float length, float t, float fraction) {
        this.length = length;
        this.t = t;
        this.fraction = fraction;
    }

    LengthItem(float length, float t) {
        this.length = length;
        this.t = t;
    }

    public float getLength() {
        return length;
    }

    public float getT() {
        return t;
    }

    public float getFraction() {
        return fraction;
    }

    void setFraction(float totalLength) {
        fraction = length / totalLength;
    }
}
