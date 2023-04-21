package org.jfree.beans;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

/**
 * A JavaBean that displays an area chart.
 */
public class JAreaChart extends NumericalXYChart {

    /**
     * Creates a new pie chart bean.
     */
    public JAreaChart() {
        super();
    }

    /**
     * Creates a default chart.
     * 
     * @return The default chart.
     */
    protected JFreeChart createDefaultChart() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries s1 = new XYSeries("Series 1");
        s1.add(1.0, 5.0);
        s1.add(2.0, 7.0);
        s1.add(3.0, 3.0);
        s1.add(4.0, 6.0);
        dataset.addSeries(s1);
        JFreeChart chart = ChartFactory.createXYAreaChart("JAreaChart - Title", "X", "Y", dataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(4, 4, 4, 4));
        return chart;
    }

    /**
     * Returns the dataset used by the chart.
     * 
     * @return The dataset (possibly <code>null</code>).
     * 
     * @see #setDataset(XYDataset)
     */
    public XYDataset getDataset() {
        XYDataset result = null;
        XYPlot plot = (XYPlot) this.chart.getPlot();
        if (plot != null) {
            result = plot.getDataset();
        }
        return result;
    }

    /**
     * Sets the dataset used by the chart and sends a 
     * {@link PropertyChangeEvent} for the <code>dataset</code> property to all
     * registered listeners.
     * 
     * @param dataset  the dataset (<code>null</code> permitted).
     * 
     * @see #getDataset()
     */
    public void setDataset(XYDataset dataset) {
        XYPlot plot = (XYPlot) this.chart.getPlot();
        if (plot != null) {
            XYDataset old = plot.getDataset();
            plot.setDataset(dataset);
            firePropertyChange("dataset", old, dataset);
        }
    }
}
