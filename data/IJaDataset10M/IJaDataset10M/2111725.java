package org.jfree.workbook;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Stores information about all the selection ranges in a worksheet.
 */
public class Selections {

    /** The column in which the cursor is located. */
    protected int cursorColumn;

    /** The row in which the cursor is located. */
    protected int cursorRow;

    /** A list of all selections. */
    protected List<Selection> selections;

    /**
     * Default constructor.
     */
    public Selections() {
        this.cursorColumn = 0;
        this.cursorRow = 0;
        this.selections = new ArrayList<Selection>();
        this.selections.add(new Selection(0, 0, 0, 0));
    }

    /**
     * Returns the cursor column.
     * 
     * @return The column.
     */
    public int getCursorColumn() {
        return this.cursorColumn;
    }

    /**
     * Returns the cursor row.
     * 
     * @return The row.
     */
    public int getCursorRow() {
        return this.cursorRow;
    }

    /**
     * Returns an iterator that provides access to the selections.
     * 
     * @return The iterator.
     */
    public Iterator getIterator() {
        return selections.iterator();
    }
}
