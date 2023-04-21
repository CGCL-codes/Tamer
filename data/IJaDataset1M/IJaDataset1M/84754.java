package net.sourceforge.webcompmath.functions;

import net.sourceforge.webcompmath.data.*;

/**
 * A TableFunction is a function that is specified by a table of (x,y)-points.
 * Values are interpolated between the specified x-values. This can be done in
 * several differnt ways; the method that is used is controlled by the "Style"
 * property. Since a TableFunction extends FunctionParserExtension, a
 * TableFunction can be added to a Parser (provided it has a name), and it can
 * then be used in expressions parsed by that parser. Note that this class is
 * meant to be used for functions that are defined by a fairly small number of
 * points, since each function evaluation involves a linear search through the
 * list of x-values of the defining points.
 */
public class TableFunction extends FunctionParserExtension implements CloneableExpressionCommand, CloneableFunction, Cloneable {

    private static final long serialVersionUID = 783838829796275934L;

    /**
	 * If the style of the function is set to SMOOTH, then cubic interpolation
	 * is used to find the value of the functions for x-values between the
	 * points that define the function.
	 */
    public static final int SMOOTH = 0;

    /**
	 * If the style of the function is set to PIECEWISE_LINEAR, then linear
	 * interpolation is used to find the value of the functions for x-values
	 * between the points that define the function.
	 */
    public static final int PIECEWISE_LINEAR = 1;

    /**
	 * If the style of the function is set to STEP, then the function is
	 * piecewise constant, and the value of the function at x is taken from the
	 * nearest point in the list of points that define the function.
	 */
    public static final int STEP = 2;

    /**
	 * If the style of the function is set to STEP_LEFT, then the function is
	 * piecewise constant, and the value of the function at x is taken from the
	 * nearest point to the left in the list of points that define the function.
	 */
    public static final int STEP_LEFT = 3;

    /**
	 * If the style of the function is set to STEP_RIGHT, then the function is
	 * piecewise constant, and the value of the function at x is taken from the
	 * nearest point to the right in the list of points that define the
	 * function.
	 */
    public static final int STEP_RIGHT = 4;

    private int style;

    private double[] xCoords = new double[10];

    private double[] yCoords = new double[10];

    private CubicSegment[] segments;

    private int pointCt;

    /**
	 * Create a TableFunction with SMOOTH style and no points.
	 */
    public TableFunction() {
        this(SMOOTH);
    }

    /**
	 * Create a TableFunction with specified style and no points.
	 * 
	 * @param style
	 *            The style for the function: SMOOTH, PIECEWISE_LINEAR, STEP,
	 *            STEP_LEFT, or STEP_RIGHT.
	 */
    public TableFunction(int style) {
        this.style = style;
        if (style == SMOOTH) segments = new CubicSegment[9];
    }

    /**
	 * 
	 * Copy data from another TableFunction, except that the name of the funcion
	 * is not duplicated. The new TableFunction is nameless.
	 * 
	 * @param source
	 *            table to copy from
	 */
    public void copyDataFrom(TableFunction source) {
        xCoords = (double[]) source.xCoords.clone();
        yCoords = (double[]) source.yCoords.clone();
        style = -1;
        setStyle(source.style);
    }

    /**
	 * Set the style of this TableFunction, to specify how values are
	 * interpolated between points on the curve.
	 * 
	 * @param style
	 *            One of the style constants SMOOTH, PIECEWISE_LINEAR, STEP,
	 *            STEP_LEFT, STEP_RIGHT. Other values are ignored.
	 */
    public void setStyle(int style) {
        if (style == this.style || style < 0 || style > 4) return;
        this.style = style;
        if (style == SMOOTH) {
            segments = new CubicSegment[xCoords.length - 1];
            for (int i = 0; i < pointCt - 1; i++) segments[i] = new CubicSegment(xCoords[i], xCoords[i + 1], yCoords[i], yCoords[i + 1], 0, 0);
            for (int i = 0; i < pointCt - 1; i++) interpolateDerivatives(i);
        } else {
            segments = null;
        }
    }

