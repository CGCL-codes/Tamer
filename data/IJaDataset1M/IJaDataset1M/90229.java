package org.geoforge.guillcolg.popupmenu.ecl;

import java.awt.event.ActionListener;
import java.util.logging.Logger;
import org.geoforge.guillc.menuitem.MimTrsIdZoomTloAlwaysYesMan;
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
public class PmuCtlCtrLeafTloDskPthMan extends PmuCtlCtrLeafTloDskPthAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(PmuCtlCtrLeafTloDskPthMan.class.getName());

    static {
        PmuCtlCtrLeafTloDskPthMan._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    public PmuCtlCtrLeafTloDskPthMan(ActionListener alrController, String strUniqueId, TreAbs tree, ActionListener alrParentPanelMvc) {
        super(alrController, strUniqueId, tree);
        super._mimZoom = new MimTrsIdZoomTloAlwaysYesMan(alrParentPanelMvc, strUniqueId);
    }
}
