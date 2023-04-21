package exformation.geom;

import processing.core.PGraphics;
import exformation.utils.MathUtil;

/**
 * @author mweskamp
 * 2008 marcos@exformation.net
 * 
 */
public class Point {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2466395344324735006L;

    public float x, y, z;

    public Point(float _x, float _y, float _z) {
        x = _x;
        y = _y;
        z = _z;
    }

    public Point() {
        this(0, 0, 0);
    }

    public Point(float _x, float _y) {
        this(_x, _y, 0);
    }

    public void random(Rectangle rect) {
        x = MathUtil.random(rect.width) + rect.x;
        y = MathUtil.random(rect.height) + rect.y;
    }

    /**
	 * Get the center of the circle passing through the points a, b, and c. 
	 * */
    public static Point getCircumcenter(Point a, Point b, Point c) {
        float u = ((a.x - b.x) * (a.x + b.x) + (a.y - b.y) * (a.y + b.y)) / 2;
        float v = ((b.x - c.x) * (b.x + c.x) + (b.y - c.y) * (b.y + c.y)) / 2;
        float d = (a.x - b.x) * (b.y - c.y) - (b.x - c.x) * (a.y - b.y);
        float x = (u * (b.y - c.y) - v * (a.y - b.y)) / d;
        float y = (v * (a.x - b.x) - u * (b.x - c.x)) / d;
        return new Point(x, y);
    }

    /**
	 * Rotates this point over an axis and a magnitude
	 * */
    public void rotate(Point origin, float angle) {
        angle *= Math.PI / 180;
        double radius = distance(origin);
        double theta = angle + Math.atan2(this.y - origin.y, this.x - origin.x);
        this.x = (float) (origin.x + (radius * Math.cos(theta)));
        this.y = (float) (origin.y + (radius * Math.sin(theta)));
    }

    /**
	 * Calculates the distance between the point and the given one.
	 * 
	 * @param p the given point.
	 * @return the distance.
	 */
    public float distance(Point p) {
        float xDiff = Math.abs(x - p.x);
        float yDiff = Math.abs(y - p.y);
        return (float) Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }

    /**
	 * Sets the value of this point
	 * @param p
	 */
    public void copy(Point p) {
        x = p.x;
        y = p.y;
    }

    /**
	 * A handy method to set the value of this point
	 * @param x
	 * @param y
	 */
    public void setValue(long x, long y) {
        this.x = x;
        this.y = y;
    }

    /**
	 * Sums this point to another one.
	 * @param p
	 */
    public void sum(Point p) {
        x += p.x;
        y += p.y;
    }

    public void sum(double x, double y) {
        this.x += x;
        this.y += y;
    }

    /**
	 * subtraction operator
	 * */
    public Point sub(Point p) {
        return new Point(x -= p.x, y -= p.y);
    }

    /**
	 * multiplication operator
	 * */
    public Point dot(Point p) {
        return new Point(x *= p.x, y *= p.y);
    }

    public void scale(float scaleX, float scaleY) {
        x *= scaleX;
        y *= scaleY;
    }

    /**
	 * division operator.
	 * */
    public Point div(Point p) {
        return new Point(x /= p.x, y /= p.y);
    }

    /**
	 * is the point within the provided rect?
	 * */
    public boolean isWithinRect(Rectangle rect) {
        return rect.contains(this);
    }

    /**
	 * is the point within the provided rect width?
	 * */
    public boolean isXWithinRect(Rectangle rect) {
        return (x > rect.left()) && (x < rect.right());
    }

    /**
	 * is the point within the provided rect width?
	 * */
    public boolean isYWithinRect(Rectangle rect) {
        return (y > rect.top()) && (y < rect.bottom());
    }

    /**
	 * angle between 2 points
	 * */
    public static float getAngle(Point a, Point b) {
        float dx = a.x - b.x;
        float dy = a.y - b.y;
        float theta = (float) (Math.atan2(dy, dx) * 180 / Math.PI);
        return theta;
    }

    /**
	 * gets the angle between this point and the provided one
	 */
    public float angleTo(Point p) {
        return Point.getAngle(this, p);
    }

    /**
	 * finds the average position in an array of points in space
	 * */
    public static Point getAveragePosition(Point[] arr) {
        Point total = new Point(0.0f, 0.0f);
        int len = arr.length;
        for (int n = 0; n < len; n++) {
            Point p = arr[n];
            total.sum(p);
        }
        total.div(new Point(len, len));
        return total;
    }

    /**
	 * Rounds the point to the MathP precision.
	 *
	 */
    public void round() {
        x = Math.round(x);
        y = Math.round(y);
    }

    /**
	 * Constrains the Point in the given rectangle.
     * @param rect
     * */
    public void constrain(Rectangle rect) {
        x = Math.max(Math.min(x, rect.x + rect.width), rect.x);
        y = Math.max(Math.min(y, rect.y + rect.height), rect.y);
    }

