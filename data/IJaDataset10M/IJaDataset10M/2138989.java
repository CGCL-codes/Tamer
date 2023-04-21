package com.qasystems.swing.table;

import com.qasystems.debug.DebugWriter;
import java.awt.Rectangle;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;

/**
 * This class implements the view actually showing the table.
 */
public abstract class View extends JTable {

    private int[] MINIMUM_COLUMN_WIDTHS;

    private int[] PREFERRED_COLUMN_WIDTHS;

    /**
   * Default constructor
   */
    public View() {
        super();
        try {
            initialize();
        } catch (Exception e) {
            new DebugWriter().writeException(e, this);
        }
    }

    /**
   * override JComponent
   * - prevent scrolling in table cell when clicking on it
   * - scrolling possible through scrollbars
   *
   * - when scrolling is needed override this method in subclass
   *
   * @param aRect the rectangle
   */
    public void scrollRectToVisible(Rectangle aRect) {
    }

    /**
   * set the minimum column sizes
   *
   * @param width the minimum sizes
   */
    public void setMinimumColumnWidths(int[] width) {
        MINIMUM_COLUMN_WIDTHS = width;
    }

    /**
   * get the minimum column sizes
   *
   * @return the minimum column sizes
   */
    public int[] getMinimumColumnWidths() {
        return (MINIMUM_COLUMN_WIDTHS);
    }

    /**
   * set the preferred column sizes
   *
   * @param width the preferred column sizes
   */
    public void setPreferredColumnWidths(int[] width) {
        PREFERRED_COLUMN_WIDTHS = width;
    }

    /**
   * get the preferred column sizes
   *
   * @return the preferred column sizes
   */
    public int[] getPreferredColumnWidths() {
        return (PREFERRED_COLUMN_WIDTHS);
    }

    /**
   * set the model for the table
   *
   * @param table the model
   */
    public void setModel(Sorter table) {
        super.setModel(table);
    }

    /**
   * Called when the table changed. Re-calculates the column widths
   * and tries to force a repaint of the observation pane.
   * Implements <tt>javax.swing.event.TableModelListener</tt>
   *
   * @param e the event
   */
    public void tableChanged(TableModelEvent e) {
        int row = getSelectedRow();
        if ((row == -1) && (getRowCount() > 0)) {
            row = 0;
        }
        clearSelection();
        super.tableChanged(e);
        if ((row >= 0) && (row < getRowCount())) {
            setRowSelectionInterval(row, row);
        }
        repaintTable();
    }

    /**
   * Reset the view of the observation table.
   */
    public void reset() {
        for (int i = 0; i < PREFERRED_COLUMN_WIDTHS.length; i++) {
            setPreferredColumnWidth(i, -1);
        }
    }

    /**
   * Get the minimum width of the column.
   *
   * @param column the column
   * @return the width of the given column
   */
    public int getMinimumColumnWidth(int column) {
        return (MINIMUM_COLUMN_WIDTHS[column]);
    }

    /**
   * Get the preferred width of the column.
   *
   * @param column the column
   * @return the width of the given column
   */
    public int getPreferredColumnWidth(int column) {
        return (PREFERRED_COLUMN_WIDTHS[column]);
    }

    /**
   * Set the preferred width of the column.
   * Typically, this method is called when the user has changed
   * the column width using manually.
   *
   * @param column the column
   * @param newWidth the width. If the width is set to -1,
   *                 default column width are used.
   */
    public synchronized void setPreferredColumnWidth(int column, int newWidth) {
        PREFERRED_COLUMN_WIDTHS[column] = newWidth;
    }

    /**
   * Returns <i>class_name</i>@<i>object_hashcode</i>.
   *
   * @return the string
   */
    public String toString() {
        return (getClass().getName() + "@" + Integer.toHexString(hashCode()));
    }

    private void initialize() throws Exception {
        setEnabled(true);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void repaintTable() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                repaint();
            }
        });
    }
}
