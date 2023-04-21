package hu.uszeged.inf.wlab.netspotter.common.device;

import java.util.*;

/**
 *	It stores the set of neighbors of the ND table  
 * @author Vajk V�radi
 *
 */
public class NDTable {

    protected int actualSize;

    protected TreeMap<Integer, NDEntry> table;

    public NDTable() {
        this.actualSize = 0;
        this.table = new TreeMap<Integer, NDEntry>();
    }

    /**
	 * 
	 * @param size	the length of ND table (for  index key)
	 * @param rtable: a complete ND table
	 */
    public NDTable(int size, TreeMap<Integer, NDEntry> rtable) {
        this.actualSize = size;
        this.table = rtable;
    }

    /**Adding a single ND entry to the ND table
	 * @param arpEntry
	 */
    public void addSingleNDEntry(NDEntry ndEntry) {
        this.table.put(actualSize++, ndEntry);
    }

    /**Returns the ND table in Set of ND entry's 
	 * @return
	 */
    public TreeMap<Integer, NDEntry> getTable() {
        return this.table;
    }

    /**Returns the actual number of ND entry's
	 * @return
	 */
    public int getSize() {
        return this.actualSize;
    }

    /**Setting the size of the ND table
	 * @param size
	 */
    public void setSize(int size) {
        this.actualSize = size;
    }

    public String toString() {
        String ret = "";
        for (int i = 0; i < this.actualSize; i++) {
            ret += this.table.get(i);
        }
        return ret;
    }
}
