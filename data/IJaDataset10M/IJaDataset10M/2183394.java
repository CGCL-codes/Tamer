package com.rapidminer.gui.plotter.charts;

import java.awt.Font;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.PieDataset;
import com.rapidminer.gui.plotter.PlotterConfigurationModel;

/**
 * A simple 2D ring chart plotter.
 * 
 * @author Ingo Mierswa
 */
public class RingChartPlotter extends AbstractPieChartPlotter {

    private static final long serialVersionUID = 4950755498257276805L;

    /**
	 * @param settings
	 */
    public RingChartPlotter(PlotterConfigurationModel settings) {
        super(settings);
    }

    @Override
    public JFreeChart createChart(PieDataset pieDataSet, boolean createLegend) {
        JFreeChart chart = ChartFactory.createRingChart(null, pieDataSet, createLegend, true, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionOutlinesVisible(false);
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 11));
        plot.setNoDataMessage("No data available");
        plot.setCircular(false);
        plot.setLabelGap(0.02);
        return chart;
    }

    @Override
    public boolean isSupportingExplosion() {
        return true;
    }

    @Override
    public String getPlotterName() {
        return PlotterConfigurationModel.RING_CHART;
    }
}
