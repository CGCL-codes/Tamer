package org.jfree.workbook;

/**
 * Represents one cell in a worksheet.
 */
public class Cell implements CellConstants, Comparable<Cell> {

    /** The column number (0 to Worksheet.MAX_COLUMNS-1). */
    protected int column;

    /** The row number (0 to Worksheet.MAX_ROWS-1). */
    protected int row;

    /**
     * The type of data in the cell.
     * So far I know of: 30=date, 40=value, 60=label;
     * For expressions, no value type is shown, so assume 0 for now.
     */
    protected int type;

    /** The cell contents. */
    protected String content;

    /** The value format (if required). */
    protected String valueFormat;

    /**
     * Constructs a new cell.
     * 
     * @param type  the cell type.
     * @param content  the cell contents.
     * @param row  the row.
     * @param column  the column.
     */
    protected Cell(int type, String content, int row, int column) {
        this.type = type;
        this.content = content;
        this.row = row;
        this.column = column;
    }

    /**
     * Returns the cell's column.
     * 
     * @return The column.
     */
    public int getColumn() {
        return this.column;
    }

    /**
     * Returns the cell's row.
     * 
     * @return The row.
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Returns the cell's type.
     * 
     * @return The type.
     */
    public int getType() {
        return this.type;
    }

    /**
     * Returns the cell's content.
     * 
     * @return The content.
     */
    public String getContent() {
        return this.content;
    }

    /**
     * Implements the Comparable interface so that cells can easily be sorted.
     * 
     * @param other The object being compared to.
     * 
     * @return An integer that indicates the relative order of the objects.
     */
    public int compareTo(Cell otherCell) {
        return this.getSerialNumber() - otherCell.getSerialNumber();
    }

    /**
     * Returns an integer that increases across columns and down rows.
     * 
     * @return A serial number for the cell.
     */
    protected int getSerialNumber() {
        return (row * Worksheet.MAX_COLUMNS) + column;
    }
}
