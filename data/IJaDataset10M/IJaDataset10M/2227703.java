package org.geoforge.guitlcgsi.frame.secrun.thread;

import java.util.logging.Logger;
import org.geoforge.guillc.optionpane.GfrOptionPaneAbs;
import org.geoforge.guillcgsi.wwd.panel.PnlDspWwdSecRunGsiAbs;
import org.geoforge.guitlcgsi.frame.secrun.panel.PnlControlsViewerGsiGlobeAbs;
import org.geoforge.lang.util.logging.FileHandlerLogger;

/**
 *
 * @author bantchao
 */
public abstract class ThreadUpdateDisplayGlobeAbs extends Thread {

    private static final Logger _LOGGER_ = Logger.getLogger(ThreadUpdateDisplayGlobeAbs.class.getName());

    static {
        ThreadUpdateDisplayGlobeAbs._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    protected PnlControlsViewerGsiGlobeAbs _pnlControls = null;

    protected PnlDspWwdSecRunGsiAbs _pnlDisplay = null;

    protected ThreadUpdateDisplayGlobeAbs(String strName, PnlControlsViewerGsiGlobeAbs pnlControls, PnlDspWwdSecRunGsiAbs pnlDisplay) {
        super(strName);
        this._pnlControls = pnlControls;
        this._pnlDisplay = pnlDisplay;
        super.setDaemon(true);
        super.setPriority(Thread.MIN_PRIORITY);
    }

    @Override
    public void run() {
        try {
            _doJob();
        } catch (Exception exc) {
            exc.printStackTrace();
            String str = exc.getMessage();
            ThreadUpdateDisplayGlobeAbs._LOGGER_.severe(str);
            GfrOptionPaneAbs.s_showDialogError(null, str);
            System.exit(1);
        }
    }

    protected void _doJob() throws Exception {
        ThreadUpdateDisplayGlobeAbs._LOGGER_.info("in progress ...");
    }
}
