package org.gruposp2p.dnie.client.ui.widget.dynatable;

public interface DynaTableDataProvider {

    void updateRowData(int startRow, int maxRows, RowDataAcceptor acceptor);
}
