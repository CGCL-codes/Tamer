package org.personalsmartspace.ipojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

/**
 * iPOJO Internal event dispatcher.
 * This class provides an internal service event dispatcher in order to tackle the
 * event storm that can happen when starting large-scale applications.
 * @see Extender
* @author <a href="mailto:patx.cheevers@intel.com">Persist Project Team</a>
 */
public final class PSSEventDispatcher implements ServiceListener {

    /**
     * The internal event dispatcher.
     * This dispatcher is a singleton.
     */
    private static PSSEventDispatcher DISPATCHER;

    /**
     * The list of listeners.
     * Service interface -> List of {@link ServiceListener}
     */
    private Map m_listeners;

    /**
     * The global bundle context.
     */
    private BundleContext m_context;

    /**
     * Creates the EventDispatcher.
     * @param bc the bundle context used to register and unregister
     * {@link ServiceListener}.
     */
    private PSSEventDispatcher(BundleContext bc) {
        m_context = bc;
        m_listeners = new HashMap();
        m_context.addServiceListener(this);
    }

    /**
     * Creates the internal event
     * dispatcher.
     * @param bc the iPOJO bundle context to send to the 
     * internal event dispatcher.
     */
    public static void create(BundleContext bc) {
        DISPATCHER = new PSSEventDispatcher(bc);
    }

    /**
     * Stops and delete the internal event dispatcher.
     * This method must be call only
     * if iPOJO is stopping.
     */
    public static void dispose() {
        DISPATCHER.stop();
        DISPATCHER = null;
    }

    /**
     * Gets the iPOJO event dispatcher.
     * @return the event dispatcher or
     * <code>null</code> if not created.
     */
    public static PSSEventDispatcher getDispatcher() {
        return DISPATCHER;
    }

    /**
     * Stops the event dispatcher.
     * This method unregisters the {@link ServiceListener}.
     * This methods must be called only when the iPOJO bundle
     * stops.
     */
    private void stop() {
        synchronized (this) {
            m_context.removeServiceListener(this);
            m_listeners.clear();
        }
    }

    /**
     * Method called when a {@link ServiceEvent} is
     * fired by the OSGi framework. 
     * According to the event, this method dispatches
     * to interested registered listers from
     * the {@link PSSEventDispatcher#m_listeners} map.
     * @param event the service event
     * @see org.osgi.framework.ServiceListener#serviceChanged(org.osgi.framework.ServiceEvent)
     */
    public void serviceChanged(ServiceEvent event) {
        String[] itfs = (String[]) event.getServiceReference().getProperty(Constants.OBJECTCLASS);
        for (int s = 0; s < itfs.length; s++) {
            List list;
            synchronized (this) {
                List stored = (List) m_listeners.get(itfs[s]);
                if (stored == null) {
                    return;
                }
                list = new ArrayList(stored);
            }
            for (int i = 0; i < list.size(); i++) {
                ((ServiceListener) list.get(i)).serviceChanged(event);
            }
        }
    }

    /**
     * Adds a new service listener to the {@link PSSEventDispatcher#m_listeners}
     * map. This method specifies the listen service interface
     * @param itf the service interface
     * @param listener the service listener
     */
    public void addListener(String itf, ServiceListener listener) {
        synchronized (this) {
            List list = (List) m_listeners.get(itf);
            if (list == null) {
                list = new ArrayList(1);
                list.add(listener);
                m_listeners.put(itf, list);
            } else {
                list.add(listener);
            }
        }
    }

    /**
     * Removes a service listener.
     * @param listener the service listener to remove
     * @return <code>true</code> if the listener is 
     * successfully removed.
     */
    public boolean removeListener(ServiceListener listener) {
        boolean removed = false;
        synchronized (this) {
            Set keys = m_listeners.keySet();
            Iterator it = keys.iterator();
            while (it.hasNext()) {
                String itf = (String) it.next();
                List list = (List) m_listeners.get(itf);
                removed = removed || list.remove(listener);
            }
        }
        return removed;
    }
}
