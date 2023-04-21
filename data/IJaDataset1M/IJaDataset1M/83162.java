package com.googlecode.gchartjava;

/**
 * Top level plot interface. All plots can be annotated with {@link Marker}s.
 *
 * @author Julien Chastang (julien.c.chastang at gmail dot com)
 *
 */
public interface Plot {

    /**
     * Set the legend.
     *
     * @param legend
     *            plot legend. Cannot be null.
     */
    void setLegend(final String legend);

    /**
     * Set the color for this plot.
     *
     * @param color
     *            color. Cannot be null
     */
    void setColor(final Color color);

    /**
     * Add a shape marker to a point.
     *
     * @param shape
     *            Arrows, Diamonds, etc. Cannot be null.
     * @param color
     *            Color of the shape maker. Cannot be null.
     * @param size
     *            The size of the shape marker. Must be > 0.
     * @param index
     *            The index of the point decorated with a shape marker. Must be >=
     *            0.
     */
    void addShapeMarker(final Shape shape, final Color color, final int size, final int index);

    /**
     * Add a text marker to a point.
     *
     * @param text
     *            Text marker. Cannot be null.
     * @param color
     *            Color of text marker. Cannot be null.
     * @param size
     *            The size of the text marker. Must be > 0.
     * @param index
     *            The index at which the text marker should be added. Must be >=
     *            0.
     */
    void addTextMarker(final String text, final Color color, final int size, final int index);

    /**
     * Add a shape marker to each point on a plot.
     *
     * @param shape
     *            Arrows, Diamonds, etc. Cannot be null.
     * @param color
     *            Color of the shape maker. Cannot be null.
     * @param size
     *            The size of the shape marker. Must be > 0.
     */
    void addShapeMarkers(final Shape shape, final Color color, final int size);

    /**
     * Add a {@link Marker}.
     *
     * @param marker
     *            The text or shape marker. Cannot be null.
     * @param index
     *            The index at which the text marker should be added. Must be >=
     *            0.
     */
    void addMarker(final Marker marker, final int index);
}
