package net.assimilator.utilities.viewer.client;

import java.util.*;
import java.rmi.*;
import net.jini.config.*;
import net.jini.discovery.*;
import net.jini.core.lookup.*;

/**
 * The ServiceCache is a helper class created to provide a live cache of services that 
 * match a submitted template across all discovered lookup services. As lookup services
 * are added (discovered) event registration will occur to that newly discovered lookup
 * service
 */
public class ServiceCache implements ServiceEventListener, DiscoveryListener {

    /**
     * The ServiceWatcher for this ServiceCache
     */
    private ServiceWatcher watcher;

    /**
     * The ServiceTemplate to use for discovery
     */
    private ServiceTemplate template;

    /**
     * List of ServiceCacheListener instances
     */
    List listenerList = Collections.synchronizedList(new LinkedList());

    /**
     * Instance of DiscoveryManagement to use
     */
    private DiscoveryManagement discoverer = null;

    /**
     * Collection of ServiceCacheItem instances
     */
    private List cacheItems = Collections.synchronizedList(new LinkedList());

    /**
     * The next index into the cache
     */
    private int nextIndex = 0;

    /** 
     * Holds value of property serviceComparator to sort the list of services
     */
    private Comparator serviceComparator;

    /**
     * Configuration for the ServiceCache
     */
    private Configuration config;

    /**
     * Create a ServiceCache for a specified listener
     * 
     * @param listener - The ServiceCacheListener
     */
    public ServiceCache(ServiceCacheListener listener) throws Exception {
        this(listener, EmptyConfiguration.INSTANCE);
    }

    /**
     * Create a ServiceCache for a specified listener
     * 
     * @param listener - The ServiceCacheListener
     * @param config - The Configuration object
     */
    public ServiceCache(ServiceCacheListener listener, Configuration config) throws Exception {
        if (config == null) throw new IllegalArgumentException("config is null");
        this.config = config;
        addListener(listener);
    }

    /**
     * Get the number of items in the cache
     */
    public int getItemCount() {
        return (cacheItems.size());
    }

    /**
     * Get the ServiceTemplate used to create this cache
     */
    public ServiceTemplate getServiceTemplate() {
        return (template);
    }

    /**
     * Add a listener to this cache
     *
     * @param listener - The ServiceCacheListener to add
     */
    public void addListener(ServiceCacheListener listener) {
        if (listener == null) throw new NullPointerException("listener is null");
        listenerList.add(listener);
    }

    /**
     * Remove a listener from this cache
     *
     * @param listener - The ServiceCacheListener to remove
     */
    public void removeListener(ServiceCacheListener listener) {
        if (listener != null) {
            listenerList.remove(listener);
        }
    }

    /**
     * Get the number of listeners that have subscribed for notifications of changes
     * to this cache
     */
    public int getListenerCount() {
        return (listenerList.size());
    }

    /**
     * Get a array of services that match the selection criteria
     *
     * @return an array of service objects, if a comparator is list is sorted based on that comparator
     */
    public Object[] getServices() {
        synchronized (cacheItems) {
            sortServiceItems();
            LinkedList list = new LinkedList();
            Iterator iter = cacheItems.iterator();
            while (iter.hasNext()) {
                ServiceCacheItem item = (ServiceCacheItem) iter.next();
                if (item.getServiceItem() != null) list.add(item.getServiceItem().service);
            }
            return list.toArray(new Object[list.size()]);
        }
    }

    /**
     * Get a array of service items that matches the selection criteria
     * for this cache, if a comparator is list is sorted based on that comparator
     *
     * @return an array of service items
     */
    public ServiceItem[] getServiceItems() {
        synchronized (cacheItems) {
            sortServiceItems();
            LinkedList list = new LinkedList();
            Iterator iter = cacheItems.iterator();
            while (iter.hasNext()) {
                ServiceCacheItem item = (ServiceCacheItem) iter.next();
                if (item.getServiceItem() != null) list.add(item.getServiceItem());
            }
            return (ServiceItem[]) list.toArray(new ServiceItem[list.size()]);
        }
    }

    /**
     * Get a service from the cache
     *
     * @return the next service that matches the selection criteria
     * for this cache, if a comparator is present the best service is returned
     */
    public Object getService() {
        ServiceItem item = getServiceItem();
        if (item != null) return (item.service); else return (null);
    }

