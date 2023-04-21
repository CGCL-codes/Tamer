package net.tourbook.statistics;

import net.tourbook.chart.ChartDataModel;

public class StatisticMonthAltitude extends StatisticMonth {

    @Override
    ChartDataModel updateChart() {
        final ChartDataModel chartDataModel = new ChartDataModel(ChartDataModel.CHART_TYPE_BAR);
        createXDataMonths(chartDataModel);
        createYDataAltitude(chartDataModel);
        return chartDataModel;
    }
}
