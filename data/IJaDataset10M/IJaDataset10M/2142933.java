package org.loon.framework.android.game.core.geom;

/**
 * The <code>QuadCurve2D</code> class defines a quadratic parametric curve
 * segment in {@code (x,y)} coordinate space.
 * <p>
 * This class is only the abstract superclass for all objects that store a 2D
 * quadratic curve segment. The actual storage representation of the coordinates
 * is left to the subclass.
 * 
 * @author Jim Graham
 * @since 1.2
 */
public abstract class QuadCurve2D implements Shape, Cloneable {

    /**
	 * This is an abstract class that cannot be instantiated directly.
	 * Type-specific implementation subclasses are available for instantiation
	 * and provide a number of formats for storing the information necessary to
	 * satisfy the various accessor methods below.
	 * 
	 * @see and.awt.geom.QuadCurve2D.Float
	 * @see and.awt.geom.QuadCurve2D.Double
	 * @since 1.2
	 */
    protected QuadCurve2D() {
    }

    /**
	 * Returns the X coordinate of the start point in <code>double</code> in
	 * precision.
	 * 
	 * @return the X coordinate of the start point.
	 * @since 1.2
	 */
    public abstract double getX1();

    /**
	 * Returns the Y coordinate of the start point in <code>double</code>
	 * precision.
	 * 
	 * @return the Y coordinate of the start point.
	 * @since 1.2
	 */
    public abstract double getY1();

    /**
	 * Returns the start point.
	 * 
	 * @return a <code>Point2D</code> that is the start point of this
	 *         <code>QuadCurve2D</code>.
	 * @since 1.2
	 */
    public abstract Point2D getP1();

    /**
	 * Returns the X coordinate of the control point in <code>double</code>
	 * precision.
	 * 
	 * @return X coordinate the control point
	 * @since 1.2
	 */
    public abstract double getCtrlX();

    /**
	 * Returns the Y coordinate of the control point in <code>double</code>
	 * precision.
	 * 
	 * @return the Y coordinate of the control point.
	 * @since 1.2
	 */
    public abstract double getCtrlY();

    /**
	 * Returns the control point.
	 * 
	 * @return a <code>Point2D</code> that is the control point of this
	 *         <code>Point2D</code>.
	 * @since 1.2
	 */
    public abstract Point2D getCtrlPt();

    /**
	 * Returns the X coordinate of the end point in <code>double</code>
	 * precision.
	 * 
	 * @return the x coordiante of the end point.
	 * @since 1.2
	 */
    public abstract double getX2();

    /**
	 * Returns the Y coordinate of the end point in <code>double</code>
	 * precision.
	 * 
	 * @return the Y coordinate of the end point.
	 * @since 1.2
	 */
    public abstract double getY2();

    /**
	 * Returns the end point.
	 * 
	 * @return a <code>Point</code> object that is the end point of this
	 *         <code>Point2D</code>.
	 * @since 1.2
	 */
    public abstract Point2D getP2();

    /**
	 * Sets the location of the end points and control point of this curve to
	 * the specified <code>double</code> coordinates.
	 * 
	 * @param x1
	 *            the X coordinate of the start point
	 * @param y1
	 *            the Y coordinate of the start point
	 * @param ctrlx
	 *            the X coordinate of the control point
	 * @param ctrly
	 *            the Y coordinate of the control point
	 * @param x2
	 *            the X coordinate of the end point
	 * @param y2
	 *            the Y coordinate of the end point
	 * @since 1.2
	 */
    public abstract void setCurve(double x1, double y1, double ctrlx, double ctrly, double x2, double y2);

    /**
	 * Sets the location of the end points and control points of this
	 * <code>QuadCurve2D</code> to the <code>double</code> coordinates at the
	 * specified offset in the specified array.
	 * 
	 * @param coords
	 *            the array containing coordinate values
	 * @param offset
	 *            the index into the array from which to start getting the
	 *            coordinate values and assigning them to this
	 *            <code>QuadCurve2D</code>
	 * @since 1.2
	 */
    public void setCurve(double[] coords, int offset) {
        setCurve(coords[offset + 0], coords[offset + 1], coords[offset + 2], coords[offset + 3], coords[offset + 4], coords[offset + 5]);
    }

