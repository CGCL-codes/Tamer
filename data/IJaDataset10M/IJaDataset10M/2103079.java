package org.flow.framework;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.flow.framework.ServiceLifecycleException;
import org.osgi.framework.Bundle;

/**
 * ServiceDescriptors hold metadata used to describe a service and its
 * dependencies.
 * 
 * @author Daniel Meyer
 * @since 0.1
 * 
 */
public abstract class ServiceDescriptor {

    /** describes a static service dependency */
    public final class ServiceDependency {

        private final String serviceInterface;

        private final String namedReference;

        private final String name;

        private final Set resolvedDescriptors = new HashSet();

        public ServiceDependency(String name, String serviceInterface, String namedInstance) {
            this.serviceInterface = serviceInterface;
            this.name = name;
            this.namedReference = namedInstance != null ? namedInstance : "";
        }

        public String getNamedReference() {
            return namedReference.length() > 0 ? namedReference : null;
        }

        public String getServiceInterface() {
            return serviceInterface;
        }

        public String getName() {
            return name;
        }

        public Set getResolvedDescriptors() {
            return resolvedDescriptors;
        }

        public void addResolvedDescriptor(ServiceDescriptor descriptor) {
            synchronized (ServiceDescriptor.this) {
                resolvedDescriptors.add(descriptor);
                descriptor.addDependentDescriptor(ServiceDescriptor.this);
            }
        }

        public void removeResolvedDescriptor(ServiceDescriptor descriptor) {
            synchronized (ServiceDescriptor.this) {
                resolvedDescriptors.remove(descriptor);
                resolved = false;
            }
        }

        public void addResolvedDescriptors(ServiceDescriptor[] descriptors) {
            synchronized (ServiceDescriptor.this) {
                for (int i = 0; i < descriptors.length; i++) {
                    addResolvedDescriptor(descriptors[i]);
                }
            }
        }

        public void unresolve() {
            synchronized (ServiceDescriptor.this) {
                for (Iterator iterator = resolvedDescriptors.iterator(); iterator.hasNext(); ) {
                    ServiceDescriptor descriptor = (ServiceDescriptor) iterator.next();
                    descriptor.removeDependentDescriptor(ServiceDescriptor.this);
                }
                resolved = false;
            }
        }
    }

    protected final Class implementation;

    protected Class serviceInterface;

    protected Bundle bundle;

    private boolean resolved = false;

    protected boolean singleton = false;

    protected boolean autostart = false;

    protected String instanceName = "";

    protected Map dependencies = new HashMap();

    protected Set dependentDescriptors = new HashSet();

    protected final Map properties = new HashMap();

    protected Set instances = new HashSet();

    public ServiceDescriptor(Class clazz, Bundle bundle) {
        if (clazz == null) throw new IllegalArgumentException("class cannot be null");
        if (bundle == null) throw new IllegalArgumentException("bundle cannot be null");
        this.implementation = clazz;
        this.bundle = bundle;
        if (dependencies.size() == 0) {
            resolved = true;
        }
    }

    public void checkResolved() {
        if (resolved == true) return;
        for (Iterator iterator = dependencies.values().iterator(); iterator.hasNext(); ) {
            Set depSet = (Set) iterator.next();
            for (Iterator iterator2 = depSet.iterator(); iterator2.hasNext(); ) {
                ServiceDependency dependency = (ServiceDependency) iterator2.next();
                if (!checkResoled(dependency)) {
                    resolved = false;
                    return;
                }
            }
        }
        resolved = true;
        for (Iterator iterator = dependentDescriptors.iterator(); iterator.hasNext(); ) {
            ServiceDescriptor descriptor = (ServiceDescriptor) iterator.next();
            descriptor.checkResolved();
        }
    }

    private boolean checkResoled(ServiceDependency dependency) {
        if (dependency.resolvedDescriptors.size() == 0) return false;
        for (Iterator iterator = dependency.resolvedDescriptors.iterator(); iterator.hasNext(); ) {
            if (((ServiceDescriptor) iterator.next()).isResolved() == true) return true;
        }
        return false;
    }

    public boolean isResolved() {
        return resolved;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public ServiceDependency[] getDependencies() {
        Set result = new HashSet();
        synchronized (this) {
            for (Iterator iterator = dependencies.values().iterator(); iterator.hasNext(); ) {
                result.addAll((Set) iterator.next());
            }
        }
        return (ServiceDependency[]) result.toArray(new ServiceDependency[0]);
    }

    public Class getServiceInterface() {
        return serviceInterface;
    }

    public void addDependentDescriptor(ServiceDescriptor descriptor) {
        synchronized (this) {
            dependentDescriptors.add(descriptor);
        }
    }

    public void removeDependentDescriptor(ServiceDescriptor descriptor) {
        synchronized (this) {
            dependentDescriptors.remove(descriptor);
        }
    }

    public ServiceDescriptor[] getDependentDescriptors() {
        synchronized (this) {
            return (ServiceDescriptor[]) dependentDescriptors.toArray(new ServiceDescriptor[0]);
        }
    }

    /** returns a new Instance of {@link #implementation} */
    public ServiceInstance getInstance() throws ServiceLifecycleException {
        if (singleton && instances.size() >= 1) {
            throw new ServiceLifecycleException(null, "Singleton instance already created!");
        }
        try {
            if (bundle.getState() != Bundle.ACTIVE) {
                bundle.start();
            }
            Constructor constructor = implementation.getDeclaredConstructor(new Class[0]);
            ServiceInstance instance = new ServiceInstance(this);
            Object service = constructor.newInstance(new Object[0]);
            instance.setServiceObject(service);
            instance.setUnderlyingServiceObject(service);
            if (service instanceof IContextAware) {
                ((IContextAware) service).setBundleContext(bundle.getBundleContext());
            }
            instances.add(instance);
            return instance;
        } catch (Exception e) {
            throw new ServiceLifecycleException("Exception while instantiating service " + implementation.getCanonicalName(), e);
        }
    }

    public Class getImplementation() {
        return implementation;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public boolean isAutostart() {
        return autostart;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public ServiceInstance[] getAllInstances() {
        return (ServiceInstance[]) instances.toArray(new ServiceInstance[0]);
    }

    public ServiceDependency[] getDependencies(String serviceInterface) {
        Set result = (Set) dependencies.get(serviceInterface);
        if (result == null) return new ServiceDependency[0];
        return (ServiceDependency[]) result.toArray(new ServiceDependency[0]);
    }

    public String toString() {
        return implementation.getName() + " (implementing " + serviceInterface.getName() + ")";
    }
}
