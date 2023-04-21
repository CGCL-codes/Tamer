package org.geoforge.guillcolg.treenode.ecl;

import java.awt.event.ActionListener;
import java.util.Observer;
import java.util.logging.Logger;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.geoforge.guillcolg.popupmenu.ecl.PmuCtlCtrFolderTopSecDskPths;
import org.geoforge.guillc.tree.TreAbs;
import org.geoforge.guillc.panel.PnlStatusBarAbs;
import org.geoforge.guillc.treenode.GfrNodAbs;
import org.geoforge.guillcolg.treenode.GfrNodCtrFixFolderTopSecLclOlgAbs;
import org.geoforge.guillc.treenode.GfrNodFolderSorter;
import org.geoforge.ioecl.finder.GfrFactoryIconEcl;
import org.geoforge.io.finder.GfrFactoryIconAbs;
import org.geoforge.lang.util.logging.FileHandlerLogger;
import org.geoforge.mdldatolg.ecl.GfrMdlDtaIdObjTloLclPth;
import org.geoforge.mdldatolg.ecl.GfrMdlDtaSetTlosDskPth;
import org.geoforge.wrpbasprsdsp.state.singleton.selecttlo.GfrWrpObjSttSngSelTlo;
import org.geoforge.wrpbasprssynolg.ecl.GfrWrpBasSynTopPths;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class GfrNodCtrFixFolderTopSecLclPthsGlobe extends GfrNodCtrFixFolderTopSecLclOlgAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(GfrNodCtrFixFolderTopSecLclPthsGlobe.class.getName());

    static {
        GfrNodCtrFixFolderTopSecLclPthsGlobe._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    private static final String _F_STR_LABEL_ = "Paths";

    public GfrNodCtrFixFolderTopSecLclPthsGlobe(ActionListener alrController, TreAbs tre, ActionListener alrParentPanelMvc, PnlStatusBarAbs pnlStatusBar, String strIdViewer) throws Exception {
        super(alrController, GfrNodCtrFixFolderTopSecLclPthsGlobe._F_STR_LABEL_, GfrFactoryIconEcl.s_getPaths(GfrFactoryIconAbs.INT_SIZE_SMALL), tre, alrParentPanelMvc, pnlStatusBar, strIdViewer);
        super._pop_ = new PmuCtlCtrFolderTopSecDskPths(alrController, tre, (ActionListener) null, alrParentPanelMvc);
    }

    @Override
    public void loadTransient() throws Exception {
        GfrMdlDtaSetTlosDskPth.getInstance().addObserver((Observer) this);
        GfrMdlDtaIdObjTloLclPth.getInstance().addObserver((Observer) this);
        super.loadTransient();
    }

    @Override
    public void releaseTransient() throws Exception {
        GfrMdlDtaSetTlosDskPth.getInstance().deleteObserver((Observer) this);
        GfrMdlDtaIdObjTloLclPth.getInstance().deleteObserver((Observer) this);
        super.releaseTransient();
    }

    @Override
    public void load() throws Exception {
        String[] strsUniqueId = GfrWrpBasSynTopPths.getInstance().getIds();
        if (strsUniqueId == null || strsUniqueId.length < 1) return;
        for (int i = 0; i < strsUniqueId.length; i++) addObject(strsUniqueId[i]);
    }

    @Override
    public Object addObject(String strId) throws Exception {
        if (!GfrWrpBasSynTopPths.getInstance().contains(strId)) return null;
        if (!GfrWrpObjSttSngSelTlo.getInstance().isEnabled(strId)) return null;
        GfrNodAbs nod = new GfrNodCtrRdrChkLeafRunOlgTloPth(super._alrControllerSpcPrj, super._strIdViewer, strId, super._tree_, super._alrParentPanelMvc_, super._pnlStatusBar);
        if (!nod.init()) {
            String str = "! nodCur.init()";
            GfrNodCtrFixFolderTopSecLclPthsGlobe._LOGGER_.severe(str);
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
