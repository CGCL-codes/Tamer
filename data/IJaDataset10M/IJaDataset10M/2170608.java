package com.googlecode.gchartjava;

import com.googlecode.gchartjava.collect.ImmutableList;
import com.googlecode.gchartjava.collect.Lists;
import com.googlecode.gchartjava.parameters.ChartType;

/**
 * Private radar chart. Not part of API. See RadarChart for public API.
 *
 * @author Julien Chastang (julien.c.chastang at gmail dot com)
 *
 * @see GCharts
 */
class PrivateRadarChart extends AbstractLineChart {

    /** Plots to be rendered by this chart.  **/
    private final ImmutableList<Plot> plots;

    /** Is this radar chart a splined chart..  **/
    private boolean isSpline = false;

    /**
     * @see GCharts#newRadarChart(java.util.List)
     */
    PrivateRadarChart(final ImmutableList<? extends Plot> plots) {
        super(plots);
        this.plots = Lists.copyOf(plots);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prepareData() {
        super.prepareData();
        boolean hasLineStyle = false;
        for (Plot p : plots) {
            final PlotImpl plot = (PlotImpl) p;
            hasLineStyle |= (plot.getLineStyle() != null);
        }
        for (Plot p : plots) {
            final PlotImpl plot = (PlotImpl) p;
            parameterManager.addData(plot.getData());
            if (hasLineStyle) {
                parameterManager.addLineChartLineStyle(plot.getLineStyle() != null ? plot.getLineStyle() : LineStyle.newLineStyle(1, 1, 0));
            }
        }
        if (isSpline) {
            parameterManager.setChartTypeParameter(ChartType.RADAR_SPLINE_CHART);
        } else {
            parameterManager.setChartTypeParameter(ChartType.RADAR_CHART);
        }
    }

    /**
     * @see RadarChart#setSpline(boolean)
     */
    void setSpline(final boolean isSpline) {
        this.isSpline = isSpline;
    }
}
