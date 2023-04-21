package org.jCharts.chartData.interfaces;

public interface IDataSeries extends IAxisDataSeries {

    /******************************************************************************************
	 * Returns the number of labels on the x-axis
	 *
	 * @return int
	 ******************************************************************************************/
    public int getNumberOfAxisLabels();

    /******************************************************************************************
	 * Returns the axis label at the specified index.
	 *
	 * @param index
	 * @return String the x-axis label
	 ******************************************************************************************/
    public String getAxisLabel(int index);
}