    /**
	 * Get the style of this TableFunction, which specifies how values are
	 * interpolated between points on the curve.
	 * 
	 * @return The style of this TableFunction. This is one of the constants
	 *         SMOOTH, PIECEWISE_LINEAR, STEP, STEP_LEFT, or STEP_RIGHT.
	 */
    public int getStyle() {
        return style;
    }

    /**
	 * Add points to the table. The x-coordinates of the points are taken from
	 * the xCoords array. The y-coordinate for the i-th point is yCoords[i], if
	 * an i-th position exists in this array. Otherwise, the y-coordinate is is
	 * zero. (Note that if xCoords[i] duplicates an x-value already in the
	 * table, then no new point is added but the corresponging y-value is
	 * changed.)
	 * 
	 * @param xCoords
	 *            A list of x-coordinates to be added to the table. If this is
	 *            null, then nothing is done.
	 * @param yCoords
	 *            The value of yCoords[i], if it exists, is the y-coordinate
	 *            corresponding to xCoords[i]. Otherwise, the y-coordinate is
	 *            undefined. This can be null, in which case all y-coordinates
	 *            are zero.
	 */
    public void addPoints(double[] xCoords, double[] yCoords) {
        if (xCoords == null) return;
        int ct = xCoords.length;
        if (yCoords == null) ct = 0; else if (yCoords.length < ct) ct = yCoords.length;
        for (int i = 0; i < ct; i++) addPoint(xCoords[i], yCoords[i]);
        for (int i = ct; i < xCoords.length; i++) addPoint(xCoords[i], 0);
    }

    /**
	 * Add points to the table. The number of points added is intervals + 1. The
	 * x-coordinates are evenly spaced between xmin and xmax. The y-coordinates
	 * are zero.
	 * 
	 * @param intervals
	 *            The number of intervals. The number of points added is
	 *            intervals + 1. The value should be at least 1. If not, nothing
	 *            is done.
	 * @param xmin
	 *            The minimim x-coordinate for added points.
	 * @param xmax
	 *            The maximum x-coodinate for added points. Should be greater
	 *            than xmin, for efficiency, but no error occurs if it is not.
	 */
    public void addIntervals(int intervals, double xmin, double xmax) {
        if (intervals < 1) return;
        double dx = (xmax - xmin) / intervals;
        for (int i = 0; i < intervals; i++) addPoint(xmin + i * dx, 0);
        addPoint(xmax, 0);
    }

    /**
	 * Add a point with the specified x and y coordinates. If a point with the
	 * given x coordinate already exists in the table, then no new point is
	 * added, but the associated y-value is changed. (If x is Double.NaN, then
	 * no change is made and the return value is -1.)
	 * 
	 * @param x
	 *            The x-coordinate of the point to be added or modified.
	 * @param y
	 *            The y-coordinate of the point.
	 * @return the position of the point in the list of points, where the first
	 *         point is at position zero.
	 */
    public int addPoint(double x, double y) {
        if (Double.isNaN(x)) return -1;
        int pos = 0;
        while (pos < pointCt && xCoords[pos] < x) pos++;
        if (pos < pointCt && xCoords[pos] == x) {
            yCoords[pos] = y;
        } else {
            if (pointCt == xCoords.length) {
                double[] temp = new double[2 * xCoords.length];
                System.arraycopy(xCoords, 0, temp, 0, xCoords.length);
                xCoords = temp;
                temp = new double[2 * yCoords.length];
                System.arraycopy(yCoords, 0, temp, 0, yCoords.length);
                yCoords = temp;
                if (style == SMOOTH) {
                    CubicSegment[] temps = new CubicSegment[xCoords.length - 1];
                    System.arraycopy(segments, 0, temps, 0, pointCt - 1);
                    segments = temps;
                }
            }
            for (int i = pointCt; i > pos; i--) {
                xCoords[i] = xCoords[i - 1];
                yCoords[i] = yCoords[i - 1];
            }
            xCoords[pos] = x;
            yCoords[pos] = y;
            if (style == SMOOTH && pointCt > 0) {
                if (pos == pointCt) segments[pointCt - 1] = new CubicSegment(); else {
                    for (int i = pointCt - 1; i > pos; i--) segments[i] = segments[i - 1];
                    segments[pos] = new CubicSegment();
                }
            }
            pointCt++;
        }
        if (style == SMOOTH && pointCt > 0) {
            if (pos > 0) segments[pos - 1].setData(xCoords[pos - 1], xCoords[pos], yCoords[pos - 1], yCoords[pos], 0, 0);
            if (pos < pointCt - 1) segments[pos].setData(xCoords[pos], xCoords[pos + 1], yCoords[pos], yCoords[pos + 1], 0, 0);
            for (int i = pos - 2; i <= pos + 1; i++) interpolateDerivatives(i);
        }
        return pos;
    }