    /**
     * Get a service item from the cache
     *
     * @return a service item that matches the selection criteria
     * for this cache, if a comparator is present the best service item is returned
     */
    public ServiceItem getServiceItem() {
        synchronized (cacheItems) {
            int size = cacheItems.size();
            if (size == 0) return (null);
            if (size == 1) {
                ServiceCacheItem item = (ServiceCacheItem) cacheItems.get(0);
                return item.getServiceItem();
            }
            if (serviceComparator == null) {
                if (nextIndex >= size) nextIndex = 0;
                int startIndex = nextIndex;
                do {
                    ServiceCacheItem item = (ServiceCacheItem) cacheItems.get(nextIndex++);
                    if (nextIndex >= size) nextIndex = 0;
                    if (item.getServiceItem() != null) {
                        return (item.getServiceItem());
                    }
                } while (nextIndex != startIndex);
                return null;
            } else {
                sortServiceItems();
                ServiceCacheItem item = (ServiceCacheItem) cacheItems.get(0);
                return item.getServiceItem();
            }
        }
    }

    /**
     * Get a service from the cache
     *
     * @param timeout the maximum time to wait for a service, zero(0) means wait forever
     *
     * @return a service that matches the selection criteria
     * for this cache, if a comparator is present the best service is returned.
     */
    public Object getService(long timeout) {
        Object s = null;
        s = getService();
        if (s == null) {
            synchronized (this) {
                try {
                    wait(timeout);
                } catch (Exception ignore) {
                    ;
                }
            }
            s = getService();
        }
        return (s);
    }

    /**
     * Get a service item from the cache
     *
     * @param timeout the maximum time to wait for a service, zero(0) means wait forever
     *
     * @return a service item that matches the selection criteria
     * for this cache, if a comparator is present the best service is returned
     */
    public ServiceItem getServiceItem(long timeout) {
        ServiceItem s = null;
        s = getServiceItem();
        if (s == null) {
            synchronized (this) {
                try {
                    wait(timeout);
                } catch (Exception ignore) {
                    ;
                }
            }
            s = getServiceItem();
        }
        return (s);
    }

    /**
     * Get a ServiceItem from the cache given a ServiceID
     *
     * @param id the ServiceID for the service
     *
     * @return the ServiceItem from the cache given a ServiceID or null if no service matched 
     * the serviceID
     */
    public ServiceItem getServiceItemFromCache(ServiceID id) {
        ServiceCacheItem[] cItems = getServiceCacheItems();
        for (int i = 0; i < cItems.length; i++) {
            ServiceItem item = cItems[i].getServiceItem();
            if (item.serviceID.equals(id)) return (item);
        }
        return (null);
    }

    /**
     * Stop this ServiceCache. This will unregister from the lookup services, clear the listener
     * list and unexport the ServiceWatcher instance
     */
    public void terminate() {
        if (listenerList != null) {
            listenerList.clear();
            listenerList = null;
        }
        if (watcher != null) watcher.terminate();
        cacheItems.clear();
    }

    /**
     * Implement ServiceEventListener#serviceNotify
     */
    public void serviceNotify(ServiceEvent sEvent, ServiceRegistrar registrar) {
        ServiceID serviceID = sEvent.getServiceID();
        ServiceItem sItem = sEvent.getServiceItem();
        ServiceCacheEvent cEvent = new ServiceCacheEvent(serviceID, sItem, registrar);
        switch(sEvent.getTransition()) {
            case ServiceRegistrar.TRANSITION_MATCH_MATCH:
                if (sItem == null) return;
                changeCacheEvent(cEvent);
                break;
            case ServiceRegistrar.TRANSITION_MATCH_NOMATCH:
                removeCacheEvent(cEvent);
                break;
            case ServiceRegistrar.TRANSITION_NOMATCH_MATCH:
                if (sItem == null) return;
                addCacheItem(cEvent);
                break;
            default:
                System.out.println("ServiceCache: Unknown transition");
                break;
        }
    }

