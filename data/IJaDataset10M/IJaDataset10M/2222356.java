package org.geoforge.guillcogc.popupmenu;

import java.awt.event.ActionListener;
import java.util.logging.Logger;
import org.geoforge.guillcogc.menuitem.MimTransientDisplayWmsNoTersMan;
import org.geoforge.guillcogc.menuitem.MimTransientDisplayWmsYesTersMan;
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
public class PmuCtlCtrFolderSetRmtWmsTersMan extends PmuCtlCtrFolderSetRmtWmsTersAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(PmuCtlCtrFolderSetRmtWmsTersMan.class.getName());

    static {
        PmuCtlCtrFolderSetRmtWmsTersMan._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    public PmuCtlCtrFolderSetRmtWmsTersMan(ActionListener alrParentNode, ActionListener alrParentCpd, String strUniqueId, TreAbs tree) throws Exception {
        super(tree);
        super._mimDisplayYes = new MimTransientDisplayWmsYesTersMan(alrParentNode, alrParentCpd, strUniqueId);
        super._mimDisplayNo = new MimTransientDisplayWmsNoTersMan(alrParentNode, alrParentCpd, strUniqueId);
    }
}
