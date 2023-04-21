package ihm;

import java.awt.Insets;

/**
 * D�finit les caracteristiques d'une cellule d'un XGridBag
 *
 * @see XGridBag
 * @see java.awt.GridBagConstraints
 */
public class CellStyle {

    /**
     * anchor CENTER, NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST.
     */
    int anchor;

    /**
     * fill NONE, HORIZONTAL, VERTICAL, BOTH
     */
    int fill;

    /**
     * insets (int top, int left, int bottom, int right)
     */
    Insets insets;

    /**
     * internal X padding
     */
    int ipadx;

    /**
     * internal Y padding
     */
    int ipady;

    /**
     * extra X space (none=0.0 ... all=1.0)
     */
    double weightx;

    /**
     * extra Y space (none=0.0 ... all=1.0)
     */
    double weighty;

    /**
     * CellStyle constructor.
     *
     * @param weightx extra X space usage (none=0.0 ... all=1.0)
     * @param weighty extra Y space usage (none=0.0 ... all=1.0)
     * @param anchor location if component doesn't occupy entire cell:
     * <br><code>
     * --------------------------------------------------<br>
     * |FIRST_LINE_START...PAGE_START.....FIRST_LINE_END|<br>
     * |LINE_START...........CENTER.............LINE_END|<br>
     * |LAST_LINE_START.....PAGE_END.......LAST_LINE_END|<br>
     * --------------------------------------------------<br>
     * <code>
     * @param fill NONE, HORIZONTAL, VERTICAL, BOTH
     * @param insets (int top, int left, int bottom, int right)
     * @param ipadx internal X padding
     * @param ipady internal Y padding
     *
     * @see XGridBag
     * @see java.awt.GridBagConstraints
     * @see java.awt.Insets
     */
    public CellStyle(double weightx, double weighty, int anchor, int fill, Insets insets, int ipadx, int ipady) {
        super();
        this.fill = fill;
        this.ipadx = ipadx;
        this.ipady = ipady;
        this.insets = insets;
        this.anchor = anchor;
        this.weightx = weightx;
        this.weighty = weighty;
    }
}
