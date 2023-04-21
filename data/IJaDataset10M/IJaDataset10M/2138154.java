package org.geoforge.guitlcolg.frame.secrun.menuitem;

import org.geoforge.guillc.menuitem.MimHlpOfflineOnthisAbs;
import org.geoforge.guihlpolg.property.oxp.PrpMgrPrivateHelpClass2idOxp;

/**
 * 
 *
 */
public class MimHlpOfflineOnthisWinViewLogs extends MimHlpOfflineOnthisAbs {

    public static final String STR_TEXT = "well logs viewer";

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public MimHlpOfflineOnthisWinViewLogs() throws Exception {
        super(MimHlpOfflineOnthisWinViewLogs.STR_TEXT);
        super.setPrp(PrpMgrPrivateHelpClass2idOxp.getPrpHlpWinViewOlgLogsExp());
    }
}
