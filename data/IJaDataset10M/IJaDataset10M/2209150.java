package org.geoforge.guitlcgsi.frame.secrun.panel;

import org.geoforge.guitlc.frame.secrun.panel.PnlControlChkSecAbs;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.logging.Logger;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionListener;
import org.geoforge.guillc.frame.FrmGfrAbs;
import org.geoforge.guillc.optionpane.GfrOptionPaneAbs;
import org.geoforge.guillc.panel.PnlStatusBarSec;
import org.geoforge.guitlcogc.panel.ctr.scrollpane.ScrControlChkSecSrvOgc;
import org.geoforge.guitlcogc.panel.ctr.toolbar.TbrSubControlChkSrvOgcSec;
import org.geoforge.lang.util.logging.FileHandlerLogger;

/**
 *
 * @author bantchao
 */
public class PnlControlSecViewerGlobeWms extends PnlControlChkSecAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(PnlControlSecViewerGlobeWms.class.getName());

    static {
        PnlControlSecViewerGlobeWms._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    public PnlControlSecViewerGlobeWms(ActionListener alrParentPanelMvc, MouseListener alrParentCad, PnlStatusBarSec pnlStatusBar, String strIdViewer) {
        super(pnlStatusBar);
        try {
            super._tbr_ = new TbrSubControlChkSrvOgcSec(alrParentPanelMvc, (ActionListener) this, (MouseListener) super.getBorder());
        } catch (Exception exc) {
            exc.printStackTrace();
            String str = exc.getMessage();
            PnlControlSecViewerGlobeWms._LOGGER_.severe(str);
            GfrOptionPaneAbs.s_showDialogError(FrmGfrAbs.s_getFrameOwner(super._pnlStatusBar), str);
            System.exit(1);
        }
        super._scr_ = new ScrControlChkSecSrvOgc(alrParentCad, (TreeSelectionListener) super._tbr_, (TreeExpansionListener) super._tbr_, (TreeModelListener) super._tbr_, alrParentPanelMvc, super._pnlStatusBar, strIdViewer);
    }

    @Override
    public void doJobQueued(ActionListener alrControllerSpcPrj) throws Exception {
        try {
            open(alrControllerSpcPrj);
        } catch (Exception exc) {
            throw exc;
        }
    }
}
