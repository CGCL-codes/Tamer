package nl.BobbinWork.diagram.math;

import java.awt.geom.*;

/***
 * Calculates the nearest point on a straight line or on a cubic bezier curve.
 *
 * Calculations for the Bezier parts come from:
 * "Solving the Nearest Point-on-Curve Problem" and "A Bezier Curve-Based Root-Finder"
 * by Philip J. Schneider from "Graphics Gems", Academic Press, 1990.
 *
 * @author Mark Donszelmann
 * 
 * original version from xref -- Id: NearestPoint.java 258 2004-06-08 06:27:49Z duns 
 * official version http://java.freehep.org/svn/showfile.svn?path=%2fwired%2ftrunk%2fwired-plugin%2fsrc%2fmain%2fjava%2fhep%2fwired%2fheprep%2futil%2fNearestPoint.java&revision=HEAD&name=freehep
 */
public class NearestPoint {

    private static final int MAXDEPTH = 64;

    private static final double EPSILON = 1.0 * Math.pow(2, -MAXDEPTH - 1);

    private static final int DEGREE = 3;

    private static final int W_DEGREE = 5;

    protected NearestPoint() {
    }

    /***
     * Return the nearest point (pn) on cubic Bezier curve c nearest to point pa.
     *
     * @param c cubic curve
     * @param pa arbitrary point
     * @param pn nearest point found (return param)
     * @return distance squared between pa and nearest point (pn)
     */
    public static double onCurve(CubicCurve2D c, Point2D pa, Point2D pn) {
        double[] tCandidate = new double[W_DEGREE];
        Point2D[] v = { c.getP1(), c.getCtrlP1(), c.getCtrlP2(), c.getP2() };
        Point2D[] w = convertToBezierForm(v, pa);
        int nSolutions = findRoots(w, W_DEGREE, tCandidate, 0);
        double minDistance = pa.distanceSq(c.getP1());
        double t = 0.0;
        for (int i = 0; i < nSolutions; i++) {
            Point2D p = bezier(v, DEGREE, tCandidate[i], null, null);
            double distance = pa.distanceSq(p);
            if (distance < minDistance) {
                minDistance = distance;
                t = tCandidate[i];
            }
        }
        double distance = pa.distanceSq(c.getP2());
        if (distance < minDistance) {
            t = 1.0;
        }
        if (pn == null) {
            pn = new Point2D.Double();
        }
        pn.setLocation(bezier(v, DEGREE, t, null, null));
        return pn.distanceSq(pa);
    }

    /***
     *  FindRoots :
     *  Given a 5th-degree equation in Bernstein-Bezier form, find
     *  all of the roots in the interval [0, 1].  Return the number
     *  of roots found.
     */
    private static int findRoots(Point2D[] w, int degree, double[] t, int depth) {
        switch(crossingCount(w, degree)) {
            case 0:
                {
                    return 0;
                }
            case 1:
                {
                    if (depth >= MAXDEPTH) {
                        t[0] = (w[0].getX() + w[W_DEGREE].getX()) / 2.0;
                        return 1;
                    }
                    if (controlPolygonFlatEnough(w, degree)) {
                        t[0] = computeXIntercept(w, degree);
                        return 1;
                    }
                    break;
                }
        }
        Point2D[] left = new Point2D.Double[W_DEGREE + 1];
        Point2D[] right = new Point2D.Double[W_DEGREE + 1];
        double[] leftT = new double[W_DEGREE + 1];
        double[] rightT = new double[W_DEGREE + 1];
        bezier(w, degree, 0.5, left, right);
        int leftCount = findRoots(left, degree, leftT, depth + 1);
        int rightCount = findRoots(right, degree, rightT, depth + 1);
        for (int i = 0; i < leftCount; i++) {
            t[i] = leftT[i];
        }
        for (int i = 0; i < rightCount; i++) {
            t[i + leftCount] = rightT[i];
        }
        return leftCount + rightCount;
    }

    private static final double[][] cubicZ = { { 1.0, 0.6, 0.3, 0.1 }, { 0.4, 0.6, 0.6, 0.4 }, { 0.1, 0.3, 0.6, 1.0 } };

