package org.blogtrader.platform.core.analysis.chart;

import java.util.List;
import org.blogtrader.platform.core.data.timeseries.TimeVar;
import org.blogtrader.platform.core.option.OptionsManager;

/**
 *
 * @author Caoyuan Deng
 */
public class ZigzagChart extends AbstractChart {

    private TimeVar var;

    protected void plotChart() {
        this.var = (TimeVar) args[0];
        color = OptionsManager.getColorTheme().getChartColor(depth);
        int index1 = getFirstIndexOfEffectiveValue(0);
        while (true) {
            if (index1 < 0) {
                break;
            }
            int position1 = masterTimeSeries.positionOfTime(timeSeries.timeOfIndex(index1));
            int bar1 = bp(position1);
            if (bar1 > nBars) {
                break;
            }
            int index2 = getFirstIndexOfEffectiveValue(index1 + 1);
            if (index2 < 0) {
                break;
            }
            int position2 = masterTimeSeries.positionOfTime(timeSeries.timeOfIndex(index2));
            int bar2 = bp(position2);
            if (bar2 < 1) {
                index1 = index2;
                continue;
            }
            float value1 = timeSeries.getData(tb(bar1)).getFloat(var);
            float value2 = timeSeries.getData(tb(bar2)).getFloat(var);
            float x1 = xb(bar1);
            float x2 = xb(bar2);
            float y1 = yv(value1);
            float y2 = yv(value2);
            plotLineSegment(x1, y1, x2, y2, color);
            index1 = index2;
        }
    }

    private int getFirstIndexOfEffectiveValue(int fromIndex) {
        int index = -1;
        List<Float> values = var.values();
        for (int i = fromIndex, n = values.size(); i < n; i++) {
            Float value = values.get(i);
            if (value != null && !Float.isNaN(value)) {
                index = i;
                break;
            }
        }
        return index;
    }
}
