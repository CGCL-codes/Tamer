package org.geoforge.guitlcolg.frame.main.prsrun.panel;

import java.util.logging.Logger;
import org.geoforge.guitlc.frame.main.panel.PnlSpaceWelcomeAbs;
import org.geoforge.ioolg.finder.GfrFndResHtmOlg;
import org.geoforge.lang.util.logging.FileHandlerLogger;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class PnlSpaceWelcomeProjectRunOlg extends PnlSpaceWelcomeAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(PnlSpaceWelcomeProjectRunOlg.class.getName());

    static {
        PnlSpaceWelcomeProjectRunOlg._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    public PnlSpaceWelcomeProjectRunOlg() {
        super();
        super._epn = GfrFndResHtmOlg.s_getWelcomeSpaceProjectRun();
    }

    @Override
    public void loadTransient() throws Exception {
        super.loadTransient();
    }
}