    /**
	 * Sets the location of the end points and control point of this
	 * <code>QuadCurve2D</code> to the specified <code>Point2D</code>
	 * coordinates.
	 * 
	 * @param p1
	 *            the start point
	 * @param cp
	 *            the control point
	 * @param p2
	 *            the end point
	 * @since 1.2
	 */
    public void setCurve(Point2D p1, Point2D cp, Point2D p2) {
        setCurve(p1.getX(), p1.getY(), cp.getX(), cp.getY(), p2.getX(), p2.getY());
    }

    /**
	 * Sets the location of the end points and control points of this
	 * <code>QuadCurve2D</code> to the coordinates of the <code>Point2D</code>
	 * objects at the specified offset in the specified array.
	 * 
	 * @param pts
	 *            an array containing <code>Point2D</code> that define
	 *            coordinate values
	 * @param offset
	 *            the index into <code>pts</code> from which to start getting
	 *            the coordinate values and assigning them to this
	 *            <code>QuadCurve2D</code>
	 * @since 1.2
	 */
    public void setCurve(Point2D[] pts, int offset) {
        setCurve(pts[offset + 0].getX(), pts[offset + 0].getY(), pts[offset + 1].getX(), pts[offset + 1].getY(), pts[offset + 2].getX(), pts[offset + 2].getY());
    }

    /**
	 * Sets the location of the end points and control point of this
	 * <code>QuadCurve2D</code> to the same as those in the specified
	 * <code>QuadCurve2D</code>.
	 * 
	 * @param c
	 *            the specified <code>QuadCurve2D</code>
	 * @since 1.2
	 */
    public void setCurve(QuadCurve2D c) {
        setCurve(c.getX1(), c.getY1(), c.getCtrlX(), c.getCtrlY(), c.getX2(), c.getY2());
    }

    /**
	 * Returns the square of the flatness, or maximum distance of a control
	 * point from the line connecting the end points, of the quadratic curve
	 * specified by the indicated control points.
	 * 
	 * @param x1
	 *            the X coordinate of the start point
	 * @param y1
	 *            the Y coordinate of the start point
	 * @param ctrlx
	 *            the X coordinate of the control point
	 * @param ctrly
	 *            the Y coordinate of the control point
	 * @param x2
	 *            the X coordinate of the end point
	 * @param y2
	 *            the Y coordinate of the end point
	 * @return the square of the flatness of the quadratic curve defined by the
	 *         specified coordinates.
	 * @since 1.2
	 */
    public static double getFlatnessSq(double x1, double y1, double ctrlx, double ctrly, double x2, double y2) {
        return Line2D.ptSegDistSq(x1, y1, x2, y2, ctrlx, ctrly);
    }

    /**
	 * Returns the flatness, or maximum distance of a control point from the
	 * line connecting the end points, of the quadratic curve specified by the
	 * indicated control points.
	 * 
	 * @param x1
	 *            the X coordinate of the start point
	 * @param y1
	 *            the Y coordinate of the start point
	 * @param ctrlx
	 *            the X coordinate of the control point
	 * @param ctrly
	 *            the Y coordinate of the control point
	 * @param x2
	 *            the X coordinate of the end point
	 * @param y2
	 *            the Y coordinate of the end point
	 * @return the flatness of the quadratic curve defined by the specified
	 *         coordinates.
	 * @since 1.2
	 */
    public static double getFlatness(double x1, double y1, double ctrlx, double ctrly, double x2, double y2) {
        return Line2D.ptSegDist(x1, y1, x2, y2, ctrlx, ctrly);
    }

