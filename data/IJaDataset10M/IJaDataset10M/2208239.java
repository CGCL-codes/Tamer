package com.google.gwt.visualization.client.visualizations.corechart;

import com.google.gwt.visualization.client.AbstractDataTable;

/**
 * Visualization with horizontal bars showing the values.
 *
 * @see <a href=
 *      "http://code.google.com/apis/visualization/documentation/gallery/barchart.html"
 *      > Bar Chart Visualization Reference</a>
 */
public class BarChart extends CoreChart {

    /**
   * @param data
   * @param options
   */
    public BarChart(AbstractDataTable data, Options options) {
        super(data, options);
        options.setType(CoreChart.Type.BARS);
    }
}
