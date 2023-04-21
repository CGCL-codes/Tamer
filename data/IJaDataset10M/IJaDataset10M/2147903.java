package mipt.gui.graph.plot;

import java.awt.Color;
import mipt.gui.chart.extra.Colors;
import mipt.gui.graph.GraphRegion;
import mipt.gui.graph.GraphUtils;
import mipt.gui.graph.Scale;

/**
 * Implements only one CurveRenderer method (because data format is not specified)
 *  paintCurve - delegating its parts to new abstract methods.
 * Optionally, can use ColorsCollection to paint different dots/lines by different colors. 
 *  It is possible to get current (last point's) color defined by policy.
 * Optionally, can draw some vertical/horizontal lines through all or some points
 *  - from point to the edge or to the given level (or even from edge to egde).
 * @author Evdokimov
 */
public abstract class AbstractCurveRenderer<T> implements CurveRenderer {

    private Colors colorPolicy = null;

    protected int equalColorPointCount = 1;

    protected int colorIndex = 0;

    protected int horizontalLines = 0, verticalLines = 0;

    protected double horizontalX0 = Double.NEGATIVE_INFINITY, verticalY0 = Double.NEGATIVE_INFINITY;

    private Scale yScale = null;

    /**
	 * @return Colors
	 */
    public final Colors getColorPolicy() {
        return colorPolicy;
    }

    /**
	 * For external use 
	 */
    public final Color getCurrentPolicyColor() {
        if (colorPolicy == null) return null;
        return colorPolicy.getColor(colorIndex);
    }

    /**
	 * For 1-curve plots!
	 * @param colorPolicy - by default is null and line color is defined in LineStyle.
	 *   Does not affect dots color. 
	 *   colorPolicy.getColor(index) will be called with point index which maximum
	 *     is realSize-1 (skipCount is assumed automatically!)
	 *     But if equalColorPointCount>1 maximum is realSize/equalColorPointCount-1 
	 * @param equalColorPointCount - it's better to be equal to 0 here because this option
	 *   is only for specific cases (e.g. to draw moving figures of definite color)  
	 */
    public void setColorPolicy(Colors colorPolicy, int equalColorPointCount) {
        this.colorPolicy = colorPolicy;
        if (equalColorPointCount > 0) this.equalColorPointCount = equalColorPointCount;
    }

    /**
	 * Sets policy of drawing horizontal lines through all or some points  
	 * @param drawLines - if false, skipDotCount and y0 does not matter
	 * @param y0 - y for the second end of the line; if Double.NEGATIVE_INFINITY,
	 *   minY is used as end; if Double.POSTIVE_INFINITY, maxY is used as end;
	 *   if Double.NaN both ends are minY and maxY (not point!).
	 * @param skipPointCount - if drawLines==true, defines number of lines to skip;
	 *   -1 - no skip (precisely, skip is the same as for dots) 
	 */
    public void setVerticalLinePolicy(boolean drawLines, double y0, int skipPointCount) {
        this.verticalLines = drawLines ? (skipPointCount > 0 ? skipPointCount + 1 : -1) : 0;
        this.verticalY0 = y0;
    }

    /**
	 * Sets policy of drawing horizontal lines through all or some points  
	 * @see setVerticalLinePolicy(boolean, int) for params description
	 * @param x0 - x for the second end of the line -//-
	 */
    public void setHorizontalLinePolicy(boolean drawLines, double x0, int skipPointCount) {
        this.horizontalLines = drawLines ? (skipPointCount > 0 ? skipPointCount + 1 : -1) : 0;
        this.horizontalX0 = x0;
    }

    /**
	 * @see mipt.gui.graph.plot.CurveRenderer#paintCurve(mipt.gui.graph.GraphRegion, mipt.gui.graph.plot.CurveStyle)
	 */
    public void paintCurve(GraphRegion rgn, CurveStyle style) {
        if (style == null) return;
        T data;
        int pointCount;
        data = getData(style);
        pointCount = getPointCount();
        if (data == null) return;
        int delta = 1 + style.skipDotCount;
        paintBefore(rgn, style, data, delta);
        if (style.lineStyle != null && pointCount > 0) {
            rgn.gr.setColor(getColorPolicy() == null ? style.lineStyle.color : getColorPolicy().getColor(0));
            int equalColorPoints = 1;
            colorIndex = 0;
            int i = 0;
            int n = pointCount - delta;
            for (i = 0; i < n; i += delta) {
                if (getColorPolicy() != null) {
                    if (equalColorPoints == equalColorPointCount) {
                        colorIndex += delta;
                        rgn.gr.setColor(getColorPolicy().getColor(colorIndex));
                        equalColorPoints = 1;
                    } else equalColorPoints++;
                }
                paintInterval(rgn, style, data, i, delta);
            }
            paintLastInterval(rgn, style, data, i, pointCount);
        }
        if (style.dot != null) {
            for (int i = 0; i < pointCount; i += delta) {
                paintDot(rgn, style, data, i, delta);
            }
        }
    }

