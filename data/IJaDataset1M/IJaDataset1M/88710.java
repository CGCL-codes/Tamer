package org.geoforge.guillcolg.treenode.exp;

import java.awt.event.ActionListener;
import java.util.logging.Logger;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.geoforge.guillcolg.popupmenu.exp.PmuCtlCtrFolderTopSecDskWllsLogs;
import org.geoforge.guillc.panel.PnlStatusBarAbs;
import org.geoforge.guillc.tree.TreAbs;
import org.geoforge.guillc.treenode.GfrNodAbs;
import org.geoforge.guillc.treenode.GfrNodFolderSorter;
import org.geoforge.lang.util.logging.FileHandlerLogger;
import org.geoforge.wrpbasprsdsp.state.singleton.selecttlo.GfrWrpObjSttSngSelTlo;
import org.geoforge.wrpbasprssynolg.oxp.GfrWrpBasSynTopWlls;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class GfrNodCtrFixFolderTopSecLclWllsLogs extends GfrNodCtrFixFolderTopSecLclWllsAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(GfrNodCtrFixFolderTopSecLclWllsLogs.class.getName());

    static {
        GfrNodCtrFixFolderTopSecLclWllsLogs._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    public GfrNodCtrFixFolderTopSecLclWllsLogs(ActionListener alrController, TreAbs tre, ActionListener alrParentPanelMvc, PnlStatusBarAbs pnlStatusBar, String strIdViewer) throws Exception {
        super(alrController, tre, alrParentPanelMvc, pnlStatusBar, strIdViewer);
        super._pop_ = new PmuCtlCtrFolderTopSecDskWllsLogs(alrController, tre, (ActionListener) null, alrParentPanelMvc);
    }

    @Override
    public void load() throws Exception {
        String[] strsUniqueId = GfrWrpBasSynTopWlls.getInstance().getIds();
        if (strsUniqueId == null || strsUniqueId.length < 1) return;
        for (int i = 0; i < strsUniqueId.length; i++) addObject(strsUniqueId[i]);
    }

    @Override
    public Object addObject(String strId) throws Exception {
        if (!GfrWrpBasSynTopWlls.getInstance().contains(strId)) return null;
        if (!GfrWrpObjSttSngSelTlo.getInstance().isEnabled(strId)) return null;
        GfrNodAbs nod = new GfrNodCtrMovFolderChkTloRunWlllLogs(super._alrControllerSpcPrj, super._strIdViewer, strId, super._tree_, super._alrParentPanelMvc_, super._pnlStatusBar);
        if (!nod.init()) {
            String str = "! nodCur.init()";
            GfrNodCtrFixFolderTopSecLclWllsLogs._LOGGER_.severe(str);
            throw new Exception(str);
        }
        String strName = (String) nod.getUserObject();
        int intPos = GfrNodFolderSorter.s_getPositionChildToAdd(this, strName);
        DefaultTreeModel dtm = (DefaultTreeModel) super._tree_.getModel();
        dtm.insertNodeInto(nod, this, intPos);
        TreePath tphCur = new TreePath(nod.getPath());
        if (!super._tree_.isVisible(tphCur)) {
            TreePath tphThis = new TreePath(this.getPath());
            super._tree_.expandPath(tphThis);
        }
        return nod;
    }
}
