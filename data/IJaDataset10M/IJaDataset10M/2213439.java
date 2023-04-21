package net.pleso.framework.client.dal;

import net.pleso.framework.client.dal.db.IDBValue;

/**
 * Represents data row. It can be called as simple data record or infoclass.
 */
public interface IDataRow {

    /**
	 * Returns columns count.
	 * 
	 * @return columns count
	 */
    int getColumnCount();

    /**
	 * Returns {@link IDataColumn} by its zero based index. Last index equals
	 * columns count calculated by {@link #getColumnCount()} minus one.
	 * 
	 * @param index
	 *            zero based column index
	 * @return {@link IDataColumn} instance with specified index
	 */
    IDataColumn getColumn(int index);

    /**
	 * Returns {@link IDBValue} instance representing current value of specified
	 * column. Returned value is not binded to row column, so changing in will
	 * not change corresponding
	 * 
	 * @param column
	 * @return {@link IDBValue} instance with column value
	 */
    IDBValue getCell(IDataColumn column);

    /**
	 * Sets the value for specified data row column. Value can't be null.
	 * 
	 * @param column
	 *            data row column to change
	 * @param value
	 *            new value for data row column
	 */
    void setCell(IDataColumn column, IDBValue value);
}
