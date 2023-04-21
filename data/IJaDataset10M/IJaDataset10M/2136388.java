package com.volantis.synergetics.osgi.impl.framework.manager;

import com.volantis.synergetics.osgi.framework.FrameworkManager;
import com.volantis.synergetics.osgi.impl.framework.bridge.BridgeServiceHelper;
import com.volantis.synergetics.osgi.impl.framework.bridge.BridgeServiceRegistration;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import java.util.Dictionary;

public class FrameworkManagerImpl implements FrameworkManager {

    private final ClassLoader contextClassLoader;

    private final BundleContext bundleContext;

    public FrameworkManagerImpl(BundleContext bundleContext, ClassLoader contextClassLoader) {
        this.bundleContext = bundleContext;
        this.contextClassLoader = contextClassLoader;
    }

    public ServiceRegistration registerExportedBridgeService(Class clazz, Object service, Dictionary properties) {
        Dictionary extended = BridgeServiceHelper.addBridgeServiceProperty(properties, BridgeServiceHelper.VOLANTIS_EXPORT_SERVICE);
        Object proxy = createContextSwitchingProxy(clazz, service);
        final ServiceRegistration registration = bundleContext.registerService(clazz.getName(), proxy, extended);
        return new BridgeServiceRegistration(registration);
    }

    public Object getImportedBridgeService(Class clazz) {
        ServiceReference[] references;
        try {
            references = bundleContext.getServiceReferences(clazz.getName(), BridgeServiceHelper.VOLANTIS_IMPORTED_SERVICE_FILTER);
        } catch (InvalidSyntaxException e) {
            IllegalStateException ise = new IllegalStateException();
            ise.initCause(e);
            throw ise;
        }
        Object service = null;
        if (references != null) {
            ServiceReference reference = references[0];
            service = bundleContext.getService(reference);
        }
        return service;
    }

    public Object createContextSwitchingProxy(Class clazz, Object object) {
        return BridgeServiceHelper.createContextSwitchingProxy(contextClassLoader, clazz, object);
    }
}