    /**
	 * Returns the square of the flatness, or maximum distance of a control
	 * point from the line connecting the end points, of the quadratic curve
	 * specified by the control points stored in the indicated array at the
	 * indicated index.
	 * 
	 * @param coords
	 *            an array containing coordinate values
	 * @param offset
	 *            the index into <code>coords</code> from which to to start
	 *            getting the values from the array
	 * @return the flatness of the quadratic curve that is defined by the values
	 *         in the specified array at the specified index.
	 * @since 1.2
	 */
    public static double getFlatnessSq(double coords[], int offset) {
        return Line2D.ptSegDistSq(coords[offset + 0], coords[offset + 1], coords[offset + 4], coords[offset + 5], coords[offset + 2], coords[offset + 3]);
    }

    /**
	 * Returns the flatness, or maximum distance of a control point from the
	 * line connecting the end points, of the quadratic curve specified by the
	 * control points stored in the indicated array at the indicated index.
	 * 
	 * @param coords
	 *            an array containing coordinate values
	 * @param offset
	 *            the index into <code>coords</code> from which to start getting
	 *            the coordinate values
	 * @return the flatness of a quadratic curve defined by the specified array
	 *         at the specified offset.
	 * @since 1.2
	 */
    public static double getFlatness(double coords[], int offset) {
        return Line2D.ptSegDist(coords[offset + 0], coords[offset + 1], coords[offset + 4], coords[offset + 5], coords[offset + 2], coords[offset + 3]);
    }

    /**
	 * Returns the square of the flatness, or maximum distance of a control
	 * point from the line connecting the end points, of this
	 * <code>QuadCurve2D</code>.
	 * 
	 * @return the square of the flatness of this <code>QuadCurve2D</code>.
	 * @since 1.2
	 */
    public double getFlatnessSq() {
        return Line2D.ptSegDistSq(getX1(), getY1(), getX2(), getY2(), getCtrlX(), getCtrlY());
    }

    /**
	 * Returns the flatness, or maximum distance of a control point from the
	 * line connecting the end points, of this <code>QuadCurve2D</code>.
	 * 
	 * @return the flatness of this <code>QuadCurve2D</code>.
	 * @since 1.2
	 */
    public double getFlatness() {
        return Line2D.ptSegDist(getX1(), getY1(), getX2(), getY2(), getCtrlX(), getCtrlY());
    }

    /**
	 * Subdivides this <code>QuadCurve2D</code> and stores the resulting two
	 * subdivided curves into the <code>left</code> and <code>right</code> curve
	 * parameters. Either or both of the <code>left</code> and
	 * <code>right</code> objects can be the same as this
	 * <code>QuadCurve2D</code> or <code>null</code>.
	 * 
	 * @param left
	 *            the <code>QuadCurve2D</code> object for storing the left or
	 *            first half of the subdivided curve
	 * @param right
	 *            the <code>QuadCurve2D</code> object for storing the right or
	 *            second half of the subdivided curve
	 * @since 1.2
	 */
    public void subdivide(QuadCurve2D left, QuadCurve2D right) {
        subdivide(this, left, right);
    }

    /**
	 * Subdivides the quadratic curve specified by the <code>src</code>
	 * parameter and stores the resulting two subdivided curves into the
	 * <code>left</code> and <code>right</code> curve parameters. Either or both
	 * of the <code>left</code> and <code>right</code> objects can be the same
	 * as the <code>src</code> object or <code>null</code>.
	 * 
	 * @param src
	 *            the quadratic curve to be subdivided
	 * @param left
	 *            the <code>QuadCurve2D</code> object for storing the left or
	 *            first half of the subdivided curve
	 * @param right
	 *            the <code>QuadCurve2D</code> object for storing the right or
	 *            second half of the subdivided curve
	 * @since 1.2
	 */
    public static void subdivide(QuadCurve2D src, QuadCurve2D left, QuadCurve2D right) {
        double x1 = src.getX1();
        double y1 = src.getY1();
        double ctrlx = src.getCtrlX();
        double ctrly = src.getCtrlY();
        double x2 = src.getX2();
        double y2 = src.getY2();
        double ctrlx1 = (x1 + ctrlx) / 2.0;
        double ctrly1 = (y1 + ctrly) / 2.0;
        double ctrlx2 = (x2 + ctrlx) / 2.0;
        double ctrly2 = (y2 + ctrly) / 2.0;
        ctrlx = (ctrlx1 + ctrlx2) / 2.0;
        ctrly = (ctrly1 + ctrly2) / 2.0;
        if (left != null) {
            left.setCurve(x1, y1, ctrlx1, ctrly1, ctrlx, ctrly);
        }
        if (right != null) {
            right.setCurve(ctrlx, ctrly, ctrlx2, ctrly2, x2, y2);
        }
    }

