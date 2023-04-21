package com.googlecode.gchartjava.parameters;

import java.util.List;
import com.googlecode.gchartjava.collect.Lists;

/**
 * Class for building pie chart and google-o-meter legends parameter string for
 * the Google Chart API.
 *
 * @author Julien Chastang (julien.c.chastang at gmail dot com)
 */
final class PieChartAndGoogleOMeterLegendParameter implements Parameter {

    /** The Google Chart API pie chart and google-o-meter legends parameter. */
    private static final String URL_PARAMETER_KEY = "chl";

    /** The legends. */
    private final List<String> legends = Lists.newLinkedList();

    /**
     * Add a legend.
     *
     * @param legend
     *            the legend
     */
    void addLegend(final String legend) {
        this.legends.add(legend);
    }

    /**
     * {@inheritDoc}
     */
    public String toURLParameterString() {
        final StringBuilder sb = new StringBuilder(URL_PARAMETER_KEY + "=");
        int cnt = 0;
        for (String legend : legends) {
            final String l = ParameterUtil.utf8Encode(legend);
            sb.append(cnt++ > 0 ? "|" + l : l);
        }
        return !legends.isEmpty() ? sb.toString() : "";
    }
}