    private void interpolateDerivatives(int pos) {
        if (pos < 0 || pos > pointCt - 2) return;
        if (pointCt == 2) segments[0].setDerivativesFromNeighbors(Double.NaN, 0, Double.NaN, 0); else if (pos == 0) segments[0].setDerivativesFromNeighbors(Double.NaN, 0, xCoords[2], yCoords[2]); else if (pos == pointCt - 2) segments[pointCt - 2].setDerivativesFromNeighbors(xCoords[pointCt - 3], yCoords[pointCt - 3], Double.NaN, 0); else segments[pos].setDerivativesFromNeighbors(xCoords[pos - 1], yCoords[pos - 1], xCoords[pos + 2], yCoords[pos + 2]);
    }

    /**
	 * Gets the number of points in the table.
	 * 
	 * @return number of points
	 */
    public int getPointCount() {
        return pointCt;
    }

    /**
	 * Get the x-coordinate in the i-th point, where the first point is number
	 * zero. Throws an IllegalArgumentException if i is less than zero or
	 * greater than or equal to the number of points.
	 * 
	 * @param i
	 *            index of point
	 * @return it's x coordinate
	 */
    public double getX(int i) {
        if (i >= 0 && i < pointCt) return xCoords[i]; else throw new IllegalArgumentException("Point index out of range: " + i);
    }

    /**
	 * Get the y-coordinate in the i-th point, where the first point is number
	 * zero. Throws an IllegalArgumentException if i is less than zero or
	 * greater than or equal to the number of points.
	 * 
	 * @param i
	 *            index of point
	 * @return it's y coordinate
	 */
    public double getY(int i) {
        if (i >= 0 && i < pointCt) return yCoords[i]; else throw new IllegalArgumentException("Point index out of range: " + i);
    }

    /**
	 * Set the y-coordinate in the i-th point to y, where the first point is
	 * number zero. Throws an IllegalArgumentException if i is less than zero or
	 * greater than or equal to the number of points.
	 * 
	 * @param i
	 *            index of point
	 * @param y
	 *            y coordinate to set
	 */
    public void setY(int i, double y) {
        if (i >= 0 && i < pointCt) yCoords[i] = y; else throw new IllegalArgumentException("Point index out of range: " + i);
        if (style == SMOOTH) {
            if (i > 0) segments[i - 1].setData(xCoords[i - 1], xCoords[i], yCoords[i - 1], yCoords[i], 0, 0);
            if (i < pointCt - 1) segments[i].setData(xCoords[i], xCoords[i + 1], yCoords[i], yCoords[i + 1], 0, 0);
            for (int j = i - 2; j <= i + 1; j++) interpolateDerivatives(j);
        }
    }

    /**
	 * If there is a point in the list with x-coordinate x, then this function
	 * returns the index of that point in the list (where the index of the first
	 * point is zero). If there is no such point, then -1 is returned.
	 * 
	 * @param x
	 *            x coordinate to check
	 * @return index of the point
	 */
    public int findPoint(double x) {
        int i = 0;
        while (i < pointCt) {
            if (x == xCoords[i]) return i; else if (x > xCoords[i]) i++; else break;
        }
        return -1;
    }

