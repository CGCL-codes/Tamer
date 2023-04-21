package jimo.osgi.modules.core;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import jimo.osgi.api.EventListenerAdapter;
import jimo.osgi.api.FrameworkException;
import jimo.osgi.api.JIMOConstants;
import jimo.osgi.api.Persistence;
import jimo.osgi.api.util.LogUtil;
import jimo.osgi.impl.framework.BundleImpl;
import jimo.osgi.impl.framework.EventRegistryException;
import jimo.osgi.impl.framework.EventRegistryImpl;
import jimo.osgi.impl.framework.FrameworkImpl;
import jimo.osgi.impl.framework.FrameworkImpl.Event;
import jimo.osgi.impl.util.Executable;
import jimo.osgi.impl.util.ExecutableClassloaderWrapper;
import jimo.osgi.impl.util.ExecutableRunnable;
import jimo.osgi.impl.util.FilterImpl;
import jimo.osgi.impl.util.ExecutableRunnable.ExceptionHandler;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationEvent;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ConfigurationListener;
import org.osgi.service.cm.ConfigurationPlugin;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class ConfigurationAdminImpl {

    public class EventListenerAdapterImpl implements EventListenerAdapter {

        public void handle(EventObject event, EventListener listener) {
            ConfigurationEvent configurationEvent = (ConfigurationEvent) event.getSource();
            ServiceReference[] serviceReferences = null;
            try {
                serviceReferences = context.getServiceReferences(ConfigurationListener.class.getName(), null);
            } catch (InvalidSyntaxException e) {
            }
            if (serviceReferences == null) {
                return;
            }
            for (int i = 0; i < serviceReferences.length; i++) {
                ServiceReference reference = serviceReferences[i];
                BundleImpl bundle = (BundleImpl) reference.getBundle();
                ClassLoader loader = bundle.getBundleClassLoader();
                ConfigurationListener handler = (ConfigurationListener) context.getService(reference);
                if (handler != null) {
                    ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
                    Thread.currentThread().setContextClassLoader(loader);
                    try {
                        handler.configurationEvent(configurationEvent);
                    } catch (Throwable e) {
                        Core.getLogService().log(LogService.LOG_WARNING, "ConfigurationListener error", e);
                    } finally {
                        Thread.currentThread().setContextClassLoader(oldLoader);
                    }
                }
            }
        }
    }

    ;

    public class ConfigurationAdminFactoryImpl implements ServiceFactory {

        public Object getService(final Bundle bundle, ServiceRegistration registration) {
            return new ConfigurationAdmin() {

                public Configuration createFactoryConfiguration(String factoryPid) throws IOException {
                    return ConfigurationAdminImpl.this.createFactoryConfiguration(factoryPid, bundle.getLocation(), bundle);
                }

                public Configuration createFactoryConfiguration(String factoryPid, String location) throws IOException {
                    return ConfigurationAdminImpl.this.createFactoryConfiguration(factoryPid, location, bundle);
                }

                public Configuration getConfiguration(String pid) throws IOException {
                    return ConfigurationAdminImpl.this.getConfiguration(pid, bundle.getLocation(), bundle);
                }

                public Configuration getConfiguration(String pid, String location) throws IOException {
                    return ConfigurationAdminImpl.this.getConfiguration(pid, location, bundle);
                }

                public Configuration[] listConfigurations(String filter) throws IOException, InvalidSyntaxException {
                    return ConfigurationAdminImpl.this.listConfigurations(filter);
                }
            };
        }

        public void ungetService(Bundle bundle, ServiceRegistration registration, Object service) {
        }
    }

    class ConfigurationImpl implements Configuration {

        ConfigurationDictionary properties;

        String location;

        private String factoryPid;

        private String pid;

        public ConfigurationImpl(String pid, String factoryPid, String location) {
            log.debug("Created configuration <" + pid + " " + factoryPid + "> for bundle " + location);
            this.pid = pid;
            this.factoryPid = factoryPid;
            this.location = location;
        }

        public void delete() throws IOException {
            log.debug("Deleting configuration <" + pid + " " + factoryPid + ">");
            mapConfigurations.remove(pid);
            getStorage().removeItem(pid);
            final ManagedServiceFactory factory = (ManagedServiceFactory) mapPidServices.get(factoryPid);
            ExecutableRunnable r = null;
            if (factory != null) {
                String factoryLocation = (String) mapPidLocation.get(factoryPid);
                BundleImpl factoryBundle = (BundleImpl) FrameworkImpl.INSTANCE.getBundleRegistry().getBundle(new URL(factoryLocation));
                if (checkConfigurationPermissions(this, factoryLocation)) {
                    r = new ExecutableClassloaderWrapper(new Executable() {

                        public Object execute() throws ConfigurationException {
                            log.debug("Factory delete configuration <" + pid + " " + factoryPid + ">");
                            factory.deleted(pid);
                            return null;
                        }
                    }, factoryBundle.getBundleClassLoader());
                }
            }
            final ManagedService managedService = (ManagedService) mapPidServices.get(pid);
            if (managedService != null) {
                if (factory != null) throw new RuntimeException("Naming collision on service.pid=" + pid);
                String serviceLocation = (String) mapPidLocation.get(pid);
                BundleImpl serviceBundle = (BundleImpl) FrameworkImpl.INSTANCE.getBundleRegistry().getBundle(new URL(serviceLocation));
                if (checkConfigurationPermissions(this, serviceLocation)) {
                    r = new ExecutableClassloaderWrapper(new Executable() {

                        public Object execute() throws ConfigurationException {
                            log.debug("Service delete configuration <" + pid + " " + factoryPid + ">");
                            managedService.updated(null);
                            return null;
                        }
                    }, serviceBundle.getBundleClassLoader());
                }
            }
            if (r != null) {
                r.setHandler(new ExceptionHandler() {

                    public void handle(Throwable exception) {
                        Core.getLogService().log(LogService.LOG_ERROR, "Framework error", exception);
                    }
                });
                Event event;
                try {
                    event = FrameworkImpl.newEvent(r, true);
                    FrameworkImpl.fireEvent(event);
                } catch (FrameworkException e) {
                    log.error(e);
                }
            }
            postEvent(new ConfigurationEvent(registration.getReference(), ConfigurationEvent.CM_DELETED, factoryPid, pid));
        }

        public String getBundleLocation() {
            return location;
        }

        public String getFactoryPid() {
            return factoryPid;
        }

        public String getPid() {
            return pid;
        }

        public Dictionary getProperties() {
            if (properties == null) return null;
            Hashtable table = new Hashtable();
            String[] strings = properties.keyArray();
            for (int i = 0; i < strings.length; i++) {
                table.put(strings[i], properties.get(strings[i]));
            }
            return table;
        }

        public void setBundleLocation(String bundleLocation) {
            location = bundleLocation;
        }

        public void update() throws IOException {
            log.debug("Updating configuration <" + pid + " " + factoryPid + ">");
            final Hashtable table = new Hashtable();
            if (properties != null) {
                String[] keys = properties.keyArray();
                for (int i = 0; i < keys.length; i++) {
                    table.put(keys[i], properties.get(keys[i]));
                }
                getStorage().storeItem(table);
                ServiceReference reference = (ServiceReference) mapPidReference.get(getPid());
                String filter = "(| (" + ConfigurationPlugin.CM_TARGET + "=" + getPid() + ") " + " (! (" + ConfigurationPlugin.CM_TARGET + "=*" + ") )" + " )";
                ServiceReference[] serviceReferences = null;
                try {
                    serviceReferences = context.getServiceReferences(ConfigurationPlugin.class.getName(), filter);
                } catch (InvalidSyntaxException e1) {
                    log.error("Syntax error");
                    log.error(e1);
                }
                if (serviceReferences != null) {
                    Arrays.sort(serviceReferences, new Comparator() {

                        public int compare(Object o1, Object o2) {
                            ServiceReference ref1 = (ServiceReference) o1;
                            ServiceReference ref2 = (ServiceReference) o2;
                            Integer rank1 = null;
                            try {
                                rank1 = (Integer) ref1.getProperty(ConfigurationPlugin.CM_RANKING);
                            } catch (ClassCastException e) {
                            }
                            if (rank1 == null) rank1 = new Integer(0);
                            Integer rank2 = null;
                            try {
                                rank2 = (Integer) ref2.getProperty(ConfigurationPlugin.CM_RANKING);
                            } catch (ClassCastException e) {
                            }
                            if (rank2 == null) rank2 = new Integer(0);
                            return rank1.compareTo(rank2);
                        }
                    });
                    for (int i = 0; i < serviceReferences.length; i++) {
                        ConfigurationPlugin plugin = (ConfigurationPlugin) context.getService(serviceReferences[i]);
                        try {
                            log.debug("ConfigurationPlugin found for pid <" + pid + ">");
                            plugin.modifyConfiguration(reference, table);
                        } catch (Throwable e) {
                            log.warn(e);
                        }
                    }
                    properties = new ConfigurationDictionary(table);
                }
                table.remove(ConfigurationAdmin.SERVICE_BUNDLELOCATION);
            }
            ExecutableRunnable r = null;
            String factoryLocation = (String) mapPidLocation.get(factoryPid);
            if (factoryLocation != null && checkConfigurationPermissions(this, factoryLocation)) {
                BundleImpl factoryBundle = (BundleImpl) FrameworkImpl.INSTANCE.getBundleRegistry().getBundle(new URL(factoryLocation));
                r = new ExecutableClassloaderWrapper(new Executable() {

                    public Object execute() throws ConfigurationException {
                        log.debug("Factory update for pid <" + pid + " " + factoryPid + ">");
                        ManagedServiceFactory factory = (ManagedServiceFactory) mapPidServices.get(factoryPid);
                        if (factory == null) {
                            return null;
                        }
                        if (properties != null) {
                            factory.updated(pid, table);
                        } else {
                            factory.updated(pid, null);
                        }
                        postEvent(new ConfigurationEvent(registration.getReference(), ConfigurationEvent.CM_UPDATED, factoryPid, pid));
                        return null;
                    }
                }, factoryBundle.getBundleClassLoader());
            }
            final ManagedService managedService = (ManagedService) mapPidServices.get(pid);
            if (managedService != null) {
                String serviceLocation = (String) mapPidLocation.get(pid);
                BundleImpl serviceBundle = (BundleImpl) FrameworkImpl.INSTANCE.getBundleRegistry().getBundle(new URL(serviceLocation));
                if (checkConfigurationPermissions(this, serviceLocation)) {
                    r = new ExecutableClassloaderWrapper(new Executable() {

                        public Object execute() throws ConfigurationException {
                            log.debug("Service update for pid <" + pid + ">");
                            if (properties != null) {
                                managedService.updated(table);
                            } else {
                                managedService.updated(null);
                            }
                            postEvent(new ConfigurationEvent(registration.getReference(), ConfigurationEvent.CM_UPDATED, factoryPid, pid));
                            return null;
                        }
                    }, serviceBundle.getBundleClassLoader());
                }
            }
            if (r != null) {
                r.setHandler(new ExceptionHandler() {

                    public void handle(Throwable exception) {
                        log.warn("Configuration error");
                        log.warn(exception);
                    }
                });
                try {
                    Event event = FrameworkImpl.newEvent(r, true);
                    FrameworkImpl.fireEvent(event);
                } catch (FrameworkException e) {
                    log.error(e);
                }
            }
        }

        public void update(Dictionary updatedProperties) throws IOException {
            log.debug("Configuration update");
            if (updatedProperties != null) {
                updatedProperties.put(Constants.SERVICE_PID, pid);
                if (factoryPid != null) updatedProperties.put(ConfigurationAdmin.SERVICE_FACTORYPID, factoryPid);
                if (location != null) updatedProperties.put(ConfigurationAdmin.SERVICE_BUNDLELOCATION, location);
                properties = new ConfigurationDictionary(updatedProperties);
            } else {
                properties = null;
            }
            update();
        }
    }

    /**
	 * Map to bind pid to location
 	 */
    Map mapPidLocation = Collections.synchronizedMap(new HashMap());

    /**
	 * Map to bind pid to service reference
	 */
    Map mapPidReference = Collections.synchronizedMap(new HashMap());

    /**
	 * Map servicePid to service, or factory pid to factory
	 */
    Map mapPidServices = Collections.synchronizedMap(new HashMap());

    /**
	 * Map pid to configuration
	 */
    Map mapConfigurations = Collections.synchronizedMap(new HashMap());

    ServiceTracker trackManagedServiceFactories;

    ServiceTracker trackManagedServices;

    Map mapLocation = Collections.synchronizedMap(new HashMap());

    String storageImplClassname;

    private Persistence storage;

    private BundleContext context;

    private EventRegistryImpl eventRegistry;

    private ServiceRegistration registration;

    static LogUtil log;

    public ConfigurationAdminImpl(final BundleContext context) {
        this.context = context;
        log = new LogUtil(context, getClass().getName(), null);
        registration = context.registerService(ConfigurationAdmin.class.getName(), new ConfigurationAdminFactoryImpl(), null);
        trackManagedServiceFactories = new ServiceTracker(context, ManagedServiceFactory.class.getName(), new ServiceTrackerCustomizer() {

            public Object addingService(ServiceReference reference) {
                return addManagedServiceFactory(reference);
            }

            public void modifiedService(ServiceReference reference, Object service) {
            }

            public void removedService(ServiceReference reference, Object service) {
                removeServiceFactory(reference);
            }
        });
        trackManagedServiceFactories.open();
        trackManagedServices = new ServiceTracker(context, ManagedService.class.getName(), new ServiceTrackerCustomizer() {

            public Object addingService(ServiceReference reference) {
                return addManagedService(reference);
            }

            public void modifiedService(ServiceReference reference, Object service) {
            }

            public void removedService(ServiceReference reference, Object service) {
                removeService(reference);
            }
        });
        trackManagedServices.open();
        storageImplClassname = context.getProperty(JIMOConstants.KEY_CONFIGURATIONPERSISTENCEIMPL);
        eventRegistry = new EventRegistryImpl(new EventListenerAdapterImpl());
        eventRegistry.addListener(new EventListener() {
        }, getClass().getClassLoader(), null);
        ServiceReference[] serviceReferences = trackManagedServiceFactories.getServiceReferences();
        if (serviceReferences != null) {
            for (int i = 0; i < serviceReferences.length; i++) {
                ServiceReference reference = serviceReferences[i];
                addManagedServiceFactory(reference);
            }
        }
        if (serviceReferences != null) {
            serviceReferences = trackManagedServices.getServiceReferences();
            for (int i = 0; i < serviceReferences.length; i++) {
                ServiceReference reference = serviceReferences[i];
                addManagedService(reference);
            }
        }
        log.info(getClass().getName() + " started.");
        log.debug("Backing store is <" + storageImplClassname + ">");
    }

    protected void removeService(ServiceReference reference) {
        String pid = (String) reference.getProperty(Constants.SERVICE_PID);
        log.debug("ManagedService" + pid + " removed.");
        if (pid != null) {
            mapPidServices.remove(pid);
            mapPidLocation.remove(pid);
            mapPidReference.remove(pid);
        }
    }

    protected ManagedService addManagedService(ServiceReference reference) {
        ManagedService managedService = (ManagedService) context.getService(reference);
        String pid = (String) reference.getProperty(Constants.SERVICE_PID);
        if (pid != null) {
            log.debug("Adding ManagedService " + pid);
            mapPidServices.put(pid, managedService);
            mapPidLocation.put(pid, reference.getBundle().getLocation());
            mapPidReference.put(pid, reference);
            Dictionary dictionary = null;
            try {
                dictionary = getStorage().findItem(pid);
                if (dictionary == null) {
                    dictionary = new Hashtable();
                    String[] keys = reference.getPropertyKeys();
                    for (int i = 0; i < keys.length; i++) {
                        dictionary.put(keys[i], reference.getProperty(keys[i]));
                    }
                }
                Configuration configuration = createConfiguration(dictionary, reference.getBundle());
            } catch (IOException e) {
                Core.getLogService().log(LogService.LOG_ERROR, "IO Exception", e);
            }
        }
        return managedService;
    }

    protected void removeServiceFactory(ServiceReference reference) {
        String pid = (String) reference.getProperty(Constants.SERVICE_PID);
        log.debug("ManagedServiceFactory " + pid + " removed.");
        if (pid != null) {
            mapPidServices.remove(pid);
            mapPidLocation.remove(pid);
            mapPidReference.remove(pid);
        }
    }

    protected ManagedServiceFactory addManagedServiceFactory(ServiceReference reference) {
        String factoryPid = (String) reference.getProperty(Constants.SERVICE_PID);
        ManagedServiceFactory factory = (ManagedServiceFactory) context.getService(reference);
        log.debug("Adding ManagedServiceFactory " + factoryPid);
        if (factoryPid != null) {
            mapPidServices.put(factoryPid, factory);
            mapPidLocation.put(factoryPid, reference.getBundle().getLocation());
            mapPidReference.put(factoryPid, reference);
            Dictionary[] dictionaries = null;
            try {
                dictionaries = getStorage().findItems(factoryPid);
            } catch (IOException e) {
                log.error(e);
            }
            if (dictionaries != null) {
                for (int i = 0; i < dictionaries.length; i++) {
                    try {
                        Configuration configuration = createConfiguration(dictionaries[i], reference.getBundle());
                    } catch (IOException e) {
                        log.error(e);
                    }
                }
            }
            return factory;
        } else {
            return null;
        }
    }

    public Configuration createFactoryConfiguration(String factoryPid, String location, Bundle bundle) throws IOException {
        String pid = createPid(bundle);
        return createConfiguration(pid, factoryPid, location);
    }

    static long pidNumber = 0;

    private String createPid(Bundle bundle) {
        String pid = null;
        while (pid == null) {
            pid = "" + bundle.getBundleId() + "." + pidNumber++;
            if (mapConfigurations.containsKey(pid)) {
                pid = null;
            }
        }
        return pid;
    }

    public Configuration getConfiguration(String pid, String location, Bundle bundle) throws IOException {
        Configuration configuration;
        log.debug("getConfiguration " + pid + " " + location);
        configuration = (Configuration) mapConfigurations.get(pid);
        if (configuration == null) {
            configuration = createConfiguration(pid, null, location);
        }
        if (!checkConfigurationPermissions(configuration, bundle.getLocation())) return null;
        return configuration;
    }

    private boolean checkConfigurationPermissions(Configuration configuration, String bundleLocation) {
        if (configuration.getBundleLocation() == null || configuration.getBundleLocation().equals(bundleLocation)) return true;
        return true;
    }

    private Configuration createConfiguration(String pid, String factoryPid, String location) throws IOException {
        log.debug("createConfiguration " + pid + " " + " " + factoryPid + " " + location);
        ConfigurationImpl configuration = new ConfigurationImpl(pid, factoryPid, location);
        mapConfigurations.put(pid, configuration);
        Dictionary dictionary = getStorage().findItem(pid);
        configuration.update(dictionary);
        return configuration;
    }

    protected Configuration createConfiguration(Dictionary dictionary, Bundle bundle) throws IOException {
        String pid = (String) dictionary.get(Constants.SERVICE_PID);
        String factoryPid = (String) dictionary.get(ConfigurationAdmin.SERVICE_FACTORYPID);
        String location = (String) dictionary.get(ConfigurationAdmin.SERVICE_BUNDLELOCATION);
        Configuration configuration = createConfiguration(pid, factoryPid, location);
        if (dictionary != null) {
            configuration.update(dictionary);
        }
        return configuration;
    }

    private Persistence getStorage() {
        if (storage == null) {
            try {
                Class cls = Class.forName(storageImplClassname);
                if (cls != null) {
                    storage = (Persistence) cls.newInstance();
                } else {
                    log.error("Could not find configuration persistence class <" + storageImplClassname + ">");
                }
            } catch (ClassNotFoundException e) {
                log.error("Exception creating ConfigurationStorage <" + storageImplClassname + ">");
                log.error(e);
            } catch (InstantiationException e) {
                log.error("Exception creating ConfigurationStorage <" + storageImplClassname + ">");
                log.error(e);
            } catch (IllegalAccessException e) {
                log.error("Exception creating ConfigurationStorage <" + storageImplClassname + ">");
                log.error(e);
            }
        }
        return storage;
    }

    public Configuration[] listConfigurations(String filter) throws IOException, InvalidSyntaxException {
        log.debug("listConfigurations " + filter);
        if (filter == null) return (Configuration[]) mapConfigurations.values().toArray(new Configuration[mapConfigurations.values().size()]);
        Set res = new HashSet();
        FilterImpl f = new FilterImpl(filter);
        for (Iterator iter = mapConfigurations.values().iterator(); iter.hasNext(); ) {
            Configuration element = (Configuration) iter.next();
            if (f.match(element.getProperties())) res.add(element);
        }
        return (Configuration[]) res.toArray(new Configuration[res.size()]);
    }

    protected void postEvent(ConfigurationEvent configEvent) {
        EventObject eventObject = new EventObject(configEvent);
        try {
            eventRegistry.postEvent(eventObject);
        } catch (EventRegistryException e) {
            Core.getLogService().log(LogService.LOG_ERROR, "ConfigurationEvent registry error", e);
        }
    }
}
