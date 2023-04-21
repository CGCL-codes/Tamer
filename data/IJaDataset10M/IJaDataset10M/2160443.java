package com.patientis.framework.controls.table;

import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import com.patientis.framework.concurrency.SwingWorker;
import com.patientis.framework.locale.ColorUtil;
import com.patientis.framework.logging.Log;
import com.patientis.framework.utility.SwingUtil;
import com.patientis.model.common.IBaseModel;

/**
 * @author gcaulton
 *
 */
public abstract class DefaultCustomTableModel {

    /**
	 * 
	 */
    private ISTable fixedTable = null;

    /**
	 * @return the tableCellRenderer
	 */
    public IRenderTableCell getTableCellRenderer() {
        return new DefaultRenderTableCell() {

            @Override
            public boolean isCustomRender() {
                return false;
            }
        };
    }

    /**
	 * 
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
    public abstract Object getGridValue(int rowIndex, int columnIndex);

    /**
	 * 
	 * @return
	 */
    public TableModel getGridTableModel() {
        return new AbstractTableModel() {

            @Override
            public int getColumnCount() {
                return getGridColumns().size();
            }

            @Override
            public int getRowCount() {
                return getGridRows().size();
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return getGridValue(rowIndex, columnIndex);
            }
        };
    }

    /**
	 * 
	 * @return
	 */
    public abstract List<ITableColumn> getGridColumns();

    /**
	 * 
	 * @return
	 */
    public abstract List<Object> getGridRows();

    /**
	 * 
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
    public Object getFixedValue(int rowIndex, int columnIndex) {
        return null;
    }

    /**
	 * 
	 * @return
	 */
    public TableModel getLeftFixedTableModel() {
        return new AbstractTableModel() {

            @Override
            public int getColumnCount() {
                return 1;
            }

            @Override
            public int getRowCount() {
                return getGridRows().size();
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return getFixedValue(rowIndex, columnIndex);
            }
        };
    }

    /**
	 * 
	 * @return
	 */
    public ITableModel getITableModel() {
        return new ITableModel() {

            @Override
            public TableModel getTableModel() {
                return getGridTableModel();
            }

            @Override
            public List<ITableColumn> getColumns() {
                return getGridColumns();
            }
        };
    }

    /**
	 * 
	 * @return
	 */
    public ISTable getFixedTable() {
        return getFixedTable(false);
    }

    /**
	 * 
	 * @return
	 */
    public ISTable getFixedTable(boolean rowSelectionAllowed) {
        if (fixedTable == null) {
            fixedTable = new ISTable();
            fixedTable.setModel(getLeftFixedTableModel());
            try {
                fixedTable.createColumnsAfterModel(getFixedTableColumns(), false);
            } catch (Exception ex) {
                Log.exception(ex);
            }
            fixedTable.setBackground(ColorUtil.getTableDefaultBackground());
            fixedTable.setRowSelectionAllowed(rowSelectionAllowed);
        }
        return fixedTable;
    }

    /**
	 * 
	 */
    public abstract void refresh(List<IBaseModel> orderInstances) throws Exception;

    /**
	 * 
	 */
    public abstract List<ITableColumn> getFixedTableColumns() throws Exception;

    /**
	 * @param fixedTable the fixedTable to set
	 */
    public void setFixedTable(ISTable fixedTable) {
        this.fixedTable = fixedTable;
    }
}
