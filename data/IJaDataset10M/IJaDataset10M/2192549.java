package freets.gui.design;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * <code>ConfigurableTable</code> is a replacement for the standard <code>JTable</code>.
 * It provides methods for initializing table column names and widths. The rows of the
 * table can be sorted regarding to the contents of a specific column by clicking on
 * the header of this column. Double-clicking on a header-column reverses the order.<p>
 * A special table-cell renderer for <code>Boolean</code> values is provided. <p>
 * A table-cell editor for <coderInteger</code> values is bound to Integer
 * columns.
 *
 * @author  T. F�rster  
 * @version $Revision: 1.1.1.1 $
 */
public class ConfigurableTable extends JTable {

    /** Names of table columns */
    protected String[] columnNames;

    /** Classes for table columns */
    protected Class[] columnClasses;

    /** Initial column widths */
    protected int[] columnWidths;

    /** Flags that indicate wheather a column is visible (unused) */
    protected boolean[] columnVisible;

    /** Display-order of columns */
    protected int[] displayOrder;

    /** Flag that indicates if this table can be sorted */
    protected boolean sortingEnabled;

    /** Flag that indicates if the cells of this table are editable */
    protected boolean editable;

    /**
     * Creates a new <code>ConfigurableTable</code> with a specific header descriptor.
     * @param dm the table�s model
     * @param thd the header descriptor
     */
    public ConfigurableTable(TableModel dm, TableHeaderDescriptor thd) {
        super(dm);
        this.columnNames = thd.getHeaders();
        this.columnClasses = thd.getClasses();
        this.columnWidths = thd.getWidths();
        this.columnVisible = thd.getVisible();
        this.displayOrder = thd.getDisplayOrder();
        this.sortingEnabled = true;
        setRowHeight(20);
        setAutoResizeMode(AUTO_RESIZE_OFF);
        setColumnSelectionAllowed(false);
        setShowVerticalLines(false);
        setDefaultRenderer(Boolean.class, new BooleanTableCellRenderer());
        setDefaultRenderer(DragDropItem.class, new DragDropItemCellRenderer());
        setDefaultEditor(Integer.class, new IntegerTableCellEditor(new JTextField()));
        sizeColumnsToFit(0);
        for (int z = 0; z < columnClasses.length; z++) {
            try {
                getColumn(columnNames[z]).setWidth(columnWidths[z]);
            } catch (Exception ec) {
                System.out.println("--->" + columnNames[z]);
            }
            sizeColumnsToFit(z);
        }
        getTableHeader().addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                sortTableByRow(convertColumnIndexToModel(((JTableHeader) e.getSource()).columnAtPoint(e.getPoint())));
                if (e.getClickCount() == 2) {
                    reverseTableOrder();
                }
            }
        });
    }

    /**
     * Returns the class of a column.
     * @param c the index of the column (view index)
     * @return the class of the column
     */
    public Class getColumnClass(int c) {
        return columnClasses[convertColumnIndexToModel(c)];
    }

    /**
     * Checks, if a cell is editable. All cells in column 0 are not
     * editable, because it contains the icon of the respective item.
     * @param row row of table
     * @param column column of table (view index)
     * @return <i>false</i>, if column == 0, else call method of super-class
     */
    public boolean isCellEditable(int row, int column) {
        if (!editable) return false;
        if (convertColumnIndexToModel(column) == 0) return false; else return super.isCellEditable(row, column);
    }

    /**
     * Sorts the table  by the content of a specific column.
     * @param column the reference column 
     */
    protected void sortTableByRow(int column) {
        if (sortingEnabled) {
            DefaultTableModel model = (DefaultTableModel) getModel();
            for (int i = model.getRowCount(); i > 0; i--) {
                for (int j = 1; j < i; j++) {
                    if (model.getValueAt(j - 1, column).toString().compareTo(model.getValueAt(j, column).toString()) > 0) {
                        model.moveRow(j - 1, j - 1, j);
                    }
                }
            }
        }
    }

    /**
     * Discards the currently used table data-model and applies a new one.
     * @param newData an array of arrays containing the new data
     * @param columnNames names of columns
     */
    public void setNewDataVector(Object[][] newData, Object[] columnNames) {
        if (columnNames instanceof String[]) {
            this.columnNames = (String[]) columnNames;
        }
        for (int z = 0; z < getColumnCount(); z++) {
            columnWidths[z] = getColumn(getColumnName(z)).getWidth();
        }
        ((DefaultTableModel) getModel()).setDataVector(newData, columnNames);
        sizeColumnsToFit(0);
        for (int z = 0; z < columnClasses.length; z++) {
            getColumn(columnNames[z]).setWidth(columnWidths[z]);
            sizeColumnsToFit(z);
        }
    }

    public void clear() {
        setNewDataVector(new Object[][] {}, columnNames);
    }

    /**
     * Reverses the row-order of the table.
     */
    protected void reverseTableOrder() {
        if (sortingEnabled) {
            DefaultTableModel model = (DefaultTableModel) getModel();
            for (int i = model.getRowCount() - 1; i > 0; i--) {
                model.moveRow(0, 0, i);
            }
        }
    }

    /**
     * Sets the sorting flag.
     * @param v <code>true</code>, if sorting shall be allowed
     */
    public void setSortingEnabled(boolean v) {
        sortingEnabled = v;
    }

    /**
     * Check if this table is sortable.
     * @return the state of the sorting flag
     */
    public boolean isSortingEnabled() {
        return sortingEnabled;
    }

    /**
     * Sets the editable flag.
     * @param v <code>true</code>, if this table shll be editable
     */
    public void setEditable(boolean v) {
        editable = v;
    }

    /**
     * Check if this table is editable.
     * @return the state of the editable flag
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * A special cell editor that is used for <code>Integer</code> columns.
     * Only integer values are accepted by this editor. The editing is cancelled
     * if a non-integer value has been entered (i.e. the recent value is restored).
     */
    class IntegerTableCellEditor extends DefaultCellEditor {

        /**
         * Creates a new <code>IntegerTableCellEditor</code>.
         * @param x a textfield to be used as editor
         */
        public IntegerTableCellEditor(JTextField x) {
            super(x);
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            ((JTextField) editorComponent).setText(value.toString());
            return editorComponent;
        }

        /**
         * Checks the content of the editor. If it is a valid representation
         * of an integer, <code>super.fireEditingStopped()</code> is called, else
         * <code>super.fireEditingCanceled()</code>
         */
        public boolean stopCellEditing() {
            try {
                Integer.parseInt(((JTextField) editorComponent).getText());
                super.fireEditingStopped();
            } catch (NumberFormatException e) {
                super.fireEditingCanceled();
            } finally {
                return true;
            }
        }

        /**
         * Returns the value of the editor.
         * @return an <code>Integer</code> value
         */
        public Object getCellEditorValue() {
            Integer result = null;
            try {
                result = Integer.decode(((JTextField) editorComponent).getText());
            } catch (NumberFormatException e) {
                System.out.println("editor cell value no Integer: " + e);
            } finally {
                return result;
            }
        }
    }

    /**
     * The <code>BooleanTableCellRenderer</code> is used for displaying and rendering
     * cells of Boolean-columns.
     */
    class BooleanTableCellRenderer extends JCheckBox implements TableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value == null) return new JLabel();
            setSelected(((Boolean) value).booleanValue());
            setHorizontalAlignment(CENTER);
            this.setBackground((Color) ((isSelected) ? UIManager.get("Table.selectionBackground") : Color.white));
            return this;
        }
    }
}