    /**
     * Start a ServiceWatcher
     *
     * @param discoverer - The DiscoveryManagement to use
     * @param template - The match for services
     * @param registrars - The registrars to use to lookup the services matching the
     * template
     */
    public void startWatch(DiscoveryManagement discoverer, ServiceTemplate template, ServiceRegistrar[] registrars) throws Exception {
        if (template == null) throw new IllegalArgumentException("template is null");
        if (discoverer == null) throw new IllegalArgumentException("discoverer is null");
        discoverer.addDiscoveryListener(this);
        this.template = template;
        this.watcher = new ServiceWatcher(registrars, template, this, config);
        watcher.setDiscoverer(discoverer);
        for (int i = 0; i < registrars.length; i++) doLookup(registrars[i]);
    }

    /**
     * Perform a lookup
     * 
     * @param registrar - The ServiceRegistrar to perform lookups against
     */
    private void doLookup(ServiceRegistrar registrar) {
        ServiceItem[] items = null;
        try {
            ServiceMatches matches = registrar.lookup(template, Integer.MAX_VALUE);
            items = matches.items;
            if (items == null) return;
            for (int i = 0; i < items.length; i++) {
                ServiceCacheEvent cEvent = new ServiceCacheEvent(items[i].serviceID, items[i], registrar);
                addCacheItem(cEvent);
            }
        } catch (RemoteException re) {
            re.printStackTrace();
            if (discoverer != null) discoverer.discard(registrar);
        }
    }

    /**
     * Get the registered ServiceCacheListener instances
     * 
     * @return ServiceCacheListener[] - Registered ServiceCacheListener instances
     */
    private ServiceCacheListener[] getServiceCacheListeners() {
        synchronized (listenerList) {
            return ((ServiceCacheListener[]) listenerList.toArray(new ServiceCacheListener[listenerList.size()]));
        }
    }

