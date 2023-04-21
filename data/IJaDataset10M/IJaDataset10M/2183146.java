package de.fraunhofer.isst.axbench.timing.ui.chart.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.data.gantt.GanttCategoryDataset;
import org.jfree.ui.RectangleEdge;

public class GanttRendererSimulator3 extends GanttRenderer {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4417948688948307832L;

    /**
     * Creates a new renderer.
     */
    public GanttRendererSimulator3() {
        super();
        this.setSeriesPaint(0, Color.BLUE);
        this.setSeriesPaint(1, Color.CYAN);
        this.setSeriesPaint(2, Color.GREEN);
    }

    /**
     * Draws the tasks/subtasks for one item.
     *
     * @param g2  the graphics device.
     * @param state  the renderer state.
     * @param dataArea  the data plot area.
     * @param plot  the plot.
     * @param domainAxis  the domain axis.
     * @param rangeAxis  the range axis.
     * @param dataset  the data.
     * @param row  the row index (zero-based).
     * @param column  the column index (zero-based).
     */
    protected void drawTasks(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis, ValueAxis rangeAxis, GanttCategoryDataset dataset, int row, int column) {
        int count = dataset.getSubIntervalCount(row, column);
        if (count == 0) {
            drawTask(g2, state, dataArea, plot, domainAxis, rangeAxis, dataset, row, column);
        }
        double markersX[] = new double[count];
        double markersY[] = new double[count];
        double markersZ[] = new double[count];
        Paint markersP[] = new Paint[count];
        markersP[0] = Color.RED;
        markersP[1] = Color.GREEN;
        markersP[2] = Color.RED;
        for (int subinterval = 0; subinterval < count; subinterval++) {
            RectangleEdge rangeAxisLocation = plot.getRangeAxisEdge();
            Number value0 = dataset.getStartValue(row, column, subinterval);
            if (value0 == null) {
                return;
            }
            double translatedValue0 = rangeAxis.valueToJava2D(value0.doubleValue(), dataArea, rangeAxisLocation);
            Number value1 = dataset.getEndValue(row, column, subinterval);
            if (value1 == null) {
                return;
            }
            double translatedValue1 = rangeAxis.valueToJava2D(value1.doubleValue(), dataArea, rangeAxisLocation);
            if (translatedValue1 < translatedValue0) {
                double temp = translatedValue1;
                translatedValue1 = translatedValue0;
                translatedValue0 = temp;
            }
            double rectStart = calculateBarW0(plot, plot.getOrientation(), dataArea, domainAxis, state, row, column);
            double rectLength = Math.abs(translatedValue1 - translatedValue0);
            double rectBreadth = state.getBarWidth();
            Rectangle2D bar = null;
            markersX[subinterval] = translatedValue0 + rectLength;
            markersY[subinterval] = rectStart;
            markersZ[subinterval] = rectBreadth;
            if (plot.getOrientation() == PlotOrientation.HORIZONTAL) {
                bar = new Rectangle2D.Double(translatedValue0, rectStart, rectLength, rectBreadth);
            } else if (plot.getOrientation() == PlotOrientation.VERTICAL) {
                bar = new Rectangle2D.Double(rectStart, translatedValue0, rectBreadth, rectLength);
            }
            Paint seriesPaint = getItemPaint(row, column);
            g2.setPaint(seriesPaint);
            g2.fill(bar);
            if (state.getBarWidth() > BAR_OUTLINE_WIDTH_THRESHOLD) {
                g2.setStroke(getItemStroke(row, column));
                g2.setPaint(getItemOutlinePaint(row, column));
                g2.draw(bar);
            }
            if (state.getInfo() != null) {
                EntityCollection entities = state.getInfo().getOwner().getEntityCollection();
                if (entities != null) {
                    String tip = null;
                    if (getToolTipGenerator(row, column) != null) {
                        tip = getToolTipGenerator(row, column).generateToolTip(dataset, row, column);
                    }
                    String url = null;
                    if (getItemURLGenerator(row, column) != null) {
                        url = getItemURLGenerator(row, column).generateURL(dataset, row, column);
                    }
                    CategoryItemEntity entity = new CategoryItemEntity(bar, tip, url, dataset, dataset.getRowKey(row), dataset.getColumnKey(column));
                    entities.add(entity);
                }
            }
        }
        for (int subinterval = 0; subinterval < count; subinterval++) {
            Line2D line = null;
            if (plot.getOrientation() == PlotOrientation.HORIZONTAL) {
                line = new Line2D.Double(markersX[subinterval], markersY[subinterval], markersX[subinterval], markersY[subinterval] + markersZ[subinterval]);
            } else if (plot.getOrientation() == PlotOrientation.VERTICAL) {
                line = new Line2D.Double(markersY[subinterval], markersX[subinterval], markersY[subinterval] + markersZ[subinterval], markersX[subinterval]);
            }
            g2.setPaint(markersP[subinterval]);
            g2.setStroke(new BasicStroke(3.0F));
            g2.draw(line);
        }
    }

