package org.zkoss.zss.model;

import org.zkoss.zss.model.impl.SparseShortIndex;

/**
 * <p>A sparse array indexing(0-based); used to indicate whether a specified index is empty or with elements.</p>
 * @author henrichen
 * @see SparseShortIndex
 *
 */
public interface SparseIndex {

    /**
	 * Returns next element/empty index from specified index(inclusive).
	 * @param index refence starting index
	 * @param empty true means find next empty index; false means find next element index.
	 * @return next element/empty index per the specified empty since the specified index (inclusive).
	 */
    public int nextIndex(int index, boolean empty);

    /**
	 * Check whether the specified index is empty.
	 * @param index specified index
	 * @return true if an empty at the specified index.
	 */
    public boolean isEmpty(int index);

    /**
	 * Sets the specified index range to empty(true)/element(false).
	 * @param fromIndex the specified begin index (inclusive)
	 * @param toIndex the specified end index (exclusive) 
	 * @param empty true to set the specified index to empty; false to element.
	 */
    public void setEmpties(int fromIndex, int toIndex, boolean empty);

    /**
	 * Sets the specified index to empty(true)/element(false).
	 * @param index the specified index
	 * @param empty true to set the specified index to empty; false to element.
	 */
    public void setEmpty(int index, boolean empty);

    /**
	 * Returns the size indexed by the sparse index.
	 * @return size indexed by the sparse index.
	 */
    public int getSize();
}
