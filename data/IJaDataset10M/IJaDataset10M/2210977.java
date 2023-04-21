package org.expasy.jpl.dev.msmsviewer.view;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import org.expasy.jpl.dev.msmsviewer.graphics.CustomXYBarRenderer;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class SpectrumPanel extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -235824720712589531L;

    private ChartPanel chartPanel;

    public ChartPanel getChartPanel() {
        return chartPanel;
    }

    public void setChartPanel(ChartPanel chartPanel) {
        this.chartPanel = chartPanel;
    }

    public SpectrumPanel() {
        super(new BorderLayout(1, 1));
        JFreeChart chart = createChart();
        chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);
    }

    /**
	 * Creates and returns a new empty JFreeChart 
	 * @return the JFreeChart
	 */
    private JFreeChart createChart() {
        XYSeriesCollection xyData = new XYSeriesCollection();
        xyData.addSeries(new XYSeries(""));
        CustomXYBarRenderer renderer1 = new CustomXYBarRenderer(0);
        NumberAxis rangeAxis1 = new NumberAxis("Intensity");
        XYPlot subplot1 = new XYPlot(xyData, null, rangeAxis1, renderer1);
        subplot1.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        CustomXYBarRenderer renderer2 = new CustomXYBarRenderer(1);
        NumberAxis rangeAxis2 = new NumberAxis("Intensity");
        rangeAxis2.setAutoRangeIncludesZero(false);
        XYPlot subplot2 = new XYPlot(xyData, null, rangeAxis2, renderer2);
        subplot2.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
        subplot2.getRangeAxis().setInverted(true);
        CombinedDomainXYPlot plot = new CombinedDomainXYPlot(new NumberAxis("m/z"));
        plot.setGap(10.0);
        plot.add(subplot1);
        plot.add(subplot2);
        plot.setOrientation(PlotOrientation.VERTICAL);
        return new JFreeChart("Spectrum Plot", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
    }
}
