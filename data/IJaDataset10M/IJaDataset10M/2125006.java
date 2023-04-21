package org.geoforge.guitlcolg.frame.main.spcprtwork.panel;

import org.geoforge.guitlc.frame.main.spcprtwork.panel.PnlSpaceWelcomePrtWorkAbs;
import java.util.logging.Logger;
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
public class PnlSpaceWelcomePrtWorkOlg extends PnlSpaceWelcomePrtWorkAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(PnlSpaceWelcomePrtWorkOlg.class.getName());

    static {
        PnlSpaceWelcomePrtWorkOlg._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    public PnlSpaceWelcomePrtWorkOlg() {
        super();
        super._epn = GfrFndResHtmOlg.s_getWelcomeSpaceWork();
    }
}
