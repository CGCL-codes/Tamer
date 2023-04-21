package org.ujac.util.table;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Name: FilteredTableIterator<br>
 * Description: An iterator for the class FilteredTable.
 * 
 * @author lauerc
 */
public class FilteredTableIterator implements Iterator {

    /** The table to iterate. */
    private Table table = null;

    /** The filter rule to use. */
    private RowFilterRule filterRule = null;

    /** The table iterator, used to iterate over the given table. */
    private Iterator tableIterator = null;

    /** The prefetched data row. */
    private Row prefetchedRow = null;

    /**
   * Constructs a FilteredTableIterator instance with specific attributes.
   * @param table The table to iterate.
   * @param filterRule The filter rule to use.
   */
    protected FilteredTableIterator(Table table, RowFilterRule filterRule) {
        this.table = table;
        this.filterRule = filterRule;
        this.tableIterator = table.iterator();
        prefetchRow();
    }

    /**
   * @see java.util.Iterator#hasNext()
   */
    public boolean hasNext() {
        return prefetchedRow != null;
    }

    /**
   * @see java.util.Iterator#next()
   */
    public Object next() {
        Row currentRow = prefetchedRow;
        if (currentRow == null) {
            throw new NoSuchElementException();
        }
        prefetchedRow = null;
        prefetchRow();
        return currentRow;
    }

    /**
   * Prefetches the next valid row.
   */
    private void prefetchRow() {
        try {
            while (tableIterator.hasNext()) {
                Row row = (Row) tableIterator.next();
                if (filterRule.test(row)) {
                    prefetchedRow = row;
                    break;
                }
            }
        } catch (TableException ex) {
            throw new RuntimeException("Unexpected error while prefetching next valid row.", ex);
        }
    }

    /**
   * @see java.util.Iterator#remove()
   */
    public void remove() {
        throw new IllegalStateException("Removing rows is not permitted for filtered tables!");
    }
}
