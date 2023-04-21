package org.gudy.azureus2.ui.swt.views.tableitems.peers;

import org.gudy.azureus2.core3.util.TimeFormatter;
import org.gudy.azureus2.core3.peer.PEPeer;
import org.gudy.azureus2.plugins.ui.tables.*;
import org.gudy.azureus2.ui.swt.views.table.utils.CoreTableColumn;

/**
 *
 * @author Olivier
 * @author TuxPaper (2004/Apr/19: modified to TableCellAdapter)
 */
public class TimeToSendPieceItem extends CoreTableColumn implements TableCellRefreshListener {

    /** Default Constructor */
    public TimeToSendPieceItem(String table_id) {
        super("timetosend", ALIGN_TRAIL, POSITION_INVISIBLE, 70, table_id);
        setRefreshInterval(INTERVAL_LIVE);
    }

    public void fillTableColumnInfo(TableColumnInfo info) {
        info.addCategories(new String[] { CAT_TIME });
    }

    public void refresh(TableCell cell) {
        PEPeer peer = (PEPeer) cell.getDataSource();
        long value = (peer == null) ? 0 : peer.getUploadHint();
        Comparable sortValue = cell.getSortValue();
        long oldValue = 0;
        if (sortValue instanceof Number) {
            oldValue = ((Number) sortValue).longValue();
        }
        if (!cell.setSortValue(value) && cell.isValid()) return;
        String text = TimeFormatter.format(value / 1000);
        if (oldValue > 0) {
            text += ", " + TimeFormatter.format(oldValue / 1000);
        }
        cell.setText(text);
    }
}