    /***
     *  ConvertToBezierForm :
     *      Given a point and a Bezier curve, generate a 5th-degree
     *      Bezier-format equation whose solution finds the point on the
     *      curve nearest the user-defined point.
     */
    private static Point2D[] convertToBezierForm(Point2D[] v, Point2D pa) {
        Point2D[] c = new Point2D.Double[DEGREE + 1];
        Point2D[] d = new Point2D.Double[DEGREE];
        double[][] cdTable = new double[3][4];
        Point2D[] w = new Point2D.Double[W_DEGREE + 1];
        for (int i = 0; i <= DEGREE; i++) {
            c[i] = new Point2D.Double(v[i].getX() - pa.getX(), v[i].getY() - pa.getY());
        }
        double s = 3;
        for (int i = 0; i <= DEGREE - 1; i++) {
            d[i] = new Point2D.Double(s * (v[i + 1].getX() - v[i].getX()), s * (v[i + 1].getY() - v[i].getY()));
        }
        for (int row = 0; row <= DEGREE - 1; row++) {
            for (int column = 0; column <= DEGREE; column++) {
                cdTable[row][column] = (d[row].getX() * c[column].getX()) + (d[row].getY() * c[column].getY());
            }
        }
        for (int i = 0; i <= W_DEGREE; i++) {
            w[i] = new Point2D.Double((double) (i) / W_DEGREE, 0.0);
        }
        int n = DEGREE;
        int m = DEGREE - 1;
        for (int k = 0; k <= n + m; k++) {
            int lb = Math.max(0, k - m);
            int ub = Math.min(k, n);
            for (int i = lb; i <= ub; i++) {
                int j = k - i;
                w[i + j] = new Point2D.Double(w[i + j].getX(), w[i + j].getY() + cdTable[j][i] * cubicZ[j][i]);
            }
        }
        return w;
    }

    /***
     * CrossingCount :
     *  Count the number of times a Bezier control polygon 
     *  crosses the 0-axis. This number is >= the number of roots.
     *
     */
    private static int crossingCount(Point2D[] v, int degree) {
        int nCrossings = 0;
        int sign = v[0].getY() < 0 ? -1 : 1;
        int oldSign = sign;
        for (int i = 1; i <= degree; i++) {
            sign = v[i].getY() < 0 ? -1 : 1;
            if (sign != oldSign) nCrossings++;
            oldSign = sign;
        }
        return nCrossings;
    }

    private static boolean controlPolygonFlatEnough(Point2D[] v, int degree) {
        double a = v[0].getY() - v[degree].getY();
        double b = v[degree].getX() - v[0].getX();
        double c = v[0].getX() * v[degree].getY() - v[degree].getX() * v[0].getY();
        double abSquared = (a * a) + (b * b);
        double[] distance = new double[degree + 1];
        for (int i = 1; i < degree; i++) {
            distance[i] = a * v[i].getX() + b * v[i].getY() + c;
            if (distance[i] > 0.0) {
                distance[i] = (distance[i] * distance[i]) / abSquared;
            }
            if (distance[i] < 0.0) {
                distance[i] = -((distance[i] * distance[i]) / abSquared);
            }
        }
        double maxDistanceAbove = 0.0;
        double maxDistanceBelow = 0.0;
        for (int i = 1; i < degree; i++) {
            if (distance[i] < 0.0) {
                maxDistanceBelow = Math.min(maxDistanceBelow, distance[i]);
            }
            if (distance[i] > 0.0) {
                maxDistanceAbove = Math.max(maxDistanceAbove, distance[i]);
            }
        }
        double a1 = 0.0;
        double b1 = 1.0;
        double c1 = 0.0;
        double a2 = a;
        double b2 = b;
        double c2 = c + maxDistanceAbove;
        double det = a1 * b2 - a2 * b1;
        double dInv = 1.0 / det;
        double intercept1 = (b1 * c2 - b2 * c1) * dInv;
        a2 = a;
        b2 = b;
        c2 = c + maxDistanceBelow;
        det = a1 * b2 - a2 * b1;
        dInv = 1.0 / det;
        double intercept2 = (b1 * c2 - b2 * c1) * dInv;
        double leftIntercept = Math.min(intercept1, intercept2);
        double rightIntercept = Math.max(intercept1, intercept2);
        double error = 0.5 * (rightIntercept - leftIntercept);
        return error < EPSILON;
    }

    private static double computeXIntercept(Point2D[] v, int degree) {
        double XNM = v[degree].getX() - v[0].getX();
        double YNM = v[degree].getY() - v[0].getY();
        double XMK = v[0].getX();
        double YMK = v[0].getY();
        double detInv = -1.0 / YNM;
        return (XNM * YMK - YNM * XMK) * detInv;
    }

    private static Point2D bezier(Point2D[] c, int degree, double t, Point2D[] left, Point2D[] right) {
        Point2D[][] p = new Point2D.Double[W_DEGREE + 1][W_DEGREE + 1];
        for (int j = 0; j <= degree; j++) {
            p[0][j] = new Point2D.Double(c[j].getX(), c[j].getY());
        }
        for (int i = 1; i <= degree; i++) {
            for (int j = 0; j <= degree - i; j++) {
                p[i][j] = new Point2D.Double((1.0 - t) * p[i - 1][j].getX() + t * p[i - 1][j + 1].getX(), (1.0 - t) * p[i - 1][j].getY() + t * p[i - 1][j + 1].getY());
            }
        }
        if (left != null) {
            for (int j = 0; j <= degree; j++) {
                left[j] = p[j][0];
            }
        }
        if (right != null) {
            for (int j = 0; j <= degree; j++) {
                right[j] = p[degree - j][j];
            }
        }
        return p[degree][0];
    }
}
