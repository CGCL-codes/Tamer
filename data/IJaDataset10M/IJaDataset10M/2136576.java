package org.geoforge.guitlcolg.frame.secrun;

import org.geoforge.guitlcolg.frame.secrun.menubar.MbrWindowViewerOlgGlobeExp;
import org.geoforge.guitlcolg.frame.secrun.toolbar.TbrWinViewMainOlgGlobeExp;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;
import org.geoforge.guihlp.property.PrpMgrPrivateHelpClass2idGfr;
import org.geoforge.guitlcolg.frame.secrun.panel.PnlContentsWindowViewerOlgGlobeExp;
import org.geoforge.guillc.panel.PnlStatusBarSec;
import org.geoforge.lang.util.logging.FileHandlerLogger;
import org.geoforge.wrpbasprsdsp.state.multiple.run.GfrWrpObjSttMltRun;
import org.geoforge.wrpbasprsdsp.state.singleton.selecttlo.GfrWrpObjSttSngSelTlo;
import org.geoforge.wrpbasprsdsp.viewer.GfrWrpDspViewer;
import org.geoforge.wrpbasprssynolg.oxp.GfrWrpBasSynTopS2ds;
import org.geoforge.wrpbasprssynolg.oxp.GfrWrpBasSynTopS3ds;
import org.geoforge.wrpbasprssynolg.oxp.GfrWrpBasSynTopWlls;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class FrmGfrWindowViewerGlobeOlgExp extends FrmGfrWindowViewerGlobeOlgAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(FrmGfrWindowViewerGlobeOlgExp.class.getName());

    static {
        FrmGfrWindowViewerGlobeOlgExp._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    public FrmGfrWindowViewerGlobeOlgExp(ActionListener alrProject, WindowListener wlrRun, ActionListener alrRun, String strId) throws Exception {
        this(alrProject, wlrRun, alrRun, strId, GfrWrpDspViewer.s_getInstance().getName(strId));
        _saveDisplay();
    }

    public FrmGfrWindowViewerGlobeOlgExp(ActionListener alrProject, WindowListener wlrRun, ActionListener alrRun, String strUniqueId, String strUniqueName) throws Exception {
        super(alrProject, wlrRun, alrRun, strUniqueId, strUniqueName);
        super._mbr = new MbrWindowViewerOlgGlobeExp(alrRun, (ActionListener) this);
        super._tbr = new TbrWinViewMainOlgGlobeExp(alrRun, (MouseListener) super._pnlContentPane_.getBorder());
        super._pnlContents = new PnlContentsWindowViewerOlgGlobeExp((PropertyChangeListener) this, (PnlStatusBarSec) super._pnlStatusBar, alrRun, super.getUniqueName(), strUniqueId);
        super._setPrpWHAT_S_THAT(PrpMgrPrivateHelpClass2idGfr.getPrpHlpWinViewGlobe());
    }

    @Override
    public void loadTransient() throws Exception {
        super.loadTransient();
    }

    @Override
    public void releaseTransient() throws Exception {
        super.releaseTransient();
    }

    @Override
    protected void _saveDisplay() throws Exception {
        super._saveDisplay();
        String[] strs = null;
        strs = GfrWrpBasSynTopWlls.getInstance().getIds();
        for (int i = 0; i < strs.length; i++) if (GfrWrpObjSttSngSelTlo.getInstance().isEnabled(strs[i])) GfrWrpObjSttMltRun.getInstance().save(this.getUniqueId(), strs[i]);
        strs = GfrWrpBasSynTopS3ds.getInstance().getIds();
        for (int i = 0; i < strs.length; i++) if (GfrWrpObjSttSngSelTlo.getInstance().isEnabled(strs[i])) GfrWrpObjSttMltRun.getInstance().save(this.getUniqueId(), strs[i]);
        strs = GfrWrpBasSynTopS2ds.getInstance().getIds();
        for (int i = 0; i < strs.length; i++) if (GfrWrpObjSttSngSelTlo.getInstance().isEnabled(strs[i])) GfrWrpObjSttMltRun.getInstance().save(this.getUniqueId(), strs[i]);
    }
}
