package mipt.crec.lab.compmath.gui;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import mipt.aaf.edit.swing.form.TableFieldEditor;
import mipt.gui.choice.TableModel;

/**
 *
 * @author Lebedeva
 */
public class PointTableFieldEditor extends TableFieldEditor {

    /**
	 * 
	 */
    public PointTableFieldEditor() {
        super();
    }

    /**
	 * @param table
	 */
    public PointTableFieldEditor(JTable table) {
        super(table);
    }

    @Override
    protected javax.swing.table.TableModel createTableModel(Object[][] values, Object[] headers) {
        return new TableModel(values, headers) {

            public void setValueAt(Object value, int row, int column) {
                super.setValueAt(value, row, column);
                int lastRowIndex = getRowCount() - 1;
                if (lastRowIndex == row) {
                    Double val1 = (Double) getValueAt(lastRowIndex, 1);
                    Double val2 = (Double) getValueAt(lastRowIndex, 2);
                    if ((column == 1 && val2 != null && !val2.isNaN()) || (column == 2 && val1 != null && !val1.isNaN())) {
                        addRow(new Object[] { new Integer(1 + getRowCount()), null, null });
                    }
                }
            }
        };
    }

    @Override
    protected Object[] initColumnHeaders(Object[][] values) {
        return new String[] { "i", "x", "y" };
    }

    @Override
    public void setValue(Object value) {
        Double[][] xy = (Double[][]) value;
        Object[][] arr = new Object[xy.length + 1][3];
        for (int i = 0; i < xy.length; i++) {
            arr[i][0] = new Integer(1 + i);
            arr[i][1] = xy[i][0];
            arr[i][2] = xy[i][1];
        }
        arr[xy.length][0] = new Integer(1 + xy.length);
        arr[xy.length][1] = arr[xy.length][2] = new Double(Double.NaN);
        super.setValue(arr);
        getTable().getColumnModel().getColumn(0).setCellEditor(new TableCellEditor() {

            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                return null;
            }

            public void addCellEditorListener(CellEditorListener l) {
            }

            public void cancelCellEditing() {
            }

            public Object getCellEditorValue() {
                return null;
            }

            public void removeCellEditorListener(CellEditorListener l) {
            }

            public boolean stopCellEditing() {
                return false;
            }

            public boolean shouldSelectCell(EventObject anEvent) {
                return false;
            }

            public boolean isCellEditable(EventObject anEvent) {
                if (!(anEvent instanceof MouseEvent)) return false;
                if (((MouseEvent) anEvent).getClickCount() < 2) return false;
                removeSelectedRow();
                return false;
            }
        });
    }

    protected void removeSelectedRow() {
        int row = getTable().getSelectedRow();
        if (row < 0) return;
        TableModel model = getTableModel();
        int n = model.getRowCount();
        for (int i = row + 1; i < n; i++) {
            model.setValueAt(new Integer(i), i, 0);
        }
        model.removeRow(row);
    }

    @Override
    protected Object[] getArray2Value(javax.swing.table.TableModel model) {
        Object[][] arr = new Double[-1 + model.getRowCount()][2];
        if (arr == null) return null;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                arr[i][j] = model.getValueAt(i, 1 + j);
            }
        }
        return arr;
    }
}
