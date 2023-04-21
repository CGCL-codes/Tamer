package com.patientis.framework.controls.table;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import com.patientis.model.common.IBaseModel;

/**
 * ISTableSelect implements the table select interface for a single table
 *
 * Design Patterns: <a href="/functionality/rm/1000073.html">Tables</a>
 * <br/>  
 */
public class ISTableSelect implements ITableSelect {

    /**
	 * table 
	 */
    private ITable table = null;

    /**
	 * Implement table selection for the specified table
	 * 
	 * @param table
	 */
    public ISTableSelect(ITable table) {
        if (table == null) {
            throw new NullPointerException("table");
        }
        this.table = table;
    }

    /**
	 * 
	 * @see com.patientis.framework.controls.table.ITableSelect#isRowSelected()
	 */
    public boolean isRowSelected() {
        return table.getSelectedRow() > -1;
    }

    /**
	 * 
	 * @see com.patientis.framework.controls.table.ITableSelect#getSelectedRowCellObject(int)
	 */
    public Object getSelectedRowCellObject(int column) {
        if (isRowSelected()) {
            return table.getValueAt(table.getSelectedRow(), column);
        } else {
            return null;
        }
    }

    /**
	 * 
	 * @see com.patientis.framework.controls.table.ITableSelect#getSelectedModel()
	 */
    public IBaseModel getSelectedModel() {
        if (isRowSelected()) {
            int originalRow = table.convertRowIndexToModel(table.getSelectedRow());
            if (table.getModel() instanceof ISTableModel) {
                ISTableModel adapter = (ISTableModel) table.getModel();
                if (adapter.getModelAt(originalRow) instanceof IBaseModel) {
                    return (IBaseModel) adapter.getModelAt(originalRow);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
	 * 
	 * @see com.patientis.framework.controls.table.ITableSelect#getSelectedModelList()
	 */
    public List<IBaseModel> getSelectedModelList() {
        if (isRowSelected()) {
            List<IBaseModel> selected = new ArrayList<IBaseModel>(table.getSelectedRows().length);
            for (int i = 0; i < table.getSelectedRows().length; i++) {
                int originalRow = table.convertRowIndexToModel(table.getSelectedRows()[i]);
                if (table.getModel() instanceof ISTableModel) {
                    ISTableModel tableModel = (ISTableModel) table.getModel();
                    if (tableModel.getModelAt(originalRow) instanceof IBaseModel) {
                        selected.add((IBaseModel) tableModel.getModelAt(originalRow));
                    }
                }
            }
            return selected;
        } else {
            return null;
        }
    }

    /**
	 * Remove the selected models (setting delete indicator)
	 *
	 */
    public void removeSelectedModels() {
        if (table.getSelectedRowCount() > 0) {
            int[] selected = table.getSelectedRows();
            for (int i = selected.length - 1; i >= 0; i--) {
                int selectedRow = selected[i];
                ((ISTableModel) table.getModel()).removeRow(selectedRow);
            }
            table.clearSelection();
        }
    }

    /**
	 * @see com.patientis.framework.controls.table.ITableSelect#getSelectedList()
	 */
    public List<ITableRow> getSelectedObjects() {
        if (isRowSelected()) {
            List<ITableRow> selected = new ArrayList<ITableRow>(table.getSelectedRows().length);
            for (int i = 0; i < table.getSelectedRows().length; i++) {
                int originalRow = table.convertRowIndexToModel(table.getSelectedRows()[i]);
                if (table.getModel() instanceof ISTableModel) {
                    ISTableModel tableModel = (ISTableModel) table.getModel();
                    selected.add(tableModel.getModelAt(originalRow));
                }
            }
            return selected;
        } else {
            return null;
        }
    }

    /**
	 * @see com.patientis.framework.controls.table.ITableSelect#getSelectedObject()
	 */
    public ITableRow getSelectedObject() {
        if (isRowSelected()) {
            int originalRow = table.convertRowIndexToModel(table.getSelectedRow());
            if (table.getModel() instanceof ISTableModel) {
                ISTableModel adapter = (ISTableModel) table.getModel();
                return adapter.getModelAt(originalRow);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
	 * @see com.patientis.framework.controls.table.ITableSelect#getObjectAtPoint()
	 */
    public ITableRow getObjectAtPoint(Point p) {
        if (p != null && table.rowAtPoint(p) > -1) {
            int originalRow = table.convertRowIndexToModel(table.rowAtPoint(p));
            if (table.getModel() instanceof ISTableModel) {
                ISTableModel adapter = (ISTableModel) table.getModel();
                return adapter.getModelAt(originalRow);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
	 * @see com.patientis.framework.controls.table.ITableSelect#getObjectAtPoint()
	 */
    public IBaseModel getModelAtPoint(Point p) {
        Object o = getObjectAtPoint(p);
        if (o != null && o instanceof IBaseModel) {
            return (IBaseModel) o;
        } else {
            return null;
        }
    }
}
