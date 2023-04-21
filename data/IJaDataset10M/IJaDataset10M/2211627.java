package com.safi.workshop.sqlexplorer.sqleditor.results;

import java.text.Format;

/**
 * A CellRange represents a range of cells (sic) within a ResultsTable; it is an interface
 * because selections are implemented as a subset of another CellRange (theoretically,
 * sub-selections would also be possible)
 * 
 * @author John Spackman
 * 
 */
public interface CellRange {

    public static class Column {

        private int columnIndex;

        private String caption;

        private boolean rightJustify;

        /**
     * Constructor
     * 
     * @param caption
     * @param rightJustify
     */
        public Column(int colIndex, String caption, boolean rightJustify) {
            super();
            this.columnIndex = colIndex;
            this.caption = caption;
            this.rightJustify = rightJustify;
        }

        /**
     * @return The column name
     */
        public String getCaption() {
            return caption;
        }

        /**
     * @return Whether the column is right justified (eg a number)
     */
        public boolean isRightJustify() {
            return rightJustify;
        }

        /**
     * Returns the zero-based index of this column
     * 
     * @return
     */
        public int getColumnIndex() {
            return columnIndex;
        }

        /**
     * @return The java.text.Format used to present this format
     */
        protected Format getFormat() {
            return null;
        }

        /**
     * Called to return a non-null displayable version of the given value
     * 
     * @param value
     *          The value to be displayed
     * @return A String version of the value, never null
     */
        public String getDisplayValue(Object value) {
            if (value == null) return "<null>";
            Format format = getFormat();
            if (format != null) return format.format(value);
            return value.toString();
        }
    }

    /**
   * Returns the Column definition
   * 
   * @param colIndex
   *          The zero-based column number
   * @return
   */
    public Column getColumn(int colIndex);

    /**
   * @return The number of columns
   */
    public int getNumberOfColumns();

    /**
   * @return The number of Rows
   */
    public int getNumberOfRows();

    /**
   * @return The rows
   */
    public CellRangeRow[] getRows();
}
