package org.jCharts.test;

import org.jCharts.chartData.*;
import org.jCharts.properties.ChartTypeProperties;
import org.jCharts.properties.StockChartProperties;
import java.awt.*;

/******************************************************************************************
 * This file provides examples of how to create all the different chart types provided by
 *  this package.
 *
 *******************************************************************************************/
public final class StockTestDriver extends AxisChartTestBase {

    boolean supportsImageMap() {
        return true;
    }

    /******************************************************************************************
	 * Separate this so can use for combo chart test
	 *
	 ******************************************************************************************/
    static ChartTypeProperties getChartTypeProperties(int numberOfDataSets) {
        StockChartProperties stockChartProperties = new StockChartProperties();
        return stockChartProperties;
    }

    /******************************************************************************************
	 * Test for LineChart
	 *
	 * @throws ChartDataException
	 ******************************************************************************************/
    DataSeries getDataSeries() throws ChartDataException {
        int dataSize = (int) TestDataGenerator.getRandomNumber(2, 25);
        int numberOfDataSets = 1;
        DataSeries dataSeries = super.createDataSeries(dataSize);
        StockChartDataSet stockChartDataSet;
        double[] highs = TestDataGenerator.getRandomNumbers(dataSize, 500, 1000);
        double[] lows = TestDataGenerator.getRandomNumbers(dataSize, 100, 300);
        double[] opens = TestDataGenerator.getRandomNumbers(dataSize, 350, 450);
        double[] closes = TestDataGenerator.getRandomNumbers(dataSize, 350, 450);
        StockChartProperties stockChartProperties = new StockChartProperties();
        stockChartDataSet = new StockChartDataSet(highs, "High", lows, "Low", Color.black, stockChartProperties);
        stockChartDataSet.setOpenValues(opens, "Open", Color.red);
        stockChartDataSet.setCloseValues(closes, "Close", Color.green);
        String[] legendLabels = TestDataGenerator.getRandomStrings(numberOfDataSets, 10, false);
        Paint[] paints = TestDataGenerator.getRandomPaints(numberOfDataSets);
        dataSeries.addIAxisPlotDataSet(stockChartDataSet);
        return dataSeries;
    }
}
