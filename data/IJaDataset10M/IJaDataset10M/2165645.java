package org.matsim.counts.algorithms.graphs;

import java.util.List;
import org.jfree.chart.JFreeChart;
import org.matsim.counts.CountSimComparison;

public abstract class CountsGraph {

    protected List<CountSimComparison> ccl_;

    protected JFreeChart chart_;

    protected int iteration_;

    /**
	 * The title of the chart, may contain whitespaces
	 */
    private String chartTitle_;

    /**
	 * the filename of the chart should not contain any whitespaces 
	 */
    private String filename;

    public CountsGraph() {
    }

    public CountsGraph(final List<CountSimComparison> ccl, final int iteration, final String filename, final String chartTitle) {
        this.ccl_ = ccl;
        this.iteration_ = iteration;
        this.chartTitle_ = chartTitle;
        this.filename = filename;
    }

    public String getChartTitle() {
        return this.chartTitle_;
    }

    public JFreeChart getChart() {
        return this.chart_;
    }

    public void setChartTitle(final String chartTitle) {
        this.chartTitle_ = chartTitle;
    }

    public abstract JFreeChart createChart(int nbr);

    /**
	 * @return the filename
	 */
    public String getFilename() {
        return this.filename;
    }

    /**
	 * @param filename the filename to set
	 */
    public void setFilename(final String filename) {
        this.filename = filename;
    }
}
