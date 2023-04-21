package org.geoforge.guillcogc.menuitem;

import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;
import org.geoforge.guillc.optionpane.GfrOptionPaneAbs;
import org.geoforge.lang.util.logging.FileHandlerLogger;
import org.geoforge.mdldatogc.GfrMdlDtaIdObjTloRmtOgcWms;
import org.geoforge.mdldatogc.event.GfrEvtMdlIdDtaAddedLloWmsLayer;
import org.geoforge.mdldatogc.event.GfrEvtMdlIdDtaRemovedAllLayers;
import org.geoforge.mdldatogc.event.GfrEvtMdlIdDtaRemovedLayer;
import org.geoforge.mdldsp.event.state.singleton.GfrEvtMdlSttSngAbs;
import org.geoforge.mdl.event.GfrEvtMdlIdAbs;
import org.geoforge.wrpbasprssynogc.GfrWrpBasTloSynOgcWms;

/**
 *
 * @author bantchao
 */
public abstract class MimTransientDisplayWmsNoLyrsAbs extends MimTransientDisplayWmsNoAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(MimTransientDisplayWmsNoLyrsAbs.class.getName());

    static {
        MimTransientDisplayWmsNoLyrsAbs._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    protected abstract void _update() throws Exception;

    protected MimTransientDisplayWmsNoLyrsAbs(ActionListener alrParentNode, ActionListener alrParentCad, String strId) throws Exception {
        super(alrParentNode, alrParentCad, " all", strId);
        _update();
        GfrMdlDtaIdObjTloRmtOgcWms.getInstance().addObserver((Observer) this);
    }

    @Override
    public void destroy() {
        GfrMdlDtaIdObjTloRmtOgcWms.getInstance().deleteObserver((Observer) this);
        super.destroy();
    }

    @Override
    public void update(Observable obs, Object objEvt) {
        try {
            if (objEvt instanceof GfrEvtMdlSttSngAbs) {
                GfrEvtMdlSttSngAbs mdl = (GfrEvtMdlSttSngAbs) objEvt;
                String strId = mdl.getKey();
                if (!GfrWrpBasTloSynOgcWms.getInstance().containsLayer(super.getId(), strId)) return;
                _update();
                return;
            }
            if (objEvt instanceof GfrEvtMdlIdDtaAddedLloWmsLayer) {
                GfrEvtMdlIdDtaAddedLloWmsLayer mdl = (GfrEvtMdlIdDtaAddedLloWmsLayer) objEvt;
                String strIdParent = mdl.getIdParent();
                if (strIdParent.compareTo(super.getId()) != 0) return;
                _update();
                return;
            }
            if (objEvt instanceof GfrEvtMdlIdDtaRemovedAllLayers) {
                GfrEvtMdlIdAbs evt = (GfrEvtMdlIdAbs) objEvt;
                String strId = evt.getId();
                if (strId.compareTo(super.getId()) != 0) return;
                _update();
                return;
            }
            if (objEvt instanceof GfrEvtMdlIdDtaRemovedLayer) {
                GfrEvtMdlIdDtaRemovedLayer evt = (GfrEvtMdlIdDtaRemovedLayer) objEvt;
                String strIdParent = evt.getIdParent();
                if (strIdParent.compareTo(super.getId()) != 0) return;
                _update();
                return;
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            String str = exc.getMessage();
            MimTransientDisplayWmsNoLyrsAbs._LOGGER_.severe(str);
            GfrOptionPaneAbs.s_showDialogError(null, str);
            System.exit(1);
        }
    }
}
