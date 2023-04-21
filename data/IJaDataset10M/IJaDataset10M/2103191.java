package graphs;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class XYInputsVOuputGraph {

    protected XYSeriesCollection dataSet;

    protected ChartFrame chartFrame;

    protected JFreeChart chart;

    protected String XAxisLabel = "X-Axis";

    protected String YAxisLabel = "Y-Axis";

    protected String title = "Title";

    public final boolean CONTAINS_DUPLICATES;

    public XYInputsVOuputGraph() {
        CONTAINS_DUPLICATES = false;
        dataSet = new XYSeriesCollection();
    }

    public XYInputsVOuputGraph(boolean containsDuplicates) {
        CONTAINS_DUPLICATES = containsDuplicates;
        dataSet = new XYSeriesCollection();
    }

    /**
	 * Makes and displays graph with given inputs and outputs
	 * @param inputs
	 * @param outputs
	 */
    public XYInputsVOuputGraph(double[] inputs, double[] outputs) {
        dataSet = new XYSeriesCollection();
        XYSeries data = new XYSeries(inputs.toString());
        for (int x = 0; x < outputs.length && x < inputs.length; x++) {
            data.add(inputs[x], outputs[x]);
        }
        CONTAINS_DUPLICATES = false;
        dataSet.addSeries(data);
        this.makeChart();
    }

    public void setXAxisLabel(String XAxisLabel) {
        if (this.chart != null) {
            this.makeChart(this.title, this.XAxisLabel = XAxisLabel, this.YAxisLabel);
        }
    }

    public void setYAxisLabel(String YAxisLabel) {
        if (this.chart != null) {
            this.makeChart(this.title, this.XAxisLabel, this.YAxisLabel = YAxisLabel);
        }
    }

    public void setTitle(String title) {
        if (this.chart != null) {
            this.makeChart(this.title = title, this.XAxisLabel, YAxisLabel);
        }
    }

    /**
	 * Makes and displays default, empty chart
	 */
    protected void makeChart() {
        chart = ChartFactory.createScatterPlot("Input vs Output", "Inputs", "Outputs", dataSet, PlotOrientation.VERTICAL, true, false, false);
        chartFrame = new ChartFrame(null, chart);
        chartFrame.pack();
        chartFrame.setVisible(true);
    }

    /**
	 * Makes/displays default empty chart with Title
	 * @param title - Titleof the graph
	 */
    protected void makeChart(String title) {
        chart = ChartFactory.createScatterPlot(this.title = title, "Inputs", "Outputs", dataSet, PlotOrientation.VERTICAL, true, false, false);
        chartFrame = new ChartFrame(null, chart);
        chartFrame.pack();
        chartFrame.setVisible(true);
    }

    /**
	 * Makes/dusplays default empty chart with title, x- and y-axis labels
	 * @param title - Title of the graph
	 * @param inputsName - X-axis label
	 * @param outputsName- Y-axis label
	 */
    protected void makeChart(String title, String inputsName, String outputsName) {
        chart = ChartFactory.createScatterPlot(this.title = title, this.XAxisLabel = inputsName, this.YAxisLabel = outputsName, dataSet, PlotOrientation.VERTICAL, true, false, false);
        chartFrame = new ChartFrame(null, chart);
        chartFrame.pack();
        chartFrame.setVisible(true);
    }

    /**
	 * Checks if given input array matches data series
	 * @param input
	 * @return
	 */
    public boolean contains(double[] input) {
        if (this.dataSet.getSeries() == null || input == null) {
            return false;
        }
        for (int x = 0; x < this.dataSet.getSeriesCount(); x++) {
            XYSeries serie = this.dataSet.getSeries(x);
            if (util.Util.compareArraysByValue(input, serie.toArray()[0])) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(Comparable input) {
        if (this.dataSet.getSeries() == null || input == null || !CONTAINS_DUPLICATES) {
            return false;
        }
        for (int x = 0; x < this.dataSet.getSeriesCount(); x++) {
            XYSeries serie = this.dataSet.getSeries(x);
            if (((String) serie.getKey()).compareTo(input.toString()) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Adds input and ouput as X and Y series, correspondingly 
	 * @param inputs - X-series
	 * @param outputs- Y-series
	 */
    public void addXYSeries(double[] inputs, double[] outputs) {
        XYSeries data = new XYSeries(inputs.toString());
        for (int x = 0; x < outputs.length; x++) {
            data.add(inputs[x], outputs[x]);
        }
        if (this.contains(inputs) == false || this.CONTAINS_DUPLICATES == false) {
            this.dataSet.addSeries(data);
        }
    }

    /**
	 * Sets visibility of display frame (similar to JFrame.setVisible)
	 * @param toggle - true to display, false to hide
	 */
    public void setVisible(boolean toggle) {
        this.chartFrame.setVisible(toggle);
    }
}
