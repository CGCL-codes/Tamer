package aria.samples.components;

import org.formaria.swing.Page;
import org.formaria.swing.Graph;
import java.awt.Component;
import org.formaria.aria.ProjectManager;
import org.formaria.aria.data.DataModel;

/**
 * Demonstarte some features of the charting component
 * <p>Copyright: Formaria Ltd. (c) 2008</p>
 * <p>License: see license.txt</p>
 * <p>$Revision: 1.6 $</p>
 */
public class LineChart extends Page {

    private Graph chart;

    private double[] capacityPoints;

    private int numSeries;

    private String[] seriesNames;

    /** Creates a new instance of LineChart */
    public LineChart() {
    }

    /**
   * Do some chart setup
   */
    public void pageCreated() {
        chart = (Graph) findComponent("lineChart");
        String[] names = { "Series 1", "Series 2", "Series 3", "Series 4", "Series 5" };
        seriesNames = names;
        setupChart(false, false);
    }

    /**
   * A response handler for the radio buttons. Uses the mode attribute to
   * determine the chart setting
   */
    public void setMode() {
        Component comp = (Component) getCurrentEvent().getSource();
        int mode = Integer.parseInt(getAttribute("mode", comp.getName()).toString());
        chart.setMode(mode);
    }

    /**
   * A response handler for the fade button - toggles the chart animation
   */
    public void toggleFade() {
        chart.toggleYAxisAnimation();
    }

    /**
   * Setup the chart
   * @param chartType the type of chart e.g. performance, current etc..
   * @param seriesNames the names of the data series
   * @param animate true to animate
   */
    public void showChart(int chartType, String[] seriesNames, boolean animate) {
        double[] points;
        chart.setTitle("Line Chart Demo");
        chart.setXScale(-45, 20, 0, "Evaporating temperature (�C)", null);
        chart.setYScale(chart.LOGARITHMIC, "Cooling capacity (W)");
        points = capacityPoints;
        chart.setData(points, numSeries, seriesNames, animate);
        chart.repaint();
    }

    /**
   * Setup the chart data
   * @param animate true to animate
   * @param outputTable true to output data to tables instead of charts
   */
    protected void setupChart(boolean animate, boolean outputTable) {
        DataModel root = ProjectManager.getModel();
        DataModel chartData = (DataModel) root.get("Outcome");
        if (chartData != null) {
            chart.setTitle("Opportunities by Outcome");
            chart.setXScale(-45, 20, 0, "Outcome", null);
            chart.setYScale(chart.LOGARITHMIC, "Value");
            chart.setModelStructure(chart.SERIES, 1);
            chart.setModelStructure(chart.LABELS, 0);
            chart.setModelStructure(chart.VALUES, 2);
            chart.setDataModel(chartData);
        } else {
            numSeries = 5;
            int numPoints = 14;
            int seriesPts = numPoints * 2 * numSeries;
            capacityPoints = new double[seriesPts];
            double te[] = { -45.0, -40.0, -35.0, -30.0, -25.0, -20.0, -15.0, -10.0, -5.0, 0.0, 5.0, 10.0, 15.0, 20.0 };
            double ts[] = { 0.9, 1.0, 1.1, 2.0, 2.1 };
            int idx = 0;
            for (int j = 0; j < numSeries; j++) {
                for (int i = 0; i < numPoints; i++) {
                    capacityPoints[idx++] = te[i];
                    capacityPoints[idx++] = te[i] * te[i] * ts[j];
                }
            }
            showChart(1, seriesNames, animate);
        }
    }

    /**
   * Print the chart
   */
    public void printChart() {
    }
}
