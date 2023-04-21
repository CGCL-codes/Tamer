package org.gudy.azureus2.ui.swt.views.tableitems.files;

import org.gudy.azureus2.core3.disk.DiskManagerFileInfo;
import org.gudy.azureus2.plugins.ui.tables.*;
import org.gudy.azureus2.ui.swt.views.table.utils.CoreTableColumn;
import org.gudy.azureus2.core3.internat.MessageText;

/**
 *
 * @author Parg
 */
public class StorageTypeItem extends CoreTableColumn implements TableCellRefreshListener {

    /** Default Constructor */
    public StorageTypeItem() {
        super("storagetype", ALIGN_LEAD, POSITION_INVISIBLE, 70, TableManager.TABLE_TORRENT_FILES);
        setRefreshInterval(INTERVAL_LIVE);
    }

    public void fillTableColumnInfo(TableColumnInfo info) {
        info.addCategories(new String[] { CAT_CONTENT });
        info.setProficiency(TableColumnInfo.PROFICIENCY_INTERMEDIATE);
    }

    public void refresh(TableCell cell) {
        DiskManagerFileInfo fileInfo = (DiskManagerFileInfo) cell.getDataSource();
        String tmp;
        if (fileInfo == null) {
            tmp = "";
        } else {
            int st = fileInfo.getStorageType();
            if (st == DiskManagerFileInfo.ST_LINEAR) {
                tmp = MessageText.getString("FileItem.storage.linear");
            } else if (st == DiskManagerFileInfo.ST_COMPACT) {
                tmp = MessageText.getString("FileItem.storage.compact");
            } else {
                tmp = MessageText.getString("FileItem.storage.reorder");
            }
        }
        cell.setText(tmp);
    }
}
