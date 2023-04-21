package net.kolmodin.yalt;

import java.util.*;
import java.io.*;
import org.apache.log4j.*;

/**
*
 *  A possibly  ordered list of Rows with
 *  the same nr of elements. The elements are called columns.
 *  Concrete  subclasses provides constructors which
 *  populates the report   with rows.
*
*   <p>Each Row is a Vector containing results. Each element
*   in the Row corresponds to a column in the result set.
*   The name (and order) of the columns are defined in the
*   Vector  <i>columns</i>. All Rows has identical columns.
*   
*   <p>The rows are returned in an undefined order unless the 
*   isSortable() method is true and the sort() method has been
*   invoked.
*    <p>
*   Copyright (C) 2000 Michael Kolmodin
*   All rights reserved
*   <br> Last revised: $Date: 2002/04/09 08:45:31 $
*   <br> Revision: $Revision: 1.1.1.1 $
*
*   @author Michael Kolmodin
*   @version $Version:$
*
*/
public abstract class Report extends Vector {

    private Category log = Category.getInstance("Report");

    /** Each row contains values corresponing to these keys */
    protected Vector columns;

    /** This map maps column names to indexes */
    protected HashMap columnMap = null;

    /** The index used by to compare rows */
    protected int sortIndex = 0;

    /** The sorted view of the Rows */
    protected SortedSet sortedView;

    protected int[] sortedRows = null;

    protected class RowComparator implements Comparator {

        Report report = null;

        /**
           * Compare two Rows using current sort order. Comparator semantics.
           *
           * @param o1   A Row.
           * @param o2   Another Row.
           * @return      0 if row1 == row2, -1 if row1 < row2, else 1.
           *
           */
        public int compare(Object o1, Object o2) {
            Row row1 = (Row) o1;
            Row row2 = (Row) o2;
            String s1 = report.get(row1.index(), sortIndex);
            String s2 = report.get(row2.index(), sortIndex);
            int result = s1.compareToIgnoreCase(s2);
            if (result == 0) {
                s1 += report.get(row1.index(), 1);
                s2 += report.get(row2.index(), 1);
                result = s1.compareToIgnoreCase(s2);
                if (result == 0) log.warn("Two items in report with same key: " + report.get(row1.index(), sortIndex) + report.get(row1.index(), 1));
            }
            return result;
        }

        /**
           * Check if two Rows are equals with respect to current sort order. 
           *
           * @param o1   A Row.
           * @param o2   Another Row.
           * @return    (o1 == o2)
           *
           */
        public boolean equals(Object o1, Object o2) {
            Row row1 = (Row) o1;
            Row row2 = (Row) o2;
            String s1 = report.get(row1.index(), sortIndex);
            String s2 = report.get(row2.index(), sortIndex);
            boolean result = s1.equalsIgnoreCase(s2);
            if (result) {
                s1 += report.get(row1.index(), 1);
                s2 += report.get(row2.index(), 1);
                result = s1.equalsIgnoreCase(s2);
                if (result) log.warn("Two items in report with same key: " + report.get(row1.index(), sortIndex) + report.get(row1.index(), 1));
            }
            return s1.equalsIgnoreCase(s2);
        }

        public RowComparator(Report r) {
            report = r;
        }
    }

    /**
       *
       * Get the name and order of the columns of each Row
       *
       * @return a Vector of Strings containing the names of the
       *          columns.
       *
       */
    protected String[] getColumns() {
        String[] retval = new String[columns.size()];
        for (int i = 0; i < retval.length; i += 1) {
            retval[i] = (String) columns.get(i);
        }
        return retval;
    }

    /**
       * Return the index for a given column
       */
    protected int getColumnIndex(String columnName) {
        Integer index = (Integer) columnMap.get(columnName);
        return (index != null ? index.intValue() : -1);
    }

    /**
      *
      * Get a specified column from a row
      *
      */
    protected final String get(int rowIndex, int columnIndex) {
        Vector row = (Vector) this.get(rowIndex);
        return (String) row.get(columnIndex);
    }

    /**
       * 
       * Define the column which the rows are sorted from.
       *
       * BUG: If the source (this Vector) contains multiple 
       * entries with the same sortig key, only one entry will
       * be present in sortedRows which becomes shorter than
       * the source => indexOutOfBounds later on.
       *
       * Solution: 
       *    - new function size() returns actual # unique entries. Check
       *      local use of this.size() !
       *    - Better, longer key when two entries have the same sorting key.
       *      Use all columns (fixed list?) if primary key is the same?!
       *
       * @param column Name of column to sort from.
       *
       * @throws IllegalStateException if the list isn't sortable
       * @throws IllegalArgumentException if the column name is invalid -
       *         that is, it's not part of the list defined in setColumnOrder.
       *
       */
    public void sort(String column) throws IllegalArgumentException, IllegalStateException {
        int ix = getColumnIndex(column);
        if (ix == -1) throw new IllegalArgumentException("No such column: " + column);
        sortIndex = ix;
        Vector tmp = new Vector(super.size());
        for (int i = 0; i < super.size(); i += 1) {
            Row row = new Row();
            row.bind(this, i);
            tmp.add(row);
        }
        sortedView = new TreeSet(new RowComparator(this));
        sortedView.addAll(tmp);
        Iterator i = sortedView.iterator();
        sortedRows = new int[sortedView.size()];
        for (int j = 0; i.hasNext(); j += 1) {
            sortedRows[j] = ((Row) i.next()).index();
        }
        log.debug("Sorting done, report.size: " + super.size() + ", sortedRows: " + sortedRows.length);
    }

    /**
       *
       * Define the column order in each row.
       *
       * @param columnOrder Array of Strings containing the name of the columns
       *        in each Row.
       *
       */
    public void setColumnOrder(String[] columnOrder) {
        columns = new Vector(columnOrder.length);
        for (int i = 0; i < columnOrder.length; i += 1) columns.add(columnOrder[i]);
        columnMap = new HashMap(columns.size());
        for (int i = 0; i < columns.size(); i += 1) columnMap.put(columnOrder[i], new Integer(i));
    }

    ;

    /**
      *
      * Return # entries in this report
      *
      */
    public int size() {
        return sortedRows.length;
    }

    /**
       *
       * Check if it's possible to sort this list.
       *
       * @return True the list is sortable, else false.
       *
       */
    public boolean isSortable() {
        return false;
    }

    /**
     *
     * Get a specified column from a row
     *
     */
    public String get(int rowIndex, String columnName) {
        Assert.check(rowIndex >= 0 && rowIndex < this.size(), "Illegal row index: " + rowIndex);
        Integer columnIndex = (Integer) columnMap.get(columnName);
        Assert.check(columnIndex != null, "Bad column: " + columnName);
        int ix = sortedRows[rowIndex];
        if (ix < 0 || ix >= this.size()) {
            log.debug("Strange mapping from: " + rowIndex + " to: " + ix);
            ix = rowIndex;
        }
        Vector row = (Vector) this.get(ix);
        return (String) row.get(columnIndex.intValue());
    }

    /**
       * A sorted iterator
       *
       */
    public Iterator iterator() {
        if (sortedView == null) sortedView = new TreeSet(this);
        return sortedView.iterator();
    }
}
