package de.progra.charting;

import de.progra.charting.model.ChartDataModelConstraints;

/**
 * The CoordSystem contains two or possibly three Axis objects for the x-axis
 * and the at most two y-axis.
 * @author mueller
 * @version 1.0
 */
public class Axis {

    /** Defines a horizontal x-axis. */
    public static final int HORIZONTAL = 1;

    /** Defines a vertical y-axis. */
    public static final int VERTICAL = 2;

    /** Defines a logarithmic scale. */
    public static final int LOGARITHMIC = 3;

    /** Defines a linear scale. */
    public static final int LINEAR = 4;

    /** The axis' alignment. */
    private int align = HORIZONTAL;

    ChartDataModelConstraints constraints;

    int length = Integer.MAX_VALUE;

    /** Creates new Axis.
     * @param align the alignment of the axis.
     * @param c the ChartDataModelConstraints 
     */
    public Axis(int align, ChartDataModelConstraints c) {
        if (align == HORIZONTAL || align == VERTICAL) this.align = align;
        this.constraints = c;
    }

    /** Returns the alignment of the axis.
     * @return the alignment constant: <CODE>Axis.VERTICAL</CODE> or <CODE>Axis.HORIZONTAL</CODE>
     */
    public int getAlignment() {
        return align;
    }

    /** Sets the Pixel length of the axis.
     * @param length the length in pixel
     */
    public void setLength(int length) {
        this.length = length;
    }

    /** Returns length of the axis in pixels.
     * @return the length in pixels
     */
    public int getLength() {
        return length;
    }

    /** Returns the point on the axis for a specific value.
     * If the axis is a x-axis and the column values are not numeric,
     * this isn't needed since then the axis can be divided into
     * equally long parts. This is a relative pixel distance to
     * the starting pixel of the axis.
     * @param value the double value to compute the pixel distance for
     * @return the pixel distance for the given value relative to the start of the axis
     */
    public double getPixelForValue(double value) {
        if (getAlignment() == Axis.VERTICAL) {
            return (value - constraints.getMinimumValue().doubleValue()) / getPointToPixelRatio();
        } else {
            return (value - constraints.getMinimumColumnValue()) / getPointToPixelRatio();
        }
    }

    /** Returns the ratio between a value unit and the screen pixels.
     * This is only useful for linear scales.
     * @return the ratio points / pixel length for the axis.
     */
    public double getPointToPixelRatio() {
        if (getAlignment() == Axis.VERTICAL) {
            return (constraints.getMaximumValue().doubleValue() - constraints.getMinimumValue().doubleValue()) / length;
        } else return (constraints.getMaximumColumnValue() - constraints.getMinimumColumnValue()) / length;
    }
}
