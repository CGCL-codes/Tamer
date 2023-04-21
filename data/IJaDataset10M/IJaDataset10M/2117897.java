package org.modelibra.swing.widget;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public abstract class ModelibraTable extends JTable {

    public ModelibraTable(AbstractTableModel modelibraTableModel) {
        super(modelibraTableModel);
    }

    public void setSelectedRow(int ix) {
        if (getRowCount() <= 0) return;
        if (ix < 0) {
            ix = getSelectedRow();
        }
        if ((ix >= 0) && (ix <= getRowCount() - 1)) {
            setRowSelectionInterval(ix, ix);
            scrollRectToVisible(getCellRect(ix, 0, true));
        }
    }
}
