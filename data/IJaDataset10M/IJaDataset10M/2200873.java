package org.geoforge.guillcogc.wwd.button;

import java.awt.event.MouseListener;
import org.geoforge.guillc.button.BIcnHlpOfflineOnthisAbs;
import org.geoforge.guihlpogc.property.PrpMgrPrivateHelpClass2idOgc;
import org.geoforge.io.finder.GfrFactoryIconShared;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class BIcnHlpOfflineOnthisManageWmsDsp extends BIcnHlpOfflineOnthisAbs {

    private static final long serialVersionUID = 1L;

    public BIcnHlpOfflineOnthisManageWmsDsp(MouseListener mlr) throws Exception {
        super(GfrFactoryIconShared.s_getHelpOfflineHint(GfrFactoryIconShared.INT_SIZE_SMALL), GfrFactoryIconShared.INT_SIZE_SMALL, mlr);
        super._setPropertyValueHelp(PrpMgrPrivateHelpClass2idOgc.getPrpCntManageDataWmsDsp());
    }
}
