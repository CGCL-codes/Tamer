package org.jopenchart.linechart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import org.jopenchart.ArrayOfInt;
import org.jopenchart.Axis;
import org.jopenchart.BottomAxis;
import org.jopenchart.Chart;
import org.jopenchart.DataModel1D;
import org.jopenchart.DataModelMultiple;
import org.jopenchart.LeftAxis;
import org.jopenchart.marker.ShapeMarker;

public class LineChart extends Chart {

    private Number lowerRange;

    private Number higherRange;

    private Color fillColor;

    private Double gridXStep;

    private Double gridYStep;

    private Stroke gridStroke;

    private Color gridColor = Color.LIGHT_GRAY;

    private List<Stroke> lineStrokes = new ArrayList<Stroke>();

    private List<ShapeMarker> markers = new ArrayList<ShapeMarker>();

    public LineChart() {
        left = new LeftAxis();
        bottom = new BottomAxis();
        gridStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, new float[] { 5, 3 }, 0);
    }

    public Stroke getStroke(int index) {
        if (index >= lineStrokes.size()) return new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        return lineStrokes.get(index);
    }

    public DataModelMultiple getDataModel() {
        return (DataModelMultiple) this.model;
    }

    public void setDataModel(DataModelMultiple m) {
        this.model = m;
    }

    public void setDataModel(DataModel1D m) {
        this.model = new DataModelMultiple();
        this.getDataModel().addModel(m);
    }

    public void setLowerRange(Number n) {
        this.lowerRange = n;
    }

    public void setHigherRange(Number n) {
        this.higherRange = n;
    }

    public void setFillColor(Color c) {
        this.fillColor = c;
    }

    public Number getLowerRange() {
        if (this.lowerRange == null) {
            return this.getDataModel().getMinValue();
        }
        return this.lowerRange;
    }

    public Number getHigherRange() {
        if (this.higherRange == null) {
            return this.getDataModel().getMaxValue();
        }
        return this.higherRange;
    }

    public void setData(List<Number> data) {
        DataModel1D m = new DataModel1D();
        m.addAll(data);
        setDataModel(m);
    }

    public void setLeftAxis(Axis axis) {
        left = new LeftAxis(axis);
    }

    public void setBottomAxis(Axis axis) {
        bottom = new BottomAxis(axis);
    }

    @Override
    public void prepareRendering(Graphics2D g) {
        int leftWidth = 1 + left.getMaxLabelWidth(g) + left.getMarkerLenght() + left.getMarkerSpacing();
        int rightWidth = bottom.getMaxLabelWidth(g) / 2;
        int topHeight = left.getMaxLabelHeight(g) / 2;
        int bottomHeight = 1 + bottom.getMaxLabelHeight(g) + bottom.getMarkerLenght() + bottom.getMarkerSpacing();
        int graphWidth = this.getDimension().width - leftWidth - rightWidth;
        int graphHeight = this.getDimension().height - topHeight - bottomHeight;
        left.setX(0);
        left.setY(topHeight);
        left.setWidth(leftWidth);
        left.setHeight(graphHeight);
        bottom.setX(leftWidth - 1);
        bottom.setY(topHeight + graphHeight);
        bottom.setWidth(graphWidth);
        bottom.setHeight(bottomHeight);
        this.setChartRectangle(new Rectangle(leftWidth, topHeight, graphWidth, graphHeight));
    }

    public void renderPlot(Graphics2D g) {
        renderGrid(g);
        DataModelMultiple models = this.getDataModel();
        for (int index = 0; index < models.getSize(); index++) {
            DataModel1D model1 = models.getModel(index);
            double maxXValue = this.getHigherRange().doubleValue();
            double minXValue = this.getLowerRange().doubleValue();
            double rangeXValue = maxXValue - minXValue;
            int length = model1.getSize();
            int graphPosX = this.getChartRectangle().x;
            int graphPosY = this.getChartRectangle().y;
            int graphWidth = this.getChartRectangle().width;
            int graphHeight = this.getChartRectangle().height;
            double dx = (double) graphWidth / (length - 1);
            double ratioy = (double) graphHeight / rangeXValue;
            double x1 = graphPosX;
            ArrayOfInt lx = new ArrayOfInt();
            ArrayOfInt ly = new ArrayOfInt();
            Number n2 = null;
            for (int i = 0; i < length - 1; i++) {
                Number n1 = model1.getValueAt(i);
                n2 = model1.getValueAt(i + 1);
                if (n1 != null) {
                    int y1 = graphPosY + graphHeight - (int) ((n1.doubleValue() - minXValue) * ratioy);
                    lx.add((int) x1);
                    ly.add(y1);
                } else if (n1 == null && n2 != null && !lx.isEmpty()) {
                    g.drawPolyline(lx.getArray(), ly.getArray(), lx.getSize());
                    lx.clear();
                    ly.clear();
                }
                x1 += dx;
            }
            if (n2 != null) {
                int y1 = graphPosY + graphHeight - (int) ((n2.doubleValue() - minXValue) * ratioy);
                lx.add((int) x1);
                ly.add(y1);
            }
            int polyLength = lx.getSize();
            if (fillColor != null && polyLength > 0) {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                g.setStroke(new BasicStroke());
                g.setColor(fillColor);
                lx.add(lx.get(polyLength - 1));
                ly.add(graphPosY + graphHeight);
                lx.add(lx.get(0));
                ly.add(graphPosY + graphHeight);
                GradientPaint gp = new GradientPaint(graphPosX + graphWidth, graphPosY, Color.white, graphPosX, graphPosY + graphHeight, new Color(230, 235, 250), false);
                g.setPaint(gp);
                g.fillPolygon(lx.getArray(), ly.getArray(), polyLength + 2);
            }
            if (!lx.isEmpty()) {
                g.setPaint(null);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setStroke(this.getStroke(index));
                g.setColor(this.getColor(index));
                g.drawPolyline(lx.getArray(), ly.getArray(), polyLength);
            }
        }
        renderMarkers(g);
    }

    private void renderMarkers(Graphics2D g) {
        for (ShapeMarker marker : this.markers) {
            marker.draw(this, g);
        }
    }

    private void renderGrid(Graphics2D g) {
        int graphPosX = this.getChartRectangle().x - 1;
        int graphPosY = this.getChartRectangle().y + this.getChartRectangle().height;
        g.setColor(gridColor);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setStroke(gridStroke);
        if (this.gridXStep != null) {
            final double gridXStep2 = (this.gridXStep.doubleValue() * this.getChartRectangle().width) / 100D;
            for (double x = graphPosX + this.getChartRectangle().width; x > graphPosX; x -= gridXStep2) {
                g.drawLine((int) x, graphPosY, (int) x, this.getChartRectangle().y);
            }
        }
        if (this.gridYStep != null) {
            final double gridYStep2 = (this.gridYStep.doubleValue() * this.getChartRectangle().height) / 100D;
            for (double y = this.getChartRectangle().y; y < graphPosY; y += gridYStep2) {
                g.drawLine(graphPosX, (int) y, graphPosX + this.getChartRectangle().width, (int) y);
            }
        }
    }

    @Override
    public void renderAxis(Graphics2D g) {
        g.setColor(Color.GRAY);
        g.setStroke(new BasicStroke());
        left.render(g);
        bottom.render(g);
    }

    /**
     * 
     * @param dx 0 - 100
     */
    public void setGridXStep(Double dx) {
        this.gridXStep = dx;
    }

    /**
     * 
     * @param dx 0 - 100
     */
    public void setGridYStep(Double dy) {
        this.gridYStep = dy;
    }

    public void setGridStroke(Stroke stroke) {
        this.gridStroke = stroke;
    }

    public void setGridColor(Color color) {
        this.gridColor = color;
    }

    public void setGridSegment(float lineLength, float blankLenght) {
        this.gridStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, new float[] { lineLength, blankLenght }, 0);
    }

    public void setMultipleData(List<List<Number>> multipleData) {
        this.setDataModel(new DataModelMultiple(multipleData));
    }

    public void setStrokes(List<Stroke> strokes) {
        this.lineStrokes.clear();
        this.lineStrokes.addAll(strokes);
    }

    public void addMarkers(List<ShapeMarker> markers) {
        this.markers.addAll(markers);
    }
}
