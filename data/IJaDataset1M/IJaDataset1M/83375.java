package org.gudy.azureus2.ui.swt.views.tableitems.mytorrents;

import org.gudy.azureus2.core3.download.DownloadManager;
import org.gudy.azureus2.core3.peer.PEPeerManager;
import org.gudy.azureus2.ui.swt.views.table.utils.CoreTableColumn;
import org.gudy.azureus2.plugins.download.Download;
import org.gudy.azureus2.plugins.ui.tables.TableCell;
import org.gudy.azureus2.plugins.ui.tables.TableCellRefreshListener;
import org.gudy.azureus2.plugins.ui.tables.TableColumnInfo;

/** Availability/"Seeing Copies" Column
 *
 * @author TuxPaper
 */
public class AvailabilityItem extends CoreTableColumn implements TableCellRefreshListener {

    public static final Class DATASOURCE_TYPE = Download.class;

    private static final String zeros = "0000";

    private static final int numZeros = zeros.length();

    public static final String COLUMN_ID = "availability";

    private int iTimesBy;

    /** Default Constructor */
    public AvailabilityItem(String sTableID) {
        super(DATASOURCE_TYPE, COLUMN_ID, ALIGN_TRAIL, 50, sTableID);
        setRefreshInterval(INTERVAL_LIVE);
        setMinWidthAuto(true);
        iTimesBy = 1;
        for (int i = 1; i < numZeros; i++) iTimesBy *= 10;
    }

    public void fillTableColumnInfo(TableColumnInfo info) {
        info.addCategories(new String[] { CAT_SWARM });
        info.setProficiency(TableColumnInfo.PROFICIENCY_INTERMEDIATE);
    }

    public void refresh(TableCell cell) {
        String sText = "";
        DownloadManager dm = (DownloadManager) cell.getDataSource();
        if (dm == null) return;
        PEPeerManager pm = dm.getPeerManager();
        if (pm != null) {
            float f = pm.getMinAvailability();
            if (!cell.setSortValue((long) (f * 1000)) && cell.isValid()) return;
            sText = String.valueOf((int) (f * iTimesBy));
            if (numZeros - sText.length() > 0) sText = zeros.substring(0, numZeros - sText.length()) + sText;
            sText = sText.substring(0, sText.length() - numZeros + 1) + "." + sText.substring(sText.length() - numZeros + 1);
        } else {
            cell.setSortValue(0);
        }
        cell.setText(sText);
    }
}
