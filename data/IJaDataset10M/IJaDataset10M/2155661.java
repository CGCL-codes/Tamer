package net.sourceforge.webcompmath.draw;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import net.sourceforge.webcompmath.awt.Computable;
import net.sourceforge.webcompmath.awt.WcmWorker;
import net.sourceforge.webcompmath.data.CloneableFunction;
import net.sourceforge.webcompmath.data.DataUtils;
import net.sourceforge.webcompmath.data.Function;
import net.sourceforge.webcompmath.data.Parser;
import net.sourceforge.webcompmath.data.QueueableValue;
import net.sourceforge.webcompmath.data.SimpleFunction;
import net.sourceforge.webcompmath.data.Value;
import net.sourceforge.webcompmath.data.Variable;

/**
 * A RiemannSumRects calculates a Riemann sum for a function. It implements
 * Computable. You can specify and change the number of intervals in the sum, as
 * well as the method used to calculate the sum. Functions exist to return Value
 * objects for the sum using different computations. This class was written by
 * Gabriel Weinstock, with some modifications by David Eck, and extensive
 * enhancements by Tom Downey.
 * <p>
 * A RiemannSumRects can also show washers (for volumes of revolution), various
 * geometric shapes (for volumes of known cross section) or a segment of a
 * circle for polar area. The volume types are designed to give a 3D look.
 * <p>
 * Washers and some known cross section shapes come in two halves. You add the
 * rear half to a DisplayCanvas before the other drawables, then add the front
 * half at the end. This improves the 3d effect. You can use a partly
 * transparent fill color to help enhance things. In general you should not use
 * the TRAPEZOID method for the 3D volume shapes, as the drawing algorithm
 * doesn't really work well in this case.
 */
public class RiemannSumRects extends Drawable implements Computable {

    private static final long serialVersionUID = -5673548881859233319L;

    private Color posFillColor = new Color(255, 255, 180);

    private Color posOutlineColor = new Color(180, 180, 0);

    private Color negFillColor = new Color(255, 255, 180);

    private Color negOutlineColor = new Color(180, 180, 0);

    private Value intervalCount;

    private Function func, deriv, lowerFunc, lowerDeriv;

    private double[] param = new double[1];

    private boolean changed = true;

    private boolean inverse = false;

    private RiemannSlice rs = new RiemannSlice();

    private Value xMin, xMax;

    private Variable xVar = new Variable();

    /**
	 * The width, in pixels, of the outlines of the rects. It is restricted to
	 * being an integer in the range from 0 to 10. A value of 0 means that lines
	 * won't be drawn at all; this would only be useful for a filled rectangle
	 * that has a colored interior.
	 */
    protected int lineWidth = 1;

    /**
	 * Summation method type: left end point
	 */
    public static final int LEFTENDPOINT = 0;

    /**
	 * Summation method type: right end point
	 */
    public static final int RIGHTENDPOINT = 1;

    /**
	 * Summation method type: mid point
	 */
    public static final int MIDPOINT = 2;

    /**
	 * Summation method type: circumscribed (maximum value on the rectangle's
	 * interval)
	 */
    public static final int CIRCUMSCRIBED = 3;

    /**
	 * Summation method type: inscribed (minimum value on the rectangle's
	 * interval)
	 */
    public static final int INSCRIBED = 4;

    /**
	 * Summation method type: trapezoid
	 */
    public static final int TRAPEZOID = 5;

    /**
	 * For use in getValueObject(), to indicate whatever summation method is
	 * currently set for drawing.
	 */
    public static final int CURRENT_METHOD = -1;

    private int method;

    /**
	 * Rectangle (or trapezoid) shape
	 */
    public static final int RECTANGLE = 0;

    /**
	 * Front half of washer
	 */
    public static final int WASHER_FRONT = 1;

    /**
	 * Rear half of washer
	 */
    public static final int WASHER_REAR = 2;

    /**
	 * Square cross section with side along the base.
	 */
    public static final int SQUARE_SIDE = 3;

    /**
	 * Square cross section with diagonal along the base.
	 */
    public static final int SQUARE_DIAG_FRONT = 4;

    /**
	 * Square cross section with diagonal along the base.
	 */
    public static final int SQUARE_DIAG_REAR = 5;

    /**
	 * Semicircle cross section with diameter along the base.
	 */
    public static final int SEMICIRCLE = 6;

    /**
	 * Circle cross section with diameter along the base.
	 */
    public static final int CIRCLE_FRONT = 7;

    /**
	 * Circle cross section with diameter along the base.
	 */
    public static final int CIRCLE_REAR = 8;

    /**
	 * Equilateral triangle cross section with side along the base.
	 */
    public static final int EQUILATERAL = 9;

    /**
	 * Right isosceles triangle cross section with hypotenuse along the base.
	 */
    public static final int ISOSCELES_HYP = 10;

    /**
	 * Right isosceles triangle cross section with leg along the base.
	 */
    public static final int ISOSCELES_LEG = 11;

    /**
	 * Circular segment used for polar areas
	 */
    public static final int POLAR = 12;

    private int shape = RECTANGLE;

    private SetupData setupData = null;

    private RiemannSumRectsWorker worker;

    private ArrayList<QueueableValue> qv = new ArrayList<QueueableValue>();

    /**
	 * Get the shape for the Riemann sum.
	 * 
	 * @return the shape
	 */
    public int getShape() {
        return shape;
    }

    /**
	 * Set the shape for the Riemann sum. The default is RECTANGLE. Can be one
	 * of: RECTANGLE or POLAR for areas, WASHER_FRONT or WASHER_REAR for volumes
	 * of revolution, and SQUARE_SIDE, SQUARE_DIAG_FRONT, SQUARE_DIAG_REAR,
	 * SEMICIRCLE, CIRCLE_FRONT, CIRCLE_REAR, EQUILATERAL, ISOSCELES_HYP, or
	 * ISOSCELES_LEG for volumes of known cross section.
	 * 
	 * @param shape
	 *            the shape to set
	 */
    public void setShape(int shape) {
        this.shape = shape;
        rs.setShape(shape);
        needsRedraw();
    }

    /**
	 * Get the current (positive) color used to draw the rectangles
	 * 
	 * @return rectangle color
	 */
    public Color getColor() {
        return posFillColor;
    }

    /**
	 * Set the color used to draw the rectangles. The default color is a light
	 * yellow. Sets both the positive and negative colors
	 * 
	 * @param c
	 *            rectangle color
	 */
    public void setColor(Color c) {
        if (c != null) {
            posFillColor = c;
            negFillColor = c;
            rs.setFillColor(c);
            rs.setOutlineColor(c);
            needsRedraw();
        }
    }

    /**
	 * Set the color that will be used to draw outlines around the rects. If
	 * this is null, then no outlines are drawn. The default is a medium-dark
	 * red that looks brownish next to the default yellow fill color. Sets both
	 * the positive and negative colors.
	 * 
	 * @param c
	 *            outline color
	 */
    public void setOutlineColor(Color c) {
        posOutlineColor = c;
        negOutlineColor = c;
        rs.setOutlineColor(c);
        needsRedraw();
    }

