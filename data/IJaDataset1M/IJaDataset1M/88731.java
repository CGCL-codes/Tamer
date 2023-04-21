package org.remotercp.common.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.remotercp.common.CommonActivator;

/**
 * This class offers an application wide container for services.
 * <p>
 * To register as a listener for a specific service, simply implement the
 * {@link ServiceListener} interface in your class and register the listener
 * using:
 * 
 * <pre>
 * ServiceProvider.addServiceListener(IService.class, this);
 * </pre>
 * <p>
 * The service offering plug-in can simply register it's service using the
 * BundleContext capabilities for registering services.
 * <p>
 * 
 */
public final class ServiceProvider {

    /**
	 * Map that holds the service implementation for a given service interface.
	 */
    private static final Map<Class<?>, Object> services = new HashMap<Class<?>, Object>();

    /**
	 * Map that holds a list of listeners for a given service interface.
	 */
    private static final Map<Class<?>, List<ServiceListener<?>>> listeners = new HashMap<Class<?>, List<ServiceListener<?>>>();

    /**
	 * Holds all service bridges, so we don't have to create them twice
	 */
    private static final Map<Class<?>, ServiceBridge<?>> bridges = new HashMap<Class<?>, ServiceBridge<?>>();

    /**
	 * Application wide, so no instances needed.
	 */
    private ServiceProvider() {
    }

    /**
	 * Checks if a service for the given class is available.
	 * 
	 * @param clazz
	 *            - the class object of the service's interface
	 * @return <code>true</code> if service is available, <code>false</code>
	 *         else
	 */
    public static boolean hasService(Class<?> clazz) {
        return getServicePrivate(clazz) != null;
    }

    /**
	 * Obtains - if available - an implementation of the given class.
	 * 
	 * @require <code>hasService(clazz) == true</code>
	 * 
	 * @param <S>
	 *            - represents the type of the service
	 * @param clazz
	 *            - the class object of the service's interface
	 * @return the service implementation or <code>null</code> if not available
	 */
    @SuppressWarnings("unchecked")
    public static synchronized <S> S getService(Class<S> clazz) {
        assert hasService(clazz) : "Precondition violated: hasService(" + clazz + ") == true";
        return (S) getServicePrivate(clazz);
    }

    /**
	 * Adds a service identified by the <code>clazz</code> parameter to the
	 * container.
	 * <p>
	 * Registered listeners will get notified.
	 * 
	 * @param <S>
	 *            - represents the type of the service
	 * @param clazz
	 *            - the class object of the service's interface
	 * @param service
	 *            - the service implementation
	 */
    static final synchronized <S> void addService(Class<?> clazz, S service) {
        if (services.containsKey(clazz)) {
            throw new IllegalArgumentException("an implementation of '" + clazz.getName() + "' was already added");
        }
        services.put(clazz, service);
        if (listeners.containsKey(clazz)) {
            notifyListenersForServiceAdded(clazz);
        }
    }

    /**
	 * Removes a service identified by the <code>clazz</code> parameter from the
	 * container.
	 * <p>
	 * Registered listeners will get notified.
	 * 
	 * @param <S>
	 *            - represents the type of the service
	 * @param clazz
	 *            - the class object of the service's interface
	 */
    @SuppressWarnings("unchecked")
    static final synchronized <S> void removeService(Class<S> clazz) {
        S service = (S) services.get(clazz);
        if (service != null) {
            services.remove(clazz);
            notifyListenersForServiceRemoved(clazz, service);
        }
    }

    /**
	 * Adds a service listener for the service identified by the
	 * <code>clazz</code> parameter.
	 * <p>
	 * The listener gets notified when the given service is added or removed. If
	 * the matching service was registered before this listener was added, the
	 * listener will still get notified.
	 * 
	 * @param <S>
	 *            - represents the type of the service
	 * @param clazz
	 *            - the class object of the service's interface
	 * @param listener
	 *            - the listener class
	 */
    public static final synchronized <S> void addServiceListener(Class<S> clazz, ServiceListener<S> listener) {
        if (!bridges.containsKey(clazz)) {
            addServiceBridge(clazz);
        }
        if (listeners.get(clazz) == null) {
            listeners.put(clazz, new ArrayList<ServiceListener<?>>());
        }
        if (!listeners.get(clazz).contains(listener)) {
            listeners.get(clazz).add(listener);
            if (services.get(clazz) != null) {
                notifyListenerForServiceAdded(listener, clazz);
            }
        }
    }

    /**
	 * Removes a listener for the service identified by the <code>clazz</code>
	 * parameter.
	 * <p>
	 * The listener gets notified when the given service is added or removed.
	 * 
	 * @param <S>
	 *            - represents the type of the service
	 * @param clazz
	 *            - the class object of the service's interface
	 * @param listener
	 *            - the listener class
	 */
    public static final synchronized <S> void removeServiceListener(Class<S> clazz, ServiceListener<S> listener) {
        if (listeners.get(clazz) != null) {
            listeners.get(clazz).remove(listener);
        }
        if (listeners.get(clazz).size() == 0) {
            bridges.get(clazz).close();
            bridges.remove(clazz);
        }
    }

    private static synchronized <S> Object getServicePrivate(Class<S> clazz) {
        if (!bridges.containsKey(clazz)) {
            addServiceBridge(clazz);
        }
        return services.get(clazz);
    }

    /**
	 * Notifies the listeners when the matching service was added.
	 * 
	 * @param <S>
	 *            - represents the type of the service
	 * @param clazz
	 *            - the class object of the service's interface
	 */
    @SuppressWarnings("unchecked")
    private static final synchronized <S> void notifyListenersForServiceAdded(Class<S> clazz) {
        for (ServiceListener<?> listener : listeners.get(clazz)) {
            ServiceListener<S> casted = (ServiceListener<S>) listener;
            casted.bindService((S) services.get(clazz));
        }
    }

    /**
	 * Notifies a single listener that the matching service was added.
	 * 
	 * @param <S>
	 *            - represents the type of the service
	 * @param clazz
	 *            - the class object of the service's interface
	 */
    @SuppressWarnings("unchecked")
    private static final synchronized <S> void notifyListenerForServiceAdded(ServiceListener<S> listener, Class<S> clazz) {
        ServiceListener<S> casted = (ServiceListener<S>) listener;
        casted.bindService((S) services.get(clazz));
    }

    /**
	 * Notifies the listeners when the matching service was removed.
	 * 
	 * @param <S>
	 *            - represents the type of the service
	 * @param clazz
	 *            - the class object of the service's interface
	 * @param service
	 *            - the service implementation that was removed
	 */
    @SuppressWarnings("unchecked")
    private static final synchronized <S> void notifyListenersForServiceRemoved(Class<S> clazz, S service) {
        if (listeners.get(clazz) != null) {
            for (ServiceListener<?> listener : listeners.get(clazz)) {
                ServiceListener<S> casted = (ServiceListener<S>) listener;
                casted.unbindService(service);
            }
        }
    }

    /**
	 * When the first listener is for an interface is added, this create a
	 * {@link ServiceBridge} which extends from {@link ServiceTracker} so we get
	 * notified when the given service is added or removed from the registry.
	 * 
	 * @param <S>
	 *            - the interface we are listening for
	 * @param clazz
	 *            - the class object of the interface we are listening for
	 */
    private static final synchronized <S> void addServiceBridge(Class<S> clazz) {
        BundleContext bundleContext = CommonActivator.getBundleContext();
        ServiceBridge<S> bridge = new ServiceBridge<S>(bundleContext, clazz);
        bridge.open();
        bridges.put(clazz, bridge);
    }
}