    /**
	 * Removes the i-th point from the list of points. Throws an
	 * IllegalArgumentException if i is less than zero or greater than or equal
	 * to the number of points.
	 * 
	 * @param i
	 *            index of point to remove
	 */
    public void removePointAt(int i) {
        if (i < 0 || i >= pointCt) throw new IllegalArgumentException("Point index out of range: " + i);
        pointCt--;
        for (int j = i; j < pointCt; j++) {
            xCoords[j] = xCoords[j + 1];
            yCoords[j] = yCoords[j + 1];
        }
        if (style == SMOOTH) {
            style = -1;
            setStyle(SMOOTH);
        }
    }

    /**
	 * Remove all points. The resulting function is undefined everywhere.
	 */
    public void removeAllPoints() {
        pointCt = 0;
        xCoords = new double[10];
        yCoords = new double[10];
    }

    /**
	 * Get the value of the function at x, using interpolation if x lies between
	 * two x-coordinates in the list of points that define the function. If x is
	 * outside the range of x-coords in the table, the value of the function is
	 * Double.NaN.
	 * 
	 * @param x
	 *            x coordinate
	 * @return value of function
	 */
    public double getVal(double x) {
        return computeValue(x, null, 0);
    }

    private double computeValue(double x, Cases cases, int derivativeLevel) {
        if (Double.isNaN(x)) return Double.NaN;
        if (pointCt == 0 || x < xCoords[0] || x > xCoords[pointCt - 1]) return Double.NaN;
        if (pointCt == 1) {
            if (derivativeLevel > 0) return Double.NaN; else {
                if (cases == null) cases.addCase(0);
                return yCoords[0];
            }
        }
        int casenum;
        double ans;
        int seg = 0;
        switch(style) {
            case STEP:
                {
                    while (seg < pointCt - 1 && x > (xCoords[seg] + xCoords[seg + 1]) / 2) seg++;
                    casenum = seg;
                    if (derivativeLevel == 0) ans = yCoords[seg]; else if (x < (xCoords[seg] + xCoords[seg + 1]) / 2 || seg == pointCt - 1 || yCoords[seg] == yCoords[seg + 1]) ans = 0; else ans = Double.NaN;
                    break;
                }
            case STEP_RIGHT:
                {
                    while (seg < pointCt - 1 && x > xCoords[seg]) seg++;
                    casenum = seg;
                    if (derivativeLevel == 0) ans = yCoords[seg]; else if (x < xCoords[seg] || seg >= pointCt - 1 || yCoords[seg] == yCoords[seg + 1]) ans = 0; else ans = Double.NaN;
                    break;
                }
            case STEP_LEFT:
                {
                    while (seg < pointCt - 1 && x >= xCoords[seg + 1]) seg++;
                    casenum = seg;
                    if (derivativeLevel == 0) ans = yCoords[seg]; else if (x > xCoords[seg] || seg == 0 || yCoords[seg] == yCoords[seg - 1]) ans = 0; else ans = Double.NaN;
                    break;
                }
            case PIECEWISE_LINEAR:
                {
                    while (seg < pointCt - 1 && x > xCoords[seg]) seg++;
                    casenum = seg;
                    if (x == xCoords[seg]) {
                        if (derivativeLevel == 0) ans = yCoords[seg]; else if (seg == 0) {
                            if (derivativeLevel == 1) ans = (yCoords[1] - yCoords[0]) / (xCoords[1] - xCoords[0]); else ans = 0;
                        } else if (seg == pointCt - 1) {
                            if (derivativeLevel == 1) ans = (yCoords[pointCt - 1] - yCoords[pointCt - 2]) / (xCoords[pointCt - 1] - xCoords[pointCt - 2]); else ans = 0;
                        } else {
                            double leftslope = (yCoords[seg] - yCoords[seg - 1]) / (xCoords[seg] - xCoords[seg - 1]);
                            double rightslope = (yCoords[seg] - yCoords[seg + 1]) / (xCoords[seg] - xCoords[seg + 1]);
                            if (Math.abs(leftslope - rightslope) < 1e-12) {
                                if (derivativeLevel == 1) ans = leftslope; else ans = 0;
                            } else ans = Double.NaN;
                        }
                    } else {
                        if (derivativeLevel == 0) {
                            ans = yCoords[seg - 1] + ((yCoords[seg] - yCoords[seg - 1]) / (xCoords[seg] - xCoords[seg - 1])) * (x - xCoords[seg - 1]);
                        } else if (derivativeLevel == 1) {
                            ans = (yCoords[seg] - yCoords[seg - 1]) / (xCoords[seg] - xCoords[seg - 1]);
                        } else ans = 0;
                    }
                    break;
                }
            default:
                {
                    while (seg < pointCt - 2 && x > xCoords[seg + 1]) seg++;
                    casenum = seg;
                    if (x == xCoords[seg + 1] && seg < pointCt - 2 && seg > 0) {
                        if (derivativeLevel == 0) ans = yCoords[seg + 1]; else if (derivativeLevel == 1) ans = segments[seg].derivativeValue(x, 1); else {
                            double leftslope = segments[seg - 1].derivativeValue(x, 2);
                            double rightslope = segments[seg].derivativeValue(x, 2);
                            if (Math.abs(leftslope - rightslope) < 1e-12) ans = segments[seg].derivativeValue(x, derivativeLevel); else ans = Double.NaN;
                        }
                    } else {
                        ans = segments[seg].derivativeValue(x, derivativeLevel);
                    }
                }
        }
        if (cases != null) cases.addCase(casenum);
        return ans;
    }

