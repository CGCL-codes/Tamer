package com.googlecode.gchartjava;

/**
 * Top level interface for all charts. Herein is functionality common to all
 * charts.
 *
 * @author Julien Chastang (julien.c.chastang at gmail dot com)
 */
public interface GChart {

    /**
     * Set the chart size. If no size is specified, the chart will default to
     * 200x125.
     *
     * @param width
     *            chart width. Must be > 0 and <= 1000.
     * @param height
     *            chart height. Must be > 0 and <= 1000.
     *
     * @see <a href="http://code.google.com/apis/chart/#chart_size">Chart Size</a>
     */
    void setSize(final int width, final int height);

    /**
     * Create a URL string given the information supplied to this chart. You can
     * copy and paste this string into your web browser, and see if you get the
     * results you anticipated. Better yet, incorporate this method call or
     * {@link #toURLForHTML()} into your Internet application to dynamically
     * generate charts. URLs beyond 2000 characters are not recommended. You can
     * sometimes get away with the simple encoding scheme if URL length is a
     * problem. See {@link #setDataEncoding(DataEncoding dataEncoding)}
     *
     * @return URL String
     *
     * @see DataEncoding
     * @see <a href="http://www.boutell.com/newfaq/misc/urllength.html">WWW
     *      FAQs: What is the maximum length of a URL?</a>
     */
    String toURLString();

    /**
     * Create a URL with the ampersand character entity reference ({@literal &amp;})
     * in place of an ampersand. Useful for embedding your link in a web page.
     *
     * @return URL string
     */
    String toURLForHTML();

    /**
     * Specify background fill.
     *
     * @param fill
     *            Background fill. Cannot be null.
     * @see Fill
     */
    void setBackgroundFill(final Fill fill);

    /**
     * Specify the chart transparency. Use cautiously as this feature may
     * obscure background fills. Also there appears to be a bug in the Google
     * Chart API where setting a transparency makes the label color disappear.
     *
     * @param opacity
     *            Supply a number between 0 and 100. 0 is completely
     *            transparent, and 100 is completely opaque.
     */
    void setTransparency(final int opacity);

    /**
     * Set the data encoding scheme. The only advantage to the simple encoding
     * scheme is it will ultimately result in shorter URLs, but at the cost of
     * lower resolution. gchartjava defaults to the extended encoding, but if
     * you have lots of data and if you are willing to sacrifice resolution, the
     * simple encoding may be right for you.
     *
     * @param dataEncoding
     *            Supply the data encoding, either simple or extended. Cannot be
     *            null.
     */
    void setDataEncoding(final DataEncoding dataEncoding);
}
