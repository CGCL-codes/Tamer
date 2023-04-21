package org.axed.user.client;

import com.google.gwt.core.client.GWT;

/**
 * Holds the information of one Anchor of a Range.
 */
public class Anchor {

    public Line line;

    public Range parent;

    /**
 	 * Constructor
 	 */
    public Anchor(Range parent) {
        this.parent = parent;
        this.line = null;
    }

    /**
	 * Compares the position of two anchors.
	 *
	 * @return -1 if this anchor points earlier into the document than a.
	 *         0 if both  anchors point at the same point
	 *         +1 if this anchor points to a later point in the document.
	 *
	 *  TODO throw an exception if either pointer is undefined.
	 */
    public int compare(Anchor a) {
        if (this.equals(a)) {
            return 0;
        }
        if (line == a.line) {
            return getEntry() > a.getEntry() ? (+1) : (-1);
        }
        return line.getLineNum() > a.line.getLineNum() ? (+1) : (-1);
    }

    /**
	 * Include in the currentline the Anchor htmlText.
	 */
    public void commit() {
        line.commitAnchor(this);
    }

    /**
	 * Returns true when two anchors point at the same point
	 */
    public boolean equals(Anchor a) {
        return this == a;
    }

    /**
	 * Returns the other anchor of the range this Anchor belongs to.
	 */
    public Anchor getOther() {
        return isBegin() ? parent.getEnd() : parent.getBegin();
    }

    /**
	 * returns the entry where this anchor is positioned at.
	 */
    public int getEntry() {
        if (line == null) {
            return -1;
        }
        return line.getEntry(this);
    }

    /**
	 * returns the character column this anchor is positioned at.
	 */
    public int getCol() {
        if (line == null) {
            return -1;
        }
        return line.getCol(this);
    }

    /**
	 * Returns the HTML text for this anchor
	 */
    public String getText() {
        return parent.getText(this);
    }

    /**
 	 * Returns true if this the Begin anchor of a range
 	 */
    public boolean isBegin() {
        return this == parent.getBegin();
    }

    /**
 	 * Returns true if this the Begin anchor of a range
 	 */
    public boolean isEnd() {
        return this == parent.getEnd();
    }

    /**
	 * Sets this anchor to line and col.
	 */
    public void set(Line line, int col) {
        if (this.line != null && this.line != line) {
            this.line.removeAnchor(this);
            this.line.refresh();
        }
        this.line = line;
        line.addAnchor(this, col);
    }

    /**
	 * Unsets this anchor.
	 */
    public void unset() {
        if (this.line != null) {
            line.removeAnchor(this);
            line.refresh();
        }
        line = null;
    }
}