    /**
	 * Returns data type to send to paint* methods
	 */
    public abstract T getData(CurveStyle style);

    /**
	 * Paints dot of curve 
	 * @param rgn
	 * @param style
	 * @param data - all curve data
	 * @param i - dot index
	 * @param delta - 1+skipDotCount - rarely used
	 */
    protected abstract void paintDot(GraphRegion rgn, CurveStyle style, T data, int i, int delta);

    /**
	 * Paints vertical and/or horizontal lines with the same style and color as main line.
	 * Should be called from paintInterval() => is painted only if style.lineStyle is not null (we can't imagine other cases).
	 * @param rgn
	 * @param style
	 * @param x - x coord of the end
	 * @param y - y coord of the end
	 * @param i - dot index
	 */
    protected void paintHorVerLines(GraphRegion rgn, CurveStyle style, double x, double y, int i) {
        if (horizontalLines != 0 && (horizontalLines < 0 || i % horizontalLines == 0)) {
            double[] mM = getMinMaxXY();
            GraphUtils.drawClippingHorLine(rgn, Double.isNaN(horizontalX0) ? mM[0] : x, y, Double.isNaN(horizontalX0) || horizontalX0 == Double.POSITIVE_INFINITY ? mM[1] : (horizontalX0 == Double.NEGATIVE_INFINITY ? mM[0] : horizontalX0), style.lineStyle);
        }
        if (verticalLines != 0 && (verticalLines < 0 || i % verticalLines == 0)) {
            double[] mM = getMinMaxXY();
            GraphUtils.drawClippingVerLine(rgn, x, Double.isNaN(verticalY0) ? mM[2] : y, Double.isNaN(verticalY0) || verticalY0 == Double.POSITIVE_INFINITY ? mM[3] : (verticalY0 == Double.NEGATIVE_INFINITY ? mM[2] : verticalY0), style.lineStyle);
        }
    }

    /**
	 * Paints (not last!) interval of curve 
	 * @param rgn
	 * @param style
	 * @param data - all curve data
	 * @param i - interval index
	 * @param delta - 1+skipDotCount
	 */
    protected abstract void paintInterval(GraphRegion rgn, CurveStyle style, T data, int i, int delta);

    /**
	 * Paints last interval of curve 
	 * @param rgn
	 * @param style
	 * @param data - all curve data
	 * @param i - last interval index
	 * @param pointCount (can't use getPointCount() due to asynchronous calls) - real interpolator's point count
	 */
    protected abstract void paintLastInterval(GraphRegion rgn, CurveStyle style, T data, int i, int pointCount);

    /**
	 * Paints something before intervals loop - even if style.lineStyle is null!
	 * Is not needed for most renderers.
	 * @param rgn
	 * @param style
	 * @param data - all curve data
	 * @param delta - 1+skipDotCount
	 */
    protected void paintBefore(GraphRegion rgn, CurveStyle style, T data, int delta) {
    }

    /**
	 * It's obligatory to call this method in sublasses
	 */
    protected final double getX(double x) {
        return x;
    }

    /**
	 * It's obligatory to call this method in sublasses
	 */
    protected final double getY(double y) {
        if (yScale == null) return y;
        return yScale.get(y);
    }

    /**
	 * Scales min and max (if a scale!=null). To be called in getMinMaxXY().
	 */
    protected double[] updateMinMaxXY(double[] a) {
        if (yScale != null) {
            double min = a[2], max = a[3];
            a[2] = yScale.getMin(min, max);
            a[3] = yScale.getMax(min, max);
        }
        return a;
    }

    public final Scale getYScale() {
        return yScale;
    }

    public void setYScale(Scale scale) {
        yScale = scale;
    }

    public final Scale getXScale() {
        throw new UnsupportedOperationException("X axis Scale is not supported yet");
    }

    public void setXScale(Scale scale) {
        throw new UnsupportedOperationException("X axis Scale is not supported yet");
    }
}
