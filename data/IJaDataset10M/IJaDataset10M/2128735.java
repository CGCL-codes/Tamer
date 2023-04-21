package org.knopflerfish.bundle.component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.SynchronousBundleListener;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationEvent;
import org.osgi.service.cm.ConfigurationListener;
import org.osgi.service.component.ComponentConstants;

class SCR implements SynchronousBundleListener {

    private Hashtable bundleConfigs = new Hashtable();

    private Collection components = new ArrayList();

    private Hashtable factoryConfigs = new Hashtable();

    private Hashtable serviceConfigs = new Hashtable();

    private static long componentId = 0;

    private static SCR instance;

    public static void init(BundleContext bc) {
        if (instance == null) {
            instance = new SCR(bc);
            Bundle[] bundles = bc.getBundles();
            for (int i = 0; i < bundles.length; i++) {
                if (bundles[i].getState() == Bundle.ACTIVE) {
                    instance.bundleChanged(new BundleEvent(BundleEvent.STARTED, bundles[i]));
                }
            }
        }
    }

    public static SCR getInstance() {
        return instance;
    }

    private SCR(BundleContext bc) {
        bc.addBundleListener(this);
        bc.registerService(ConfigurationListener.class.getName(), new CMListener(), new Hashtable());
    }

    public synchronized void shutdown() {
        for (Enumeration e = bundleConfigs.keys(); e.hasMoreElements(); ) {
            Bundle bundle = (Bundle) e.nextElement();
            bundleChanged(new BundleEvent(BundleEvent.STOPPING, bundle));
        }
        instance = null;
    }

    public synchronized void bundleChanged(BundleEvent event) {
        Bundle bundle = event.getBundle();
        String manifestEntry = (String) bundle.getHeaders().get(ComponentConstants.SERVICE_COMPONENT);
        if (manifestEntry == null) {
            return;
        }
        switch(event.getType()) {
            case BundleEvent.STARTED:
                if (manifestEntry.length() == 0) {
                    Activator.log.error("bundle #" + bundle.getBundleId() + ": header " + ComponentConstants.SERVICE_COMPONENT + " present but empty");
                    return;
                }
                Collection addedConfigs = new ArrayList();
                String[] manifestEntries = Parser.splitwords(manifestEntry, ",");
                for (int i = 0; i < manifestEntries.length; i++) {
                    URL resourceURL = bundle.getResource(manifestEntries[i]);
                    if (resourceURL == null) {
                        Activator.log.error("Resource not found: " + manifestEntries[i]);
                        continue;
                    }
                    try {
                        Collection configs = Parser.readXML(bundle, resourceURL);
                        if (configs.isEmpty()) {
                            Activator.log.warn("bundle #" + bundle.getBundleId() + ": xml-file did not contain any valid component declarations");
                        }
                        addedConfigs.addAll(configs);
                    } catch (Throwable e) {
                        Activator.log.error("bundle #" + bundle.getBundleId() + ": Failed to parse " + resourceURL + ": " + e.getCause(), e);
                    }
                }
                bundleConfigs.put(bundle, new ArrayList());
                for (Iterator iter = addedConfigs.iterator(); iter.hasNext(); ) {
                    Config config = (Config) iter.next();
                    if (config.isAutoEnabled()) {
                        Component component = config.createComponent();
                        component.enable();
                    }
                    String[] services = config.getServices();
                    if (services != null) {
                        for (int i = 0; i < services.length; i++) {
                            ArrayList existing = (ArrayList) serviceConfigs.get(services[i]);
                            if (existing == null) {
                                existing = new ArrayList();
                                serviceConfigs.put(services[i], existing);
                            }
                            existing.add(config);
                        }
                        ArrayList cycle = new ArrayList();
                        if (findCycle(config, cycle)) {
                            String message = "Possible cycle found in references of " + config.getName() + ": ";
                            Iterator citer = cycle.iterator();
                            if (citer.hasNext()) {
                                Config cycleItem = (Config) citer.next();
                                message += cycleItem.getName();
                                while (citer.hasNext()) {
                                    cycleItem = (Config) citer.next();
                                    message += " references " + cycleItem.getName();
                                }
                            }
                            Activator.log.error("bundle #" + bundle.getBundleId() + ": " + message);
                        }
                    }
                }
                Collection tmp = (Collection) bundleConfigs.get(bundle);
                tmp.addAll(addedConfigs);
                bundleConfigs.put(bundle, tmp);
                break;
            case BundleEvent.STOPPING:
                if (Activator.log.doDebug()) {
                    Activator.log.debug("bundle #" + bundle.getBundleId() + ": Bundle is STOPPING. Disable components.");
                }
                Collection removedConfigs = (Collection) bundleConfigs.remove(bundle);
                if (removedConfigs != null) {
                    for (Iterator iter = removedConfigs.iterator(); iter.hasNext(); ) {
                        Config config = (Config) iter.next();
                        config.disable();
                        String[] services = config.getServices();
                        if (services != null) {
                            for (int i = 0; i < services.length; i++) {
                                ArrayList existing = (ArrayList) serviceConfigs.get(services[i]);
                                if (existing == null) continue;
                                existing.remove(config);
                                if (existing.size() == 0) {
                                    serviceConfigs.remove(services[i]);
                                }
                            }
                        }
                    }
                }
                break;
        }
    }