    /**
	 * Get the (positive) color that is used to draw outlines around the rects.
	 * If this is null, then no outlines are drawn.
	 * 
	 * @return outline color
	 */
    public Color getOutlineColor() {
        return posOutlineColor;
    }

    /**
	 * Get the negative fill color
	 * 
	 * @return the negFillColor
	 */
    public Color getNegFillColor() {
        return negFillColor;
    }

    /**
	 * Set the negative fill color. Not used by POLAR shapes.
	 * 
	 * @param negFillColor
	 *            the negFillColor to set
	 */
    public void setNegFillColor(Color negFillColor) {
        this.negFillColor = negFillColor;
        needsRedraw();
    }

    /**
	 * Get the negative outline color
	 * 
	 * @return the negOutlineColor
	 */
    public Color getNegOutlineColor() {
        return negOutlineColor;
    }

    /**
	 * Set the negative outline color. Not used by POLAR shapes.
	 * 
	 * @param negOutlineColor
	 *            the negOutlineColor to set
	 */
    public void setNegOutlineColor(Color negOutlineColor) {
        this.negOutlineColor = negOutlineColor;
        needsRedraw();
    }

    /**
	 * Get the positive fill color
	 * 
	 * @return the posFillColor
	 */
    public Color getPosFillColor() {
        return posFillColor;
    }

    /**
	 * Set the positive fill color
	 * 
	 * @param posFillColor
	 *            the posFillColor to set
	 */
    public void setPosFillColor(Color posFillColor) {
        this.posFillColor = posFillColor;
        needsRedraw();
    }

    /**
	 * Get the positive outline color
	 * 
	 * @return the posOutlineColor
	 */
    public Color getPosOutlineColor() {
        return posOutlineColor;
    }

    /**
	 * Set the positive outline color
	 * 
	 * @param posOutlineColor
	 *            the posOutlineColor to set
	 */
    public void setPosOutlineColor(Color posOutlineColor) {
        this.posOutlineColor = posOutlineColor;
        needsRedraw();
    }

    /**
	 * Set the function whose Riemann sums are to be computed. If null, nothing
	 * is drawn. The function, if non-null, must have arity 1, or an
	 * IllegalArgumentException is thrown.
	 * 
	 * @param func
	 *            function to computer Riemann sum for
	 */
    public void setFunction(Function func) {
        if (func != null && func.getArity() != 1) throw new IllegalArgumentException("Function for Riemann sums must have arity 1.");
        this.func = func;
        rs.setUpperFunction(func);
        deriv = (func == null) ? null : func.derivative(1);
        changed = true;
        needsRedraw();
    }

    /**
	 * Returns the function whose Riemann sums are computed. Can be null.
	 * 
	 * @return function to computer Riemann sum for
	 */
    public Function getFunction() {
        return func;
    }

    /**
	 * Sets the upper function. Just calls setFunction(func)
	 * 
	 * @param func
	 *            the upper function
	 */
    public void setUpperFunction(Function func) {
        setFunction(func);
    }

    /**
	 * Returns the upper function. Just calls getFunction()
	 * 
	 * @return the upper function.
	 */
    public Function getUpperFunction() {
        return getFunction();
    }

    /**
	 * Set the lower function, when computing area between curves. If null, then
	 * the horizontal axis is used. The function, if non-null, must have arity
	 * 1, or an IllegalArgumentException is thrown. Not used by POLAR shape.
	 * 
	 * @param func
	 *            lower function
	 */
    public void setLowerFunction(Function func) {
        if (func != null && func.getArity() != 1) throw new IllegalArgumentException("Function for Riemann sums must have arity 1.");
        if (func == null) {
            lowerFunc = new SimpleFunction(new Parser().parse("0"), new Variable("x"));
        } else {
            lowerFunc = func;
        }
        lowerDeriv = lowerFunc.derivative(1);
        rs.setLowerFunction(func);
        changed = true;
        needsRedraw();
    }

    /**
	 * Returns the lower function. Can be null.
	 * 
	 * @return function to computer Riemann sum for
	 */
    public Function getLowerFuction() {
        return lowerFunc;
    }

    /**
	 * Set the method used to calculate the rectangles.
	 * 
	 * @param m
	 *            can be: LEFTENDPOINT, RIGHTENDPOINT, MIDPOINT, CIRCUMSCRIBED,
	 *            INSCRIBED or TRAPEZOID. TRAPEZOID is not valid for POLAR
	 *            shapes, and is not recommended for the volume shapes.
	 */
    public void setMethod(int m) {
        method = m;
        changed = true;
        rs.setMethod(m);
        needsRedraw();
    }

    /**
	 * Return the current method used to find the rectangle sums
	 * 
	 * @return can be: LEFTENDPOINT, RIGHTENDPOINT, MIDPOINT, CIRCUMSCRIBED,
	 *         INSCRIBED or TRAPEZOID (these are integers ranging from 0 to 5,
	 *         respectively)
	 */
    public int getMethod() {
        return method;
    }

    /**
	 * Set the width, in pixels, of lines that are drawn. This is also used for
	 * outlines of rects and ovals. If set to 0, the thinnest possible line is
	 * drawn.
	 * 
	 * @param width
	 *            line width
	 */
    public void setLineWidth(int width) {
        if (width != lineWidth) {
            lineWidth = width;
            if (lineWidth > 10) {
                lineWidth = 10;
            } else if (lineWidth < 0) {
                lineWidth = 0;
            }
            rs.setLineWidth(width);
            needsRedraw();
        }
    }

    /**
	 * Get the width, in pixels, of lines that are drawn. This is also used for
	 * outlines of rects and ovals.
	 * 
	 * @return line width
	 */
    public int getLineWidth() {
        return lineWidth;
    }

    /**
	 * This is generally called by a Controller. Indicates that all data should
	 * be recomputed because input values that the data depends on might have
	 * changed.
	 */
    public void compute() {
        changed = true;
        needsRedraw();
    }

    /**
	 * Get the number of intervals used.
	 * 
	 * @return a Value object representing the number of intervals
	 */
    public Value getIntervalCount() {
        return intervalCount;
    }

    /**
	 * Set the interval count (the RiemannSumRects will be redrawn after this
	 * function is called). The value will be clamped to be a value between 1
	 * and 5000. If the value is null, the default number of intervals, five, is
	 * used.
	 * 
	 * @param c
	 *            a Value object representing the interval count
	 */
    public void setIntervalCount(Value c) {
        changed = true;
        intervalCount = c;
        needsRedraw();
    }

    /**
	 * Get the max x value
	 * 
	 * @return Returns the xMax.
	 */
    public Value getXMax() {
        return xMax;
    }

