package edu.ucla.stat.SOCR.util.tablemodels;

/**
 * SortOrderConstants class specifies sort order constants for {@link SortedTableModel}
 *
 * @see SortedTableModel#getColumnSortOrder(int)
 * @see SortedTableModel#sortColumn(int, int, boolean)
 */
public interface SortOrderConstants {

    /**
     * Specifies ascending sort order
     */
    public static final int ASCENDING = 0;

    /**
     * Specifies descending sort order
     */
    public static final int DESCENDING = 1;

    /**
     * Specifies no sort order
     */
    public static final int NOT_SORTED = 2;
}
