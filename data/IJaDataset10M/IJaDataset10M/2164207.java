package com.jlocksmith;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Table Header Listener
 *
 * @author $Author: Derek Helbert $
 * @version $Revision: 1.1 $
 */
public class TableHeaderListener extends MouseAdapter {

    /** Table */
    private JTable table = null;

    /** Table Model */
    private SortableTableModel model = null;

    /** Last Column */
    private int lastColumn = -1;

    /** Direction 0 == down, 1 == up  */
    private int direction = 0;

    /**
	 * Constructor for class TableHeaderListener.
	 *
	 * @param table JTable
	 * @param model DicRowTableModel
	 * @param config Module Configuration
	 */
    public TableHeaderListener(JTable table, SortableTableModel model) {
        this.table = table;
        this.model = model;
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setDefaultRenderer(new SortRenderer(this));
    }

    /**
	 * Get Direction Flag
	 *
	 * @return boolean
	 */
    public boolean isDirectionUp() {
        return direction == 1 ? true : false;
    }

    /**
	 * Reset Column Sorted and Direction
	 */
    public void reset() {
        lastColumn = -1;
        direction = 0;
        table.getTableHeader().resizeAndRepaint();
    }

    /**
	 * Swap Direction
	 */
    private void swapDirection() {
        if (direction == 0) {
            direction = 1;
        } else {
            direction = 0;
        }
    }

    /**
	 * Get Last Column Sorted
	 *
	 * @return int
	 */
    public int getLastColumn() {
        return lastColumn;
    }

    /**
	 * Mouse Clicked
	 *
	 * @param event Mouse Event
	 */
    public void mouseClicked(MouseEvent event) {
        try {
            table.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
            int selected = table.getSelectedRow();
            int tableColumn = table.columnAtPoint(event.getPoint());
            int modelColumn = table.convertColumnIndexToModel(tableColumn);
            if (modelColumn == lastColumn) {
                swapDirection();
            }
            lastColumn = tableColumn;
            model.sort(modelColumn, isDirectionUp());
            if (selected != -1) {
                table.setRowSelectionInterval(selected, selected);
            }
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            table.getTableHeader().resizeAndRepaint();
            table.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }
}
