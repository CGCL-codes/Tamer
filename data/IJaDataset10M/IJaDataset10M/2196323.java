package org.jhotdraw.util;

import java.awt.Rectangle;
import java.awt.Point;
import java.awt.geom.Line2D;

/**
 * Some geometric utilities.
 *
 * @version <$CURRENT_VERSION$>
 */
public class Geom {

    private Geom() {
    }

    /**
	 * Tests if a point is on a line.
	 */
    public static boolean lineContainsPoint(int x1, int y1, int x2, int y2, int px, int py) {
        Rectangle r = new Rectangle(new Point(x1, y1));
        r.add(x2, y2);
        r.grow(2, 2);
        if (!r.contains(px, py)) {
            return false;
        }
        double a, b, x, y;
        if (x1 == x2) {
            return (Math.abs(px - x1) < 3);
        }
        if (y1 == y2) {
            return (Math.abs(py - y1) < 3);
        }
        a = (double) (y1 - y2) / (double) (x1 - x2);
        b = (double) y1 - a * (double) x1;
        x = (py - b) / a;
        y = a * px + b;
        return (Math.min(Math.abs(x - px), Math.abs(y - py)) < 4);
    }

    public static final int NORTH = 1;

    public static final int SOUTH = 2;

    public static final int WEST = 3;

    public static final int EAST = 4;

    /**
	 * Returns the direction NORTH, SOUTH, WEST, EAST from
	 * one point to another one.
	 */
    public static int direction(int x1, int y1, int x2, int y2) {
        int direction = 0;
        int vx = x2 - x1;
        int vy = y2 - y1;
        if (vy < vx && vx > -vy) {
            direction = EAST;
        } else if (vy > vx && vy > -vx) {
            direction = NORTH;
        } else if (vx < vy && vx < -vy) {
            direction = WEST;
        } else {
            direction = SOUTH;
        }
        return direction;
    }

    public static Point south(Rectangle r) {
        return new Point(r.x + r.width / 2, r.y + r.height);
    }

    public static Point center(Rectangle r) {
        return new Point(r.x + r.width / 2, r.y + r.height / 2);
    }

    public static Point west(Rectangle r) {
        return new Point(r.x, r.y + r.height / 2);
    }

    public static Point east(Rectangle r) {
        return new Point(r.x + r.width, r.y + r.height / 2);
    }

    public static Point north(Rectangle r) {
        return new Point(r.x + r.width / 2, r.y);
    }

    /**
	 * Returns the corner (bottom right) of the rectangle
	 *
	 * @param r  the rectangle
	 * @return   the corner
	 */
    public static Point corner(Rectangle r) {
        return new Point((int) r.getMaxX(), (int) r.getMaxY());
    }

    /**
	 * Returns the top left corner of the rectangle
	 *
	 * @param r  the rectangle
	 * @return   the corner
	 */
    public static Point topLeftCorner(Rectangle r) {
        return r.getLocation();
    }

    /**
	 * Returns the top right corner of the rectangle
	 *
	 * @param r  the rectangle
	 * @return   the corner
	 */
    public static Point topRightCorner(Rectangle r) {
        return new Point((int) r.getMaxX(), (int) r.getMinY());
    }

    /**
	 * Returns the bottom left corner of the rectangle
	 *
	 * @param r  the rectangle
	 * @return   the corner
	 */
    public static Point bottomLeftCorner(Rectangle r) {
        return new Point((int) r.getMinX(), (int) r.getMaxY());
    }

    /**
	 * Returns the bottom right corner of the rectangle.
	 * Same as corner, added for naming coherence with the other
	 * corner extracting methods
	 *
	 * @param r  the rectangle
	 * @return   the corner
	 */
    public static Point bottomRightCorner(Rectangle r) {
        return corner(r);
    }

    /**
	 * Constains a value to the given range.
	 * @return the constrained value
	 */
    public static int range(int min, int max, int value) {
        if (value < min) {
            value = min;
        }
        if (value > max) {
            value = max;
        }
        return value;
    }

