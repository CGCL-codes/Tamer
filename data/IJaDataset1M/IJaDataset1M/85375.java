package co.edu.unal.ungrid.services.client.applet.atlas.view;

import java.io.Serializable;
import java.util.Hashtable;
import co.edu.unal.ungrid.services.client.applet.view.OpenGlViewParamSet;
import co.edu.unal.ungrid.services.client.applet.view.ViewEventListener.ViewEvent;

public class SliceViewParamSet extends OpenGlViewParamSet {

    public static final long serialVersionUID = 1L;

    public SliceViewParamSet() {
        super();
    }

    public SliceViewParamSet(final SliceViewParamSet ps) {
        super(ps);
    }

    @Override
    public OpenGlViewParamSet clone() {
        return new SliceViewParamSet(this);
    }

    @Override
    public Hashtable<String, Serializable> getDefaultParams() {
        Hashtable<String, Serializable> ht = new Hashtable<String, Serializable>();
        ht.put(OFFSET_X, 0.0);
        ht.put(OFFSET_Y, 0.0);
        ht.put(ZOOM_LVL, 1.0);
        ht.put(SLICE_ID, 128.0);
        ht.put(STATUS, ViewEvent.NORMAL);
        return ht;
    }

    @Override
    public void reset() {
        Hashtable<String, Serializable> ht = getDefaultParams();
        ht.put(SLICE_ID, getSliceIdx());
        setParams(ht);
    }

    public double getSliceIdx() {
        return getDouble(SLICE_ID);
    }

    public double getZoomLevel() {
        return getDouble(ZOOM_LVL);
    }

    public boolean isValid() {
        double id = getSliceIdx();
        double zl = getZoomLevel();
        return (id > 0.0 && zl > 0.0);
    }

    @Override
    public ViewEvent getStatus() {
        return (ViewEvent) get(STATUS);
    }

    @Override
    public void setStatus(final ViewEvent status) {
        set(STATUS, status);
    }

    public static final String OFFSET_X = "sv-offset-x";

    public static final String OFFSET_Y = "sv-offset-y";

    public static final String ZOOM_LVL = "sv-zoom-lvl";

    public static final String SLICE_ID = "sv-slice-id";

    public static final String STATUS = "sv-status";
}
