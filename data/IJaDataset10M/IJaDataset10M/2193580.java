package org.geoforge.guillcolg.popupmenu.exp;

import java.awt.event.ActionListener;
import java.util.logging.Logger;
import org.geoforge.guillcolg.menuitem.exp.MimTrsIdZoomTloAlwaysYesSelS3d;
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
public class PmuCtlCtrLeafTloDskS3dSel extends PmuCtlCtrLeafTloDskS3dAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(PmuCtlCtrLeafTloDskS3dSel.class.getName());

    static {
        PmuCtlCtrLeafTloDskS3dSel._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    public PmuCtlCtrLeafTloDskS3dSel(ActionListener alrController, String strUniqueId, TreAbs tree, ActionListener alrParentPanelMvc) throws Exception {
        super(alrController, strUniqueId, tree);
        super._mimZoom = new MimTrsIdZoomTloAlwaysYesSelS3d(alrParentPanelMvc, strUniqueId);
    }
}
