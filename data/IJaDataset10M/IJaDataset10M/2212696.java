package org.ujac.chart;

import java.util.List;

/**
 * Name: Base2DStackedChart<br>
 * Description: A base class for two dimensional stacked charts.
 * 
 * @author lauerc
 */
public class Base2DStackedChart extends Base2DScaleChart {

    /** The minimum chart value. */
    private double minValue = 0.0;

    /** The maximum chart value. */
    private double maxValue = 0.0;

    /**
   * Sets the chart model.
   * @param model The chart model to set.
   * @exception ChartException In case the givem model contained invalid data. 
   */
    public void setModel(ChartModel model) throws ChartException {
        super.setModel(model);
        updateMinMaxValues();
    }

    /**
   * Tells, whether the chart supports negative values or not.
   * @return true, in case the chart supports negative values else false.
   */
    public boolean supportsNegativeValues() {
        return false;
    }

    /**
   * Updates the minimum / maximum values for the current chart model.
   */
    private void updateMinMaxValues() {
        if (model == null) {
            return;
        }
        List<double[]> chartData = model.getChartData();
        int numSegments = chartData.size();
        double min = 0.0;
        double max = 0.0;
        for (int i = 0; i < numSegments; i++) {
            double[] row = (double[]) chartData.get(i);
            double areaSum = 0.0;
            for (int j = 0; j < row.length; j++) {
                areaSum += row[j];
            }
            min = Math.min(min, areaSum);
            max = Math.max(max, areaSum);
        }
        minValue = min;
        maxValue = max;
    }

    /**
   * Gets the minimum value from the chart data.
   * @return The minimum chart data value.
   */
    protected double getMinValue() {
        return minValue;
    }

    /**
   * Gets the maximum value from the chart data.
   * @return The maximum chart data value.
   */
    protected double getMaxValue() {
        return maxValue;
    }
}
