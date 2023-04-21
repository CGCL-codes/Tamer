package org.viewaframework.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import javax.swing.SwingWorker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.viewaframework.util.BeanUtils;
import org.viewaframework.view.ViewContainer;
import org.viewaframework.view.ViewContainerFrame;
import org.viewaframework.view.ViewContainerLocator;
import org.viewaframework.view.ViewContainerLocatorAware;
import org.viewaframework.view.ViewManager;

/**
 * @author Mario Garcia
 * 
 * This class is an abstract controller using the functionality of a {@javax.swing.SwingWorker}. It would be
 * very useful for those actions that need progress information, or need to be cancel at a given time.
 * 
 * This implementation is a workaround of what a real reusable SwingWorker should be. It creates copies of
 * the source controller and launches these copies.
 *
 * @param <EL> EventListener the controller is going to represent
 * @param <EO> EventObject the controller is going to handle
 * @param <T> Object the method doInBackground is going to return (It will always return null)
 * @param <V> Object the method handleViewPublising is going to receive from publish(...) calls
 * 
 * @since 1.0
 */
public abstract class AbstractViewControllerWorker<EL extends EventListener, EO extends EventObject, T, V> extends SwingWorker<T, V> implements ViewController<EL, EO>, ViewContainerLocatorAware {

    private static final Object PROPERTY_NAME_PROGRESS = "progress";

    private static final Object PROPERTY_NAME_STATE = "state";

    private static final Log logger = LogFactory.getLog(AbstractViewControllerWorker.class);

    private EO eventObject;

    private ViewContainer view;

    private ViewManager viewManager;

    @Override
    protected T doInBackground() throws Exception {
        this.handleView(view, eventObject);
        return null;
    }

    @Override
    protected void process(List<V> chunks) {
        this.handleViewPublising(view, eventObject, chunks);
    }

    /**
	 * @param view2
	 * @param eventObject2
	 * @param chunks
	 */
    public abstract void handleViewPublising(ViewContainer view2, EO eventObject2, List<V> chunks);

    @Override
    protected void done() {
        if (!isCancelled()) {
            this.postHandlingView(view, eventObject);
        }
    }

    @SuppressWarnings("unchecked")
    public void executeHandler(final ViewContainer view, final EO eventObject) {
        this.view = view;
        this.viewManager = this.view.getApplication().getViewManager();
        this.eventObject = eventObject;
        Object o = null;
        try {
            o = this.getClass().newInstance();
            final AbstractViewControllerWorker<EL, EO, T, V> sw = AbstractViewControllerWorker.class.cast(o);
            sw.addPropertyChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals(PROPERTY_NAME_STATE) && evt.getNewValue().equals(SwingWorker.StateValue.STARTED)) {
                        sw.preHandlingView(view, eventObject);
                    } else if (evt.getPropertyName().equals(PROPERTY_NAME_PROGRESS)) {
                        sw.handleViewProgress(view, eventObject, Integer.valueOf(evt.getNewValue().toString()));
                    }
                }
            });
            AbstractViewControllerWorker.class.cast(BeanUtils.copyProperties(sw, this)).execute();
        } catch (IllegalAccessException e) {
            logger.fatal(e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.fatal(e.getMessage());
        } catch (SecurityException e) {
            logger.fatal(e.getMessage());
        } catch (InstantiationException e) {
            logger.fatal(e.getMessage());
        }
    }

    public ViewManager getViewManager() {
        return this.viewManager;
    }

    /**
	 * This method (together with setView()) is here for being copied for subsequent cloned workers.
	 * 
	 * @return The current controller view
	 */
    public ViewContainer getView() {
        return this.view;
    }

    /**
	 * @return
	 */
    public EO getEventObject() {
        return eventObject;
    }

    public ViewController<EL, EO> getTargetController() {
        return null;
    }

    public <U extends ViewContainer> ViewContainerLocator<U> locate(Class<U> clazz) {
        return new ViewContainerLocator<U>(clazz, this.view.getApplication().getViewManager());
    }

    public ViewContainer locate(String name) {
        return this.locate(ViewContainer.class).named(name);
    }

    public ViewContainerFrame locateRootView() {
        return this.locate(ViewContainerFrame.class).named(ViewManager.ROOT_VIEW_ID);
    }

    public void handleView(ViewContainer view, EO eventObject) {
    }

    /**
	 * @param view
	 * @param eventObject
	 */
    public abstract void handleViewProgress(ViewContainer view, EO eventObject, Integer progress);

    public Object invoke(Object proxy, Method method, Object[] args) {
        return null;
    }

    public void postHandlingView(ViewContainer view, EO eventObject) {
    }

    public void preHandlingView(ViewContainer view, EO eventObject) {
    }

    /**
	 * @param eventObject
	 */
    public void setEventObject(EO eventObject) {
        this.eventObject = eventObject;
    }

    /**
	 * @param view
	 */
    public void setView(ViewContainer view) {
        this.view = view;
    }

    /**
	 * This method (together with getView()) is here for copying 
	 * ViewManager for subsequent cloned workers.
	 * 
	 * @param viewManager
	 */
    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }
}
