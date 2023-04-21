package com.potix.zul.html;

import com.potix.zul.html.event.ChartDataListener;

/**
 * Chart Model is used to hold the data model for the chart.
 * <ul>
 * <li>{@link PieModel}: one series of data objects. Each data object contains the category name and numeric value of a pie.</li>
 * <li>{@link CategoryModel}: n series of data objects. Each data object contains the category name and associated numeric value .</li>
 * <li>{@link XYModel}: n series of data objects. Each data object contains an (x,y) pair.</li>
 * </ul>
 *
 * This interface defines the data model for components like {@link Chart}
 * use to get the value of data.
 *
 * @author <a href="mailto:henrichen@potix.com">henrichen@potix.com</a>
 * @see Chart
 * @see PieModel
 * @see CategoryModel
 * @see XYModel
 * @see com.potix.zul.html.event.ChartAreaListener
 */
public interface ChartModel {

    /** Adds a listener to the chart that's notified each time a change
	 * to the data model occurs. 
	 */
    public void addChartDataListener(ChartDataListener l);

    /** Removes a listener from the chart that's notified each time
     * a change to the data model occurs. 
     */
    public void removeChartDataListener(ChartDataListener l);
}
