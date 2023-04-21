package org.wct.table;

import java.util.*;
import org.wct.event.*;

/**
 * This interface specifies the model that the Table uses to draw its columns.<br>
 * It is based on the javax.swing.table.TableColumnModel class, but this class was not used 
 * as the WCT Table model class because of some incompatibilities on the component level.
 * @author  juliano
 * @version 0.1
 */
public interface TableColumnModel {

    /** adds a new column to the table
 * @param c the column to add
 */
    public void addColumn(TableColumn c);

    /** adds a listener to the list that is notified about changes in this column model
 * @param l the listener to be added
 */
    public void addColumnModelListener(TableColumnModelListener l);

    /** return the TableColumn object associated with the given column in the
 * data model
 * @param columnIndex the index of the column in the data model
 * @return the table column associated with the model
 * column, or null if there is no such column
 */
    public TableColumn getColumn(int columnIndex);

    /** returns the number of columns in this column model
 * @return the number of columns in this column model.
 */
    public int getColumnCount();

    /** Returns the width between each cell in the column model
 * @return the specified width
 */
    public int getColumnMargin();

    /** Returns an enumeration containing all columns in this column model
 * @return the specified enumeration
 */
    public java.util.Enumeration getColumns();

    /** Returns whether this model allows column selections
 * @return true if this model allows the selection of columns,
 * false otherwise
 */
    public boolean getColumnSelectionAllowed();

    /** Return the number of selected columns
 * @return  number of selected columns
 */
    public int getSelectedColumnCount();

    /** Returns the indexes of the currently selected columns
 * @return the specified indexes, or an empty array if
 * there is no column selected.
 */
    public int[] getSelectedColumns();

    /** Returns the current column selection model
 * @return the current column selection model
 */
    public javax.swing.ListSelectionModel getSelectionModel();

    /** Returns the total column width of this model
 * @return the total column width of this model
 */
    public int getTotalColumnWidth();

    /** Removes the specified column of the column model
 * @param column the column to remove
 */
    public void removeColumn(TableColumn column);

    /** Removes the specified listener of the list that is notified about
 * changes on this column model
 * @param l the listener to remove
 */
    public void removeColumnModelListener(TableColumnModelListener l);

    /** Sets the current margin of this column model
 * @param newMargin the new margin to be set
 */
    public void setColumnMargin(int newMargin);

    /** Sets whether column selections are allowed on this column model
 * @param val the specified value
 */
    public void setColumnSelectionAllowed(boolean val);

    /**
     * Sets the selection model for this column model
     */
    public void setSelectionModel(javax.swing.ListSelectionModel l);
}
