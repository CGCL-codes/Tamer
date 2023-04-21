package phex.gui.models;

import javax.swing.table.*;
import phex.host.*;
import phex.msg.*;
import phex.query.*;

public class SearchHistoryTableModel extends AbstractTableModel {

    private static final int numColRoutedFrom = 0;

    private static final int numColSearchText = 1;

    private static final int numColHopsTtl = 2;

    private static String[] tableColumns = { "Routed From", "Search Text", "Hops / TTL" };

    private QueryHistoryMonitor history;

    public SearchHistoryTableModel() {
        history = QueryManager.getInstance().getQueryHistoryMonitor();
    }

    public String getColumnName(int col) {
        return tableColumns[col];
    }

    public int getColumnCount() {
        return tableColumns.length;
    }

    public int getRowCount() {
        return history.getHistorySize();
    }

    public Object getValueAt(int row, int col) {
        MsgQuery searchQuery = history.getSearchQueryAt(row);
        if (searchQuery == null) {
            fireTableRowsDeleted(row, row);
            return null;
        }
        switch(col) {
            case numColRoutedFrom:
                Host fromHost = searchQuery.getHeader().getFromHost();
                if (fromHost == null) {
                    return "<Unknown>";
                }
                return fromHost.getHostAddress().getHostName() + ":" + fromHost.getHostAddress().getPort();
            case numColSearchText:
                return searchQuery.getSearchString();
            case numColHopsTtl:
                int hops = searchQuery.getHeader().getHopsTaken();
                int ttl = hops + searchQuery.getHeader().getTTL();
                return hops + " / " + ttl;
        }
        return "";
    }

    public Class getColumnClass(int col) {
        return String.class;
    }
}
