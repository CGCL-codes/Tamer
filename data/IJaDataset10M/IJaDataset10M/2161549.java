package org.geoforge.guillcolg.popupmenu.exp;

import java.awt.event.ActionListener;
import java.util.logging.Logger;
import org.geoforge.guillcolg.menu.exp.MenWwdParentTloDskS2dSel;
import org.geoforge.lang.util.logging.FileHandlerLogger;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class PmuCtlDspWwdMloPrfSel extends PmuCtlDspWwdMloPrfAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(PmuCtlDspWwdMloPrfSel.class.getName());

    static {
        PmuCtlDspWwdMloPrfSel._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    public PmuCtlDspWwdMloPrfSel(ActionListener alrController, String strUniqueId, String strIdParent) throws Exception {
        super(alrController, strUniqueId, strIdParent);
        super._menParent = new MenWwdParentTloDskS2dSel(alrController, strIdParent);
    }
}