    /**
	 * Get the value of the function at the specified parameter value.
	 * 
	 * @param params
	 *            should be an array of length 1 holding the argument of the
	 *            function. However if the length is greater than one, the extra
	 *            arguments are simply ignored.
	 * @param cases
	 *            if cases is non-null, a case value is stored here, for help in
	 *            continuity computations.
	 * @return value of function
	 */
    public double getValueWithCases(double[] params, Cases cases) {
        return computeValue(params[0], cases, 0);
    }

    /**
	 * Get the value of the function at the specified parameter value.
	 * 
	 * @param params
	 *            should be an array of length 1 holding the argument of the
	 *            function. However if the length is greater than one, the extra
	 *            arguments are simply ignored.
	 * @return value of function
	 */
    public double getVal(double[] params) {
        return computeValue(params[0], null, 0);
    }

    /**
	 * Compute the derivative of this function. The value of the parameter, wrt,
	 * must be 1 or an IllegalArguemntException will be thrown.
	 * 
	 * @param wrt
	 *            arg. number with respect to
	 * @return the derivative
	 */
    public Function derivative(int wrt) {
        if (wrt != 1) throw new IllegalArgumentException("Attempt to take the derivative of a function of one argument with respect to argument number " + wrt);
        return new Deriv(this);
    }

    /**
	 * Returns null. It really should be the constant function zero, but I don't
	 * expect this ever to be called. Since dependsOn(wrt) returns false, it
	 * will never be called within the JCM system.
	 * 
	 * @param wrt
	 *            variable with respect to
	 * @return the derivative
	 */
    public Function derivative(Variable wrt) {
        return null;
    }

    /**
	 * Returns false.
	 * 
	 * @param wrt
	 *            variable with respect to
	 * @return always false in this case
	 */
    public boolean dependsOn(Variable wrt) {
        return false;
    }

    /**
	 * Returns the arity of the function, which is 1.
	 * 
	 * @return arity of function
	 */
    public int getArity() {
        return 1;
    }

    /**
	 * Override method apply() from interface FunctionParserExtension, to handle
	 * cases properly. Not meant to be called directly.
	 * 
	 * @param stack
	 *            stack to use
	 * @param cases
	 *            cases to use
	 */
    @Override
    public void apply(StackOfDouble stack, Cases cases) {
        double x = stack.pop();
        stack.push(computeValue(x, cases, 0));
    }

    private static class Deriv extends FunctionParserExtension {

        private static final long serialVersionUID = 1L;