    private boolean findCycle(Config config, ArrayList visited) {
        if (visited.contains(config)) {
            visited.add(config);
            return true;
        }
        visited.add(config);
        ArrayList references = config.getReferences();
        for (Iterator riter = references.iterator(); riter.hasNext(); ) {
            Reference ref = (Reference) riter.next();
            if (ref.isSatisfied()) continue;
            String service = ref.getInterfaceName();
            ArrayList providers = (ArrayList) serviceConfigs.get(service);
            if (providers != null) {
                for (Iterator piter = providers.iterator(); piter.hasNext(); ) {
                    Config provider = (Config) piter.next();
                    if (findCycle(provider, visited)) {
                        return true;
                    }
                }
            }
        }
        visited.remove(config);
        return false;
    }

    public Collection getComponents(Bundle bundle) {
        return (Collection) bundleConfigs.get(bundle);
    }

    public synchronized void initComponent(Component component) {
        component.setComponentId(new Long(++componentId));
        initConfig(component);
        components.add(component);
    }

    public synchronized void removeComponent(Component component) {
        if (component != null) {
            removeConfig(component);
            components.remove(component);
        }
    }

    private ConfigurationAdmin getCM(Component component) {
        BundleContext bc = component.getBundleContext();
        ServiceReference ref = bc.getServiceReference(ConfigurationAdmin.class.getName());
        if (ref == null) return null;
        return (ConfigurationAdmin) bc.getService(ref);
    }

    private synchronized void removeConfig(Component component) {
        Config config = component.getConfig();
        Dictionary dict = (Dictionary) factoryConfigs.get(config.getName());
        if (dict != null) {
            for (Enumeration e = dict.keys(); e.hasMoreElements(); ) {
                Object key = e.nextElement();
                if (dict.get(key) == component) {
                    dict.remove(key);
                    break;
                }
            }
            if (dict.isEmpty()) {
                factoryConfigs.remove(config.getName());
            }
        }
    }

    private synchronized void initConfig(Component component) {
        Config config = component.getConfig();
        ConfigurationAdmin admin = getCM(component);
        String name = config.getName();
        if (admin == null) return;
        try {
            Configuration[] conf = admin.listConfigurations("(" + ConfigurationAdmin.SERVICE_FACTORYPID + "=" + name + ")");
            if (conf != null) {
                Dictionary table = (Dictionary) factoryConfigs.get(name);
                if (table == null) {
                    table = new Hashtable();
                    factoryConfigs.put(name, table);
                }
                Collection configs = (Collection) bundleConfigs.get(config.getBundle());
                if (table.get(conf[0].getPid()) == null) {
                    component.cmUpdated(conf[0].getProperties());
                    table.put(conf[0].getPid(), component);
                }
                for (int i = conf.length - 1; i >= 0; i--) {
                    String pid = conf[i].getPid();
                    Component instance = (Component) table.get(pid);
                    if (instance == null) {
                        Config copy = config.copy();
                        instance = copy.createComponent();
                        instance.cmUpdated(conf[i].getProperties());
                        table.put(pid, instance);
                        configs.add(copy);
                        instance.enable();
                    }
                }
            } else {
                conf = admin.listConfigurations("(" + Constants.SERVICE_PID + "=" + name + ")");
                if (conf != null && conf.length == 1) {
                    component.cmUpdated(conf[0].getProperties());
                }
            }
        } catch (InvalidSyntaxException e) {
            Activator.log.error("The name (" + name + ") must have screwed up the filter.", e);
        } catch (IOException e) {
            Activator.log.error("Declarative Services could not retrieve " + "the configuration for component " + name + ". Got IOException.", e);
        }
    }

