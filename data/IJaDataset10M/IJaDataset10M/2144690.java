package org.epoline.phoenix.common.shared;

import java.awt.Color;
import java.io.Serializable;

public interface TableDescriptor extends Serializable {

    /**
	 * Returns preferred background colour of the table cell.
	 * 
	 * @param item the item represented by a row of the table
	 * @param column the column index of the table cell
	 * @return preferred background colour. Null if no preference.
	 */
    public Color getBackground(Item item, int column);

    /**
	 * Returns class of queried column.
	 * 
	 * @param columnIndex the column being queried
	 * @return the Object.class
	 */
    public Class getColumnClass(int columnIndex);

    /**
	 * Returns array of column names.
	 * 
	 * @return java.lang.String[]
	 */
    public String[] getColumnNames();

    /**
	 * Returns the default number of characters for a value cell. Holds for
	 * values that will be rendered as strings.
	 */
    public int getDefaultValueCellWidthInChars(int columnIndex);

    /**
	 * @return java.lang.Object
	 * @param item org.epoline.phoenix.common.shared.Item
	 * @param index int
	 */
    public Object getField(Item item, int index);

    /**
	 * Returns preferred foreground colour of the table cell.
	 * 
	 * @param item the item represented by a row of the table
	 * @param column the column index of the table cell
	 * @return preferred foreground colour. Null if no preference.
	 */
    public Color getForeground(Item item, int column);

    /**
	 * Returns whether given cell can be edited by default. Default returns
	 * isColumnEditable(col)
	 * 
	 * @param col column index
	 * @return whether given cell can be edited by default
	 */
    public boolean isCellEditable(Item item, int col);

    /**
	 * Returns whether given column font is bold. Default returns false
	 * 
	 * @param item org.epoline.phoenix.common.shared.Item
	 * @param column int
	 * @return boolean
	 */
    public boolean isColumnBold(Item item, int column);

    /**
	 * Returns whether given column can be edited by default. Default returns
	 * false
	 * 
	 * @param col column index
	 * @return whether given column can be edited by default
	 */
    public boolean isColumnEditable(int col);

    /**
	 * Returns whether given column can be sorted by default. Default returns
	 * true
	 * 
	 * @param col column index
	 * @return whether given column can be sorted by default
	 */
    public boolean isColumnSortable(int col);

    /**
	 * Returns whether given column has fixed width by default. Default returns
	 * true for column classes Boolean and java.sql.Date
	 * 
	 * @param col column index
	 * @return whether given column has fixed width by default
	 */
    public boolean isDefaultFixedWidthColumn(int col);

    /**
	 * Sets the specified field of the item
	 * 
	 * @param Object
	 * @param item item to be modified
	 * @param index index of field to be modified
	 */
    public void setField(Object value, Item item, int index);
}
