package net.tourbook.chart;

import java.util.ArrayList;
import java.util.HashMap;

public class BarChartMinMaxKeeper {

    /**
	 * min/max values for the y-axis data
	 */
    private HashMap<Integer, Float> _minValues = new HashMap<Integer, Float>();

    private HashMap<Integer, Float> _maxValues = new HashMap<Integer, Float>();

    public void resetMinMax() {
        _minValues.clear();
        _maxValues.clear();
    }

    /**
	 * save the min/max values from the chart data model
	 * 
	 * @param chartDataModel
	 */
    public void setMinMaxValues(final ChartDataModel chartDataModel) {
        if (chartDataModel == null) {
            return;
        }
        final ArrayList<ChartDataYSerie> yDataSerie = chartDataModel.getYData();
        Integer yDataId = 0;
        for (final ChartDataSerie yData : yDataSerie) {
            if (yData instanceof ChartDataYSerie) {
                setYDataMinMaxValues((ChartDataYSerie) yData, yDataId++);
            }
        }
    }

    private void setYDataMinMaxValues(final ChartDataYSerie yData, final Integer yDataId) {
        final float minValue = yData.getOriginalMinValue();
        final float maxValue = yData.getOriginalMaxValue();
        Float keeperMinValue = _minValues.get(yDataId);
        Float keeperMaxValue = _maxValues.get(yDataId);
        if (keeperMinValue == null) {
            keeperMinValue = minValue;
            keeperMaxValue = maxValue;
        } else {
            keeperMinValue = Math.min(keeperMinValue, minValue);
            keeperMaxValue = Math.max(keeperMaxValue, maxValue);
        }
        _minValues.put(yDataId, keeperMinValue);
        _maxValues.put(yDataId, keeperMaxValue);
        yData.setVisibleMinValue(keeperMinValue);
        yData.setVisibleMaxValue(keeperMaxValue);
    }
}
