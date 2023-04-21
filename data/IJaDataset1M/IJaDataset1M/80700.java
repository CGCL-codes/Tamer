package xmage.math;

import huf.misc.HMath;

/**
 * Class representing single-precision line segment in 2D that lies between two points.
 */
public class Segment2f extends Line2f {

    /**
	 * Create new line segment.
	 *
	 * <p>
	 * Default line segment lies between points <code>(0.0f, 0.0f)</code>
	 * and <code>(0.0f, 1.0f)</code>.
	 * </p>
	 */
    public Segment2f() {
    }

    /**
	 * Create new line segment by specifying two ending points.
	 *
	 * @param ax X coordinate of line segment's starting point
	 * @param ay Y coordinate of line segment's starting point
	 * @param bx X coordinate of line segment's ending point
	 * @param by Y coordinate of line segment's ending point
	 */
    public Segment2f(float ax, float ay, float bx, float by) {
        super(ax, ay, bx, by);
    }

    /**
	 * Create new line segment by specifying two points lying on its ends.
	 *
	 * @param a line segment begin
	 * @param b line segment end
	 */
    public Segment2f(Point2f a, Point2f b) {
        super(a, b);
    }

    /**
	 * Find point on line nearest to specified point.
	 *
	 * @param x point X coordinate
	 * @param y point Y coordinate
	 * @param nearest point in which computed nearest point will be stored;
	 *        this is the object which will be returned from the method
	 * @return point on line nearest to specified point
	 */
    @Override
    public Point2f nearest(float x, float y, Point2f nearest) {
        float r = nearestRatio(x, y);
        if (r <= 0.0f) {
            nearest.set(a);
        } else if (r >= 1.0f) {
            nearest.set(b);
        } else {
            nearest.set(b).sub(a).mul(r).add(a);
        }
        return nearest;
    }

    /**
	 * Calculate intersection point between two segments.
	 *
	 * @param segment second segment
	 * @param intersection point in which computed intersection point will be stored;
	 *        this is the object which will be returned from the method
	 * @return intersection point
	 */
    public Point2f intersection(Segment2f segment, Point2f intersection) {
        float denom = ((segment.b.y - segment.a.y) * (b.x - a.x)) - ((segment.b.x - segment.a.x) * (b.y - a.y));
        float numA = ((segment.b.x - segment.a.x) * (a.y - segment.a.y)) - ((segment.b.y - segment.a.y) * (a.x - segment.a.x));
        float numB = ((b.x - a.x) * (a.y - segment.a.y)) - ((b.y - a.y) * (a.x - segment.a.x));
        float ratioA = numA / denom;
        float ratioB = numB / denom;
        if (HMath.isClose(0.0f, denom) || ratioA < 0.0f || ratioA > 1.0f || ratioB < 0.0f || ratioB > 1.0f) {
            return null;
        }
        return intersection.set(a.x + (ratioA * (b.x - a.x)), a.y + (ratioA * (b.y - a.y)));
    }
}
