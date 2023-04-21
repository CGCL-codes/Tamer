package org.geoforge.guitlcolg.frame.secrun.toolbar;

import org.geoforge.guitlc.frame.secrun.toolbar.TbrWinViewSubDisplayAbs;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.logging.Logger;
import org.geoforge.guitlcolg.frame.secrun.button.BIcnHlpOfflineOnthisWinViewDisplayLogs;
import org.geoforge.io.finder.GfrFactoryIconShared;
import org.geoforge.lang.util.logging.FileHandlerLogger;

/**
 *
 * 
 */
public class TbrWinViewSubDisplayOlgLogsExp extends TbrWinViewSubDisplayAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(TbrWinViewSubDisplayOlgLogsExp.class.getName());

    static {
        TbrWinViewSubDisplayOlgLogsExp._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    public TbrWinViewSubDisplayOlgLogsExp(ActionListener alrParent, MouseListener mlrEffectsBorder) throws Exception {
        super(mlrEffectsBorder, alrParent);
        super._btnHelpThisFrame = new BIcnHlpOfflineOnthisWinViewDisplayLogs(GfrFactoryIconShared.s_getHelpOfflineHint(GfrFactoryIconShared.INT_SIZE_MEDIUM), GfrFactoryIconShared.INT_SIZE_MEDIUM, mlrEffectsBorder);
    }
}
