package shag.table;

import java.util.Map;

/**
 * ColumnFilterable is an interface that defines any object, typically a TableModel,
 * that can be filtered by column.
 * 
 * @author   dondi
 * @version  $Revision: 1.1 $ $Date: 2005/03/29 02:10:07 $
 */
public interface ColumnFilterable {

    /**
     * Returns the collection of known filters, as a Map keyed with Integer
     * instances for each column that has an active filter.
     */
    public Map<Integer, ValueFilter> getColumnFilters();

    /**
     * Sets the filter for the given column to be the given ValueFilter.  A null
     * ValueFilter effectively clears the filter.
     */
    public void setColumnFilter(int column, ValueFilter filter);

    /**
     * Applies all current filters to the underlying table data, producing
     * a new filtered set of rows.
     */
    public void applyColumnFilters();

    /**
     * Clears all current filters, restoring the underlying data to an
     * unfiltered set of rows.
     */
    public void clearAllColumnFilters();
}
