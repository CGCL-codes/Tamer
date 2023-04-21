package org.adempiere.webui.event;

import org.zkoss.zul.ListModel;
import org.zkoss.zul.event.ListDataEvent;

/**
 * @author andy
 *
 */
public class WTableModelEvent extends ListDataEvent {

    /** Specifies all rows. */
    public static final int ALL_ROWS = -1;

    /** Specifies all columns in a row or rows. */
    public static final int ALL_COLUMNS = -1;

    /** Identifies the header row. */
    public static final int HEADER_ROW = -1;

    protected int m_column;

    /**
	 * All row data in the table has changed, listeners should discard any state
	 * that was based on the rows and requery the <code>TableModel</code> to
	 * get the new row count and all the appropriate values. The
	 * <code>WListbox</code> will repaint the entire visible region on receiving
	 * this event, querying the model for the cell values that are visible. The
	 * structure of the table ie, the column names, types and order have not
	 * changed.
	 *
	 * @param source	The list model that has changed
	 */
    public WTableModelEvent(ListModel source) {
        this(source, 0, Integer.MAX_VALUE, ALL_COLUMNS, CONTENTS_CHANGED);
    }

    /**
	 * This row of data has been updated. To denote the arrival of a completely
	 * new table with a different structure use <code>HEADER_ROW</code> as the
	 * value for the <code>row</code>.
	 *
	 * @param source	The list model that has changed
	 * @param row		Index of the affected row
	 */
    public WTableModelEvent(ListModel source, int row) {
        this(source, row, row, ALL_COLUMNS, CONTENTS_CHANGED);
    }

    /**
	 * The cell in [<I>row</I>,<I>column</I>] has been updated.
	 *
	 * @param source	The list model that has changed
	 * @param row		Index of the affected row
	 * @param column	Index of the affected column
	 */
    public WTableModelEvent(ListModel source, int row, int column) {
        this(source, row, row, column, CONTENTS_CHANGED);
    }

    /**
	 * The cells from (firstRow, column) to (lastRow, column) have been changed.
	 * The <I>column</I> refers to the column index of the cell in the model's
	 * co-ordinate system. When <I>column</I> is ALL_COLUMNS, all cells in the
	 * specified range of rows are considered changed.
	 * <p>
	 * The <I>type</I> should be one of: CONTENTS_CHANGED,  INTERVAL_ADDED, INTERVAL_REMOVED.
	 *
	 * @param source	The list model that has changed
	 * @param firstRow	Index of the first affected row
	 * @param lastRow	Index of the last affected row
	 * @param column	Index of the affected column
	 * @param type		the type of change
	 */
    public WTableModelEvent(ListModel source, int firstRow, int lastRow, int column, int type) {
        super(source, type, firstRow, lastRow);
        m_column = column;
    }

    /**
	 * Returns the column for the event. If the return value is <code>ALL_COLUMNS</code> it
	 * means every column in the specified row has been affected.
	 *
	 * @return the affected column, or {@link #ALL_COLUMNS}
	 */
    public int getColumn() {
        return m_column;
    }

    ;

    /**
	 * Obtain the index of the first affected row
	 *
	 * @return the index of the first affected row
	 */
    public int getFirstRow() {
        return getIndex0();
    }

    /**
	 * Obtain the index of the last affected row
	 *
	 * @return the index of the last affected row
	 */
    public int getLastRow() {
        return getIndex1();
    }
}
