package phex.gui.tabs.search.monitor;

import java.awt.EventQueue;
import javax.swing.table.AbstractTableModel;
import org.bushe.swing.event.annotation.EventTopicSubscriber;
import phex.common.Phex;
import phex.event.PhexEventTopics;
import phex.host.Host;
import phex.msg.MsgHeader;
import phex.query.QueryHistoryMonitor;
import phex.query.QueryHistoryMonitor.HistoryEntry;
import phex.utils.Localizer;

public class QueryHistoryMonitorTableModel extends AbstractTableModel {

    private static final int numColRoutedFrom = 0;

    private static final int numColSearchText = 1;

    private static final int numColHopsTtl = 2;

    private static final String[] tableColumns = { Localizer.getString("SearchMonitorTab_RoutedFrom"), Localizer.getString("SearchMonitorTab_SearchText"), Localizer.getString("SearchMonitorTab_HopsTtl") };

    private QueryHistoryMonitor history;

    public QueryHistoryMonitorTableModel(QueryHistoryMonitor history) {
        this.history = history;
        Phex.getEventService().processAnnotations(this);
    }

    @Override
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
        HistoryEntry histEntry = history.getSearchQueryAt(row);
        if (histEntry == null) {
            fireTableRowsDeleted(row, row);
            return null;
        }
        MsgHeader header = histEntry.getQueryMsg().getHeader();
        switch(col) {
            case numColRoutedFrom:
                Host fromHost = histEntry.getSourceHost();
                if (fromHost == null) {
                    return "<Unknown>";
                }
                return fromHost.getHostAddress().getFullHostName();
            case numColSearchText:
                return histEntry.getQueryMsg().getSearchString();
            case numColHopsTtl:
                int hops = header.getHopsTaken();
                int ttl = hops + header.getTTL();
                return hops + " / " + ttl;
        }
        return "";
    }

    @Override
    public Class<?> getColumnClass(int col) {
        return String.class;
    }

    @EventTopicSubscriber(topic = PhexEventTopics.Query_Monitor)
    public void onQueryMonitorEvent(String topic, Object event) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                fireTableDataChanged();
            }
        });
    }
}
