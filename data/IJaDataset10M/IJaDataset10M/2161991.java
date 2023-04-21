package at.redcross.tacos.web.security;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.eclipse.core.runtime.ListenerList;

public class WebPermissionListenerRegistry {

    /** Property change event indicating that the cache should be invalidated */
    public static final String INVALIDATE_CACHE = "InvalidateCache";

    /** the registry instance */
    private static WebPermissionListenerRegistry instance;

    /** the registered listeners */
    private final ListenerList listeners = new ListenerList();

    /** Creates a new registry instance */
    private WebPermissionListenerRegistry() {
    }

    /** Returns the shared instance */
    public static synchronized WebPermissionListenerRegistry getInstance() {
        if (instance == null) {
            instance = new WebPermissionListenerRegistry();
        }
        return instance;
    }

    /** Adds a new listener to the list of listeners */
    public void addListener(PropertyChangeListener listener) {
        listeners.add(listener);
    }

    /** Removes the listener from the list of listeners */
    public void removeListener(PropertyChangeListener listener) {
        listeners.remove(listener);
    }

    /** Notifies the listener to invalidate the whole cache */
    public void notifyInvalidateCache() {
        notifyListener(INVALIDATE_CACHE);
    }

    /** Notifies the listeners about the changed event */
    protected void notifyListener(String eventName) {
        Object[] listenerList = listeners.getListeners();
        PropertyChangeEvent event = new PropertyChangeEvent(this, eventName, null, null);
        for (int i = 0; i < listenerList.length; i++) {
            ((PropertyChangeListener) listenerList[i]).propertyChange(event);
        }
    }
}
