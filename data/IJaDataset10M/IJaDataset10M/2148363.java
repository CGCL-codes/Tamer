package org.geoforge.guillcogc.popupmenu;

import java.awt.event.ActionListener;
import java.util.logging.Logger;
import org.geoforge.guillc.tree.TreAbs;
import org.geoforge.guillcogc.menuitem.MimTrsIdSetLloWmsTerrainsGlobe;
import org.geoforge.lang.util.logging.FileHandlerLogger;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public abstract class PmuCtlCtrLeafLloRmtWmsTerAbs extends PmuCtlCtrLeafLloRmtWmsAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(PmuCtlCtrLeafLloRmtWmsTerAbs.class.getName());

    static {
        PmuCtlCtrLeafLloRmtWmsTerAbs._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    protected PmuCtlCtrLeafLloRmtWmsTerAbs(ActionListener alrController, String strUniqueId, String strIdParent, TreAbs tree) {
        super(strUniqueId, tree);
        super._mimSettings = new MimTrsIdSetLloWmsTerrainsGlobe(alrController, strUniqueId, strIdParent);
    }
}
