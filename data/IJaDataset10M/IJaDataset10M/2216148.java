package com.googlecode.charts4j;

/**
 * Interface for all charts that support legend positions. (Only MapCharts do
 * do not support legend positions.)
 *
 *
 *
 * @author Julien Chastang (julien.c.chastang at gmail dot com)
 *
 */
public interface GraphChart {

    /**
     * Specify where the chart legends will appear.
     *
     * @param legendPosition
     *            The legend position. Cannot be null.
     */
    void setLegendPosition(final LegendPosition legendPosition);

    /**
     * Specify the legend margins.
     *
     * @param legendWidth
     *            the legend width
     * @param legendHeight
     *            the legend height
     */
    void setLegendMargins(final int legendWidth, final int legendHeight);

    /**
     * Specify chart area fill.
     *
     * @param fill
     *            Chart area fill. Cannot be null. This mutable parameter is
     *            defensively copied upon entry.
     */
    void setAreaFill(final Fill fill);
}
