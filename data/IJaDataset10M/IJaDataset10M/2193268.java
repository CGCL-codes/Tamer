package co.edu.unal.ungrid.services.client.applet.view;

import java.awt.Container;
import java.awt.Dimension;
import co.edu.unal.ungrid.app.AppFactory;
import co.edu.unal.ungrid.services.client.util.OptionsListener;
import co.edu.unal.ungrid.services.client.util.ServiceOptions;
import co.edu.unal.ungrid.util.OpenGlHelper.OpenGlCanvas;

public abstract class AbstractView implements OptionsListener {

    public abstract Container getContainer();

    public abstract OpenGlCanvas[] getCanvasSet();

    public abstract void init();

    public abstract void clear();

    public abstract void update();

    public abstract void resize(final Dimension dim);

    public abstract void resetWindowing();

    public abstract double getZoomLevel();

    public abstract String getTitle();

    public abstract void requestFocus();

    protected abstract AbstractViewParamSet buildParamSet();

    public AbstractView() {
        setParamSet(buildParamSet());
        subscribeToOptions();
    }

    public void updateParamSet() {
    }

    public AbstractViewParamSet getParamSet() {
        updateParamSet();
        return m_paramSet;
    }

    public void setParamSet(final AbstractViewParamSet set) {
        assert set != null;
        if (set != null) {
            m_paramSet = set;
        }
    }

    public double getParam(final String key) {
        assert m_paramSet != null;
        assert key != null;
        return m_paramSet.getDouble(key);
    }

    public void setParam(final String key, double val) {
        assert m_paramSet != null;
        assert key != null;
        m_paramSet.set(key, val);
    }

    protected void subscribeToOptions() {
        AppFactory.getInstance().addOptionsListener(this);
    }

    public void valueChanged(final ServiceOptions opts) {
        update();
    }

    public void destroy() {
    }

    protected AbstractViewParamSet m_paramSet;
}
