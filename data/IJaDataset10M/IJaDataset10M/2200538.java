package org.das2.graph;

import java.text.ParseException;

/**
 *
 * @author  jbf
 */
public class DasColumn extends DasDevicePosition {

    public DasColumn(DasCanvas parent, double left, double right) {
        super(parent, left, right, true);
    }

    public DasColumn(DasCanvas canvas, DasColumn parent, double nMin, double nMax, double emMin, double emMax, int ptMin, int ptMax) {
        super(canvas, true, parent, nMin, nMax, emMin, emMax, ptMin, ptMax);
    }

    /**
     * makes a new DasColumn by parsing a string like "100%-5em+3pt" to get the offsets.
     * The three qualifiers are "%", "em", and "pt", but "px" is allowed as well 
     * as surely people will use that by mistake.  If an offset or the normal position
     * is not specified, then 0 is used.
     *
     * @param canvas the canvas for the layout, ignored when a parent DasColumn is used.
     * @param parent if non-null, this DasColumn is specified with respect to parent.
     * @param minStr a string like "0%+5em"
     * @param maxStr a string like "100%-7em"
     * @throws IllegalArgumentException if the strings cannot be parsed
     */
    public static DasColumn create(DasCanvas canvas, DasColumn parent, String minStr, String maxStr) {
        double[] min, max;
        try {
            min = parseFormatStr(minStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("unable to parse min: \"" + minStr + "\"");
        }
        try {
            max = parseFormatStr(maxStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("unable to parse max: \"" + maxStr + "\"");
        }
        return new DasColumn(canvas, parent, min[0], max[0], min[1], max[1], (int) min[2], (int) max[2]);
    }

    public static final DasColumn NULL = new DasColumn(null, null, 0, 0, 0, 0, 0, 0);

    /**
     * @deprecated This created a column that was not attached to anything, so
     * it was simply a convenience method that didn't save much effort.
     */
    public DasColumn createSubColumn(double pleft, double pright) {
        double left = getMinimum();
        double right = getMaximum();
        double delta = right - left;
        return new DasColumn(getCanvas(), left + pleft * delta, left + pright * delta);
    }

    public int getWidth() {
        return getDMaximum() - getDMinimum();
    }

    public static DasColumn create(DasCanvas parent) {
        return new DasColumn(parent, null, 0.0, 1.0, 5, -3, 0, 0);
    }

    public static DasColumn create(DasCanvas parent, int iplot, int nplot) {
        double min = 0.1 + iplot * (0.7) / nplot;
        double max = 0.099 + (iplot + 1) * (0.7) / nplot;
        return new DasColumn(parent, min, max);
    }

    public DasColumn createAttachedColumn(double pleft, double pright) {
        return new DasColumn(null, this, pleft, pright, 0, 0, 0, 0);
    }

    /**
     * return the left of the column.
     * @return
     */
    public int left() {
        return getDMinimum();
    }

    /**
     * return the right (non-inclusive) of the column.
     * @return
     */
    public int right() {
        return getDMaximum();
    }
}
