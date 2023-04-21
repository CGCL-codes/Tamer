package org.xnap.commons.gui.table;

/**
 *  
 * @author Steffen Pingel 
 */
public interface TableLayoutListener {

    /**
     * Invoked when the width of one or more columns was changed.
     */
    void columnLayoutChanged();

    /**
     * Invoked when the sorted column was changed.
     */
    void sortedColumnChanged();

    void columnOrderChanged();

    void columnNameChanged(int index, String newName);

    void columnVisibilityChanged(int index, boolean visible);

    void maintainSortOrderChanged(boolean newValue);
}
