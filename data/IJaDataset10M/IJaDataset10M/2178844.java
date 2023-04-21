package org.moyoman.module.geometry.geometryimpl;

import org.moyoman.log.*;
import org.moyoman.module.geometry.*;
import org.moyoman.util.*;
import java.io.*;
import java.util.*;

/** This class computes the convex hull of a set of points.
  * The algorithms used in here are taken from the book,
  * <b>Algorithms, by Robert Sedgewick<b>.  Note that the
  * definition of a convex hull differs slightly from his,
  * because he defines the convex hull as a minimum set of
  * points, and in this class, all points that are on the
  * convex hull are returned.
  */
class ConvexHullImpl implements ConvexHull {

    /** The key is a Long from Zobrist, the value is the ConvexHullImpl object.*/
    private static HashMap convexHulls;

    /** These are the vertices of the convex hull.*/
    private Point[] points;

    /** These are the points which are inside of the convex hull.*/
    private Point[] pich;

    static {
        convexHulls = new HashMap();
    }

    /** This is a test method for this class.
	  * Eventually, code like this should be moved into a common
	  * testing framework.
	  * @param args The command line arguments.
	  */
    public static void main(String args[]) {
        ArrayList al = new ArrayList();
        try {
            FileInputStream fis = new FileInputStream("points.data");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String str = br.readLine();
            while (str != null) {
                StringTokenizer st = new StringTokenizer(str);
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                Point point = Point.get(x, y);
                al.add(point);
                str = br.readLine();
            }
            Point[] arr = new Point[al.size()];
            al.toArray(arr);
            System.out.println("The convex hull for ");
            for (int i = 0; i < arr.length; i++) System.out.println(arr[i]);
            ConvexHull ch = new ConvexHullImpl(arr);
            Point[] val = ch.getVertices();
            System.out.println("\n\n");
            for (int i = 0; i < val.length; i++) System.out.println(val[i]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ConvexHullImpl get(Point[] pts) {
        Stone[] stones = new Stone[pts.length];
        for (int i = 0; i < pts.length; i++) {
            stones[i] = Stone.get(Color.BLACK, pts[i]);
        }
        Long l = Zobrist.getValue(stones);
        ConvexHullImpl chi = (ConvexHullImpl) convexHulls.get(l);
        if (chi == null) {
            chi = new ConvexHullImpl(pts);
        }
        return chi;
    }

    /** Create a ConvexHull object for the set of points.
	  * @param points The points for which the convex hull is computed.
	  */
    private ConvexHullImpl(Point[] pts) {
        if (pts.length <= 3) {
            points = new Point[pts.length];
            for (int i = 0; i < pts.length; i++) {
                points[i] = pts[i].castToPoint();
            }
        } else {
            ArrayList al = new ArrayList();
            Point[] arr = new Point[pts.length];
            for (int i = 0; i < pts.length; i++) {
                arr[i] = pts[i].castToPoint();
            }
            HashSet potential = new HashSet();
            for (int i = 0; i < arr.length; i++) {
                potential.add(arr[i]);
            }
            Point min = findMinimumVertical(arr);
            al.add(min);
            Point curr = findNext(min, potential, 0.0);
            al.add(curr);
            potential.remove(curr);
            double angle = theta(min, curr);
            double minangle = angle;
            while (true) {
                Point old = curr;
                curr = findNext(curr, potential, minangle);
                angle = theta(old, curr);
                if (angle > minangle) minangle = angle;
                if (curr.equals(min)) {
                    break;
                }
                al.add(curr);
                potential.remove(curr);
            }
            points = new Point[al.size()];
            al.toArray(points);
        }
    }

    /** Get the vertices of the convex hull.
	  * @return An array of Point objects which are the vertices of the convex hull.
	  */
    public Point[] getVertices() {
        return points;
    }

    /** Find the next point on the convex hull.
	  * @param anchor The previous point found on the convex hull.
	  * @param potential The potential points on the convex hull.
	  * @param minangle The minimum angle to be used.
	  * @return The point which is the next one on the convex hull.
	  */
    private static Point findNext(Point anchor, Set potential, double minangle) {
        double angle = 4.0;
        Iterator it = potential.iterator();
        if (potential.size() == 1) return (Point) it.next();
        Point next = null;
        while (it.hasNext()) {
            Point curr = (Point) it.next();
            double d = theta(anchor, curr);
            if ((d >= minangle) && (d < angle)) {
                next = curr;
                angle = d;
            }
        }
        next = findClosestColinearPoint(anchor, next, potential);
        return next;
    }

    /** Find the closest point to the anchor on the same line as the next point.
	  * @param anchor The first point in the line.
	  * @param next The second point in the line.
	  * @param potential The set of potential points being examined.
	  * @return A Point on the same line as anchor and next, which may or may not be next.
	  */
    private static Point findClosestColinearPoint(Point anchor, Point next, Set potential) {
        int x = next.getX() - anchor.getX();
        int y = next.getY() - anchor.getY();
        int gcd = findGCD(x, y);
        if (gcd == 1) return next;
        x /= gcd;
        y /= gcd;
        int currx = anchor.getX() + x;
        int curry = anchor.getY() + y;
        while (true) {
            Point pt = Point.get(currx, curry);
            if (potential.contains(pt)) return pt;
            if (pt.equals(next)) return pt;
            currx += x;
            curry += y;
        }
    }

    /** Determine if the given point falls on the convex hull.
	  * @param pt The point in question.
	  * @param ch The convex hull of points.
	  * @return true if the point is on the convex hull, or false.
	  */
    protected static boolean isOnConvexHull(Point pt, Point[] ch) {
        pt = pt.castToPoint();
        Point[] arr = new Point[ch.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = ch[i].castToPoint();
        }
        int minx = 19;
        int maxx = -1;
        int miny = 19;
        int maxy = -1;
        for (int i = 0; i < arr.length; i++) {
            int x = arr[i].getX();
            int y = arr[i].getY();
            if (x < minx) minx = x;
            if (x > maxx) maxx = x;
            if (y < miny) miny = y;
            if (y > maxy) maxy = y;
        }
        int ptx = pt.getX();
        int pty = pt.getY();
        if ((ptx < minx) || (ptx > maxx)) return false;
        if ((pty < miny) || (pty > maxy)) return false;
        return isColinear(pt, arr);
    }

    /** Determine if the given point is on the polygon formed by the array of points.
	  * @param pt The point in question.
	  * @param arr The points which make up the polygon.
	  * @return true if the point is on any of the edges of the polygon, or false.
	  */
    private static boolean isColinear(Point pt, Point[] arr) {
        int x = pt.getX();
        int y = pt.getY();
        HashSet hs = new HashSet();
        hs.add(pt);
        for (int i = 0; i < arr.length - 1; i++) {
            Point next = findClosestColinearPoint(arr[i], arr[i + 1], hs);
            if (next.equals(pt)) return true;
        }
        Point next = findClosestColinearPoint(arr[arr.length - 1], arr[0], hs);
        if (next.equals(pt)) return true; else return false;
    }

    /** Determine if all of the points of the convex hull are colinear.
	  * @return true if they are all colinear, or false.
	  */
    protected boolean isColinear() {
        int len = points.length;
        if (len < 3) return true;
        Point[] arr = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            arr[i] = points[i].castToPoint();
        }
        boolean flag = true;
        int x = arr[0].getX();
        for (int i = 1; i < len; i++) {
            if (arr[i].getX() != x) {
                flag = false;
                break;
            }
        }
        if (flag) return true;
        for (int i = 0; i < len - 1; i++) {
            for (int j = i + 1; j < len; j++) {
                if (arr[i].getX() > arr[j].getX()) {
                    Point temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
        HashSet hs = new HashSet();
        for (int i = 1; i < len - 1; i++) hs.add(arr[i]);
        while (hs.size() > 0) {
            Point test = findClosestColinearPoint(arr[0], arr[len - 1], hs);
            if (test.equals(arr[len - 1])) return false; else hs.remove(test);
        }
        return true;
    }

    /** Get all of the points on the convex hull, including the vertices.
	  * @param pts The vertices of the convex hull.
	  * @return An array of Point objects which are the points on the convex hull.	
	  */
    public Point[] getPointsOnConvexHull() {
        if (points.length < 4) {
            Point[] pts = (Point[]) points.clone();
            return pts;
        }
        ArrayList al = new ArrayList();
        for (int i = 0; i < points.length; i++) {
            int x;
            int y;
            if (i < points.length - 1) {
                x = points[i + 1].getX() - points[i].getX();
                y = points[i + 1].getY() - points[i].getY();
            } else {
                x = points[points.length - 1].getX() - points[0].getX();
                y = points[points.length - 1].getY() - points[0].getY();
            }
            int gcd = findGCD(x, y);
            x /= gcd;
            y /= gcd;
            Point curr = points[i];
            while (!curr.equals(points[i + 1])) {
                al.add(curr);
                curr = Point.get(curr.getX() + x, curr.getY() + y);
            }
        }
        Point[] ch = new Point[al.size()];
        al.toArray(ch);
        return ch;
    }

    /** Find the greatest common divisor of the two points.
	  * Since this is a special case, only need to try dividing	
	  * by 2, 3, 5, and 7.
	  * @param i1 The first integer.
	  * @param i2 The second integer.
	  * @return The greatest common divisor, which is an integer.
	  */
    protected static int findGCD(int i1, int i2) {
        int gcd = 1;
        while (((i1 % 2) == 0) && ((i2 % 2) == 0)) {
            i1 /= 2;
            i2 /= 2;
            gcd *= 2;
        }
        while (((i1 % 3) == 0) && ((i2 % 3) == 0)) {
            i1 /= 3;
            i2 /= 3;
            gcd *= 3;
        }
        while (((i1 % 5) == 0) && ((i2 % 5) == 0)) {
            i1 /= 5;
            i2 /= 5;
            gcd *= 5;
        }
        while (((i1 % 7) == 0) && ((i2 % 7) == 0)) {
            i1 /= 7;
            i2 /= 7;
            gcd *= 7;
        }
        return gcd;
    }

    /** Find the point with the smallest y value.
	  * If two or more points have the same smallest y value,
	  * then return the one with the smallest x value as well.
	  * @param points An array of Point objects.
	  * @return The Point from the array with the smallest y value.
	  */
    private static Point findMinimumVertical(Point[] points) {
        Point min = points[0];
        int minx = min.getX();
        int miny = min.getY();
        for (int i = 1; i < points.length; i++) {
            if (points[i].getY() < miny) {
                min = points[i];
                minx = min.getX();
                miny = min.getY();
            } else if (points[i].getY() == miny) {
                if (points[i].getX() < minx) {
                    min = points[i];
                    minx = min.getX();
                }
            }
        }
        return min;
    }

    /** Compute the theta value for the two points.
	  * For an explanation of this, see the book
	  * <b>Algorithms, by RobertSedgewick</b>.
	  * @param pt1 The first point. 
	  * @param pt2 The second point. 
	  * @return A double which is the theta value.
	  */
    private static double theta(Point pt1, Point pt2) {
        int dx;
        int dy;
        int ax;
        int ay;
        double t;
        dx = pt2.getX() - pt1.getX();
        dy = pt2.getY() - pt1.getY();
        ax = dx;
        if (ax < 0) ax *= -1;
        ay = dy;
        if (ay < 0) ay *= -1;
        if ((dx == 0) && (dy == 0)) t = 4.0; else {
            t = ((double) dy) / ((double) (ax + ay));
            if (dx < 0) t = 2.0 - t; else if (dy < 0) t = t + 4.0;
        }
        return t;
    }

    /** Get the points inside of the convex hull.
	  * All points are returned, which may be empty, or contain
	  * stones of either color.  The points are not necessarily
	  * closest to this group than to any other.
	  * @return An array of Point objects.
	  */
    public Point[] getPointsInConvexHull() {
        if (points.length < 3) return new Point[0];
        if (pich != null) {
            return pich;
        }
        int maxx = -1;
        int maxy = -1;
        int minx = 19;
        int miny = 19;
        for (int i = 0; i < points.length; i++) {
            int x = points[i].getX();
            int y = points[i].getY();
            if (x < minx) minx = x;
            if (x > maxx) maxx = x;
            if (y < miny) miny = y;
            if (y > maxy) maxy = y;
        }
        ArrayList al = new ArrayList();
        for (int i = minx; i <= maxx; i++) {
            for (int j = miny; j <= maxy; j++) {
                Point pt = Point.get(i, j);
                if (testInside(pt)) al.add(pt);
            }
        }
        Point[] pich = new Point[al.size()];
        al.toArray(pich);
        return pich;
    }

    /** Get the number of points inside of the convex hull.
	  * @param slg The loose group in question.
	  * @return The number of points inside the convex hull.
	  */
    public int getTotalPointsInConvexHull() {
        Point[] pts = getPointsInConvexHull();
        return pts.length;
    }

    /** Check if the point in question is on an edge of the polygon.
	  * @param pt The point in question.
	  * @param points The vertices of the polygon.
	  * @return true if the point is on an edge of the polygon, or false.
	  * @throws IllegalArgumentException Thrown if the point is not on an edge.
	  */
    private boolean checkEdge(Point pt, Point[] points) throws IllegalArgumentException {
        int x = pt.getX();
        int y = pt.getY();
        if (((x == 0) || (x == 18)) && ((y == 0) || (y == 18))) {
            boolean diffx = false;
            boolean diffy = false;
            for (int i = 0; i < points.length; i++) {
                int currx = points[i].getX();
                int curry = points[i].getY();
                if ((x == currx) && (y != curry)) diffy = true; else if ((x != currx) && (y == curry)) diffx = true;
            }
            if (diffx && diffy) return true; else return false;
        }
        if ((x == 0) || (x == 18)) {
            boolean belowflag = false;
            boolean aboveflag = false;
            for (int i = 0; i < points.length; i++) {
                int currx = points[i].getX();
                int curry = points[i].getY();
                if ((currx == x) && (curry > y)) aboveflag = true; else if ((currx == x) && (curry < y)) belowflag = true;
            }
            if (aboveflag && belowflag) return true; else return false;
        }
        if ((y == 0) || (y == 18)) {
            boolean leftflag = false;
            boolean rightflag = false;
            for (int i = 0; i < points.length; i++) {
                int currx = points[i].getX();
                int curry = points[i].getY();
                if ((curry == y) && (currx > x)) rightflag = true; else if ((curry == y) && (currx < x)) leftflag = true;
            }
            if (leftflag && rightflag) return true; else return false;
        }
        throw new IllegalArgumentException("Point is not on edge");
    }

    /** Determine whether a point is inside a polygon.
	  * See the book, <b>Algorithms, by Robert Sedgewick</b>
	  * for more details.  For the purposes of this module,
	  * a point on one of the edges of the polygons is
	  * considered to be outside the polygon.
	  * @param pt The Point object to be tested.
	  * @return A boolean value indicating whether the
	  * point is inside the polygon.
	  */
    private boolean testInside(Point pt) {
        try {
            if (points.length < 3) {
                return false;
            }
            if (isColinear()) {
                return false;
            }
            int ptx = pt.getX();
            int pty = pt.getY();
            if ((ptx == 0) || (ptx == 18) || (pty == 0) || (pty == 18)) {
                boolean val = checkEdge(pt, points);
                return val;
            }
            float x = pt.getX() + 0.01f;
            float y = pt.getY() + 0.01f;
            boolean flag1 = testInside(x, y);
            x = pt.getX() - 0.01f;
            y = pt.getY() + 0.01f;
            boolean flag2 = testInside(x, y);
            x = pt.getX() - 0.01f;
            y = pt.getY() - 0.01f;
            boolean flag3 = testInside(x, y);
            x = pt.getX() - 0.01f;
            y = pt.getY() + 0.01f;
            boolean flag4 = testInside(x, y);
            if ((flag1 == flag2) && (flag1 == flag3) && (flag1 == flag4)) {
                return flag1;
            } else {
                return false;
            }
        } catch (Exception e) {
            SystemLog.error(e);
            return false;
        }
    }

    /** This method tests a single point for being inside a polygon.
	  * The caller of this method must ensure that the x, y coordinates
	  * are not on any of the edges of the polygon.  It must also check
	  * that the polygon has at least three vertices, and that they are
	  * not colinear.
	  * @param x The x coordinate of the point.
	  * @param y The y coordinate of the point.
	  * @return true if the point is inside the convex hull, or false.
	  */
    private boolean testInside(float x, float y) {
        float a1 = 19 - y;
        float b1 = y - (a1 * x);
        int total = 0;
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].getX() != points[i + 1].getX()) total++;
        }
        if (points[points.length - 1].getX() != points[0].getX()) total++;
        if (total == 0) return false;
        float[] avals = new float[total];
        float[] bvals = new float[total];
        int[] minx = new int[total];
        int[] maxx = new int[total];
        ArrayList vertx = new ArrayList();
        ArrayList vertminy = new ArrayList();
        ArrayList vertmaxy = new ArrayList();
        int index = 0;
        for (int i = 0; i < points.length; i++) {
            int i2 = i + 1;
            if (i2 == points.length) i2 = 0;
            if (points[i].getX() == points[i2].getX()) {
                int y1 = points[i].getY();
                int y2 = points[i2].getY();
                if (y1 > y2) {
                    int tempval = y1;
                    y1 = y2;
                    y2 = tempval;
                }
                Integer integer1 = new Integer(points[i].getX());
                Integer integer2 = new Integer(y1);
                Integer integer3 = new Integer(y2);
                vertx.add(integer1);
                vertminy.add(integer2);
                vertmaxy.add(integer3);
            } else {
                float x1 = (float) points[i].getX();
                float y1 = (float) points[i].getY();
                float x2 = (float) points[i2].getX();
                float y2 = (float) points[i2].getY();
                avals[index] = (y2 - y1) / (x2 - x1);
                bvals[index] = y1 - (avals[index] * x1);
                minx[index] = points[i].getX();
                maxx[index] = points[i2].getX();
                if (minx[index] > maxx[index]) {
                    int tempval = minx[index];
                    minx[index] = maxx[index];
                    maxx[index] = tempval;
                }
                index++;
            }
        }
        int intcount = 0;
        for (int i = 0; i < total; i++) {
            float xint = (float) (bvals[i] - b1) / (float) (a1 - avals[i]);
            if ((xint > x) && (xint < (x + 1))) {
                if ((xint > minx[i]) && (xint < maxx[i])) {
                    intcount++;
                }
            }
        }
        for (int i = 0; i < vertx.size(); i++) {
            Integer integer = (Integer) vertx.get(i);
            int xval = integer.intValue();
            float val = (xval * a1) + b1;
            Integer i2 = (Integer) vertminy.get(i);
            Integer i3 = (Integer) vertmaxy.get(i);
            int y1 = i2.intValue();
            int y2 = i3.intValue();
            if ((val > y1) && (val < y2) && (x < xval) && ((x + 1) > xval)) intcount++;
        }
        if ((intcount % 2) == 1) return true; else return false;
    }

    /** Override the Object.clone() method.
	  * @return A ConvexHullImpl object which is a clone of this one.
	  */
    public Object clone() {
        try {
            ConvexHullImpl chi = (ConvexHullImpl) super.clone();
            chi.points = (Point[]) points.clone();
            chi.pich = (Point[]) pich.clone();
            return chi;
        } catch (Exception e) {
            SystemLog.error(e);
            return this;
        }
    }
}
