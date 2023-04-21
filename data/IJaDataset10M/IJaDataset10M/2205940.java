package net.sf.easylayouts;

/**
 * Container for RowLayout constraints.
 */
public class RowConstraints {

    private int rowid;

    private VerticalAlignment vAlign;

    private Growability hGrow;

    private Growability vGrow;

    /** constructor */
    public RowConstraints(int rowid, VerticalAlignment vAlign, Growability hGrow, Growability vGrow) {
        this.rowid = rowid;
        this.vAlign = vAlign;
        this.hGrow = hGrow;
        this.vGrow = vGrow;
    }

    /** constructor */
    public RowConstraints(int rowid) {
        this(rowid, RowLayout.BOTTOM, RowLayout.NO_GROW, RowLayout.NO_GROW);
    }

    /** constructor */
    public RowConstraints(int rowid, VerticalAlignment vAlign) {
        this(rowid, vAlign, RowLayout.NO_GROW, RowLayout.NO_GROW);
    }

    /** constructor */
    public RowConstraints(int rowid, VerticalAlignment vAlign, Growability hGrow) {
        this(rowid, vAlign, hGrow, RowLayout.NO_GROW);
    }

    public int getRow() {
        return rowid;
    }

    public VerticalAlignment getVerticalAlignment() {
        return vAlign;
    }

    public Growability getHorizontalGrow() {
        return hGrow;
    }

    public Growability getVerticalGrow() {
        return vGrow;
    }

    public void setRow(int rowid) {
        this.rowid = rowid;
    }

    public void setVerticalAlignment(VerticalAlignment vAlign) {
        this.vAlign = vAlign;
    }

    public void setHorizontalGrow(Growability hGrow) {
        this.hGrow = hGrow;
    }

    public void setVerticalGrow(Growability vGrow) {
        this.vGrow = vGrow;
    }
}
