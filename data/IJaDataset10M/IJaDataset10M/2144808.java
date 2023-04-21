package org.axed.user.client.util;

import com.google.gwt.core.client.GWT;

/**
 * Extends the Intlist by some convenient functions. 
 */
public class IntListPlus extends IntList {

    /**
	 * Constructor
	 */
    public IntListPlus() {
        super();
    }

    /**
	 * Adds value v to all elements from b to e.
	 */
    public void addFromTo(int b, int e, int v) {
        for (int i = b; i < e; i++) {
            set(i, get(i) + v);
        }
    }

    /**
	 * Substract -1 to all elements from b to e.
	 */
    public void decrementFromTo(int b, int e) {
        addFromTo(b, e, -1);
    }

    /**
	 * Adds +1 to all elements from b to e.
	 */
    public void incrementFromTo(int b, int e) {
        addFromTo(b, e, +1);
    }

    /**
	 * Returns the index of the first entry that is
	 * greater or equal than v
	 */
    public int firstGreaterOrEqualThan(int v) {
        for (int i = 0; i < size(); i++) {
            if (get(i) >= v) {
                return i;
            }
        }
        return -1;
    }
}
