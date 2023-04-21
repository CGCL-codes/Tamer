package org.columba.ristretto.pop3;

/**
 * A ScanList entry.
 * 
 * @author tstich
 *
 */
public class ScanListEntry {

    int index;

    int size;

    /**
	 * Constructs the ScanListEntry. 
	 * 
	 * @param index
	 * 
	 * @param size
	 */
    public ScanListEntry(int index, int size) {
        this.index = index;
        this.size = size;
    }

    /**
	 * @return the index
	 */
    public int getIndex() {
        return index;
    }

    /**
	 * Set the index.
	 * 
	 * @param index
	 */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
	 * @return the size.
	 */
    public int getSize() {
        return size;
    }

    /**
	 * Set the size.
	 * 
	 * @param size
	 */
    public void setSize(int size) {
        this.size = size;
    }
}
