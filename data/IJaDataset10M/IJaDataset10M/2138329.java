package net.sf.doolin.gui.window.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.doolin.gui.action.ActionContext;
import net.sf.doolin.gui.app.GUIApplication;
import net.sf.doolin.gui.view.GUIView;
import net.sf.doolin.gui.window.GUIWindow;
import net.sf.doolin.gui.window.GUIWindowListener;
import net.sf.doolin.gui.window.descriptor.GUIWindowDescriptor;

/**
 * Utility ancestor for application windows.
 * 
 * @param <B>
 *            Type of the window data
 * 
 * @author Damien Coraboeuf
 */
public abstract class AbstractGUIWindow<B> implements GUIWindow<B> {

    private GUIApplication application;

    private GUIWindowDescriptor<B> windowDescriptor;

    private B model;

    private Map<String, GUIView<?>> views = new HashMap<String, GUIView<?>>();

    private WindowActionContext<B> actionContext;

    private final Map<Object, Object> properties = new HashMap<Object, Object>();

    private boolean closed;

    private final List<GUIWindowListener<B>> windowListenerList = new ArrayList<GUIWindowListener<B>>();

    @Override
    public void addGUIWindowListener(GUIWindowListener<B> listener) {
        this.windowListenerList.add(listener);
    }

    @Override
    public void addView(GUIView<?> view) {
        String viewId = view.getID();
        if (!this.views.containsKey(viewId)) {
            this.views.put(viewId, view);
        }
    }

    /**
	 * Builds the window.
	 * 
	 * @see #getApplication()
	 * @see #getWindowData()
	 */
    protected abstract void build();

    /**
	 * Builds and initializes the window.
	 * 
	 * {@inheritDoc}
	 * 
	 * @param application
	 *            the application
	 * @param windowDescriptor
	 *            the window descriptor
	 * @param model
	 *            the model
	 */
    @Override
    public void build(GUIApplication application, GUIWindowDescriptor<B> windowDescriptor, B model) {
        this.application = application;
        this.windowDescriptor = windowDescriptor;
        this.model = model;
        this.actionContext = new WindowActionContext<B>(this);
        build();
    }

    /**
	 * Tries to close all views
	 */
    @Override
    public boolean canClose() {
        if (this.views != null) {
            Object[] viewArray = this.views.values().toArray();
            for (int i = 0; i < viewArray.length; i++) {
                GUIView<?> view = (GUIView<?>) viewArray[i];
                boolean closed = view.close(false);
                if (!closed) {
                    return false;
                }
            }
        }
        this.windowDescriptor.onClosingWindow(this);
        return true;
    }

    /**
	 * Notifies the window descriptor.
	 * 
	 * {@inheritDoc}
	 */
    @Override
    public void close() {
        this.windowDescriptor.onCloseWindow(this);
        this.closed = true;
        List<GUIWindowListener<B>> listeners = getWindowListeners();
        for (GUIWindowListener<B> listener : listeners) {
            listener.onWindowClosed(this);
        }
    }

    @Override
    public ActionContext getActionContext() {
        return this.actionContext;
    }

    /**
	 * Gets the application.
	 * 
	 * @return the application
	 */
    @Override
    public GUIApplication getApplication() {
        return this.application;
    }

    /**
	 * Gets the ID for this window
	 */
    @Override
    public String getId() {
        return this.windowDescriptor.getWindowId(getWindowData());
    }

    @Override
    public Collection<? extends GUIView<?>> getOpenViews() {
        return this.views.values();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Object getProperty(Object key) {
        return this.properties.get(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> GUIView<V> getView(String viewId) {
        return (GUIView<V>) this.views.get(viewId);
    }

    /**
	 * Gets the model.
	 * 
	 * @return the model
	 */
    @Override
    public B getWindowData() {
        return this.model;
    }

    /**
	 * Gets the window descriptor.
	 * 
	 * @return the window descriptor
	 */
    @Override
    public GUIWindowDescriptor<B> getWindowDescriptor() {
        return this.windowDescriptor;
    }

    /**
	 * Gets the list of window listeners, those of the descriptor plus the ones
	 * associated with this window.
	 */
    protected List<GUIWindowListener<B>> getWindowListeners() {
        List<GUIWindowListener<B>> list = new ArrayList<GUIWindowListener<B>>();
        list.addAll(this.windowDescriptor.getWindowListeners());
        list.addAll(this.windowListenerList);
        return list;
    }

    @Override
    public void removeView(GUIView<?> view) {
        this.views.remove(view.getID());
        getWindowDescriptor().hideView(this, view);
    }

    @Override
    public boolean isClosed() {
        return this.closed;
    }

    /**
	 * Does nothing
	 */
    @Override
    public void onPostCreate() {
        List<GUIWindowListener<B>> listeners = getWindowListeners();
        for (GUIWindowListener<B> listener : listeners) {
            listener.onWindowInit(this);
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void setProperty(Object key, Object value) {
        if (value != null) {
            this.properties.put(key, value);
        } else {
            this.properties.remove(key);
        }
    }

    @Override
    public void showView(GUIView<?> view) {
        if (!view.isClosed()) {
            getWindowDescriptor().activateView(this, view);
        }
    }
}