    /**
	 * Given a boundary rect, and a direction vector, and the steps, it will offset this point
	 * and bounce it's direction within the rectangle
	 * */
    public void bounce(Rectangle rect, Point vector) {
        if (!isXWithinRect(rect)) {
            vector.x *= -1;
        }
        if (!isYWithinRect(rect)) {
            vector.y *= -1;
        }
        constrain(rect);
    }

    /**
	 * Wraps the Point within the given rectangle.
     * @param rect
     * */
    public void wrap(Rectangle rect) {
        if (x > rect.right()) {
            x = rect.left();
        } else if (x < rect.left()) {
            x = rect.right();
        }
        if (y > rect.bottom()) {
            y = rect.top();
        } else if (y < rect.top()) {
            y = rect.bottom();
        }
    }

    /**
	 * Gets the bezier curve point provided the 3 anchor points and a value between 0 and 1;
	 * @param a the first point
	 * @param b the second point
	 * @param c the third point
	 * @param t a value between 0 and 1
	 * */
    public static Point getDiscreteBezierPosition(Point a, Point b, Point c, float t) {
        float mt = 1 - t;
        float tt = t * t;
        float pt = mt * mt;
        float at = mt * t;
        float x = a.x * pt + 2 * b.x * at + c.x * tt;
        float y = a.y * pt + 2 * b.y * at + c.y * tt;
        return new Point(x, y);
    }

    /**
	 * Gets the linear point inbetween the provided 2 points and a value between 0 and 1;
	 * @param a the first point
	 * @param b the second point
	 * @param time a value between 0 and 1
	 * */
    public static Point getDiscretePosition(Point a, Point b, float time) {
        float w = b.x - a.x;
        float h = b.y - a.y;
        return new Point(a.x + w * time, a.y + h * time);
    }

    /**
	 * Drags the point to the grid.
	 *
	 */
    public void snapTo(Point grid) {
        x = Math.round(x / grid.x) * grid.x;
        y = Math.round(y / grid.y) * grid.y;
    }

    public Point clone() {
        return new Point(x, y);
    }

    public void convertToRadialPosition(Dimension d, int totalElements, float thetaSpan, float startAngle, float innerRadius) {
        Point p = Point.convertToRadialPosition(this, d, totalElements, thetaSpan, startAngle, innerRadius);
        copy(p);
    }

    /**
	 * Converts a point to a radial position.
	 * Usefull when creating radial layouts.
	 * @param p the Point to transform
	 * @param d the Dimension to which to spread all the points
	 * @param totalElements the total number of elements to spread
	 * @param theta what's the angle of the final span to cover
	 * @param startAngle the first angle
	 * @param innerRadius, the inner radius
	 * */
    public static Point convertToRadialPosition(Point p, Dimension d, int totalElements, float thetaSpan, float startAngle, float innerRadius) {
        if (totalElements != -1) thetaSpan -= (360 / totalElements);
        float w = d.h;
        float h = d.w;
        float x = (w - p.y) * (1 - (1 / w * innerRadius)) / 1;
        float y = p.x;
        float px = (w - x) / 2;
        float py = thetaSpan / h * y;
        py += startAngle;
        float aspectRatio = w / h;
        if (aspectRatio >= 1) {
            px = h / w * px;
        }
        p = new Point(px, py);
        p.toRadial();
        p.sum(h / 2, w / 2);
        return p;
    }

    /**
	 * converts its cartesian position to it's radial equivalent
	 * */
    public void toRadial() {
        float q = x;
        float r = MathUtil.degreesToRadians(y);
        float x = (float) (q * Math.cos(r));
        float y = (float) (q * Math.sin(r));
        this.x = x;
        this.y = y;
    }

    public static Point getMidPoint(Point a, Point b) {
        Point c = new Point();
        c.calcMidPoint(a, b);
        return c;
    }

    public void calcMidPoint(Point a, Point b) {
        x = (float) ((a.x + b.x) * .5);
        y = (float) ((a.y + b.y) * .5);
    }

    public String toString() {
        return "[" + getClass() + "] x=" + x + " y=" + y;
    }

    public void draw(PGraphics g) {
        g.fill(0);
        g.rect(x - 1, y - 1, 3, 3);
        g.noFill();
    }

    public void ease(Point p, float n) {
        float dx = p.x - x;
        float dy = p.y - y;
        x += dx * n;
        y += dy * n;
    }

    public static Point lerp(Point a, Point b, float amt) {
        Point p = new Point();
        p.x = MathUtil.lerp(a.x, b.x, amt);
        p.y = MathUtil.lerp(a.y, b.y, amt);
        return p;
    }
}
