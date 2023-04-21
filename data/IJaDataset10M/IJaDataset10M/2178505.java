package org.argus.gui.monitor;

import java.awt.Font;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class XTimeChart extends JFreeChart {

    private static final long serialVersionUID = 1L;

    private TimeSeriesCollection dataset;

    public XTimeChart(String title, Font font, Plot plot, boolean legend, TimeSeriesCollection dataset) {
        super(title, font, plot, legend);
        this.dataset = dataset;
    }

    public void add(TimeSeries timeSeries) {
        dataset.addSeries(timeSeries);
    }

    public void setMin(Double number) {
        XYPlot xy = (XYPlot) getPlot();
        ValueAxis axis = xy.getRangeAxis();
        double upper = axis.getRange().getUpperBound();
        if (number > upper) {
            upper = number;
        }
        axis.setRange(number, upper);
    }

    public void setMax(Double number) {
        XYPlot xy = (XYPlot) getPlot();
        ValueAxis axis = xy.getRangeAxis();
        double lower = axis.getRange().getLowerBound();
        if (number < lower) {
            lower = number;
        }
        axis.setRange(axis.getRange().getLowerBound(), number);
    }
}