    /**
	 * Set the max x value. If Double.NaN, then use the current xMax of the
	 * graph. For POLAR shapes, 2pi is used if this isn't set.
	 * 
	 * @param max
	 *            The xMax to set.
	 */
    public void setXMax(Value max) {
        changed = true;
        xMax = max;
        rs.setXMax(max);
        needsRedraw();
    }

    /**
	 * Get the min x value
	 * 
	 * @return Returns the xMin.
	 */
    public Value getXMin() {
        return xMin;
    }

    /**
	 * Set the min x value. If Double.NaN, then use the current xMin of the
	 * graph. For POLAR shapes, -2pi is used if this isn't set.
	 * 
	 * @param min
	 *            The xMin to set.
	 */
    public void setXMin(Value min) {
        changed = true;
        xMin = min;
        rs.setXMin(min);
        needsRedraw();
    }

    /**
	 * Construct a RiemannSumRects object that initially has nothing to draw and
	 * that is set up to use the default number of intervals, 5.
	 */
    public RiemannSumRects() {
        this(null, null);
    }

    /**
	 * Construct a new RiemannSumRects object.
	 * 
	 * @param i
	 *            a Value object representing the number of intervals. If null,
	 *            five intervals are used.
	 * @param f
	 *            a Function object used to derive the Riemann sum. If null,
	 *            nothing is drawn.
	 */
    public RiemannSumRects(Function f, Value i) {
        this(f, null, i);
    }

    /**
	 * Construct a new RiemannSumRects object.
	 * 
	 * @param f
	 *            a Function object used to derive the Riemann sum. If null,
	 *            nothing is drawn.
	 * @param lf
	 *            the lower function, may be null to use the horizontal axis
	 * @param i
	 *            a Value object representing the number of intervals. If null,
	 *            five intervals are used.
	 * 
	 */
    public RiemannSumRects(Function f, Function lf, Value i) {
        intervalCount = i;
        func = f;
        lowerFunc = lf;
        if (f != null) deriv = func.derivative(1);
        if (lf == null) {
            lowerFunc = new SimpleFunction(new Parser().parse("0"), new Variable("x"));
        } else {
            lowerFunc = lf;
        }
        lowerDeriv = lowerFunc.derivative(1);
        method = LEFTENDPOINT;
        rs.setCoordinate(xVar);
        rs.setUpperFunction(func);
        rs.setLowerFunction(lowerFunc);
        rs.setFillColor(posFillColor);
        rs.setOutlineColor(posOutlineColor);
        rs.setInverse(inverse);
        rs.setLineWidth(lineWidth);
        rs.setMethod(method);
        rs.setShape(shape);
        rs.setXMax(xMax);
        rs.setXMin(xMin);
    }

