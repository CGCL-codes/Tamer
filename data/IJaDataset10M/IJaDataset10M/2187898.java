package org.geoforge.guillcolg.popupmenu.ecl;

import java.awt.event.ActionListener;
import java.util.logging.Logger;
import org.geoforge.guillcolg.menuitem.ecl.MimTrsIdZoomTloAlwaysYesSelAre;
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
public class PmuCtlCtrLeafTloDskAreSec extends PmuCtlCtrLeafTloDskAreAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(PmuCtlCtrLeafTloDskAreSec.class.getName());

    static {
        PmuCtlCtrLeafTloDskAreSec._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    public PmuCtlCtrLeafTloDskAreSec(ActionListener alrController, String strUniqueId, TreAbs tree, ActionListener alrParentPanelMvc) throws Exception {
        super(alrController, strUniqueId, tree);
        super._mimZoom = new MimTrsIdZoomTloAlwaysYesSelAre(alrParentPanelMvc, strUniqueId);
    }
}
