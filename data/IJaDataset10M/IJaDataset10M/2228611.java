package org.geoforge.guillcolg.treenode.exp;

import java.awt.event.ActionListener;
import java.util.Observer;
import java.util.logging.Logger;
import org.geoforge.guillcolg.popupmenu.exp.PmuCtlCtrLeafTloDskWllGlbSel;
import org.geoforge.guillc.tree.TreAbs;
import org.geoforge.guillcolg.treenode.GfrNodCtrMovLeafChkSelTloLclOlgAbs;
import org.geoforge.guillc.optionpane.GfrOptionPaneAbs;
import org.geoforge.ioolg.finder.GfrFactoryIconOxp;
import org.geoforge.io.finder.GfrFactoryIconAbs;
import org.geoforge.lang.util.logging.FileHandlerLogger;
import org.geoforge.mdldatolg.oxp.GfrMdlDtaIdObjTloLclWll;
import org.geoforge.wrpbasprsdsp.state.multiple.run.GfrWrpObjSttMltRun;
import org.geoforge.wrpbasprsdsp.viewer.GfrWrpDspSynViewer;
import org.geoforge.wrpbasprssynolg.oxp.GfrWrpBasTloSynOlgWll;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class GfrNodCtrMovLeafChkSelTloLclWll extends GfrNodCtrMovLeafChkSelTloLclOlgAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(GfrNodCtrMovLeafChkSelTloLclWll.class.getName());

    static {
        GfrNodCtrMovLeafChkSelTloLclWll._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    public GfrNodCtrMovLeafChkSelTloLclWll(ActionListener alrController, String strIdUnique, TreAbs tre, ActionListener alrParentPanelMvc) throws Exception {
        super(alrController, strIdUnique, GfrWrpBasTloSynOlgWll.getInstance().getName(strIdUnique), tre);
        GfrMdlDtaIdObjTloLclWll.getInstance().addObserver((Observer) this);
        super.setImageIcon(GfrFactoryIconOxp.s_getWell(GfrFactoryIconAbs.INT_SIZE_SMALL));
        super._pop_ = new PmuCtlCtrLeafTloDskWllGlbSel(alrController, strIdUnique, tre, alrParentPanelMvc);
    }

    @Override
    public void destroy() {
        GfrMdlDtaIdObjTloLclWll.getInstance().deleteObserver((Observer) this);
        super.destroy();
    }

    @Override
    public void setSelected(boolean bln) {
        try {
            _updateDisplayLogs_(super._strId, bln);
            _updateDisplayMarkers_(super._strId, bln);
        } catch (Exception exc) {
            exc.printStackTrace();
            String str = exc.getMessage();
            GfrNodCtrMovLeafChkSelTloLclWll._LOGGER_.severe(str);
            GfrOptionPaneAbs.s_showDialogError(null, str);
            System.exit(1);
        }
        super.setSelected(bln);
    }

    private void _updateDisplayLogs_(String strIdParent, boolean bln) throws Exception {
        String[] strsChildren = GfrWrpBasTloSynOlgWll.getInstance().getIdsLog(strIdParent);
        if (bln) {
            for (int i = 0; i < strsChildren.length; i++) {
                GfrWrpDspSynViewer.s_getInstance().updateExistingViewers(strsChildren[i]);
            }
        } else {
            for (int i = 0; i < strsChildren.length; i++) {
                GfrWrpObjSttMltRun.getInstance().delete(strsChildren[i]);
            }
        }
    }

    private void _updateDisplayMarkers_(String strIdParent, boolean bln) throws Exception {
        String[] strsChildren = GfrWrpBasTloSynOlgWll.getInstance().getIdsMarker(strIdParent);
        if (bln) {
            for (int i = 0; i < strsChildren.length; i++) {
                GfrWrpDspSynViewer.s_getInstance().updateExistingViewers(strsChildren[i]);
            }
        } else {
            for (int i = 0; i < strsChildren.length; i++) {
                GfrWrpObjSttMltRun.getInstance().delete(strsChildren[i]);
            }
        }
    }
}