    /**
	 * Subdivides the quadratic curve specified by the coordinates stored in the
	 * <code>src</code> array at indices <code>srcoff</code> through
	 * <code>srcoff</code>&nbsp;+&nbsp;5 and stores the resulting two subdivided
	 * curves into the two result arrays at the corresponding indices. Either or
	 * both of the <code>left</code> and <code>right</code> arrays can be
	 * <code>null</code> or a reference to the same array and offset as the
	 * <code>src</code> array. Note that the last point in the first subdivided
	 * curve is the same as the first point in the second subdivided curve.
	 * Thus, it is possible to pass the same array for <code>left</code> and
	 * <code>right</code> and to use offsets such that <code>rightoff</code>
	 * equals <code>leftoff</code> + 4 in order to avoid allocating extra
	 * storage for this common point.
	 * 
	 * @param src
	 *            the array holding the coordinates for the source curve
	 * @param srcoff
	 *            the offset into the array of the beginning of the the 6 source
	 *            coordinates
	 * @param left
	 *            the array for storing the coordinates for the first half of
	 *            the subdivided curve
	 * @param leftoff
	 *            the offset into the array of the beginning of the the 6 left
	 *            coordinates
	 * @param right
	 *            the array for storing the coordinates for the second half of
	 *            the subdivided curve
	 * @param rightoff
	 *            the offset into the array of the beginning of the the 6 right
	 *            coordinates
	 * @since 1.2
	 */
    public static void subdivide(double src[], int srcoff, double left[], int leftoff, double right[], int rightoff) {
        double x1 = src[srcoff + 0];
        double y1 = src[srcoff + 1];
        double ctrlx = src[srcoff + 2];
        double ctrly = src[srcoff + 3];
        double x2 = src[srcoff + 4];
        double y2 = src[srcoff + 5];
        if (left != null) {
            left[leftoff + 0] = x1;
            left[leftoff + 1] = y1;
        }
        if (right != null) {
            right[rightoff + 4] = x2;
            right[rightoff + 5] = y2;
        }
        x1 = (x1 + ctrlx) / 2.0;
        y1 = (y1 + ctrly) / 2.0;
        x2 = (x2 + ctrlx) / 2.0;
        y2 = (y2 + ctrly) / 2.0;
        ctrlx = (x1 + x2) / 2.0;
        ctrly = (y1 + y2) / 2.0;
        if (left != null) {
            left[leftoff + 2] = x1;
            left[leftoff + 3] = y1;
            left[leftoff + 4] = ctrlx;
            left[leftoff + 5] = ctrly;
        }
        if (right != null) {
            right[rightoff + 0] = ctrlx;
            right[rightoff + 1] = ctrly;
            right[rightoff + 2] = x2;
            right[rightoff + 3] = y2;
        }
    }

    /**
	 * Solves the quadratic whose coefficients are in the <code>eqn</code> array
	 * and places the non-complex roots back into the same array, returning the
	 * number of roots. The quadratic solved is represented by the equation:
	 * 
	 * <pre>
	 *     eqn = {C, B, A};
	 *     ax^2 + bx + c = 0
	 * </pre>
	 * 
	 * A return value of <code>-1</code> is used to distinguish a constant
	 * equation, which might be always 0 or never 0, from an equation that has
	 * no zeroes.
	 * 
	 * @param eqn
	 *            the array that contains the quadratic coefficients
	 * @return the number of roots, or <code>-1</code> if the equation is a
	 *         constant
	 * @since 1.2
	 */
    public static int solveQuadratic(double eqn[]) {
        return solveQuadratic(eqn, eqn);
    }

