package com.od.jtimeseries.ui.visualizer.chart.creator;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: Nick Ebbutt
 * Date: 27/01/11
 * Time: 23:15
 */
public class EfficientXYLineAndShapeRenderer extends XYLineAndShapeRenderer {

    private double minY = Integer.MAX_VALUE;

    private double maxY = Integer.MIN_VALUE;

    private boolean storedLine;

    private int linesDrawn;

    public EfficientXYLineAndShapeRenderer(boolean lines, boolean shapes) {
        super(lines, shapes);
        setDrawSeriesLineAsPath(false);
    }

    public XYItemRendererState initialise(Graphics2D g2, Rectangle2D dataArea, XYPlot plot, XYDataset data, PlotRenderingInfo info) {
        minY = Integer.MAX_VALUE;
        maxY = Integer.MIN_VALUE;
        storedLine = false;
        linesDrawn = 0;
        return super.initialise(g2, dataArea, plot, data, info);
    }

    /**
        * Draws the item (first pass). This method draws the lines
        * connecting the items.
        *
        * @param g2  the graphics device.
        * @param state  the renderer state.
        * @param dataArea  the area within which the data is being drawn.
        * @param plot  the plot (can be used to obtain standard color
        *              information etc).
        * @param domainAxis  the domain axis.
        * @param rangeAxis  the range axis.
        * @param dataset  the dataset.
        * @param pass  the pass.
        * @param series  the series index (zero-based).
        * @param item  the item index (zero-based).
        */
    protected void drawPrimaryLine(XYItemRendererState state, Graphics2D g2, XYPlot plot, XYDataset dataset, int pass, int series, int item, ValueAxis domainAxis, ValueAxis rangeAxis, Rectangle2D dataArea) {
        if (item == 0) {
            return;
        }
        double x1 = dataset.getXValue(series, item);
        double y1 = dataset.getYValue(series, item);
        if (Double.isNaN(y1) || Double.isNaN(x1)) {
            return;
        }
        double x0 = dataset.getXValue(series, item - 1);
        double y0 = dataset.getYValue(series, item - 1);
        if (Double.isNaN(y0) || Double.isNaN(x0)) {
            return;
        }
        RectangleEdge xAxisLocation = plot.getDomainAxisEdge();
        RectangleEdge yAxisLocation = plot.getRangeAxisEdge();
        double transX0 = domainAxis.valueToJava2D(x0, dataArea, xAxisLocation);
        double transY0 = rangeAxis.valueToJava2D(y0, dataArea, yAxisLocation);
        double transX1 = domainAxis.valueToJava2D(x1, dataArea, xAxisLocation);
        double transY1 = rangeAxis.valueToJava2D(y1, dataArea, yAxisLocation);
        if (Double.isNaN(transX0) || Double.isNaN(transY0) || Double.isNaN(transX1) || Double.isNaN(transY1)) {
            return;
        }
        int transX0Int = (int) transX0;
        int transX1Int = (int) transX1;
        boolean isSameX = transX0Int == transX1Int;
        if (isSameX) {
            minY = Math.min(transY0, minY);
            minY = Math.min(transY1, minY);
            maxY = Math.max(transY0, maxY);
            maxY = Math.max(transY1, maxY);
            storedLine = true;
        }
        if (!isSameX || isLastItem(dataset, series, item)) {
            Stroke s = getItemStroke(series, item);
            Paint p = getItemPaint(series, item);
            if (storedLine) {
                drawLine(state, g2, plot, transX0Int, minY, transX0Int, maxY, s, p);
                linesDrawn++;
            }
            drawLine(state, g2, plot, transX0Int, transY0, transX1Int, transY1, s, p);
            linesDrawn++;
            storedLine = false;
            minY = Integer.MAX_VALUE;
            maxY = Integer.MIN_VALUE;
        }
    }

    private boolean isLastItem(XYDataset dataset, int series, int item) {
        return item == dataset.getItemCount(series) - 1;
    }

    private void drawLine(XYItemRendererState state, Graphics2D g2, XYPlot plot, double transX0, double transY0, double transX1, double transY1, Stroke s, Paint p) {
        PlotOrientation orientation = plot.getOrientation();
        if (orientation == PlotOrientation.HORIZONTAL) {
            state.workingLine.setLine(transY0, transX0, transY1, transX1);
        } else if (orientation == PlotOrientation.VERTICAL) {
            state.workingLine.setLine(transX0, transY0, transX1, transY1);
        }
        g2.setStroke(s);
        g2.setPaint(p);
        g2.draw(state.workingLine);
    }
}
