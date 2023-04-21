package org.geoforge.guillcolg.wwd.panel.oxp;

import gov.nasa.worldwind.geom.LatLon;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.geoforge.guillcolg.wwd.rlrs.exp.GfrSetRlrTopMainS2dsMan;
import org.geoforge.guillcolg.wwd.rlrs.exp.GfrSetRlrTopMainS3dsMan;
import org.geoforge.guillcolg.wwd.rlrs.exp.GfrSetRlrTopMainWllsMan;
import org.geoforge.worldwind.builder.ControllerBuilderPolylineAbs;
import org.geoforge.guillcolg.wwd.util.exp.OurContextMenuControllerPicksManExpAbs;
import org.geoforge.worldwind.awt.GfrWorldWindowGLCanvasAbs;
import org.geoforge.lang.util.logging.FileHandlerLogger;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public abstract class PnlMainWwdOlgExpMainPicksManAbs extends PnlMainWwdOlgExpMainAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(PnlMainWwdOlgExpMainPicksManAbs.class.getName());

    static {
        PnlMainWwdOlgExpMainPicksManAbs._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    protected ControllerBuilderPolylineAbs _pbc = null;

    public ArrayList<LatLon> getValue() {
        return this._pbc.getValue();
    }

    public PnlMainWwdOlgExpMainPicksManAbs(GfrWorldWindowGLCanvasAbs cnv, OurContextMenuControllerPicksManExpAbs cmc) {
        super(cnv, cmc);
        super._topWlls = new GfrSetRlrTopMainWllsMan(super._cnv);
        super._topS3ds = new GfrSetRlrTopMainS3dsMan(super._cnv);
        super._topS2ds = new GfrSetRlrTopMainS2dsMan(super._cnv);
    }

    @Override
    public boolean init() {
        if (!super.init()) return false;
        if (!this._pbc.init()) return false;
        return true;
    }

    @Override
    public void destroy() {
        super.destroy();
        if (this._pbc != null) {
            this._pbc.destroy();
            this._pbc = null;
        }
    }

    public void setEnabledEditor(boolean bln) {
        if (bln) this._pbc.createNewEntry(); else this._pbc.clearEntry();
    }
}