    /**
     * Draws a single task.
     *
     * @param g2  the graphics device.
     * @param state  the renderer state.
     * @param dataArea  the data plot area.
     * @param plot  the plot.
     * @param domainAxis  the domain axis.
     * @param rangeAxis  the range axis.
     * @param dataset  the data.
     * @param row  the row index (zero-based).
     * @param column  the column index (zero-based).
     */
    protected void drawTask(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis, ValueAxis rangeAxis, GanttCategoryDataset dataset, int row, int column) {
        PlotOrientation orientation = plot.getOrientation();
        RectangleEdge rangeAxisLocation = plot.getRangeAxisEdge();
        Number value0 = dataset.getEndValue(row, column);
        if (value0 == null) {
            return;
        }
        double java2dValue0 = rangeAxis.valueToJava2D(value0.doubleValue(), dataArea, rangeAxisLocation);
        Number value1 = dataset.getStartValue(row, column);
        if (value1 == null) {
            return;
        }
        double java2dValue1 = rangeAxis.valueToJava2D(value1.doubleValue(), dataArea, rangeAxisLocation);
        if (java2dValue1 < java2dValue0) {
            double temp = java2dValue1;
            java2dValue1 = java2dValue0;
            java2dValue0 = temp;
            Number tempNum = value1;
            value1 = value0;
            value0 = tempNum;
        }
        double rectStart = calculateBarW0(plot, orientation, dataArea, domainAxis, state, row, column);
        double rectBreadth = state.getBarWidth();
        double rectLength = Math.abs(java2dValue1 - java2dValue0);
        Rectangle2D bar = null;
        if (orientation == PlotOrientation.HORIZONTAL) {
            bar = new Rectangle2D.Double(java2dValue0, rectStart, rectLength, rectBreadth);
        } else if (orientation == PlotOrientation.VERTICAL) {
            bar = new Rectangle2D.Double(rectStart, java2dValue1, rectBreadth, rectLength);
        }
        Paint seriesPaint = getItemPaint(row, column);
        g2.setPaint(seriesPaint);
        g2.fill(bar);
        if (state.getBarWidth() > BAR_OUTLINE_WIDTH_THRESHOLD) {
            Stroke stroke = getItemOutlineStroke(row, column);
            Paint paint = getItemOutlinePaint(row, column);
            if (stroke != null && paint != null) {
                g2.setStroke(stroke);
                g2.setPaint(paint);
                g2.draw(bar);
            }
        }
        CategoryItemLabelGenerator generator = getItemLabelGenerator(row, column);
        if (generator != null && isItemLabelVisible(row, column)) {
            drawItemLabel(g2, dataset, row, column, plot, generator, bar, false);
        }
        if (state.getInfo() != null) {
            EntityCollection entities = state.getInfo().getOwner().getEntityCollection();
            if (entities != null) {
                String tip = null;
                CategoryToolTipGenerator tipster = getToolTipGenerator(row, column);
                if (tipster != null) {
                    tip = tipster.generateToolTip(dataset, row, column);
                }
                String url = null;
                if (getItemURLGenerator(row, column) != null) {
                    url = getItemURLGenerator(row, column).generateURL(dataset, row, column);
                }
                CategoryItemEntity entity = new CategoryItemEntity(bar, tip, url, dataset, dataset.getRowKey(row), dataset.getColumnKey(column));
                entities.add(entity);
            }
        }
    }
}