    /**
	 * Solves the quadratic whose coefficients are in the <code>eqn</code> array
	 * and places the non-complex roots into the <code>res</code> array,
	 * returning the number of roots. The quadratic solved is represented by the
	 * equation:
	 * 
	 * <pre>
	 *     eqn = {C, B, A};
	 *     ax^2 + bx + c = 0
	 * </pre>
	 * 
	 * A return value of <code>-1</code> is used to distinguish a constant
	 * equation, which might be always 0 or never 0, from an equation that has
	 * no zeroes.
	 * 
	 * @param eqn
	 *            the specified array of coefficients to use to solve the
	 *            quadratic equation
	 * @param res
	 *            the array that contains the non-complex roots resulting from
	 *            the solution of the quadratic equation
	 * @return the number of roots, or <code>-1</code> if the equation is a
	 *         constant.
	 * @since 1.3
	 */
    public static int solveQuadratic(double eqn[], double res[]) {
        double a = eqn[2];
        double b = eqn[1];
        double c = eqn[0];
        int roots = 0;
        if (a == 0.0) {
            if (b == 0.0) {
                return -1;
            }
            res[roots++] = -c / b;
        } else {
            double d = b * b - 4.0 * a * c;
            if (d < 0.0) {
                return 0;
            }
            d = Math.sqrt(d);
            if (b < 0.0) {
                d = -d;
            }
            double q = (b + d) / -2.0;
            res[roots++] = q / a;
            if (q != 0.0) {
                res[roots++] = c / q;
            }
        }
        return roots;
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @since 1.2
	 */
    public boolean contains(double x, double y) {
        double x1 = getX1();
        double y1 = getY1();
        double xc = getCtrlX();
        double yc = getCtrlY();
        double x2 = getX2();
        double y2 = getY2();
        double kx = x1 - 2 * xc + x2;
        double ky = y1 - 2 * yc + y2;
        double dx = x - x1;
        double dy = y - y1;
        double dxl = x2 - x1;
        double dyl = y2 - y1;
        double t0 = (dx * ky - dy * kx) / (dxl * ky - dyl * kx);
        if (t0 < 0 || t0 > 1) {
            return false;
        }
        double xb = kx * t0 * t0 + 2 * (xc - x1) * t0 + x1;
        double yb = ky * t0 * t0 + 2 * (yc - y1) * t0 + y1;
        double xl = dxl * t0 + x1;
        double yl = dyl * t0 + y1;
        return (x >= xb && x < xl) || (x >= xl && x < xb) || (y >= yb && y < yl) || (y >= yl && y < yb);
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @since 1.2
	 */
    public boolean contains(Point2D p) {
        return contains(p.getX(), p.getY());
    }

    /**
	 * Fill an array with the coefficients of the parametric equation in t,
	 * ready for solving against val with solveQuadratic. We currently have: val
	 * = Py(t) = C1*(1-t)^2 + 2*CP*t*(1-t) + C2*t^2 = C1 - 2*C1*t + C1*t^2 +
	 * 2*CP*t - 2*CP*t^2 + C2*t^2 = C1 + (2*CP - 2*C1)*t + (C1 - 2*CP + C2)*t^2
	 * 0 = (C1 - val) + (2*CP - 2*C1)*t + (C1 - 2*CP + C2)*t^2 0 = C + Bt + At^2
	 * C = C1 - val B = 2*CP - 2*C1 A = C1 - 2*CP + C2
	 */
    private static void fillEqn(double eqn[], double val, double c1, double cp, double c2) {
        eqn[0] = c1 - val;
        eqn[1] = cp + cp - c1 - c1;
        eqn[2] = c1 - cp - cp + c2;
        return;
    }

    /**
	 * Evaluate the t values in the first num slots of the vals[] array and
	 * place the evaluated values back into the same array. Only evaluate t
	 * values that are within the range <0, 1>, including the 0 and 1 ends of
	 * the range iff the include0 or include1 booleans are true. If an
	 * "inflection" equation is handed in, then any points which represent a
	 * point of inflection for that quadratic equation are also ignored.
	 */
    private static int evalQuadratic(double vals[], int num, boolean include0, boolean include1, double inflect[], double c1, double ctrl, double c2) {
        int j = 0;
        for (int i = 0; i < num; i++) {
            double t = vals[i];
            if ((include0 ? t >= 0 : t > 0) && (include1 ? t <= 1 : t < 1) && (inflect == null || inflect[1] + 2 * inflect[2] * t != 0)) {
                double u = 1 - t;
                vals[j++] = c1 * u * u + 2 * ctrl * t * u + c2 * t * t;
            }
        }
        return j;
    }

    private static final int BELOW = -2;

    private static final int LOWEDGE = -1;

    private static final int INSIDE = 0;

    private static final int HIGHEDGE = 1;

    private static final int ABOVE = 2;

    /**
	 * Determine where coord lies with respect to the range from low to high. It
	 * is assumed that low <= high. The return value is one of the 5 values
	 * BELOW, LOWEDGE, INSIDE, HIGHEDGE, or ABOVE.
	 */
    private static int getTag(double coord, double low, double high) {
        if (coord <= low) {
            return (coord < low ? BELOW : LOWEDGE);
        }
        if (coord >= high) {
            return (coord > high ? ABOVE : HIGHEDGE);
        }
        return INSIDE;
    }

    /**
	 * Determine if the pttag represents a coordinate that is already in its
	 * test range, or is on the border with either of the two opttags
	 * representing another coordinate that is "towards the inside" of that test
	 * range. In other words, are either of the two "opt" points
	 * "drawing the pt inward"?
	 */
    private static boolean inwards(int pttag, int opt1tag, int opt2tag) {
        switch(pttag) {
            case BELOW:
            case ABOVE:
            default:
                return false;
            case LOWEDGE:
                return (opt1tag >= INSIDE || opt2tag >= INSIDE);
            case INSIDE:
                return true;
            case HIGHEDGE:
                return (opt1tag <= INSIDE || opt2tag <= INSIDE);
        }
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @since 1.2
	 */
    public boolean intersects(double x, double y, double w, double h) {
        if (w <= 0 || h <= 0) {
            return false;
        }
        double x1 = getX1();
        double y1 = getY1();
        int x1tag = getTag(x1, x, x + w);
        int y1tag = getTag(y1, y, y + h);
        if (x1tag == INSIDE && y1tag == INSIDE) {
            return true;
        }
        double x2 = getX2();
        double y2 = getY2();
        int x2tag = getTag(x2, x, x + w);
        int y2tag = getTag(y2, y, y + h);
        if (x2tag == INSIDE && y2tag == INSIDE) {
            return true;
        }
        double ctrlx = getCtrlX();
        double ctrly = getCtrlY();
        int ctrlxtag = getTag(ctrlx, x, x + w);
        int ctrlytag = getTag(ctrly, y, y + h);
        if (x1tag < INSIDE && x2tag < INSIDE && ctrlxtag < INSIDE) {
            return false;
        }
        if (y1tag < INSIDE && y2tag < INSIDE && ctrlytag < INSIDE) {
            return false;
        }
        if (x1tag > INSIDE && x2tag > INSIDE && ctrlxtag > INSIDE) {
            return false;
        }
        if (y1tag > INSIDE && y2tag > INSIDE && ctrlytag > INSIDE) {
            return false;
        }
        if (inwards(x1tag, x2tag, ctrlxtag) && inwards(y1tag, y2tag, ctrlytag)) {
            return true;
        }
        if (inwards(x2tag, x1tag, ctrlxtag) && inwards(y2tag, y1tag, ctrlytag)) {
            return true;
        }
        boolean xoverlap = (x1tag * x2tag <= 0);
        boolean yoverlap = (y1tag * y2tag <= 0);
        if (x1tag == INSIDE && x2tag == INSIDE && yoverlap) {
            return true;
        }
        if (y1tag == INSIDE && y2tag == INSIDE && xoverlap) {
            return true;
        }
        double[] eqn = new double[3];
        double[] res = new double[3];
        if (!yoverlap) {
            fillEqn(eqn, (y1tag < INSIDE ? y : y + h), y1, ctrly, y2);
            return (solveQuadratic(eqn, res) == 2 && evalQuadratic(res, 2, true, true, null, x1, ctrlx, x2) == 2 && getTag(res[0], x, x + w) * getTag(res[1], x, x + w) <= 0);
        }
        if (!xoverlap) {
            fillEqn(eqn, (x1tag < INSIDE ? x : x + w), x1, ctrlx, x2);
            return (solveQuadratic(eqn, res) == 2 && evalQuadratic(res, 2, true, true, null, y1, ctrly, y2) == 2 && getTag(res[0], y, y + h) * getTag(res[1], y, y + h) <= 0);
        }
        double dx = x2 - x1;
        double dy = y2 - y1;
        double k = y2 * x1 - x2 * y1;
        int c1tag, c2tag;
        if (y1tag == INSIDE) {
            c1tag = x1tag;
        } else {
            c1tag = getTag((k + dx * (y1tag < INSIDE ? y : y + h)) / dy, x, x + w);
        }
        if (y2tag == INSIDE) {
            c2tag = x2tag;
        } else {
            c2tag = getTag((k + dx * (y2tag < INSIDE ? y : y + h)) / dy, x, x + w);
        }
        if (c1tag * c2tag <= 0) {
            return true;
        }
        c1tag = ((c1tag * x1tag <= 0) ? y1tag : y2tag);
        fillEqn(eqn, (c2tag < INSIDE ? x : x + w), x1, ctrlx, x2);
        int num = solveQuadratic(eqn, res);
        evalQuadratic(res, num, true, true, null, y1, ctrly, y2);
        c2tag = getTag(res[0], y, y + h);
        return (c1tag * c2tag <= 0);
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @since 1.2
	 */
    public boolean intersects(Rectangle2D r) {
        return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @since 1.2
	 */
    public boolean contains(double x, double y, double w, double h) {
        if (w <= 0 || h <= 0) {
            return false;
        }
        return (contains(x, y) && contains(x + w, y) && contains(x + w, y + h) && contains(x, y + h));
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @since 1.2
	 */
    public boolean contains(Rectangle2D r) {
        return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @since 1.2
	 */
    public Rectangle getBounds() {
        return getBounds2D().getBounds();
    }

    /**
	 * Returns an iteration object that defines the boundary of the shape of
	 * this <code>QuadCurve2D</code>. The iterator for this class is not
	 * multi-threaded safe, which means that this <code>QuadCurve2D</code> class
	 * does not guarantee that modifications to the geometry of this
	 * <code>QuadCurve2D</code> object do not affect any iterations of that
	 * geometry that are already in process.
	 * 
	 * @param at
	 *            an optional {@link AffineTransform} to apply to the shape
	 *            boundary
	 * @return a {@link PathIterator} object that defines the boundary of the
	 *         shape.
	 * @since 1.2
	 */
    public PathIterator getPathIterator(AffineTransform at) {
        return new QuadIterator(this, at);
    }

    /**
	 * Returns an iteration object that defines the boundary of the flattened
	 * shape of this <code>QuadCurve2D</code>. The iterator for this class is
	 * not multi-threaded safe, which means that this <code>QuadCurve2D</code>
	 * class does not guarantee that modifications to the geometry of this
	 * <code>QuadCurve2D</code> object do not affect any iterations of that
	 * geometry that are already in process.
	 * 
	 * @param at
	 *            an optional <code>AffineTransform</code> to apply to the
	 *            boundary of the shape
	 * @param flatness
	 *            the maximum distance that the control points for a subdivided
	 *            curve can be with respect to a line connecting the end points
	 *            of this curve before this curve is replaced by a straight line
	 *            connecting the end points.
	 * @return a <code>PathIterator</code> object that defines the flattened
	 *         boundary of the shape.
	 * @since 1.2
	 */
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return new FlatteningPathIterator(getPathIterator(at), flatness);
    }

    /**
	 * Creates a new object of the same class and with the same contents as this
	 * object.
	 * 
	 * @return a clone of this instance.
	 * @exception OutOfMemoryError
	 *                if there is not enough memory.
	 * @see java.lang.Cloneable
	 * @since 1.2
	 */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
}