    /**
	 * Gets the square distance between two points.
	 */
    public static long length2(int x1, int y1, int x2, int y2) {
        return (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
    }

    /**
	 * Gets the distance between to points
	 */
    public static long length(int x1, int y1, int x2, int y2) {
        return (long) Math.sqrt(length2(x1, y1, x2, y2));
    }

    /**
	 * Gets the angle of a point relative to a rectangle.
	 */
    public static double pointToAngle(Rectangle r, Point p) {
        int px = p.x - (r.x + r.width / 2);
        int py = p.y - (r.y + r.height / 2);
        return Math.atan2(py * r.width, px * r.height);
    }

    /**
	 * Gets the point on a rectangle that corresponds to the given angle.
	 */
    public static Point angleToPoint(Rectangle r, double angle) {
        return angleToPoint(r, angle, new Point());
    }

    /**
	 * Gets the point on a rectangle that corresponds to the given angle.
	 */
    public static Point angleToPoint(Rectangle r, double angle, Point p) {
        double si = Math.sin(angle);
        double co = Math.cos(angle);
        double e = 0.0001;
        int x = 0;
        int y = 0;
        if (Math.abs(si) > e) {
            x = (int) ((1.0 + co / Math.abs(si)) / 2.0 * r.width);
            x = range(0, r.width, x);
        } else if (co >= 0.0) {
            x = r.width;
        }
        if (Math.abs(co) > e) {
            y = (int) ((1.0 + si / Math.abs(co)) / 2.0 * r.height);
            y = range(0, r.height, y);
        } else if (si >= 0.0) {
            y = r.height;
        }
        p.x = r.x + x;
        p.y = r.y + y;
        return p;
    }

    /**
	 * Converts a polar to a point
	 */
    public static Point polarToPoint(double angle, double fx, double fy) {
        return polarToPoint(angle, fx, fy, new Point());
    }

    /**
	 * Converts a polar to a point
	 */
    public static Point polarToPoint(double angle, double fx, double fy, Point p) {
        double si = Math.sin(angle);
        double co = Math.cos(angle);
        p.x = (int) (fx * co + 0.5);
        p.y = (int) (fy * si + 0.5);
        return p;
    }

    /**
	 * Gets the point on an oval that corresponds to the given angle.
	 */
    public static Point ovalAngleToPoint(Rectangle r, double angle) {
        return ovalAngleToPoint(r, angle, new Point());
    }

    /**
	 * Gets the point on an oval that corresponds to the given angle.
	 */
    public static Point ovalAngleToPoint(Rectangle r, double angle, Point p) {
        p = Geom.polarToPoint(angle, r.width / 2, r.height / 2, p);
        p.x = r.x + r.width / 2 + p.x;
        p.y = r.y + r.height / 2 + p.y;
        return p;
    }

    public static Point intersect(int xa, int ya, int xb, int yb, int xc, int yc, int xd, int yd) {
        double denom = ((xb - xa) * (yd - yc) - (yb - ya) * (xd - xc));
        double rnum = ((ya - yc) * (xd - xc) - (xa - xc) * (yd - yc));
        if (denom == 0.0) {
            if (rnum == 0.0) {
                if ((xa < xb && (xb < xc || xb < xd)) || (xa > xb && (xb > xc || xb > xd))) {
                    return new Point(xb, yb);
                } else {
                    return new Point(xa, ya);
                }
            } else {
                return null;
            }
        }
        double r = rnum / denom;
        double snum = ((ya - yc) * (xb - xa) - (xa - xc) * (yb - ya));
        double s = snum / denom;
        if (0.0 <= r && r <= 1.0 && 0.0 <= s && s <= 1.0) {
            int px = (int) (xa + (xb - xa) * r);
            int py = (int) (ya + (yb - ya) * r);
            return new Point(px, py);
        } else {
            return null;
        }
    }

    public static double distanceFromLine(int xa, int ya, int xb, int yb, int xc, int yc) {
        int xdiff = xb - xa;
        int ydiff = yb - ya;
        long l2 = xdiff * xdiff + ydiff * ydiff;
        if (l2 == 0) {
            return Geom.length(xa, ya, xc, yc);
        }
        double rnum = (ya - yc) * (ya - yb) - (xa - xc) * (xb - xa);
        double r = rnum / l2;
        if (r < 0.0 || r > 1.0) {
            return Double.MAX_VALUE;
        }
        double xi = xa + r * xdiff;
        double yi = ya + r * ydiff;
        double xd = xc - xi;
        double yd = yc - yi;
        return Math.sqrt(xd * xd + yd * yd);
    }

    /**
	 * compute distance of point from line segment.<br>
	 * Uses AWT Line2D utility methods
	 */
    public static double distanceFromLine2D(int xa, int ya, int xb, int yb, int xc, int yc) {
        Line2D.Double line = new Line2D.Double(xa, xb, ya, yb);
        return line.ptSegDist(xc, yc);
    }
}