        TableFunction derivativeOf;

        int derivativeLevel;

        Deriv(Deriv f) {
            derivativeLevel = f.derivativeLevel + 1;
            derivativeOf = f.derivativeOf;
        }

        Deriv(TableFunction f) {
            derivativeLevel = 1;
            derivativeOf = f;
        }

        /**
		 * @see net.sourceforge.webcompmath.functions.FunctionParserExtension#getName()
		 */
        @Override
        public String getName() {
            String name = derivativeOf.getName();
            for (int i = 0; i < derivativeLevel; i++) name += "'";
            return name;
        }

        /**
		 * @see net.sourceforge.webcompmath.functions.FunctionParserExtension#setName(java.lang.String)
		 */
        @Override
        public void setName(String name) {
        }

        /**
		 * @see net.sourceforge.webcompmath.data.Function#getValueWithCases(double[],
		 *      net.sourceforge.webcompmath.data.Cases)
		 */
        public double getValueWithCases(double[] params, Cases cases) {
            return derivativeOf.computeValue(params[0], cases, derivativeLevel);
        }

        /**
		 * @see net.sourceforge.webcompmath.data.Function#getVal(double[])
		 */
        public double getVal(double[] params) {
            return derivativeOf.computeValue(params[0], null, derivativeLevel);
        }

        /**
		 * @see net.sourceforge.webcompmath.data.Function#derivative(int)
		 */
        public Function derivative(int wrt) {
            if (wrt != 1) throw new IllegalArgumentException("Attempt to take the derivative of a function of one argument with respect to argument number " + wrt);
            return new Deriv(this);
        }

        /**
		 * @see net.sourceforge.webcompmath.data.Function#derivative(net.sourceforge.webcompmath.data.Variable)
		 */
        public Function derivative(Variable wrt) {
            return null;
        }

        /**
		 * @see net.sourceforge.webcompmath.data.Function#dependsOn(net.sourceforge.webcompmath.data.Variable)
		 */
        public boolean dependsOn(Variable wrt) {
            return false;
        }

        /**
		 * @see net.sourceforge.webcompmath.data.Function#getArity()
		 */
        public int getArity() {
            return 1;
        }

        /**
		 * @see net.sourceforge.webcompmath.functions.FunctionParserExtension#apply(net.sourceforge.webcompmath.data.StackOfDouble,
		 *      net.sourceforge.webcompmath.data.Cases)
		 */
        @Override
        public void apply(StackOfDouble stack, Cases cases) {
            double x = stack.pop();
            stack.push(derivativeOf.computeValue(x, cases, derivativeLevel));
        }
    }

    /**
	 * @see net.sourceforge.webcompmath.data.CloneableExpressionCommand#cloneExpressionCommand(net.sourceforge.webcompmath.data.Variable[])
	 */
    public ExpressionCommand cloneExpressionCommand(Variable[] vars) {
        return doClone(vars);
    }

    /**
	 * @see net.sourceforge.webcompmath.data.CloneableFunction#cloneFunction(net.sourceforge.webcompmath.data.Variable[])
	 */
    public Function cloneFunction(Variable[] vars) {
        return doClone(vars);
    }

    private TableFunction doClone(Variable[] vars) {
        TableFunction tc = null;
        try {
            tc = (TableFunction) super.clone();
            tc.xCoords = new double[xCoords.length];
            System.arraycopy(xCoords, 0, tc.xCoords, 0, xCoords.length);
            tc.yCoords = new double[yCoords.length];
            System.arraycopy(yCoords, 0, tc.yCoords, 0, yCoords.length);
            tc.segments = new CubicSegment[segments.length];
            for (int i = 0; i < segments.length; i++) {
                tc.segments[i] = (CubicSegment) segments[i].clone();
            }
        } catch (CloneNotSupportedException e) {
            return null;
        }
        return tc;
    }

    /**
	 * @see net.sourceforge.webcompmath.data.CloneableFunction#getVariables()
	 */
    public Variable[] getVariables() {
        return null;
    }
}