    private class CMListener implements ConfigurationListener {

        private Configuration getConfiguration(Component component, String pid) {
            ConfigurationAdmin admin = getCM(component);
            if (admin == null) return null;
            try {
                Configuration[] conf = admin.listConfigurations("(" + Constants.SERVICE_PID + "=" + pid + ")");
                return conf == null ? null : conf[0];
            } catch (InvalidSyntaxException e) {
                Activator.log.error("The PID (" + pid + ") must have screwed up the filter.", e);
                return null;
            } catch (IOException e) {
                Activator.log.error("Declarative Services could not retrieve " + "the configuration for component with pid " + pid + ". Got IOException.", e);
                return null;
            }
        }

        private void restart(Component component) {
            restart(component, null);
        }

        private void restart(Component component, Configuration configuration) {
            component.deactivate();
            component.unregisterService();
            if (configuration != null) {
                component.cmUpdated(configuration.getProperties());
            } else {
                component.cmDeleted();
                removeConfig(component);
            }
            component.registerService();
            component.activate();
        }

        public void configurationEvent(ConfigurationEvent evt) {
            String factoryPid = evt.getFactoryPid();
            String pid = evt.getPid();
            synchronized (SCR.getInstance()) {
                if (factoryPid != null) {
                    Dictionary table = (Dictionary) factoryConfigs.get(factoryPid);
                    if (table != null) {
                        Component component = (Component) table.get(pid);
                        if (component != null) {
                            if (evt.getType() == ConfigurationEvent.CM_DELETED) {
                                table.remove(pid);
                                if (table.isEmpty()) {
                                    factoryConfigs.remove(factoryPid);
                                    restart(component);
                                } else {
                                    component.disable();
                                }
                            } else {
                                Configuration conf = getConfiguration(component, pid);
                                if (conf == null) return;
                                restart(component, conf);
                            }
                        } else {
                            Object key = table.keys().nextElement();
                            Component src = (Component) table.get(key);
                            Config config = src.getConfig();
                            Config copy = config.copy();
                            Component instance = copy.createComponent();
                            Configuration conf = getConfiguration(instance, pid);
                            if (conf == null) return;
                            instance.cmUpdated(conf.getProperties());
                            Collection collection = (Collection) bundleConfigs.get(copy.getBundle());
                            collection.add(copy);
                            instance.enable();
                        }
                    } else {
                        for (Iterator i = components.iterator(); i.hasNext(); ) {
                            Component component = (Component) i.next();
                            Config config = component.getConfig();
                            if (factoryPid.equals(config.getName())) {
                                Configuration conf = getConfiguration(component, pid);
                                if (conf == null) continue;
                                table = new Hashtable();
                                factoryConfigs.put(factoryPid, table);
                                table.put(evt.getPid(), component);
                                restart(component, conf);
                            }
                        }
                    }
                } else {
                    for (Iterator i = components.iterator(); i.hasNext(); ) {
                        Component component = (Component) i.next();
                        Config config = component.getConfig();
                        if (pid.equals(config.getName())) {
                            if (evt.getType() == ConfigurationEvent.CM_DELETED) {
                                restart(component);
                            }
                            Configuration conf = getConfiguration(component, pid);
                            if (conf == null) continue;
                            restart(component, conf);
                        }
                    }
                }
            }
        }
    }
}
