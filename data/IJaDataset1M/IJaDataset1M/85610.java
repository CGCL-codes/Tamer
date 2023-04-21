package net.sourceforge.processdash.ev.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.RangeInfo;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;

public class RangeXYItemRenderer extends StandardXYItemRenderer {

    /** A working line (to save creating thousands of instances). */
    private transient Line2D line;

    public RangeXYItemRenderer() {
        super();
        this.line = new Line2D.Double(0.0, 0.0, 0.0, 0.0);
    }

    /** Draws the visual representation of a single data item.
     */
    @Override
    public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset, int series, int item, CrosshairState crosshairInfo, int pass) {
        EntityCollection entities = null;
        if (info != null) {
            entities = info.getOwner().getEntityCollection();
        }
        Paint paint = getItemPaint(series, item);
        Stroke seriesStroke = getItemStroke(series, item);
        g2.setPaint(paint);
        g2.setStroke(seriesStroke);
        Number x1n = dataset.getX(series, item);
        Number y1n = dataset.getY(series, item);
        if (y1n == null || x1n == null) {
            return;
        }
        double x1 = x1n.doubleValue();
        double y1 = y1n.doubleValue();
        final RectangleEdge xAxisLocation = plot.getDomainAxisEdge();
        final RectangleEdge yAxisLocation = plot.getRangeAxisEdge();
        double transX1 = domainAxis.valueToJava2D(x1, dataArea, xAxisLocation);
        double transY1 = rangeAxis.valueToJava2D(y1, dataArea, yAxisLocation);
        if (item > 0) {
            Number x0n = dataset.getX(series, item - 1);
            Number y0n = dataset.getY(series, item - 1);
            if (y0n != null && x0n != null) {
                double x0 = x0n.doubleValue();
                double y0 = y0n.doubleValue();
                double transX0 = domainAxis.valueToJava2D(x0, dataArea, xAxisLocation);
                double transY0 = rangeAxis.valueToJava2D(y0, dataArea, yAxisLocation);
                if (Double.isNaN(transX0) || Double.isNaN(transY0) || Double.isNaN(transX1) || Double.isNaN(transY1)) {
                    return;
                }
                PlotOrientation orientation = plot.getOrientation();
                if (orientation == PlotOrientation.HORIZONTAL) {
                    line.setLine(transY0, transX0, transY1, transX1);
                } else if (orientation == PlotOrientation.VERTICAL) {
                    line.setLine(transX0, transY0, transX1, transY1);
                }
                if (y1n instanceof RangeInfo) {
                    RangeInfo y1r = (RangeInfo) y1n;
                    double transY1low = rangeAxis.valueToJava2D(y1r.getRangeLowerBound(false), dataArea, yAxisLocation);
                    double transY1high = rangeAxis.valueToJava2D(y1r.getRangeUpperBound(false), dataArea, yAxisLocation);
                    drawItemRangeGradient(g2, line, paint, seriesStroke, transX1, transY1low, transX1, transY1high);
                } else if (x1n instanceof RangeInfo) {
                    RangeInfo x1r = (RangeInfo) x1n;
                    double transX1low = domainAxis.valueToJava2D(x1r.getRangeLowerBound(false), dataArea, xAxisLocation);
                    double transX1high = domainAxis.valueToJava2D(x1r.getRangeUpperBound(false), dataArea, xAxisLocation);
                    drawItemRangeGradient(g2, line, paint, seriesStroke, transX1low, transY1, transX1high, transY1);
                } else if (line.intersects(dataArea)) {
                    g2.draw(line);
                }
            }
        }
        if (entities != null && dataArea.contains(transX1, transY1)) {
            addEntity(entities, null, dataset, series, item, transX1, transY1);
        }
    }

    private void drawItemRangeGradient(Graphics2D g2, Line2D line, Paint paint, Stroke stroke, double x2, double y2, double x3, double y3) {
        Line2D edge1, edge2, mainLine;
        Polygon fillArea;
        Stroke mainLineStroke, edgeLineStroke;
        Paint mainLinePaint, edgeLinePaint, fillPaint;
        double x0 = line.getX1();
        double y0 = line.getY1();
        double x1 = line.getX2();
        double y1 = line.getY2();
        mainLine = new Line2D.Double(x0, y0, x1, y1);
        edge1 = new Line2D.Double(x0, y0, x2, y2);
        edge2 = new Line2D.Double(x0, y0, x3, y3);
        fillArea = new Polygon();
        fillArea.addPoint((int) Math.round(x0), (int) Math.round(y0));
        fillArea.addPoint((int) Math.round(x2), (int) Math.round(y2));
        fillArea.addPoint((int) Math.round(x3), (int) Math.round(y3));
        mainLinePaint = paint;
        if (mainLinePaint instanceof Color) {
            Color c = (Color) mainLinePaint;
            Color dark = transp(c, calcAlpha(c));
            Color light = transp(c, 0.01);
            edgeLinePaint = fillPaint = c;
            try {
                fillPaint = new GradientPaint(gradientStart(x0, y0, x1, y1, x2, y2, x3, y3), light, new Point2D.Double(x1, y1), dark, true);
            } catch (Exception e) {
            }
        } else {
            edgeLinePaint = fillPaint = mainLinePaint;
        }
        if (stroke instanceof BasicStroke) {
            float lineWidth = ((BasicStroke) stroke).getLineWidth();
            edgeLineStroke = new BasicStroke(lineWidth / 4);
            mainLineStroke = new BasicStroke(lineWidth * 2);
        } else {
            mainLineStroke = edgeLineStroke = stroke;
        }
        g2.setPaint(fillPaint);
        g2.fill(fillArea);
        g2.fill(fillArea);
        g2.setStroke(edgeLineStroke);
        g2.setPaint(edgeLinePaint);
        g2.draw(edge1);
        g2.draw(edge2);
        g2.setStroke(mainLineStroke);
        g2.setPaint(mainLinePaint);
        g2.draw(mainLine);
    }

    private double calcAlpha(Color c) {
        double gray = (0.30 * c.getRed() + 0.59 * c.getGreen() + 0.11 * c.getBlue()) / 255;
        double result = 0.85 * 0.123 / (1 - gray);
        if (result < 0.3) return 0.3;
        if (result > 0.8) return 0.8;
        return result;
    }

    private Color transp(Color c, double alpha) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), (int) (255 * alpha));
    }

    private Point2D gradientStart(double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3) {
        double dy = x0 - x1;
        double dx = y1 - y0;
        double startLen = Math.sqrt(dy * dy + dx * dx);
        if (startLen == 0) throw new IllegalArgumentException();
        Line2D line = new Line2D.Double(x0, y0, x1, y1);
        double len2 = line.ptLineDist(x2, y2);
        double len3 = line.ptLineDist(x3, y3);
        double len = 10;
        len = Math.max(len, len2);
        len = Math.max(len, len3);
        double fraction = len / startLen;
        return new Point2D.Double(x1 + dx * fraction, y1 + dy * fraction);
    }
}
