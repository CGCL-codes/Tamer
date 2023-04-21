package org.geoforge.guillcolg.wwd.rlrs.exp;

import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;
import org.geoforge.worldwindolg.layer.oxp.GfrRlrPptSinglePointPlacemarkTloWll;
import org.geoforge.guillc.frame.FrmGfrAbs;
import org.geoforge.guillc.optionpane.GfrOptionPaneAbs;
import org.geoforge.guillc.panel.PnlStatusBarMain;
import org.geoforge.worldwind.layer.GfrRlrAbs;
import org.geoforge.lang.util.logging.FileHandlerLogger;
import org.geoforge.mdldsp.event.state.singleton.selecttlo.GfrEvtMdlSttSngSelTlo;
import org.geoforge.mdldsp.state.singleton.selecttlo.GfrMdlSttSngSelTlo;
import org.geoforge.wrpbasprsdsp.state.singleton.selecttlo.GfrWrpObjSttSngSelTlo;

/**
 *
 * @author bantchao
 */
public class GfrSetRlrTopMainWllsSel extends GfrSetRlrTopMainWllsAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(GfrSetRlrTopMainWllsSel.class.getName());

    static {
        GfrSetRlrTopMainWllsSel._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    public GfrSetRlrTopMainWllsSel(WorldWindowGLCanvas cnv) {
        super(cnv);
        GfrMdlSttSngSelTlo.getInstance().addObserver((Observer) this);
    }

    @Override
    public void destroy() {
        GfrMdlSttSngSelTlo.getInstance().deleteObserver((Observer) this);
        super.destroy();
    }

    @Override
    public void update(Observable obs, Object objEvt) {
        try {
            if (objEvt instanceof GfrEvtMdlSttSngSelTlo) {
                GfrEvtMdlSttSngSelTlo evt = (GfrEvtMdlSttSngSelTlo) objEvt;
                String strId = evt.getKey();
                boolean bln = evt.getValue();
                _selectObject_(strId, bln);
                return;
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            String str = exc.getMessage();
            GfrSetRlrTopMainWllsSel._LOGGER_.severe(str);
            GfrOptionPaneAbs.s_showDialogError(FrmGfrAbs.s_getFrameOwner(PnlStatusBarMain.s_getInstance()), str);
            System.exit(1);
        }
        super.update(obs, objEvt);
    }

    @Override
    public Object addObject(String strId) throws Exception {
        Object obj = super.addObject(strId);
        if (obj == null) return null;
        boolean bln = GfrWrpObjSttSngSelTlo.getInstance().isEnabled(strId);
        if (bln) return obj;
        GfrRlrAbs lyr = (GfrRlrAbs) obj;
        lyr.setEnabled(false);
        return obj;
    }

    private boolean _selectObject_(String strId, boolean bln) throws Exception {
        LayerList llt = super._cnv.getModel().getLayers();
        for (Layer lyrCur : llt) {
            if (!(lyrCur instanceof GfrRlrPptSinglePointPlacemarkTloWll)) continue;
            String strIdCur = ((GfrRlrAbs) lyrCur).getIdGfr();
            if (strIdCur.compareTo(strId) != 0) continue;
            if (bln && lyrCur.isEnabled()) return false;
            if (!bln && !lyrCur.isEnabled()) return false;
            lyrCur.setEnabled(bln);
            if (_cnv != null) _cnv.redraw();
            return true;
        }
        return false;
    }
}
