package org.nextframework.view.chart.googletools;

import org.nextframework.view.chart.Chart;
import org.nextframework.view.chart.ChartRenderer;

public class ChartRendererGoogleTools implements ChartRenderer {

    public static String TYPE = "GOOGLE-TOOLS";

    public String getOutputType() {
        return TYPE;
    }

    public Object renderChart(Chart chart) {
        GoogleToolsChartBuilder builder = new GoogleToolsChartBuilder(chart);
        return builder.toString();
    }
}
