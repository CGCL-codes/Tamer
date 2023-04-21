package net.sf.webwarp.util.collection;

import java.util.ListIterator;

/**
 * A two dimensional data object similar to a TableModel but more basic (not GUI specific). 
 * @author bse
 */
public interface DataModel {

    /**
     * Returns the number of rows in the model.
     * 
     * @return the number of rows in the model
     * @see #getColumnCount
     */
    int getRowCount();

    /**
     * Returns the number of columns in the model.
     * 
     * @return the number of columns in the model
     * @see #getRowCount
     */
    int getColumnCount();

    /**
     * Returns the name of the column at <code>columnIndex</code>.
     * 
     * @param columnIndex
     *            the index of the column
     * @return the name of the column
     */
    String getColumnName(int columnIndex);

    String[] getColumnNames();

    /**
     * Returns the index of the column with given name (the first occurrence for duplicate names)
     * 
     * @param columnName
     *            the name of the column
     * @return the index of the column
     */
    int getColumnIndex(String columnName);

    /**
     * Returns the value for the cell at <code>columnIndex</code> and <code>rowIndex</code>.
     * 
     * @param rowIndex
     *            the row whose value is to be queried
     * @param columnIndex
     *            the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    Object getValueAt(int rowIndex, int columnIndex);

    /**
     * Sets the value in the cell at <code>columnIndex</code> and <code>rowIndex</code> to <code>aValue</code>.
     * 
     * @param aValue
     *            the new value
     * @param rowIndex
     *            the row whose value is to be changed
     * @param columnIndex
     *            the column whose value is to be changed
     * @see #getValueAt
     */
    void setValueAt(Object aValue, int rowIndex, int columnIndex);

    RowObject getRowAt(int rowIndex);

    void insertRow(RowObject rowObject);

    /**
     * Insert Row object at given position.
     * 
     * @param rowObject
     * @param index
     * @throws IllegalArgumentException
     *             if rowObject has wrong columnCount
     */
    void insertRow(RowObject rowObject, int index);

    void deleteRow(int index);

    /**
     * @return Iterator over RowObject
     */
    ListIterator<RowObject> iterator();

    interface RowObject extends Cloneable {

        public int getColumnCount();

        public Object getValueAt(int columnIndex);

        public void setValueAt(Object aValue, int columnIndex);

        public Object clone() throws CloneNotSupportedException;
    }
}
