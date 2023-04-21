package net.sf.excompcel.spreadsheet.impl.base;

import net.sf.excompcel.spreadsheet.ECBase;

/**
 * This Base Class holds the Base Object for e.g. Row or Cell Class.
 * <ul>
 * <li>O = Object</li>
 * </ul>
 * 
 * @author Detlev Struebig
 * @since v0.8
 *
 */
public abstract class ECBaseObject<O> implements ECBase {

    /** The original Object. */
    private O theObject;

    /**
	 * Constructor.
	 * @param theObject
	 */
    public ECBaseObject(O theObject) {
        this.theObject = theObject;
    }

    public boolean hasOriginalObject() {
        return (theObject != null);
    }

    /**
	 * Get the original Object.
	 * @return the theObject
	 */
    public O getTheObject() {
        return theObject;
    }

    /**
	 * Set the original Object.
	 * @param theObject the theObject to set
	 */
    public void setTheObject(O theObject) {
        this.theObject = theObject;
    }
}
