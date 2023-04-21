package jmetric.ui.util;

/**
 * This interface includes all the methods that are used by the JSChartIF to display the charts.
 * 
 * @author Andrew AJ Cain
 */
public interface JChartable {

    /**
	 * This method returns the chart model for the chart identified by the index.
	 * 
	 * @param index
	 *            The index of the chart to return.
	 */
    public JMetricChartModel getChart(int index);

    /**
	 * This method returns the number of charts in this JChartable class.
	 * 
	 * @return The number of charts that can be displayed from this class. this is used as the limit for the getChart
	 *         method.
	 */
    public int getChartCount();

    /**
	 * This method returns the chart name from the specified index.
	 * 
	 * @return The chart name for the chart at the index.
	 * @param index
	 *            The index of the chart name to return
	 */
    public String getChartName(int index);
}
