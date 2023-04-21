package org.jCharts.test;

import org.jCharts.axisChart.AxisChart;
import org.jCharts.axisChart.customRenderers.axisValue.renderers.*;
import org.jCharts.chartData.*;
import org.jCharts.chartData.interfaces.IAxisDataSeries;
import org.jCharts.properties.*;
import org.jCharts.properties.util.ChartStroke;
import org.jCharts.types.ChartType;
import java.awt.*;
import java.util.Locale;
import java.util.Collection;
import java.util.Iterator;
import java.text.MessageFormat;

/******************************************************************************************
 * This file provides examples of how to create all the different chart types provided by
 *  this package.
 *
 *******************************************************************************************/
public final class BarTestDriver extends AxisChartTestBase {

    boolean supportsImageMap() {
        return true;
    }

    /******************************************************************************************
	 * Separate this so can use for combo chart test
	 *
	 ******************************************************************************************/
    static ChartTypeProperties getChartTypeProperties(int numberOfDataSets) {
        BarChartProperties barChartProperties = new BarChartProperties();
        barChartProperties.setWidthPercentage(1f);
        return barChartProperties;
    }

    /******************************************************************************************
	 *
	 *
	 ******************************************************************************************/
    DataSeries getDataSeries() throws ChartDataException {
        int dataSize = (int) TestDataGenerator.getRandomNumber(2, 5);
        int numberOfDataSets = 1;
        AxisChartDataSet axisChartDataSet;
        DataSeries dataSeries = super.createDataSeries(dataSize);
        axisChartDataSet = super.createAxisChartDataSet(ChartType.BAR, getChartTypeProperties(numberOfDataSets), numberOfDataSets, dataSize, -2000, 2000);
        dataSeries.addIAxisPlotDataSet(axisChartDataSet);
        return dataSeries;
    }

    /*****************************************************************************************
	 *
	 * @param args
	 * @throws PropertyException
	 * @throws ChartDataException
	 *****************************************************************************************/
    public static void main(String[] args) throws PropertyException, ChartDataException {
        BarChartProperties barChartProperties = new BarChartProperties();
        bug2();
    }

    public static void bug() throws ChartDataException, PropertyException {
        String[] xAxisLabels = { "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII" };
        double[][] data = new double[1][12];
        for (int i = 0; i < 12; i++) {
            double d = 51;
            data[0][i] = d;
        }
        String xAxisTitle = "CHART1_XAXIS";
        String yAxisTitle = "CHART1_YAXIS";
        String title = "CHART1_TITLE hhhhhhhhhhhhhhhhhhhhh hhhhhhhhh hhhhhhhhhhh hhhhhhhhhhh hhhhhhhh hhhhhhhh hhhh  hhhhh hhhh hhhh hhhh  hhhhh hhhhhhhh";
        DataSeries dataSeries = new DataSeries(xAxisLabels, xAxisTitle, yAxisTitle, title);
        String[] legendLabels = { "CHART1_LEGEND" };
        Paint[] paints = { new GradientPaint(0, 0, new Color(255, 213, 83, 150), 0, 350, new Color(243, 116, 0, 200)) };
        BarChartProperties barChartProperties = new BarChartProperties();
        ValueLabelRenderer valueLabelRenderer = new ValueLabelRenderer(false, false, false, -1);
        valueLabelRenderer.setValueLabelPosition(ValueLabelPosition.ON_TOP);
        valueLabelRenderer.useVerticalLabels(false);
        barChartProperties.addPostRenderEventListener(valueLabelRenderer);
        AxisChartDataSet axisChartDataSet = new AxisChartDataSet(data, legendLabels, paints, ChartType.BAR, barChartProperties);
        dataSeries.addIAxisPlotDataSet(axisChartDataSet);
        ChartProperties chartProperties = new ChartProperties();
        LabelAxisProperties xAxisProperties = new LabelAxisProperties();
        DataAxisProperties yAxisProperties = new DataAxisProperties();
        yAxisProperties.setRoundToNearest(0);
        AxisProperties axisProperties = new AxisProperties(xAxisProperties, yAxisProperties);
        LegendProperties legendProperties = new LegendProperties();
        axisProperties.setBackgroundPaint(new GradientPaint(0, 0, new Color(255, 255, 255), 0, 300, new Color(167, 213, 255)));
        AxisChart axisChart = new AxisChart(dataSeries, chartProperties, axisProperties, null, 548, 350);
        ChartTestDriver.exportImage(axisChart, "Bug_BarChartTest.png");
    }

    private static void bug2() {
        BarChartProperties barChartProperties = null;
        LegendProperties legendProperties = null;
        AxisProperties axisProperties = null;
        ChartProperties chartProperties = null;
        int width = 550;
        int height = 360;
        try {
            String[] xAxisLabels = { "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004" };
            String xAxisTitle = "Years";
            String yAxisTitle = "Problems";
            String title = "Micro$oft At Work";
            IAxisDataSeries dataSeries = new DataSeries(xAxisLabels, xAxisTitle, yAxisTitle, title);
            double[][] data = new double[][] { { 1500, 6880, 4510, 2600, 1200, 1580, 8000, 4555, 4000, 6120 } };
            String[] legendLabels = { "Bugs" };
            Paint[] paints = new Paint[] { Color.blue.darker() };
            dataSeries.addIAxisPlotDataSet(new AxisChartDataSet(data, legendLabels, paints, ChartType.BAR, barChartProperties));
            AxisChart axisChart = new AxisChart(dataSeries, chartProperties, axisProperties, legendProperties, width, height);
            ChartTestDriver.exportImage(axisChart, "Bug222_BarChartTest.png");
        } catch (ChartDataException chartDataException) {
            chartDataException.printStackTrace();
        } catch (PropertyException propertyException) {
            propertyException.printStackTrace();
        }
    }
}