    /**
	 * Draw the Rieman sum rects. This is generally called by an object of class
	 * CoordinateRect
	 * 
	 * @param g
	 *            graphics context
	 * @param coordsChanged
	 *            true or false
	 */
    @Override
    public void draw(Graphics g, boolean coordsChanged) {
        if (func == null || coords == null) {
            return;
        }
        SetupData newData = new SetupData(coords, inverse, func, deriv, lowerFunc, lowerDeriv, method, shape, xMin, xMax, intervalCount);
        if (changed || coordsChanged || setupData == null) {
            changed = false;
            if (!queue.isUseWorker()) {
                setupData = setup(newData);
            } else {
                if (worker != null) {
                    if (!worker.isDone() && !worker.isCancelled()) {
                        worker.cancel(true);
                    }
                }
                worker = new RiemannSumRectsWorker(this, newData, setupData);
                worker.start();
            }
        }
        if (setupData == null || setupData.rectTops == null || setupData.rectTops.length == 0) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g;
        Color saveColor = g2.getColor();
        Stroke oldStroke = g2.getStroke();
        RenderingHints oldHints = g2.getRenderingHints();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        double x = setupData.startX;
        switch(setupData.shape) {
            case RECTANGLE:
                if (setupData.method == 5) {
                    GeneralPath gp = new GeneralPath();
                    float[] xp = new float[4];
                    float[] yp = new float[4];
                    xp[1] = xToPixel(x, setupData);
                    double rectBottomR = setupData.rectBottoms[0];
                    double rectTopR = setupData.rectTops[0];
                    double rectBottomL = 0, rectTopL = 0;
                    yp[1] = yToPixel(rectBottomR, setupData);
                    yp[2] = yToPixel(rectTopR, setupData);
                    for (int i = 0; i < setupData.intervals; i++) {
                        x += setupData.dx;
                        xp[0] = xp[3] = xp[1];
                        xp[1] = xp[2] = xToPixel(x, setupData);
                        yp[0] = yp[1];
                        yp[3] = yp[2];
                        rectBottomL = rectBottomR;
                        rectTopL = rectTopR;
                        rectBottomR = setupData.rectBottoms[i + 1];
                        rectTopR = setupData.rectTops[i + 1];
                        yp[1] = yToPixel(rectBottomR, setupData);
                        yp[2] = yToPixel(rectTopR, setupData);
                        if (!Double.isNaN(rectBottomR) && !Double.isNaN(rectBottomL) && !Double.isNaN(rectTopR) && !Double.isNaN(rectTopL)) {
                            if ((yp[0] > yp[3] && yp[1] < yp[2]) || (yp[0] < yp[3] && yp[1] > yp[2])) {
                                float[] xt = new float[3];
                                float[] yt = new float[3];
                                xt[0] = xt[2] = xp[0];
                                yt[0] = yp[0];
                                yt[2] = yp[3];
                                double xc, yc;
                                if (yt[0] > yt[2]) {
                                    double m0 = (setupData.rectBottoms[i + 1] - setupData.rectBottoms[i]) / setupData.dx;
                                    double m1 = (setupData.rectTops[i + 1] - setupData.rectTops[i]) / setupData.dx;
                                    xc = (m0 * (x - setupData.dx) - setupData.rectBottoms[i] - m1 * x + setupData.rectTops[i + 1]) / (m0 - m1);
                                    yc = m1 * (xc - x) + setupData.rectTops[i + 1];
                                } else {
                                    double m0 = (setupData.rectTops[i + 1] - setupData.rectTops[i]) / setupData.dx;
                                    double m1 = (setupData.rectBottoms[i + 1] - setupData.rectBottoms[i]) / setupData.dx;
                                    xc = (m0 * (x - setupData.dx) - setupData.rectTops[i] - m1 * x + setupData.rectBottoms[i + 1]) / (m0 - m1);
                                    yc = m1 * (xc - x) + setupData.rectBottoms[i + 1];
                                }
                                xt[1] = xToPixel(xc, setupData);
                                yt[1] = yToPixel(yc, setupData);
                                if (!setupData.inverse) {
                                    gp.reset();
                                    gp.moveTo(xt[0], yt[0]);
                                    gp.lineTo(xt[1], yt[1]);
                                    gp.lineTo(xt[2], yt[2]);
                                    gp.closePath();
                                    g2.setColor(doFillColor(setupData.swappedLimits, yt[0] > yt[2]));
                                    g2.fill(gp);
                                    if (posOutlineColor != null) {
                                        g2.setColor(doOutlineColor(setupData.swappedLimits, yt[0] > yt[2]));
                                        g2.draw(gp);
                                    }
                                } else {
                                    gp.reset();
                                    gp.moveTo(yt[0], xt[0]);
                                    gp.lineTo(yt[1], xt[1]);
                                    gp.lineTo(yt[2], xt[2]);
                                    gp.closePath();
                                    g2.setColor(doFillColor(setupData.swappedLimits, yt[0] < yt[2]));
                                    g2.fill(gp);
                                    if (posOutlineColor != null) {
                                        g2.setColor(doOutlineColor(setupData.swappedLimits, yt[0] < yt[2]));
                                        g2.draw(gp);
                                    }
                                }
                                xt[0] = xt[2] = xp[1];
                                yt[0] = yp[1];
                                yt[2] = yp[2];
                                if (!setupData.inverse) {
                                    gp.reset();
                                    gp.moveTo(xt[0], yt[0]);
                                    gp.lineTo(xt[1], yt[1]);
                                    gp.lineTo(xt[2], yt[2]);
                                    gp.closePath();
                                    g2.setColor(doFillColor(setupData.swappedLimits, yt[0] > yt[2]));
                                    g2.fill(gp);
                                    if (posOutlineColor != null) {
                                        g2.setColor(doOutlineColor(setupData.swappedLimits, yt[0] > yt[2]));
                                        g2.draw(gp);
                                    }
                                } else {
                                    gp.reset();
                                    gp.moveTo(yt[0], xt[0]);
                                    gp.lineTo(yt[1], xt[1]);
                                    gp.lineTo(yt[2], xt[2]);
                                    gp.closePath();
                                    g2.setColor(doFillColor(setupData.swappedLimits, yt[0] < yt[2]));
                                    g2.fill(gp);
                                    if (posOutlineColor != null) {
                                        g2.setColor(doOutlineColor(setupData.swappedLimits, yt[0] < yt[2]));
                                        g2.draw(gp);
                                    }
                                }
                            } else {
                                if (!setupData.inverse) {
                                    gp.reset();
                                    gp.moveTo(xp[0], yp[0]);
                                    gp.lineTo(xp[1], yp[1]);
                                    gp.lineTo(xp[2], yp[2]);
                                    gp.lineTo(xp[3], yp[3]);
                                    gp.closePath();
                                    g2.setColor(doFillColor(setupData.swappedLimits, yp[0] > yp[3] || yp[1] > yp[2]));
                                    g2.fill(gp);
                                    if (posOutlineColor != null) {
                                        g2.setColor(doOutlineColor(setupData.swappedLimits, yp[0] > yp[3]));
                                        g2.draw(gp);
                                    }
                                } else {
                                    gp.reset();
                                    gp.moveTo(yp[0], xp[0]);
                                    gp.lineTo(yp[1], xp[1]);
                                    gp.lineTo(yp[2], xp[2]);
                                    gp.lineTo(yp[3], xp[3]);
                                    gp.closePath();
                                    g2.setColor(doFillColor(setupData.swappedLimits, yp[0] < yp[3] || yp[1] < yp[2]));
                                    g2.fill(gp);
                                    if (posOutlineColor != null) {
                                        g2.setColor(doOutlineColor(setupData.swappedLimits, yp[0] < yp[3]));
                                        g2.draw(gp);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Rectangle2D rect = new Rectangle2D.Float();
                    Line2D line = new Line2D.Float();
                    float left = xToPixel(x, setupData);
                    for (int i = 0; i < setupData.intervals; i++) {
                        float right = xToPixel(x + setupData.dx, setupData);
                        double rectTop = setupData.rectTops[(setupData.method == 1) ? i + 1 : i];
                        double rectBottom = setupData.rectBottoms[(setupData.method == 1) ? i + 1 : i];
                        if (!Double.isNaN(rectTop) && !Double.isNaN(rectBottom)) {
                            float top = yToPixel(rectTop, setupData);
                            float bottom = yToPixel(rectBottom, setupData);
                            if (!setupData.inverse) {
                                float width = right - left;
                                float height = bottom - top;
                                if (height > 0) {
                                    g2.setColor(doFillColor(setupData.swappedLimits, true));
                                    rect.setFrame(left, top, width, height);
                                    g2.fill(rect);
                                } else if (height == 0) {
                                    g2.setColor(doFillColor(setupData.swappedLimits, true));
                                    line.setLine(left, bottom, left + width, bottom);
                                    g2.draw(line);
                                } else {
                                    g2.setColor(doFillColor(setupData.swappedLimits, false));
                                    rect.setFrame(left, bottom, width, -height);
                                    g2.fill(rect);
                                }
                                if (posOutlineColor != null) {
                                    if (height > 0) {
                                        g2.setColor(doOutlineColor(setupData.swappedLimits, true));
                                        rect.setFrame(left, top, width, height);
                                        g2.draw(rect);
                                    } else if (height == 0) {
                                        g2.setColor(doOutlineColor(setupData.swappedLimits, true));
                                        line.setLine(left, bottom, left + width, bottom);
                                        g2.draw(line);
                                    } else {
                                        g2.setColor(doOutlineColor(setupData.swappedLimits, false));
                                        rect.setFrame(left, bottom, width, -height);
                                        g2.draw(rect);
                                    }
                                }
                            } else {
                                float width = left - right;
                                float height = top - bottom;
                                if (height > 0) {
                                    g2.setColor(doFillColor(setupData.swappedLimits, true));
                                    rect.setFrame(bottom, right, height, width);
                                    g2.fill(rect);
                                } else if (height == 0) {
                                    g2.setColor(doFillColor(setupData.swappedLimits, true));
                                    line.setLine(bottom, left, bottom, top);
                                    g2.draw(line);
                                } else {
                                    g2.setColor(doFillColor(setupData.swappedLimits, false));
                                    rect.setFrame(top, right, -height, width);
                                    g2.fill(rect);
                                }
                                if (posOutlineColor != null) {
                                    if (height > 0) {
                                        g2.setColor(doOutlineColor(setupData.swappedLimits, true));
                                        g2.draw(rect);
                                    } else if (height == 0) {
                                        g2.setColor(doOutlineColor(setupData.swappedLimits, true));
                                        g2.draw(line);
                                    } else {
                                        g2.setColor(doOutlineColor(setupData.swappedLimits, false));
                                        g2.draw(rect);
                                    }
                                }
                            }
                        }
                        x += setupData.dx;
                        left = right;
                    }
                }
                break;
            case POLAR:
                for (int i = 0; i < setupData.intervals; i++) {
                    double radius = setupData.rectTops[(setupData.method == 1) ? i + 1 : i];
                    double theta1 = x * 180 / Math.PI;
                    if (radius < 0) {
                        radius *= -1;
                        theta1 += 180;
                    }
                    float originX = coords.xToPixelF(0);
                    float originY = coords.yToPixelF(0);
                    float radiusX = coords.xToPixelF(radius) - originX;
                    float radiusY = originY - coords.yToPixelF(radius);
                    double sliceWidth = setupData.dx * 180 / Math.PI;
                    Arc2D arc = new Arc2D.Float();
                    arc.setArc(originX - radiusX, originY - radiusY, radiusX * 2, radiusY * 2, theta1, sliceWidth, Arc2D.PIE);
                    g2.setColor(posFillColor);
                    g2.fill(arc);
                    g2.setColor(posOutlineColor);
                    g2.draw(arc);
                    x += setupData.dx;
                }
                break;
            case CIRCLE_FRONT:
            case CIRCLE_REAR:
            case EQUILATERAL:
            case ISOSCELES_HYP:
            case ISOSCELES_LEG:
            case SEMICIRCLE:
            case SQUARE_DIAG_FRONT:
            case SQUARE_DIAG_REAR:
            case SQUARE_SIDE:
            case WASHER_FRONT:
            case WASHER_REAR:
                rs.setSliceWidth(Math.abs(xToPixel(x + setupData.dx, setupData) - xToPixel(x, setupData)));
                rs.setOwnerData(canvas, coords);
                for (int i = 0; i < setupData.intervals; i++) {
                    xVar.setVal(x + setupData.dx / 2);
                    rs.draw(g, coordsChanged);
                    x += setupData.dx;
                }
                rs.setOwnerData(null, null);
                break;
            default:
                break;
        }
        g2.setStroke(oldStroke);
        g2.setRenderingHints(oldHints);
        g2.setColor(saveColor);
    }

    private float xToPixel(double x, SetupData in) {
        if (!in.inverse) {
            return CoordinateRect.xToPixelF(x, in.xmin, in.xmax, in.ymin, in.ymax, in.left, in.width, in.gap);
        } else {
            return CoordinateRect.yToPixelF(x, in.ymin, in.ymax, in.top, in.height, in.gap);
        }
    }

    private float yToPixel(double y, SetupData in) {
        if (!in.inverse) {
            return CoordinateRect.yToPixelF(y, in.ymin, in.ymax, in.top, in.height, in.gap);
        } else {
            return CoordinateRect.xToPixelF(y, in.xmin, in.xmax, in.ymin, in.ymax, in.left, in.width, in.gap);
        }
    }

    private Color doFillColor(boolean swappedLimits, boolean pos) {
        if (!swappedLimits) {
            if (pos) {
                return posFillColor;
            } else return negFillColor;
        } else if (pos) {
            return negFillColor;
        } else return posFillColor;
    }

    private Color doOutlineColor(boolean swappedLimits, boolean pos) {
        if (!swappedLimits) {
            if (pos) {
                return posOutlineColor;
            } else return negOutlineColor;
        } else if (pos) {
            return negOutlineColor;
        } else return posOutlineColor;
    }

    private SetupData setup(SetupData data) {
        if (data == null) {
            return null;
        }
        Thread ct = Thread.currentThread();
        doRectBasicData(data);
        if (data.shape != POLAR) {
            doAreaData(data);
            if (data.shape != RECTANGLE) {
                doVolumeData(data);
            }
        } else {
            doPolarData(data);
        }
        changed = false;
        if (!ct.isInterrupted()) {
            return data;
        } else {
            return null;
        }
    }

    private void doRectBasicData(SetupData data) {
        double intCtD = ((Double.isNaN(data.intervalsVal)) ? 5 : data.intervalsVal + 0.5);
        if (Double.isNaN(intCtD) || Double.isInfinite(intCtD)) intCtD = 5; else if (intCtD < 0) intCtD = 1; else if (intCtD > 5000) intCtD = 5000;
        data.intervals = (int) intCtD;
        if (data.shape != POLAR) {
            data.startX = data.xmin_val;
            if (Double.isNaN(data.startX) || Double.isInfinite(data.startX)) {
                if (!data.inverse) {
                    data.startX = data.xmin;
                } else {
                    data.startX = data.ymin;
                }
            }
            data.endX = data.xmax_val;
            if (Double.isNaN(data.endX) || Double.isInfinite(data.endX)) {
                if (!data.inverse) {
                    data.endX = data.xmax;
                } else {
                    data.endX = data.ymax;
                }
            }
            data.dx = (data.endX - data.startX) / data.intervals;
        } else {
            if (data.method == 5 || data.method == 0 || data.method == 1) {
                data.intervals -= 1;
            }
            data.startX = data.xmin_val;
            if (Double.isNaN(data.startX) || Double.isInfinite(data.startX)) {
                data.startX = Math.PI * (-2);
            }
            data.endX = data.xmax_val;
            if (Double.isNaN(data.endX) || Double.isInfinite(data.endX)) {
                data.endX = Math.PI * (-2);
            }
            data.dx = (data.endX - data.startX) / data.intervals;
        }
        if (data.startX > data.endX) {
            data.dx = -data.dx;
            double temp = data.startX;
            data.startX = data.endX;
            data.endX = temp;
            data.swappedLimits = true;
        }
    }

    private void doAreaData(SetupData data) {
        Thread ct = Thread.currentThread();
        data.endpointValsTop = new double[data.intervals + 1];
        data.endpointValsBottom = new double[data.intervals + 1];
        data.maxValsTop = new double[data.intervals];
        data.maxValsBottom = new double[data.intervals];
        data.minValsTop = new double[data.intervals];
        data.minValsBottom = new double[data.intervals];
        data.midpointValsTop = new double[data.intervals];
        data.midpointValsBottom = new double[data.intervals];
        param[0] = data.startX;
        data.endpointValsTop[0] = func.getVal(param);
        data.endpointValsBottom[0] = lowerFunc.getVal(param);
        if (data.intervals != 0) {
            int ptsPerInterval = 200 / data.intervals;
            double smalldx;
            if (ptsPerInterval < 1) {
                ptsPerInterval = 1;
                smalldx = data.dx;
            } else smalldx = data.dx / ptsPerInterval;
            boolean increasingleftTop, increasingleftBottom;
            boolean increasingrightTop = deriv.getVal(param) > 0;
            boolean increasingrightBottom = lowerDeriv.getVal(param) > 0;
            double x = data.startX;
            for (int i = 1; i <= data.intervals && !ct.isInterrupted(); i++) {
                x = Math.min(x + data.dx, data.endX);
                param[0] = x;
                data.endpointValsTop[i] = func.getVal(param);
                data.endpointValsBottom[i] = lowerFunc.getVal(param);
                param[0] = x - data.dx / 2;
                data.midpointValsTop[i - 1] = func.getVal(param);
                data.midpointValsBottom[i - 1] = lowerFunc.getVal(param);
                double max, min;
                max = min = data.endpointValsTop[i - 1];
                for (int j = 1; j <= ptsPerInterval; j++) {
                    increasingleftTop = increasingrightTop;
                    double xright = (x - data.dx) + j * smalldx;
                    param[0] = xright;
                    increasingrightTop = deriv.getVal(param) > 0;
                    if (increasingleftTop != increasingrightTop) {
                        if (increasingleftTop) {
                            double z = searchMax(func, deriv, xright - smalldx, xright, 1);
                            if (z > max) max = z;
                        } else {
                            double z = searchMin(func, deriv, xright - smalldx, xright, 1);
                            if (z < min) min = z;
                        }
                    }
                }
                if (data.endpointValsTop[i] > max) max = data.endpointValsTop[i]; else if (data.endpointValsTop[i] < min) min = data.endpointValsTop[i];
                data.minValsTop[i - 1] = min;
                data.maxValsTop[i - 1] = max;
                max = min = data.endpointValsBottom[i - 1];
                for (int j = 1; j <= ptsPerInterval; j++) {
                    increasingleftBottom = increasingrightBottom;
                    double xright = (x - data.dx) + j * smalldx;
                    param[0] = xright;
                    increasingrightBottom = lowerDeriv.getVal(param) > 0;
                    if (increasingleftBottom != increasingrightBottom) {
                        if (increasingleftBottom) {
                            double z = searchMax(lowerFunc, lowerDeriv, xright - smalldx, xright, 1);
                            if (z > max) max = z;
                        } else {
                            double z = searchMin(lowerFunc, lowerDeriv, xright - smalldx, xright, 1);
                            if (z < min) min = z;
                        }
                    }
                }
                if (data.endpointValsBottom[i] > max) max = data.endpointValsBottom[i]; else if (data.endpointValsBottom[i] < min) min = data.endpointValsBottom[i];
                data.minValsBottom[i - 1] = min;
                data.maxValsBottom[i - 1] = max;
            }
            double leftsum = 0, midpointsum = 0, rightsum = 0, maxsum = 0, minsum = 0;
            for (int i = 0; i < data.intervals && !ct.isInterrupted(); i++) {
                leftsum += data.endpointValsTop[i] - data.endpointValsBottom[i];
                midpointsum += data.midpointValsTop[i] - data.midpointValsBottom[i];
                maxsum += data.maxValsTop[i] - data.maxValsBottom[i];
                minsum += data.minValsTop[i] - data.minValsBottom[i];
            }
            if (ct.isInterrupted()) {
                return;
            }
            rightsum = leftsum - (data.endpointValsTop[0] + data.endpointValsBottom[0]) + (data.endpointValsTop[data.intervals] - data.endpointValsBottom[data.intervals]);
            double intSign = data.swappedLimits ? -1 : 1;
            data.sum[LEFTENDPOINT] = leftsum * data.dx * intSign;
            data.sum[RIGHTENDPOINT] = rightsum * data.dx * intSign;
            data.sum[MIDPOINT] = midpointsum * data.dx * intSign;
            data.sum[CIRCUMSCRIBED] = maxsum * data.dx * intSign;
            data.sum[INSCRIBED] = minsum * data.dx * intSign;
            data.sum[TRAPEZOID] = (leftsum + rightsum) / 2 * data.dx * intSign;
        } else {
            data.sum[LEFTENDPOINT] = 0;
            data.sum[RIGHTENDPOINT] = 0;
            data.sum[MIDPOINT] = 0;
            data.sum[CIRCUMSCRIBED] = 0;
            data.sum[INSCRIBED] = 0;
            data.sum[TRAPEZOID] = 0;
        }
        setRectData(data);
    }

    private void setRectData(SetupData data) {
        if (data.method == 3) setRectHeights(data.maxValsTop, data.maxValsBottom, data); else if (data.method == 4) setRectHeights(data.minValsTop, data.minValsBottom, data); else if (data.method == 2) setRectHeights(data.midpointValsTop, data.midpointValsBottom, data); else setRectHeights(data.endpointValsTop, data.endpointValsBottom, data);
    }

    private void setRectHeights(double[] top, double[] bottom, SetupData data) {
        data.rectTops = top;
        data.rectBottoms = bottom;
        changed = true;
    }

    private double searchMin(Function func, Function deriv, double x1, double x2, int depth) {
        double mid = (x1 + x2) / 2;
        param[0] = mid;
        if (depth >= 13) return func.getVal(param);
        double slope = deriv.getVal(param);
        if (slope < 0) return searchMin(func, deriv, mid, x2, depth + 1); else return searchMin(func, deriv, x1, mid, depth + 1);
    }

    private double searchMax(Function func, Function deriv, double x1, double x2, int depth) {
        double mid = (x1 + x2) / 2;
        param[0] = mid;
        if (depth >= 13) return func.getVal(param);
        double slope = deriv.getVal(param);
        if (slope > 0) return searchMax(func, deriv, mid, x2, depth + 1); else return searchMax(func, deriv, x1, mid, depth + 1);
    }

    private void doVolumeData(SetupData data) {
        Thread ct = Thread.currentThread();
        data.volumeSum = 0;
        if (data.shape == RECTANGLE) {
            return;
        }
        double bottom = 0, top = 0, bottom2 = 0, top2 = 0;
        double s = 0;
        for (int i = 0; i < data.intervals && !ct.isInterrupted(); i++) {
            switch(data.method) {
                case LEFTENDPOINT:
                    top = data.endpointValsTop[i];
                    bottom = data.endpointValsBottom[i];
                    s = top - bottom;
                    break;
                case RIGHTENDPOINT:
                    top = data.endpointValsTop[i + 1];
                    bottom = data.endpointValsBottom[i + 1];
                    s = top - bottom;
                    break;
                case MIDPOINT:
                    top = data.midpointValsTop[i];
                    bottom = data.midpointValsBottom[i];
                    s = top - bottom;
                    break;
                case INSCRIBED:
                    top = data.minValsTop[i];
                    bottom = data.minValsBottom[i];
                    s = top - bottom;
                    break;
                case CIRCUMSCRIBED:
                    top = data.maxValsTop[i];
                    bottom = data.maxValsBottom[i];
                    s = top - bottom;
                    break;
                case TRAPEZOID:
                    top = data.endpointValsTop[i];
                    bottom = data.endpointValsBottom[i];
                    top2 = data.endpointValsTop[i + 1];
                    bottom2 = data.endpointValsBottom[i + 1];
                    double s1, s2;
                    s1 = top - bottom;
                    s2 = top2 - bottom2;
                    s = (s1 * s1 + s1 * s2 + s2 * s2) / 3;
                    break;
                default:
                    break;
            }
            switch(data.shape) {
                case RECTANGLE:
                    break;
                case WASHER_FRONT:
                case WASHER_REAR:
                    double outerRadius, innerRadius, outerRadius2, innerRadius2;
                    outerRadius = Math.max(Math.abs(top - data.axisVal), Math.abs(bottom - data.axisVal));
                    if (bottom <= data.axisVal && data.axisVal <= top || top <= data.axisVal && data.axisVal <= bottom) {
                        innerRadius = 0;
                    } else {
                        innerRadius = Math.min(Math.abs(top - data.axisVal), Math.abs(bottom - data.axisVal));
                    }
                    if (data.method != TRAPEZOID) {
                        data.volumeSum += Math.PI * (outerRadius * outerRadius - innerRadius * innerRadius) * 0.5 * data.dx;
                    } else {
                        outerRadius2 = Math.max(Math.abs(top2 - data.axisVal), Math.abs(bottom2 - data.axisVal));
                        if (bottom <= data.axisVal && data.axisVal <= top || top <= data.axisVal && data.axisVal <= bottom) {
                            innerRadius2 = 0;
                        } else {
                            innerRadius2 = Math.min(Math.abs(top2 - data.axisVal), Math.abs(bottom2 - data.axisVal));
                        }
                        data.volumeSum += Math.PI / 6 * ((outerRadius * outerRadius + outerRadius * outerRadius2 + outerRadius2 * outerRadius2) - (innerRadius * innerRadius + innerRadius * innerRadius2 + innerRadius2 * innerRadius2)) * data.dx;
                    }
                    break;
                case SQUARE_SIDE:
                    data.volumeSum += s * s * data.dx;
                    break;
                case SQUARE_DIAG_FRONT:
                case SQUARE_DIAG_REAR:
                    data.volumeSum += s * s * data.dx * 0.25;
                    break;
                case SEMICIRCLE:
                case CIRCLE_FRONT:
                case CIRCLE_REAR:
                    s = s / 2;
                    data.volumeSum += Math.PI * 0.5 * s * s * data.dx;
                    break;
                case EQUILATERAL:
                    data.volumeSum += Math.sqrt(3) * 0.25 * s * s * data.dx;
                    break;
                case ISOSCELES_HYP:
                    data.volumeSum += 0.25 * s * s * data.dx;
                    break;
                case ISOSCELES_LEG:
                    data.volumeSum += 0.5 * s * s * data.dx;
                    break;
            }
        }
    }

    /**
	 * Gets a Value object that gives the area value of the Riemann sum for the
	 * specified method.
	 * 
	 * @return a Value object representing the area sum for the given method
	 * @param which
	 *            integer stating the method used to derive the sum; one of the
	 *            constants LEFTENDPOINT, RIGHTENDPOINT, MIDPOINT,
	 *            CIRCUMSCRIBED, INSCRIBED, TRAPEZOID, or CURRENT_METHOD.
	 */
    public Value getValueObject(final int which) {
        QueueableValue q = new QueueableValue() {

            private static final long serialVersionUID = 3403673634663273934L;

            public double getVal() {
                if (func == null || coords == null) return Double.NaN;
                SetupData newData = new SetupData(coords, inverse, func, deriv, lowerFunc, lowerDeriv, method, shape, xMin, xMax, intervalCount);
                if (changed) {
                    changed = false;
                    setupData = setup(newData);
                }
                if (setupData == null) {
                    return Double.NaN;
                }
                if (which == CURRENT_METHOD) {
                    return setupData.sum[method];
                } else {
                    return setupData.sum[which];
                }
            }

            @Override
            public double getQVal() {
                if (func == null || coords == null) return Double.NaN;
                if (changed) {
                    SetupData newData = new SetupData(coords, inverse, func, deriv, lowerFunc, lowerDeriv, method, shape, xMin, xMax, intervalCount);
                    changed = false;
                    if (!queue.isUseWorker()) {
                        setupData = setup(newData);
                    } else {
                        if (worker != null) {
                            if (!worker.isDone() && !worker.isCancelled()) {
                                worker.cancel(true);
                            }
                        }
                        worker = new RiemannSumRectsWorker(RiemannSumRects.this, newData, setupData);
                        worker.start();
                    }
                }
                if (setupData == null) {
                    return Double.NaN;
                }
                if (which == CURRENT_METHOD) {
                    return setupData.sum[setupData.method];
                } else {
                    return setupData.sum[which];
                }
            }
        };
        qv.add(q);
        return q;
    }

    /**
	 * Gets a Value object that gives the volume value of the Riemann sum for
	 * the current method.
	 * 
	 * @return a Value object representing the volume sum for the given method
	 */
    public Value getVolumeValueObject() {
        QueueableValue q = new QueueableValue() {

            private static final long serialVersionUID = 3403673634663273934L;

            public double getVal() {
                if (func == null || coords == null) return Double.NaN;
                setupData = new SetupData(coords, inverse, func, deriv, lowerFunc, lowerDeriv, method, shape, xMin, xMax, intervalCount);
                if (changed) {
                    changed = false;
                    setupData = setup(setupData);
                }
                if (setupData == null) {
                    return Double.NaN;
                }
                return setupData.volumeSum;
            }

            @Override
            public double getQVal() {
                if (func == null || coords == null) return Double.NaN;
                if (changed) {
                    setupData = new SetupData(coords, inverse, func, deriv, lowerFunc, lowerDeriv, method, shape, xMin, xMax, intervalCount);
                    changed = false;
                    if (!queue.isUseWorker()) {
                        setupData = setup(setupData);
                    } else {
                        if (worker != null) {
                            if (!worker.isDone() && !worker.isCancelled()) {
                                worker.cancel(true);
                            }
                        }
                        worker = new RiemannSumRectsWorker(RiemannSumRects.this, setupData, setupData);
                        worker.start();
                    }
                }
                if (setupData == null) {
                    return Double.NaN;
                }
                return setupData.volumeSum;
            }
        };
        qv.add(q);
        return q;
    }

    /**
	 * Get whether to display inverse version
	 * 
	 * @return true if inverse
	 */
    public boolean isInverse() {
        return inverse;
    }

    /**
	 * Set whether to show as inverse. Not supported by POLAR shape.
	 * 
	 * @param inverse
	 *            true for inverse
	 */
    public void setInverse(boolean inverse) {
        this.inverse = inverse;
        changed = true;
        rs.setInverse(inverse);
        needsRedraw();
    }

    /**
	 * Get the axis of revolution
	 * 
	 * @return the axis
	 */
    public Value getAxis() {
        return rs.getAxis();
    }

    /**
	 * Set the axis of revolution. Only valid for washer shapes.
	 * 
	 * @param axis
	 *            the axis to set
	 */
    public void setAxis(Value axis) {
        rs.setAxis(axis);
        needsRedraw();
    }

    /**
	 * Get the aspect ratio
	 * 
	 * @return the aspect
	 */
    public float getAspect() {
        return rs.getAspect();
    }

    /**
	 * Set the aspect ration (width to height). Default is 0.3. Only used for
	 * 3d-style shapes.
	 * 
	 * @param aspect
	 *            the aspect to set
	 */
    public void setAspect(float aspect) {
        rs.setAspect(aspect);
        needsRedraw();
    }

    /**
	 * Get the amount of slant offset
	 * 
	 * @return the slant
	 */
    public float getSlant() {
        return rs.getSlant();
    }

    /**
	 * Set the slant offset. This is from 0 to 1, with 0 being no slant and 1
	 * being 45 degrees. The default is 0.1. Only used for 3d-style shapes.
	 * 
	 * @param slant
	 *            the slant to set
	 */
    public void setSlant(float slant) {
        rs.setSlant(slant);
        needsRedraw();
    }

    private void doPolarData(SetupData data) {
        Thread ct = Thread.currentThread();
        data.endpointValsTop = new double[data.intervals + 1];
        data.maxValsTop = new double[data.intervals];
        data.minValsTop = new double[data.intervals];
        data.midpointValsTop = new double[data.intervals];
        param[0] = data.startX;
        data.endpointValsTop[0] = func.getVal(param);
        double x = data.startX;
        for (int i = 1; i <= data.intervals && !ct.isInterrupted(); i++) {
            double oldX = x;
            x = Math.min(x + data.dx, data.endX);
            param[0] = x;
            data.endpointValsTop[i] = func.getVal(param);
            param[0] = x - data.dx / 2;
            data.midpointValsTop[i - 1] = func.getVal(param);
            double max = searchMax(func, deriv, oldX, x, 1);
            double min = searchMin(func, deriv, oldX, x, 1);
            if (data.endpointValsTop[i] > max) max = data.endpointValsTop[i]; else if (data.endpointValsTop[i] < min) min = data.endpointValsTop[i];
            data.minValsTop[i - 1] = min;
            data.maxValsTop[i - 1] = max;
        }
        double leftsum = 0, midpointsum = 0, rightsum = 0, maxsum = 0, minsum = 0;
        for (int i = 0; i < data.intervals && !ct.isInterrupted(); i++) {
            leftsum += data.endpointValsTop[i] * data.endpointValsTop[i];
            midpointsum += data.midpointValsTop[i] * data.midpointValsTop[i];
            maxsum += data.maxValsTop[i] * data.maxValsTop[i];
            minsum += data.minValsTop[i] * data.minValsTop[i];
        }
        if (ct.isInterrupted()) {
            return;
        }
        rightsum = leftsum - (data.endpointValsTop[0] * data.endpointValsTop[0]) + (data.endpointValsTop[data.intervals] * data.endpointValsTop[data.intervals]);
        double intSign = data.swappedLimits ? -1 : 1;
        data.sum[LEFTENDPOINT] = leftsum * data.dx * intSign * 0.5;
        data.sum[RIGHTENDPOINT] = rightsum * data.dx * intSign * 0.5;
        data.sum[MIDPOINT] = midpointsum * data.dx * intSign * 0.5;
        data.sum[CIRCUMSCRIBED] = maxsum * data.dx * intSign * 0.5;
        data.sum[INSCRIBED] = minsum * data.dx * intSign * 0.5;
        data.sum[TRAPEZOID] = 0;
        setRectData(data);
    }

    private class RiemannSumRectsWorker extends WcmWorker<SetupData> {

        RiemannSumRects d;

        SetupData data;

        SetupData old;

        /**
		 * Construct a RiemannSumRectsWorker for the specified Drawable
		 * 
		 * @param d
		 *            the RiemannSumRectsWorker
		 * @param data
		 *            the input data
		 * @param old
		 *            the old(current) output data
		 */
        public RiemannSumRectsWorker(RiemannSumRects d, SetupData data, SetupData old) {
            super();
            this.d = d;
            this.data = data;
            this.old = old;
        }

        /**
		 * @see net.sourceforge.webcompmath.awt.WcmWorker#construct()
		 */
        @Override
        protected SetupData construct() throws Exception {
            return d.setup(data);
        }

        /**
		 * Check if all of the drawables are done.
		 * 
		 * @see net.sourceforge.webcompmath.awt.WcmWorker#finished()
		 */
        @Override
        protected void finished() {
            try {
                SetupData data = get();
                if (data != null) {
                    d.setupData = data;
                    d.needsRedraw();
                    for (Iterator iter = qv.iterator(); iter.hasNext(); ) {
                        QueueableValue element = (QueueableValue) iter.next();
                        element.fireQueueableValueUpdateEvent(element.getVal());
                    }
                }
            } catch (CancellationException e) {
            } catch (ExecutionException e) {
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private class SetupData {

        double xmin;

        double xmax;

        double ymin;

        double ymax;

        int height;

        int width;

        int top;

        int left;

        int gap;

        boolean inverse;

        Function func, deriv, lowerFunc, lowerDeriv;

        int method;

        int shape;

        double axisVal;

        double intervalsVal = Double.NaN;

        double xmin_val = Double.NaN;

        double xmax_val = Double.NaN;

        private double[] endpointValsTop, endpointValsBottom, maxValsTop, maxValsBottom, minValsTop, minValsBottom, midpointValsTop, midpointValsBottom;

        private double[] rectTops, rectBottoms;

        private int intervals;

        double startX = 0, dx = 0, endX = 0;

        boolean swappedLimits = false;

        private double[] sum;

        private double volumeSum;

        SetupData(CoordinateRect c, boolean inverse, Function func, Function deriv, Function lowerFunc, Function lowerDeriv, int method, int shape, Value xMin, Value xMax, Value intervalCount) {
            xmin = c.getXmin();
            xmax = c.getXmax();
            ymin = c.getYmin();
            ymax = c.getYmax();
            height = c.getHeight();
            width = c.getWidth();
            top = c.getTop();
            left = c.getLeft();
            gap = c.getGap();
            this.inverse = inverse;
            Variable[] clonedVars = null;
            if (queue.isUseWorker() && func != null && func instanceof CloneableFunction) {
                CloneableFunction cf = (CloneableFunction) func;
                clonedVars = DataUtils.cloneVariables(cf.getVariables(), null);
                this.func = cf.cloneFunction(clonedVars);
            } else {
                this.func = func;
            }
            if (queue.isUseWorker() && lowerFunc != null && lowerFunc instanceof CloneableFunction) {
                CloneableFunction cf = (CloneableFunction) lowerFunc;
                clonedVars = DataUtils.cloneVariables(cf.getVariables(), clonedVars);
                this.lowerFunc = cf.cloneFunction(clonedVars);
            } else {
                this.lowerFunc = lowerFunc;
            }
            this.deriv = this.func.derivative(1);
            this.lowerDeriv = this.lowerFunc.derivative(1);
            this.method = method;
            this.shape = shape;
            if (rs.getAxis() != null) {
                this.axisVal = rs.getAxis().getVal();
            }
            if (xMin != null) {
                xmin_val = xMin.getVal();
            }
            if (xMax != null) {
                xmax_val = xMax.getVal();
            }
            if (intervalCount != null) {
                intervalsVal = intervalCount.getVal();
            }
            sum = new double[6];
        }
    }
}
