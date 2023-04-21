package com.android.ide.common.api;

/**
 * Mutable point.
 * <p>
 * <b>NOTE: This is not a public or final API; if you rely on this be prepared
 * to adjust your code for the next tools release.</b>
 * </p>
 */
public class Point {

    public int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point p) {
        x = p.x;
        y = p.y;
    }

    /** Sets the point to the given coordinates. */
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /** Returns a new instance of a point with the same values. */
    public Point copy() {
        return new Point(x, y);
    }

    /**
     * Offsets this point by adding the given x,y deltas to the x,y coordinates.
     * @return Returns self, for chaining.
     */
    public Point offsetBy(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point rhs = (Point) obj;
            return this.x == rhs.x && this.y == rhs.y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = x ^ ((y >> 16) & 0x0FFFF) ^ ((y & 0x0FFFF) << 16);
        return h;
    }

    @Override
    public String toString() {
        return String.format("Point [%dx%d]", x, y);
    }
}
