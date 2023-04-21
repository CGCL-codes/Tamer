package org.geoforge.appolg.actioncontroller.space.opr;

import java.io.File;
import java.util.logging.Logger;
import org.geoforge.app.actioncontroller.AcrAcrAbs;
import org.geoforge.app.actioncontroller.space.io.file.GfrFleSpcWork;
import org.geoforge.appolg.actioncontroller.space.AcrSpcPrtRootOgcOlgAbs;
import org.geoforge.guillc.AppAbs;
import org.geoforge.guillc.frame.FrmGfrAbs;
import org.geoforge.guillc.optionpane.GfrOptionPaneAbs;
import org.geoforge.guillc.panel.PnlStatusBarMain;
import org.geoforge.guitlc.frame.main.spcprtroot.action.ActionOpenChildFromListSpaceWork;
import org.geoforge.guitlc.dialog.edit.space.GfrDlgAddSpaceworkCopy;
import org.geoforge.guitlc.dialog.edit.space.GfrDlgAddSpaceworkNew;
import org.geoforge.guitlc.dialog.edit.space.GfrEditSpaceWorkPath;
import org.geoforge.guitlc.dialog.tablespace.action.ActionChildCloneWorkspace;
import org.geoforge.guitlc.dialog.tablespace.action.ActionChildDeleteWorkspace;
import org.geoforge.guitlc.dialog.tablespace.action.ActionChildMoveWorkspace;
import org.geoforge.guitlc.dialog.tablespace.action.ActionDeleteWorkspaces;
import org.geoforge.io.file.GfrFile;
import org.geoforge.lang.util.logging.FileHandlerLogger;
import org.geoforge.wrpbasspcsynolg.root.opr.GfrWrpBasSynSpcRootOlgOpr;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class AcrSpcPrtRootOgcOlgEqp extends AcrSpcPrtRootOgcOlgAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(AcrSpcPrtRootOgcOlgEqp.class.getName());

    static {
        AcrSpcPrtRootOgcOlgEqp._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    public AcrSpcPrtRootOgcOlgEqp(AppAbs app) throws Exception {
        super(app);
        super._acrChild = new AcrSpcPrtWrkOgcOlgEqp(this._app_, (AcrAcrAbs) this);
    }

    @Override
    public void open() throws Exception {
        GfrWrpBasSynSpcRootOlgOpr.getInstance().open();
        super.open();
    }

    @Override
    public void updateContentsDialogManageChildren() throws Exception {
        super._updateContentsDialogManageChildren(GfrWrpBasSynSpcRootOlgOpr.getInstance().getSortedPathsAbsolute());
    }

    @Override
    public void deleteChild() throws Exception {
        String strTitleLong = "Confirm";
        String strBody = "Are you sure you want to delete this workspace?";
        if (!GfrOptionPaneAbs.s_showDialogConfirm(FrmGfrAbs.s_getFrameOwner(PnlStatusBarMain.s_getInstance()), strTitleLong, strBody)) return;
        String strPathSource = ActionChildDeleteWorkspace.s_getInstance().getChild();
        if (!GfrFleSpcWork.s_delete(new File(strPathSource))) {
            String str = "! GfrFleSpcWork.s_delete(new File(strPathSource))";
            AcrSpcPrtRootOgcOlgEqp._LOGGER_.warning(str);
            GfrOptionPaneAbs.s_showDialogError(super._app_.getFrame(), str);
            return;
        }
        GfrWrpBasSynSpcRootOlgOpr.getInstance().delete(strPathSource);
        updateStateActions();
        updateContentsDialogManageChildren();
    }

    @Override
    public void manageChildren() throws Exception {
        super._manageChildren(GfrWrpBasSynSpcRootOlgOpr.getInstance().getSortedPathsAbsolute());
    }

    @Override
    public String newChild() throws Exception {
        String[] strsChildrenExisting = GfrWrpBasSynSpcRootOlgOpr.getInstance().getSortedPathsAbsolute();
        GfrDlgAddSpaceworkNew dlg = new GfrDlgAddSpaceworkNew(super._app_.getFrame(), strsChildrenExisting);
        if (!dlg.init()) {
            String str = "! dlg.init()";
            AcrSpcPrtRootOgcOlgEqp._LOGGER_.severe(str);
            throw new Exception(str);
        }
        dlg.setVisible(true);
        boolean blnCancelled = dlg.getCancelled();
        String strValue = dlg.getValue();
        dlg.setVisible(false);
        dlg.destroy();
        if (blnCancelled || strValue.length() < 1) return null;
        GfrWrpBasSynSpcRootOlgOpr.getInstance().newChild(strValue);
        updateStateActions();
        updateContentsDialogManageChildren();
        return strValue;
    }

    @Override
    public void updateStateActions() throws Exception {
        String[] strsChildrenExisting = GfrWrpBasSynSpcRootOlgOpr.getInstance().getSortedPathsAbsolute();
        boolean blnHasWorkspace = false;
        if (strsChildrenExisting != null && strsChildrenExisting.length > 0) blnHasWorkspace = true;
        ActionOpenChildFromListSpaceWork.s_getInstance().setEnabled(blnHasWorkspace);
        ActionDeleteWorkspaces.s_getInstance().setEnabled(blnHasWorkspace);
    }

    @Override
    public void openChildFromList() throws Exception {
        super._openChildFromList(GfrWrpBasSynSpcRootOlgOpr.getInstance().getSortedPathsAbsolute());
    }

    @Override
    public void cloneChild() throws Exception {
        String strPathSource = ActionChildCloneWorkspace.s_getInstance().getChild();
        if (strPathSource == null) {
            String str = "strPathSource == null";
            AcrSpcPrtRootOgcOlgEqp._LOGGER_.severe(str);
            throw new Exception(str);
        }
        String[] strsChildrenExisting = GfrWrpBasSynSpcRootOlgOpr.getInstance().getSortedPathsAbsolute();
        GfrDlgAddSpaceworkCopy dlg = new GfrDlgAddSpaceworkCopy(super._app_.getFrame(), strsChildrenExisting, strPathSource);
        if (!dlg.init()) {
            String str = "! dlg.init()";
            AcrSpcPrtRootOgcOlgEqp._LOGGER_.severe(str);
            throw new Exception(str);
        }
        dlg.setVisible(true);
        boolean blnCancelled = dlg.getCancelled();
        String strValue = dlg.getValue();
        dlg.destroy();
        if (blnCancelled || strValue.length() < 1) return;
        File fleSource = new File(strPathSource);
        File fleTarget = new File(strValue);
        GfrFile.s_copyDirectory(fleSource, fleTarget);
        GfrWrpBasSynSpcRootOlgOpr.getInstance().save(strValue);
        updateContentsDialogManageChildren();
    }

    @Override
    public void deleteChildren() throws Exception {
        String strTitleLong = "Confirm";
        String strBody = "Are you sure you want to delete all workspaces?";
        if (!GfrOptionPaneAbs.s_showDialogConfirm(FrmGfrAbs.s_getFrameOwner(PnlStatusBarMain.s_getInstance()), strTitleLong, strBody)) return;
        String[] strsChildrenExisting = GfrWrpBasSynSpcRootOlgOpr.getInstance().getSortedPathsAbsolute();
        for (int i = 0; i < strsChildrenExisting.length; i++) {
            File fleCur = new File(strsChildrenExisting[i]);
            if (!GfrFleSpcWork.s_delete(fleCur)) {
                String str = "! GfrFleSpcWorkOlg.s_delete(fleCur): " + fleCur.getAbsolutePath();
                AcrSpcPrtRootOgcOlgEqp._LOGGER_.warning(str);
                GfrOptionPaneAbs.s_showDialogError(super._app_.getFrame(), str);
                return;
            }
        }
        GfrWrpBasSynSpcRootOlgOpr.getInstance().deleteAll();
        updateStateActions();
        updateContentsDialogManageChildren();
    }

    @Override
    public void moveChild() throws Exception {
        String strPathSource = ActionChildMoveWorkspace.s_getInstance().getChild();
        if (strPathSource == null) {
            String str = "strPathSource == null";
            AcrSpcPrtRootOgcOlgEqp._LOGGER_.severe(str);
            throw new Exception(str);
        }
        String[] strsChildrenExisting = GfrWrpBasSynSpcRootOlgOpr.getInstance().getSortedPathsAbsolute();
        GfrEditSpaceWorkPath dlg = new GfrEditSpaceWorkPath(super._app_.getFrame(), strsChildrenExisting, strPathSource);
        if (!dlg.init()) {
            String str = "! dlg.init()";
            AcrSpcPrtRootOgcOlgEqp._LOGGER_.severe(str);
            throw new Exception(str);
        }
        dlg.setVisible(true);
        String strValue = dlg.getValue();
        dlg.destroy();
        if (dlg.getCancelled() || strValue.length() < 1) return;
        File fleSource = new File(strPathSource);
        File fleTarget = new File(strValue);
        GfrFile.s_moveDirectory(fleSource, fleTarget);
        GfrWrpBasSynSpcRootOlgOpr.getInstance().move(strPathSource, strValue);
        updateContentsDialogManageChildren();
    }
}
