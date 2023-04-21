package org.akrogen.tkui.core.gui.widgets.tables;

import org.akrogen.tkui.core.gui.widgets.IGuiWidget;

public interface IGuiTable extends IGuiWidget {

    public IGuiTableColumn getColumn(int index);

    public int getColumnCount();

    public IGuiTableColumn[] getColumns();

    public int getWidth();

    public boolean isDisposed();

    public void setHeaderVisible(boolean headerVisible);

    public boolean getLinesVisible();

    public void setLinesVisible(boolean linesVisible);

    public void deselectAll();

    public void selectAll();

    public int getSelectionCount();

    public void refresh();

    public void removeItem(int index);

    /**
	 * Return selected rows.
	 * @return
	 */
    public IGuiTableRow[] getSelectedRows();

    /**
	 * Return all rows of the IGuiTable
	 * @return
	 */
    public IGuiTableRow[] getRows();

    /**
	 * Add row
	 * @param row
	 */
    public void addRow(IGuiTableRow row);

    /**
	 * Index of Row
	 * @param row
	 * @return
	 */
    public int indexOfRow(IGuiTableRow row);

    public IGuiTableRow getRow(int index);
}
