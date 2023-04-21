package com.worldware.html;

/** Represents a row in a TABLE in HTML. (a TR element)
 **/
public class HTMLRow extends HTMLItem {

    /** Create a row in a table (TR)
	 */
    public HTMLRow() {
        super("TR");
    }

    /** Create a row in a table (TR) with the specified text within a cell (TD) of the table
	 * @param text The contents of the cell (TD) within the row
	 */
    public HTMLRow(String text) {
        this();
        addCell(text);
    }

    /** Create a row in a table (TR) with the specified text within a cell (TD) of the table
	 * @param child The contents of the cell (TD) within the row
	 */
    public HTMLRow(HTMLItem child) {
        this();
        addChild(child);
    }

    public HTMLRow(String col1, String col2) {
        this(col1);
        addCell(col2);
    }

    public HTMLRow(String col1, HTMLItem child) {
        this(col1);
        addCell(child);
    }

    public HTMLRow(String col1, String col2, String col3) {
        this(col1, col2);
        addCell(col3);
    }

    public void addCell(String text) {
        addChild(new HTMLCell(text));
    }

    public void addCell(HTMLItem child) {
        addChild(new HTMLCell(child));
    }
}
