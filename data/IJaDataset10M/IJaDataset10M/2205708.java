package com.zara.store.client.view.swing.table;

import javax.swing.table.DefaultTableModel;

public abstract class StoreTableModel extends DefaultTableModel {

    private static final long serialVersionUID = 1L;

    public void clear() {
        for (int i = getRowCount() - 1; i >= 0; i--) {
            removeRow(i);
        }
    }
}
