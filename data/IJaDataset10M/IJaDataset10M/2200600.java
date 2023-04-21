package org.geoforge.app.actioncontroller.perspective.arraylist;

import java.util.ArrayList;
import org.geoforge.guitlc.frame.secrun.FrmGfrWindowViewerAbs;

/**
 *
 * @author bantchao
 */
public class GfrViewersRun extends java.util.Observable {

    private static GfrViewersRun _INSTANCE_ = null;

    public static GfrViewersRun s_getInstance() {
        if (GfrViewersRun._INSTANCE_ == null) {
            GfrViewersRun._INSTANCE_ = new GfrViewersRun();
        }
        return GfrViewersRun._INSTANCE_;
    }

    private ArrayList<FrmGfrWindowViewerAbs> _alrViewers = null;

    public ArrayList<FrmGfrWindowViewerAbs> get() {
        return this._alrViewers;
    }

    public boolean remove(FrmGfrWindowViewerAbs frm) {
        boolean blnResult = this._alrViewers.remove(frm);
        super.setChanged();
        super.notifyObservers("removed");
        return blnResult;
    }

    public void clear() {
        this._alrViewers.clear();
        super.setChanged();
        super.notifyObservers("cleared");
    }

    public int size() {
        return this._alrViewers.size();
    }

    public FrmGfrWindowViewerAbs get(int intId) {
        return this._alrViewers.get(intId);
    }

    public void add(FrmGfrWindowViewerAbs frm) {
        this._alrViewers.add(frm);
        super.setChanged();
        super.notifyObservers("added");
    }

    private GfrViewersRun() {
        this._alrViewers = new ArrayList<FrmGfrWindowViewerAbs>();
    }
}
