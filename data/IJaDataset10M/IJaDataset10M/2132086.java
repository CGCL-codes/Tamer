package com.lordjoe.collectiveintelligence.data.ui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.*;
import org.jfree.chart.axis.AxisSpace;
import org.jfree.chart.axis.AxisState;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.axis.ValueTick;
import org.jfree.chart.util.ResourceBundleWrapper;
import org.jfree.chart.plot.*;
import org.jfree.data.Range;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

/**
 * A fast scatter plot.
 */
public class CategoryScatterPlot extends Plot implements ValueAxisPlot, Pannable, Zoomable, Cloneable, Serializable {

    public static final int POINT_SIZE = 5;

    public static final Color[] Colors = { Color.red, Color.blue, Color.green, Color.cyan };

    /** The default grid line stroke. */
    public static final Stroke DEFAULT_GRIDLINE_STROKE = new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0.0f, new float[] { 2.0f, 2.0f }, 0.0f);

    /** The default grid line paint. */
    public static final Paint DEFAULT_GRIDLINE_PAINT = Color.lightGray;

    /** The data. */
    private final List<DPoint[]> m_Data = new ArrayList<DPoint[]>();

    /** The x data range. */
    private Range xDataRange;

    /** The y data range. */
    private Range yDataRange;

    /** The domain axis (used for the x-values). */
    private ValueAxis domainAxis;

    /** The range axis (used for the y-values). */
    private ValueAxis rangeAxis;

    /** The paint used to plot data points. */
    private final List<Paint> m_Paints = new ArrayList<Paint>(Arrays.asList(Colors));

    /** A flag that controls whether the domain grid-lines are visible. */
    private boolean domainGridlinesVisible;

    /** The stroke used to draw the domain grid-lines. */
    private transient Stroke domainGridlineStroke;

    /** The paint used to draw the domain grid-lines. */
    private transient Paint domainGridlinePaint;

    /** A flag that controls whether the range grid-lines are visible. */
    private boolean rangeGridlinesVisible;

    /** The stroke used to draw the range grid-lines. */
    private transient Stroke rangeGridlineStroke;

    /** The paint used to draw the range grid-lines. */
    private transient Paint rangeGridlinePaint;

    /**
     * A flag that controls whether or not panning is enabled for the domain
     * axis.
     *
     * @since 1.0.13
     */
    private boolean domainPannable;

    /**
     * A flag that controls whether or not panning is enabled for the range
     * axis.
     *
     * @since 1.0.13
     */
    private boolean rangePannable;

    /** The resourceBundle for the localization. */
    protected static ResourceBundle localizationResources = ResourceBundleWrapper.getBundle("org.jfree.chart.plot.LocalizationBundle");

    /**
     * Creates a new fast scatter plot.
     * <p>
     * The data is an array of x, y values:  data[0][i] = x, data[1][i] = y.
     *
     * @param data  the data (<code>null</code> permitted).
     * @param domainAxis  the domain (x) axis (<code>null</code> not permitted).
     * @param rangeAxis  the range (y) axis (<code>null</code> not permitted).
     */
    public CategoryScatterPlot(ValueAxis domainAxis, ValueAxis rangeAxis, DPoint[]... data) {
        super();
        if (domainAxis == null) {
            throw new IllegalArgumentException("Null 'domainAxis' argument.");
        }
        if (rangeAxis == null) {
            throw new IllegalArgumentException("Null 'rangeAxis' argument.");
        }
        for (int i = 0; i < data.length; i++) {
            DPoint[] dPoints = data[i];
            this.m_Data.add(dPoints);
        }
        this.xDataRange = calculateXDataRange(data);
        this.yDataRange = calculateYDataRange(data);
        this.domainAxis = domainAxis;
        this.domainAxis.setPlot(this);
        this.domainAxis.addChangeListener(this);
        this.rangeAxis = rangeAxis;
        this.rangeAxis.setPlot(this);
        this.rangeAxis.addChangeListener(this);
        this.domainGridlinesVisible = true;
        this.domainGridlinePaint = CategoryScatterPlot.DEFAULT_GRIDLINE_PAINT;
        this.domainGridlineStroke = CategoryScatterPlot.DEFAULT_GRIDLINE_STROKE;
        this.rangeGridlinesVisible = true;
        this.rangeGridlinePaint = CategoryScatterPlot.DEFAULT_GRIDLINE_PAINT;
        this.rangeGridlineStroke = CategoryScatterPlot.DEFAULT_GRIDLINE_STROKE;
    }

    /**
     * Returns a short string describing the plot type.
     *
     * @return A short string describing the plot type.
     */
    public String getPlotType() {
        return localizationResources.getString("Fast_Scatter_Plot");
    }

    /**
     * Returns the data array used by the plot.
     *
     * @return The data array (possibly <code>null</code>).
     *
     * @see #setData(float[][])
     */
    public DPoint[] getData() {
        return this.m_Data.toArray(DPoint.EMPTY_ARRAY);
    }

    /**
     * Sets the data array used by the plot and sends a {@link org.jfree.chart.event.PlotChangeEvent}
     * to all registered listeners.
     *
     * @param data  the data array (<code>null</code> permitted).
     *
     * @see #getData()
     */
    public void AddData(DPoint[] data) {
        this.m_Data.add(data);
        fireChangeEvent();
    }

    /**
     * Returns the orientation of the plot.
     *
     * @return The orientation (always {@link org.jfree.chart.plot.PlotOrientation#VERTICAL}).
     */
    public PlotOrientation getOrientation() {
        return PlotOrientation.VERTICAL;
    }

    /**
     * Returns the domain axis for the plot.
     *
     * @return The domain axis (never <code>null</code>).
     *
     * @see #setDomainAxis(org.jfree.chart.axis.ValueAxis)
     */
    public ValueAxis getDomainAxis() {
        return this.domainAxis;
    }

    /**
     * Sets the domain axis and sends a {@link org.jfree.chart.event.PlotChangeEvent} to all
     * registered listeners.
     *
     * @param axis  the axis (<code>null</code> not permitted).
     *
     * @since 1.0.3
     *
     * @see #getDomainAxis()
     */
    public void setDomainAxis(ValueAxis axis) {
        if (axis == null) {
            throw new IllegalArgumentException("Null 'axis' argument.");
        }
        this.domainAxis = axis;
        fireChangeEvent();
    }

    /**
     * Returns the range axis for the plot.
     *
     * @return The range axis (never <code>null</code>).
     *
     * @see #setRangeAxis(org.jfree.chart.axis.ValueAxis)
     */
    public ValueAxis getRangeAxis() {
        return this.rangeAxis;
    }

    /**
     * Sets the range axis and sends a {@link org.jfree.chart.event.PlotChangeEvent} to all
     * registered listeners.
     *
     * @param axis  the axis (<code>null</code> not permitted).
     *
     * @since 1.0.3
     *
     * @see #getRangeAxis()
     */
    public void setRangeAxis(ValueAxis axis) {
        if (axis == null) {
            throw new IllegalArgumentException("Null 'axis' argument.");
        }
        this.rangeAxis = axis;
        fireChangeEvent();
    }

    /**
     * Returns the paint used to plot data points.  The default is
     * <code>Color.red</code>.
     *
     * @return The paint.
     *
     * @see #setPaints(java.awt.Paint)
     */
    public Paint[] getPaints() {
        return this.m_Paints.toArray(new Paint[0]);
    }

    /**
     * Sets the color for the data points and sends a {@link org.jfree.chart.event.PlotChangeEvent}
     * to all registered listeners.
     *
     * @param paint  the paint (<code>null</code> not permitted).
     *
     * @see #getPaints()
     */
    public void setPaints(Paint[] paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.m_Paints.clear();
        m_Paints.addAll(Arrays.asList(paint));
        fireChangeEvent();
    }

    /**
     * Returns <code>true</code> if the domain gridlines are visible, and
     * <code>false<code> otherwise.
     *
     * @return <code>true</code> or <code>false</code>.
     *
     * @see #setDomainGridlinesVisible(boolean)
     * @see #setDomainGridlinePaint(java.awt.Paint)
     */
    public boolean isDomainGridlinesVisible() {
        return this.domainGridlinesVisible;
    }

    /**
     * Sets the flag that controls whether or not the domain grid-lines are
     * visible.  If the flag value is changed, a {@link org.jfree.chart.event.PlotChangeEvent} is
     * sent to all registered listeners.
     *
     * @param visible  the new value of the flag.
     *
     * @see #getDomainGridlinePaint()
     */
    public void setDomainGridlinesVisible(boolean visible) {
        if (this.domainGridlinesVisible != visible) {
            this.domainGridlinesVisible = visible;
            fireChangeEvent();
        }
    }

    /**
     * Returns the stroke for the grid-lines (if any) plotted against the
     * domain axis.
     *
     * @return The stroke (never <code>null</code>).
     *
     * @see #setDomainGridlineStroke(java.awt.Stroke)
     */
    public Stroke getDomainGridlineStroke() {
        return this.domainGridlineStroke;
    }

    /**
     * Sets the stroke for the grid lines plotted against the domain axis and
     * sends a {@link org.jfree.chart.event.PlotChangeEvent} to all registered listeners.
     *
     * @param stroke  the stroke (<code>null</code> not permitted).
     *
     * @see #getDomainGridlineStroke()
     */
    public void setDomainGridlineStroke(Stroke stroke) {
        if (stroke == null) {
            throw new IllegalArgumentException("Null 'stroke' argument.");
        }
        this.domainGridlineStroke = stroke;
        fireChangeEvent();
    }

    /**
     * Returns the paint for the grid lines (if any) plotted against the domain
     * axis.
     *
     * @return The paint (never <code>null</code>).
     *
     * @see #setDomainGridlinePaint(java.awt.Paint)
     */
    public Paint getDomainGridlinePaint() {
        return this.domainGridlinePaint;
    }

    /**
     * Sets the paint for the grid lines plotted against the domain axis and
     * sends a {@link org.jfree.chart.event.PlotChangeEvent} to all registered listeners.
     *
     * @param paint  the paint (<code>null</code> not permitted).
     *
     * @see #getDomainGridlinePaint()
     */
    public void setDomainGridlinePaint(Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.domainGridlinePaint = paint;
        fireChangeEvent();
    }

    /**
     * Returns <code>true</code> if the range axis grid is visible, and
     * <code>false<code> otherwise.
     *
     * @return <code>true</code> or <code>false</code>.
     *
     * @see #setRangeGridlinesVisible(boolean)
     */
    public boolean isRangeGridlinesVisible() {
        return this.rangeGridlinesVisible;
    }

    /**
     * Sets the flag that controls whether or not the range axis grid lines are
     * visible.  If the flag value is changed, a {@link org.jfree.chart.event.PlotChangeEvent} is
     * sent to all registered listeners.
     *
     * @param visible  the new value of the flag.
     *
     * @see #isRangeGridlinesVisible()
     */
    public void setRangeGridlinesVisible(boolean visible) {
        if (this.rangeGridlinesVisible != visible) {
            this.rangeGridlinesVisible = visible;
            fireChangeEvent();
        }
    }

    /**
     * Returns the stroke for the grid lines (if any) plotted against the range
     * axis.
     *
     * @return The stroke (never <code>null</code>).
     *
     * @see #setRangeGridlineStroke(java.awt.Stroke)
     */
    public Stroke getRangeGridlineStroke() {
        return this.rangeGridlineStroke;
    }

    /**
     * Sets the stroke for the grid lines plotted against the range axis and
     * sends a {@link org.jfree.chart.event.PlotChangeEvent} to all registered listeners.
     *
     * @param stroke  the stroke (<code>null</code> permitted).
     *
     * @see #getRangeGridlineStroke()
     */
    public void setRangeGridlineStroke(Stroke stroke) {
        if (stroke == null) {
            throw new IllegalArgumentException("Null 'stroke' argument.");
        }
        this.rangeGridlineStroke = stroke;
        fireChangeEvent();
    }

    /**
     * Returns the paint for the grid lines (if any) plotted against the range
     * axis.
     *
     * @return The paint (never <code>null</code>).
     *
     * @see #setRangeGridlinePaint(java.awt.Paint)
     */
    public Paint getRangeGridlinePaint() {
        return this.rangeGridlinePaint;
    }

    /**
     * Sets the paint for the grid lines plotted against the range axis and
     * sends a {@link org.jfree.chart.event.PlotChangeEvent} to all registered listeners.
     *
     * @param paint  the paint (<code>null</code> not permitted).
     *
     * @see #getRangeGridlinePaint()
     */
    public void setRangeGridlinePaint(Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.rangeGridlinePaint = paint;
        fireChangeEvent();
    }

    /**
     * Draws the fast scatter plot on a Java 2D graphics device (such as the
     * screen or a printer).
     *
     * @param g2  the graphics device.
     * @param area   the area within which the plot (including axis labels)
     *                   should be drawn.
     * @param anchor  the anchor point (<code>null</code> permitted).
     * @param parentState  the state from the parent plot (ignored).
     * @param info  collects chart drawing information (<code>null</code>
     *              permitted).
     */
    public void draw(Graphics2D g2, Rectangle2D area, Point2D anchor, PlotState parentState, PlotRenderingInfo info) {
        if (info != null) {
            info.setPlotArea(area);
        }
        RectangleInsets insets = getInsets();
        insets.trim(area);
        AxisSpace space = new AxisSpace();
        space = this.domainAxis.reserveSpace(g2, this, area, RectangleEdge.BOTTOM, space);
        space = this.rangeAxis.reserveSpace(g2, this, area, RectangleEdge.LEFT, space);
        Rectangle2D dataArea = space.shrink(area, null);
        if (info != null) {
            info.setDataArea(dataArea);
        }
        drawBackground(g2, dataArea);
        AxisState domainAxisState = this.domainAxis.draw(g2, dataArea.getMaxY(), area, dataArea, RectangleEdge.BOTTOM, info);
        AxisState rangeAxisState = this.rangeAxis.draw(g2, dataArea.getMinX(), area, dataArea, RectangleEdge.LEFT, info);
        drawDomainGridlines(g2, dataArea, domainAxisState.getTicks());
        drawRangeGridlines(g2, dataArea, rangeAxisState.getTicks());
        Shape originalClip = g2.getClip();
        Composite originalComposite = g2.getComposite();
        g2.clip(dataArea);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getForegroundAlpha()));
        render(g2, dataArea, info, null);
        g2.setClip(originalClip);
        g2.setComposite(originalComposite);
        drawOutline(g2, dataArea);
    }

    /**
     * Draws a representation of the data within the dataArea region.  The
     * <code>info</code> and <code>crosshairState</code> arguments may be
     * <code>null</code>.
     *
     * @param g2  the graphics device.
     * @param dataArea  the region in which the data is to be drawn.
     * @param info  an optional object for collection dimension information.
     * @param crosshairState  collects crosshair information (<code>null</code>
     *                        permitted).
     */
    public void render(Graphics2D g2, Rectangle2D dataArea, PlotRenderingInfo info, CrosshairState crosshairState) {
        int index = 0;
        for (DPoint[] data : m_Data) {
            g2.setPaint(getPaints()[index++]);
            for (int i = 0; i < data.length; i++) {
                float x = (float) data[i].getX();
                float y = (float) data[i].getY();
                int transX = (int) this.domainAxis.valueToJava2D(x, dataArea, RectangleEdge.BOTTOM);
                int transY = (int) this.rangeAxis.valueToJava2D(y, dataArea, RectangleEdge.LEFT);
                g2.fillRect(transX, transY, POINT_SIZE, POINT_SIZE);
            }
        }
    }

    /**
     * Draws the gridlines for the plot, if they are visible.
     *
     * @param g2  the graphics device.
     * @param dataArea  the data area.
     * @param ticks  the ticks.
     */
    protected void drawDomainGridlines(Graphics2D g2, Rectangle2D dataArea, List ticks) {
        if (isDomainGridlinesVisible()) {
            Iterator iterator = ticks.iterator();
            while (iterator.hasNext()) {
                ValueTick tick = (ValueTick) iterator.next();
                double v = this.domainAxis.valueToJava2D(tick.getValue(), dataArea, RectangleEdge.BOTTOM);
                Line2D line = new Line2D.Double(v, dataArea.getMinY(), v, dataArea.getMaxY());
                g2.setPaint(getDomainGridlinePaint());
                g2.setStroke(getDomainGridlineStroke());
                g2.draw(line);
            }
        }
    }

    /**
     * Draws the gridlines for the plot, if they are visible.
     *
     * @param g2  the graphics device.
     * @param dataArea  the data area.
     * @param ticks  the ticks.
     */
    protected void drawRangeGridlines(Graphics2D g2, Rectangle2D dataArea, List ticks) {
        if (isRangeGridlinesVisible()) {
            Iterator iterator = ticks.iterator();
            while (iterator.hasNext()) {
                ValueTick tick = (ValueTick) iterator.next();
                double v = this.rangeAxis.valueToJava2D(tick.getValue(), dataArea, RectangleEdge.LEFT);
                Line2D line = new Line2D.Double(dataArea.getMinX(), v, dataArea.getMaxX(), v);
                g2.setPaint(getRangeGridlinePaint());
                g2.setStroke(getRangeGridlineStroke());
                g2.draw(line);
            }
        }
    }

    /**
     * Returns the range of data values to be plotted along the axis, or
     * <code>null</code> if the specified axis isn't the domain axis or the
     * range axis for the plot.
     *
     * @param axis  the axis (<code>null</code> permitted).
     *
     * @return The range (possibly <code>null</code>).
     */
    public Range getDataRange(ValueAxis axis) {
        Range result = null;
        if (axis == this.domainAxis) {
            result = this.xDataRange;
        } else if (axis == this.rangeAxis) {
            result = this.yDataRange;
        }
        return result;
    }

    /**
     * Calculates the X data range.
     *
     * @param data  the data (<code>null</code> permitted).
     *
     * @return The range.
     */
    private Range calculateXDataRange(DPoint[][] dPoints) {
        Range result = null;
        double lowest = Float.POSITIVE_INFINITY;
        double highest = Float.NEGATIVE_INFINITY;
        for (int i = 0; i < dPoints.length; i++) {
            DPoint[] data = dPoints[i];
            for (int j = 0; j < data.length; j++) {
                double v = data[j].getX();
                if (v < lowest) {
                    lowest = v;
                }
                if (v > highest) {
                    highest = v;
                }
            }
            if (lowest <= highest) {
                result = new Range(lowest, highest);
            }
        }
        return result;
    }

    /**
     * Calculates the Y data range.
     *
     * @param data  the data (<code>null</code> permitted).
     *
     * @return The range.
     */
    private Range calculateYDataRange(DPoint[][] dPoints) {
        Range result = null;
        double lowest = Float.POSITIVE_INFINITY;
        double highest = Float.NEGATIVE_INFINITY;
        for (int i = 0; i < dPoints.length; i++) {
            DPoint[] data = dPoints[i];
            for (int j = 0; j < data.length; j++) {
                double v = data[j].getY();
                if (v < lowest) {
                    lowest = v;
                }
                if (v > highest) {
                    highest = v;
                }
            }
            if (lowest <= highest) {
                result = new Range(lowest, highest);
            }
        }
        return result;
    }

    /**
     * Multiplies the range on the domain axis by the specified factor.
     *
     * @param factor  the zoom factor.
     * @param info  the plot rendering info.
     * @param source  the source point.
     */
    public void zoomDomainAxes(double factor, PlotRenderingInfo info, Point2D source) {
        this.domainAxis.resizeRange(factor);
    }

    /**
     * Multiplies the range on the domain axis by the specified factor.
     *
     * @param factor  the zoom factor.
     * @param info  the plot rendering info.
     * @param source  the source point (in Java2D space).
     * @param useAnchor  use source point as zoom anchor?
     *
     * @see #zoomRangeAxes(double, org.jfree.chart.plot.PlotRenderingInfo, java.awt.geom.Point2D, boolean)
     *
     * @since 1.0.7
     */
    public void zoomDomainAxes(double factor, PlotRenderingInfo info, Point2D source, boolean useAnchor) {
        if (useAnchor) {
            double sourceX = source.getX();
            double anchorX = this.domainAxis.java2DToValue(sourceX, info.getDataArea(), RectangleEdge.BOTTOM);
            this.domainAxis.resizeRange2(factor, anchorX);
        } else {
            this.domainAxis.resizeRange(factor);
        }
    }

    /**
     * Zooms in on the domain axes.
     *
     * @param lowerPercent  the new lower bound as a percentage of the current
     *                      range.
     * @param upperPercent  the new upper bound as a percentage of the current
     *                      range.
     * @param info  the plot rendering info.
     * @param source  the source point.
     */
    public void zoomDomainAxes(double lowerPercent, double upperPercent, PlotRenderingInfo info, Point2D source) {
        this.domainAxis.zoomRange(lowerPercent, upperPercent);
    }

    /**
     * Multiplies the range on the range axis/axes by the specified factor.
     *
     * @param factor  the zoom factor.
     * @param info  the plot rendering info.
     * @param source  the source point.
     */
    public void zoomRangeAxes(double factor, PlotRenderingInfo info, Point2D source) {
        this.rangeAxis.resizeRange(factor);
    }

    /**
     * Multiplies the range on the range axis by the specified factor.
     *
     * @param factor  the zoom factor.
     * @param info  the plot rendering info.
     * @param source  the source point (in Java2D space).
     * @param useAnchor  use source point as zoom anchor?
     *
     * @see #zoomDomainAxes(double, org.jfree.chart.plot.PlotRenderingInfo, java.awt.geom.Point2D, boolean)
     *
     * @since 1.0.7
     */
    public void zoomRangeAxes(double factor, PlotRenderingInfo info, Point2D source, boolean useAnchor) {
        if (useAnchor) {
            double sourceY = source.getY();
            double anchorY = this.rangeAxis.java2DToValue(sourceY, info.getDataArea(), RectangleEdge.LEFT);
            this.rangeAxis.resizeRange2(factor, anchorY);
        } else {
            this.rangeAxis.resizeRange(factor);
        }
    }

    /**
     * Zooms in on the range axes.
     *
     * @param lowerPercent  the new lower bound as a percentage of the current
     *                      range.
     * @param upperPercent  the new upper bound as a percentage of the current
     *                      range.
     * @param info  the plot rendering info.
     * @param source  the source point.
     */
    public void zoomRangeAxes(double lowerPercent, double upperPercent, PlotRenderingInfo info, Point2D source) {
        this.rangeAxis.zoomRange(lowerPercent, upperPercent);
    }

    /**
     * Returns <code>true</code>.
     *
     * @return A boolean.
     */
    public boolean isDomainZoomable() {
        return true;
    }

    /**
     * Returns <code>true</code>.
     *
     * @return A boolean.
     */
    public boolean isRangeZoomable() {
        return true;
    }

    /**
     * Returns <code>true</code> if panning is enabled for the domain axes,
     * and <code>false</code> otherwise.
     *
     * @return A boolean.
     *
     * @since 1.0.13
     */
    public boolean isDomainPannable() {
        return this.domainPannable;
    }

    /**
     * Sets the flag that enables or disables panning of the plot along the
     * domain axes.
     *
     * @param pannable  the new flag value.
     *
     * @since 1.0.13
     */
    public void setDomainPannable(boolean pannable) {
        this.domainPannable = pannable;
    }

    /**
     * Returns <code>true</code> if panning is enabled for the range axes,
     * and <code>false</code> otherwise.
     *
     * @return A boolean.
     *
     * @since 1.0.13
     */
    public boolean isRangePannable() {
        return this.rangePannable;
    }

    /**
     * Sets the flag that enables or disables panning of the plot along
     * the range axes.
     *
     * @param pannable  the new flag value.
     *
     * @since 1.0.13
     */
    public void setRangePannable(boolean pannable) {
        this.rangePannable = pannable;
    }

    /**
     * Pans the domain axes by the specified percentage.
     *
     * @param percent  the distance to pan (as a percentage of the axis length).
     * @param info the plot info
     * @param source the source point where the pan action started.
     *
     * @since 1.0.13
     */
    public void panDomainAxes(double percent, PlotRenderingInfo info, Point2D source) {
        if (!isDomainPannable() || this.domainAxis == null) {
            return;
        }
        double length = this.domainAxis.getRange().getLength();
        double adj = -percent * length;
        if (this.domainAxis.isInverted()) {
            adj = -adj;
        }
        this.domainAxis.setRange(this.domainAxis.getLowerBound() + adj, this.domainAxis.getUpperBound() + adj);
    }

    /**
     * Pans the range axes by the specified percentage.
     *
     * @param percent  the distance to pan (as a percentage of the axis length).
     * @param info the plot info
     * @param source the source point where the pan action started.
     *
     * @since 1.0.13
     */
    public void panRangeAxes(double percent, PlotRenderingInfo info, Point2D source) {
        if (!isRangePannable() || this.rangeAxis == null) {
            return;
        }
        double length = this.rangeAxis.getRange().getLength();
        double adj = percent * length;
        if (this.rangeAxis.isInverted()) {
            adj = -adj;
        }
        this.rangeAxis.setRange(this.rangeAxis.getLowerBound() + adj, this.rangeAxis.getUpperBound() + adj);
    }
}
