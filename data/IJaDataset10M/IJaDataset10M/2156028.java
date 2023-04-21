package org.geoforge.guitlcolg.frame.secrun.panel;

import java.util.logging.Logger;
import org.geoforge.guillc.treenode.GfrNodCtrAbs;
import org.geoforge.guillc.treenode.IHandlerCheckableNode;
import org.geoforge.guitlc.frame.secrun.panel.PnlContentsWindowViewerAbs;
import org.geoforge.lang.util.logging.FileHandlerLogger;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public abstract class PnlContentsWindowViewerOlgAbs extends PnlContentsWindowViewerAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(PnlContentsWindowViewerOlgAbs.class.getName());

    static {
        PnlContentsWindowViewerOlgAbs._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    protected PnlContentsWindowViewerOlgAbs(boolean blnIsPropertiesPanel) {
        super(blnIsPropertiesPanel);
    }

    protected void _invokedLaterUpdateCheckUncheckTloOlg(IHandlerCheckableNode nodCheck) {
        GfrNodCtrAbs nod = (GfrNodCtrAbs) nodCheck;
        ((PnlControlsViewerOlgAbs) this._pnlControls_).updateCheckUncheckTloOlg((GfrNodCtrAbs) nod.getRoot());
    }

    protected void _invokedLaterUpdateCheckUncheckTloOgc(IHandlerCheckableNode nodCheck) {
        GfrNodCtrAbs nod = (GfrNodCtrAbs) nodCheck;
        ((PnlControlsViewerOlgGlobeAbs) this._pnlControls_).updateCheckUncheckOgc((GfrNodCtrAbs) nod.getRoot());
    }

    protected void _invokedLaterUpdateCheckUncheckTloOlg() {
        boolean blnAllowedAllCheck = ((PnlControlsViewerOlgAbs) this._pnlControls_).isAllowedCheckAllOlg();
        boolean blnAllowedAllUncheck = ((PnlControlsViewerOlgAbs) this._pnlControls_).isAllowedUncheckAllOlg();
        ((PnlControlsViewerOlgAbs) this._pnlControls_).updateCheckUncheckOlg(blnAllowedAllCheck, blnAllowedAllUncheck);
    }
}