    /**
     * Add an item to the ServiceCache
     * 
     * @param cEvent - The ServiceCacheEvent
     */
    private void addCacheItem(ServiceCacheEvent cEvent) {
        try {
            ServiceCacheItem cItem = getServiceCacheItem(cEvent);
            synchronized (cacheItems) {
                if (cItem == null) {
                    cItem = new ServiceCacheItem(cEvent.serviceID, cEvent.item, cEvent.registrar);
                    cacheItems.add(cItem);
                } else {
                    int index = cacheItems.indexOf(cItem);
                    if (cItem.addServiceRegistrar(cEvent.registrar)) cacheItems.set(index, cItem); else {
                        return;
                    }
                }
            }
            if (listenerList == null) return;
            ServiceCacheListener[] sCaches = getServiceCacheListeners();
            for (int i = 0; i < sCaches.length; i++) sCaches[i].serviceAdded(cEvent);
            synchronized (this) {
                notifyAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Modify a ServiceCache item
     * 
     * @param cEvent - The ServiceCacheEvent
     */
    private void changeCacheEvent(ServiceCacheEvent cEvent) {
        try {
            synchronized (cacheItems) {
                ServiceCacheItem cItem = getServiceCacheItem(cEvent);
                if (cItem == null) return;
                int index = cacheItems.indexOf(cItem);
                cItem.item = cEvent.item;
                cacheItems.set(index, cItem);
            }
            if (listenerList == null) return;
            ServiceCacheListener[] sCaches = getServiceCacheListeners();
            for (int i = 0; i < sCaches.length; i++) {
                sCaches[i].serviceChanged(cEvent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove an item from the ServiceCache
     * 
     * @param cEvent - The ServiceCacheEvent
     */
    private void removeCacheEvent(ServiceCacheEvent cEvent) {
        try {
            ServiceCacheItem cItem = getServiceCacheItemFromID(cEvent);
            if (cItem != null) {
                synchronized (cacheItems) {
                    int index = cacheItems.indexOf(cItem);
                    cItem.removeServiceRegistrar(cEvent.registrar);
                    if (cItem.getServiceRegistrars().length == 0) cacheItems.remove(index); else cacheItems.set(index, cItem);
                }
                cEvent.item = cItem.getServiceItem();
            }
            if (listenerList == null) return;
            ServiceCacheListener[] sCaches = getServiceCacheListeners();
            for (int i = 0; i < sCaches.length; i++) {
                sCaches[i].serviceRemoved(cEvent);
            }
            watcher.refresh(cEvent.registrar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a ServiceCacheItem from the ServiceCache using the ServiceID in the 
     * ServiceCacheEvent
     * 
     * @param cEvent - The ServiceCacheEvent
     */
    private ServiceCacheItem getServiceCacheItemFromID(ServiceCacheEvent cEvent) {
        synchronized (cacheItems) {
            sortServiceItems();
            Iterator it = cacheItems.iterator();
            while (it.hasNext()) {
                ServiceCacheItem sci = (ServiceCacheItem) it.next();
                if (sci.serviceID.equals(cEvent.serviceID)) return (sci);
            }
        }
        return (null);
    }

    /**
     * Get a ServiceCacheItem from the ServiceCache
     * 
     * @param cEvent - The ServiceCacheEvent
     */
    private ServiceCacheItem getServiceCacheItem(ServiceCacheEvent cEvent) {
        synchronized (cacheItems) {
            sortServiceItems();
            Iterator it = cacheItems.iterator();
            while (it.hasNext()) {
                ServiceCacheItem sci = (ServiceCacheItem) it.next();
                if (sci.item.service != null) {
                    if (sci.item.serviceID.equals(cEvent.item.serviceID)) return (sci);
                }
            }
        }
        return (null);
    }

    /**
     * Get all ServiceCacheItems from the ServiceCache
     * 
     * @return ServiceCacheItem[] - An array of ServiceCacheItem instances. This method 
     * allocates a new array each time, if there are no items in the cache, return
     * an empty array
     */
    private ServiceCacheItem[] getServiceCacheItems() {
        synchronized (cacheItems) {
            int size = cacheItems.size();
            sortServiceItems();
            return (ServiceCacheItem[]) cacheItems.toArray(new ServiceCacheItem[size]);
        }
    }

    /** 
     * Getter for property serviceComparator
     * 
     * @return Value of property serviceComparator, a service comparator
     * compares to ServiceCacheItems and allows the cacehed items to be sorted
     * before they are fetched, if null then a round-robin fetch is used.     
     */
    public Comparator getServiceComparator() {
        return serviceComparator;
    }

    /** 
     * Setter for property serviceComparator
     *
     * @param serviceComparator New value of property serviceComparator, a service comparator
     * compares to ServiceCacheItems and allows the cached items to be sorted
     * before they are fetched, if null then a round-robin fetch is used, and list are
     * sorted by discovery time.
     */
    public void setServiceComparator(Comparator serviceComparator) {
        this.serviceComparator = serviceComparator;
    }

    /**
     * Sort the items in the ServiceCache using a comparator. if there is no comparator
     * do nothing
     */
    private void sortServiceItems() {
        if (serviceComparator != null && cacheItems.size() > 1) Collections.sort(cacheItems, serviceComparator);
    }

    /**
     * Add ServiceRegistrar instances
     * 
     * @param registrars - ServiceRegistrar instances to add
     */
    private void addRegistrars(ServiceRegistrar[] registrars) {
        if (watcher == null) return;
        watcher.addRegistrar(registrars);
        ServiceCacheListener[] sCaches = getServiceCacheListeners();
        for (int i = 0; i < sCaches.length; i++) {
            sCaches[i].registrarsAdded(registrars);
        }
        for (int i = 0; i < registrars.length; i++) doLookup(registrars[i]);
    }

    /**
     * Remove ServiceRegistrar instances
     * 
     * @param registrars - ServiceRegistrar instances to remove
     */
    private void removeRegistrars(ServiceRegistrar[] registrars) {
        for (int i = 0; i < registrars.length; i++) {
            watcher.removeRegistrar(registrars[i]);
        }
        ServiceCacheListener[] sCaches = getServiceCacheListeners();
        for (int i = 0; i < sCaches.length; i++) sCaches[i].registrarsRemoved(registrars);
    }

    /**
     * @see net.jini.discovery.DiscoveryListener#discovered
     */
    public void discovered(DiscoveryEvent de) {
        addRegistrars(de.getRegistrars());
    }

    /**
     * @see net.jini.discovery.DiscoveryListener#discarded
     */
    public void discarded(DiscoveryEvent dEvent) {
        removeRegistrars(dEvent.getRegistrars());
    }
}
