package nz.org.venice.chart.source;

import nz.org.venice.chart.*;

/**
 * Provides an abstraction of the data being graphed, this way graphs
 * do not need to know anything about the underlying data they are graphing.
 */
public interface GraphSource {

    public static final int SYMBOL = 0;

    public static final int INDEX = 1;

    public static final int PORTFOLIO = 2;

    public static final int ADVANCEDECLINE = 3;

    /**
     * Return the name of the data.
     *
     * @return the name
     */
    public String getName();

    /**
     * Return the type of the data.
     *
     * @return the type
     */
    public int getType();

    /**
     * Get the tool tip text for the given X value
     *
     * @param	x	the X value
     * @return	the tooltip text
     */
    public String getToolTipText(Comparable x);

    /**
     * Convert the Y value to a label to be displayed in the vertical
     * axis.
     *
     * @param	value	y value
     * @return	the label text
     */
    public String getYLabel(double value);

    /**
     * Return an array of acceptable major deltas for the vertical
     * axis.
     *
     * @return	array of doubles
     * @see	nz.org.venice.chart.graph.Graph#getAcceptableMajorDeltas
     */
    public double[] getAcceptableMajorDeltas();

    /**
     * Return an array of acceptable minor deltas for the vertical
     * axis.
     *
     * @return	array of doubles
     * @see	nz.org.venice.chart.graph.Graph#getAcceptableMajorDeltas
     */
    public double[] getAcceptableMinorDeltas();

    /**
     * Get the actual graphable data.
     *
     * @return	the graphable data
     */
    public Graphable getGraphable();
}
