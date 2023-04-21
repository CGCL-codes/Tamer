package net.tourbook.tour;

import net.tourbook.chart.ChartDataModel;
import net.tourbook.data.TourData;

public interface IDataModelListener {

    /**
	 * Method is called after the chart data model was created from the {@link TourData} and
	 * configuration <b>but</b> before the chart is updated. This method can be used to set the
	 * title or markers
	 * 
	 * @param fChartDataModel
	 */
    abstract void dataModelChanged(ChartDataModel chartDataModel);
}
