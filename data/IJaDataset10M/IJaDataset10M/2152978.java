package org.richfaces.tarkus.fmk.gui.table;

/**
 * SortingColumn
 * 
 * @version $Revision$
 * @author hbourdin, last modified by $Author$
 */
public class SortingColumn {

    /** ascending */
    private boolean ascending;

    /** column name */
    private final String column;

    /**
   * SortingColumn
   * 
   * @param ascColumn
   *          [+|-]columnName
   */
    public SortingColumn(String ascColumn) {
        if (ascColumn.charAt(0) == '+') {
            ascending = true;
            column = ascColumn.substring(1);
        } else if (ascColumn.charAt(0) == '-') {
            ascending = false;
            column = ascColumn.substring(1);
        } else {
            ascending = true;
            column = ascColumn;
        }
    }

    /**
   * SortingColumn
   * 
   * @param column
   * @param ascending
   */
    public SortingColumn(String column, boolean ascending) {
        this.ascending = ascending;
        this.column = column;
    }

    /**
   * isAscending
   * 
   * @return the ascending
   */
    public boolean isAscending() {
        return ascending;
    }

    /**
   * reverse
   */
    public void reverse() {
        ascending = !ascending;
    }

    /**
   * getColumn
   * 
   * @return the column
   */
    public String getColumn() {
        return column;
    }

    /**
   * getString
   * 
   * @return String
   */
    public String getString() {
        String string;
        if (ascending) {
            string = "+";
        } else {
            string = "-";
        }
        string += column + ";";
        return string;
    }

    /**
   * toString
   * 
   * @see java.lang.Object#toString()
   */
    @Override
    public String toString() {
        return getString();
    }
}
