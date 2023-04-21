package de.intarsys.pdf.cds;

import de.intarsys.pdf.cos.COSObject;

/**
 * Abstract superclass for implementing entry objects in PDF name and number
 * trees.
 */
public abstract class CDSTreeEntry {

    public CDSTreeEntry(COSObject value) {
        super();
        this.value = value;
    }

    private COSObject value;

    /**
	 * Set the value for entry.
	 * 
	 * @param object
	 *            The new value for the entry.
	 * @return The previous value
	 */
    public COSObject setValue(COSObject object) {
        COSObject oldValue = value;
        value = object;
        return oldValue;
    }

    /**
	 * The value of the entry.
	 * 
	 * @return The value of the entry.
	 */
    public COSObject getValue() {
        return value;
    }

    public abstract COSObject getKey();
}
