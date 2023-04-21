package com.jrefinery.common.ui;

import javax.swing.table.*;

/**
 * The base class for a sortable table model.
 */
public abstract class SortableTableModel extends AbstractTableModel {

    /** The column on which the data is sorted (-1 for no sorting). */
    protected int sortingColumn;

    /** Indicates ascending (true) or descending (false) order; */
    protected boolean ascending;

    /**
   * Standard constructor.
   */
    public SortableTableModel() {
        this.sortingColumn = -1;
        this.ascending = true;
    }

    /**
   * Returns the index of the sorting column, or -1 if the data is not sorted on any column.
   */
    public int getSortingColumn() {
        return sortingColumn;
    }

    /**
   * Returns true if the data is sorted in ascending order, and false otherwise. *
   */
    public boolean getAscending() {
        return this.ascending;
    }

    /**
   * Sets the flag that determines whether the sort order is ascending or descending.
   */
    public void setAscending(boolean flag) {
        this.ascending = flag;
    }

    /**
   * Sorts the table by the specified column.
   */
    public void sortByColumn(int column, boolean ascending) {
        if (isSortable(column)) {
            this.sortingColumn = column;
        }
    }

    /**
   * Returns true if the specified column is sortable, and false otherwise.
   */
    public boolean isSortable(int column) {
        return false;
    }
}
