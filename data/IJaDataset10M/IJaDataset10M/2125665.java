package org.gudy.azureus2.ui.swt.views.tableitems.mytracker;

import org.gudy.azureus2.core3.tracker.host.*;
import org.gudy.azureus2.plugins.ui.tables.*;
import org.gudy.azureus2.ui.swt.views.table.utils.CoreTableColumn;

/**
 *
 * @author TuxPaper
 * @since 2.0.8.5
 */
public class SeedCountItem extends CoreTableColumn implements TableCellRefreshListener {

    /** Default Constructor */
    public SeedCountItem() {
        super("seeds", ALIGN_TRAIL, POSITION_LAST, 60, TableManager.TABLE_MYTRACKER);
        setRefreshInterval(INTERVAL_LIVE);
    }

    public void refresh(TableCell cell) {
        TRHostTorrent item = (TRHostTorrent) cell.getDataSource();
        long value = 0;
        if (item != null) {
            Long longObject = (Long) item.getData("GUI_SeedCount");
            if (longObject != null) value = longObject.longValue();
        }
        if (!cell.setSortValue(value) && cell.isValid()) {
            return;
        }
        cell.setText("" + value);
    }
}
