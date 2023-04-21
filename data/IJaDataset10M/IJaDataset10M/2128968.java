package cz.muni.fi.rum.gui.component;

import cz.muni.fi.rum.sender.Factory;
import cz.muni.fi.rum.sender.command.RapRequest;
import java.util.Collections;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author pmikulasek
 */
public class HistoryTableModel extends AbstractTableModel {

    private List<RapRequest> newRequests;

    public HistoryTableModel() {
        this.newRequests = Collections.EMPTY_LIST;
    }

    public void setUnsentRequests(List<RapRequest> requests) {
        if (requests == null) {
            this.newRequests = Collections.EMPTY_LIST;
        } else {
            this.newRequests = requests;
        }
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return Factory.getSenderManager().getAllExecutedRequests().size() + newRequests.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        int size = Factory.getSenderManager().getAllExecutedRequests().size();
        if (rowIndex < size) {
            return "zatim neni implementovana historie";
        } else if (rowIndex >= size) {
            return newRequests.get(rowIndex - size);
        } else {
            throw new IllegalArgumentException("row index: " + rowIndex);
        }
    }

    @Override
    public String getColumnName(int column) {
        return "RAP_Requests";
    }
}
