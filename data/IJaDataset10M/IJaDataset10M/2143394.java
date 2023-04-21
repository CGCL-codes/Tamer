package com.hongbo.cobweb.specs.locator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.SynchronousBundleListener;

public class Activator implements BundleActivator, SynchronousBundleListener {

    private static boolean debug = false;

    private ConcurrentMap<Long, Map<String, Callable<Class>>> factories = new ConcurrentHashMap<Long, Map<String, Callable<Class>>>();

    private BundleContext bundleContext;

    static {
        try {
            String prop = System.getProperty("com.hongbo.cobweb.specs.debug");
            debug = prop != null && !"false".equals(prop);
        } catch (Throwable t) {
        }
    }

    /**
     * <p>Output debugging messages.</p>
     *
     * @param msg <code>String</code> to print to <code>stderr</code>.
     */
    protected void debugPrintln(String msg) {
        if (debug) {
            System.err.println("Spec(" + bundleContext.getBundle().getBundleId() + "): " + msg);
        }
    }

    public synchronized void start(BundleContext bundleContext) throws Exception {
        this.bundleContext = bundleContext;
        debugPrintln("activating");
        debugPrintln("adding bundle listener");
        bundleContext.addBundleListener(this);
        debugPrintln("checking existing bundles");
        for (Bundle bundle : bundleContext.getBundles()) {
            if (bundle.getState() == Bundle.RESOLVED || bundle.getState() == Bundle.STARTING || bundle.getState() == Bundle.ACTIVE || bundle.getState() == Bundle.STOPPING) {
                register(bundle);
            }
        }
        debugPrintln("activated");
    }

    public synchronized void stop(BundleContext bundleContext) throws Exception {
        debugPrintln("deactivating");
        bundleContext.removeBundleListener(this);
        while (!factories.isEmpty()) {
            unregister(factories.keySet().iterator().next());
        }
        debugPrintln("deactivated");
        this.bundleContext = null;
    }

    public void bundleChanged(BundleEvent event) {
        if (event.getType() == BundleEvent.RESOLVED) {
            register(event.getBundle());
        } else if (event.getType() == BundleEvent.UNRESOLVED || event.getType() == BundleEvent.UNINSTALLED) {
            unregister(event.getBundle().getBundleId());
        }
    }

    protected void register(final Bundle bundle) {
        debugPrintln("checking bundle " + bundle.getBundleId());
        Map<String, Callable<Class>> map = factories.get(bundle.getBundleId());
        Enumeration e = bundle.findEntries("META-INF/services/", "*", false);
        if (e != null) {
            while (e.hasMoreElements()) {
                final URL u = (URL) e.nextElement();
                final String url = u.toString();
                if (url.endsWith("/")) {
                    continue;
                }
                final String factoryId = url.substring(url.lastIndexOf("/") + 1);
                if (map == null) {
                    map = new HashMap<String, Callable<Class>>();
                    factories.put(bundle.getBundleId(), map);
                }
                map.put(factoryId, new BundleFactoryLoader(factoryId, u, bundle));
            }
        }
        if (map != null) {
            for (Map.Entry<String, Callable<Class>> entry : map.entrySet()) {
                debugPrintln("registering service for key " + entry.getKey() + "with value " + entry.getValue());
                OsgiLocator.register(entry.getKey(), entry.getValue());
            }
        }
    }

    protected void unregister(long bundleId) {
        Map<String, Callable<Class>> map = factories.remove(bundleId);
        if (map != null) {
            for (Map.Entry<String, Callable<Class>> entry : map.entrySet()) {
                debugPrintln("unregistering service for key " + entry.getKey() + "with value " + entry.getValue());
                OsgiLocator.unregister(entry.getKey(), entry.getValue());
            }
        }
    }

    private class BundleFactoryLoader implements Callable<Class> {

        private final String factoryId;

        private final URL u;

        private final Bundle bundle;

        private volatile Class<?> clazz;

        public BundleFactoryLoader(String factoryId, URL u, Bundle bundle) {
            this.factoryId = factoryId;
            this.u = u;
            this.bundle = bundle;
        }

        public Class call() throws Exception {
            try {
                debugPrintln("loading factory for key: " + factoryId);
                if (clazz == null) {
                    synchronized (this) {
                        if (clazz == null) {
                            debugPrintln("creating factory for key: " + factoryId);
                            BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream(), "UTF-8"));
                            String factoryClassName = br.readLine();
                            br.close();
                            debugPrintln("factory implementation: " + factoryClassName);
                            clazz = bundle.loadClass(factoryClassName);
                        }
                    }
                }
                return clazz;
            } catch (Exception e) {
                debugPrintln("exception caught while creating factory: " + e);
                throw e;
            } catch (Error e) {
                debugPrintln("error caught while creating factory: " + e);
                throw e;
            }
        }

        @Override
        public String toString() {
            return u.toString();
        }

        @Override
        public int hashCode() {
            return u.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof BundleFactoryLoader) {
                return u.equals(((BundleFactoryLoader) obj).u);
            } else {
                return false;
            }
        }
    }
}
