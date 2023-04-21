package org.geoforge.guillcolg.popupmenu.prd;

import java.awt.event.ActionListener;
import java.util.logging.Logger;
import org.geoforge.guillcolg.menuitem.prd.MimTrsIdZoomTloAlwaysYesSelSrf;
import org.geoforge.guillc.tree.TreAbs;
import org.geoforge.lang.util.logging.FileHandlerLogger;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class PmuCtlCtrLeafTloDskSrfSec extends PmuCtlCtrLeafTloDskSrfAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(PmuCtlCtrLeafTloDskSrfSec.class.getName());

    static {
        PmuCtlCtrLeafTloDskSrfSec._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    public PmuCtlCtrLeafTloDskSrfSec(ActionListener alrController, String strUniqueId, TreAbs tree, ActionListener alrParentPanelMvc) throws Exception {
        super(alrController, strUniqueId, tree);
        super._mimZoom = new MimTrsIdZoomTloAlwaysYesSelSrf(alrParentPanelMvc, strUniqueId);
    }
}
