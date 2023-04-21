package org.flow.framework.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import org.flow.framework.IServiceAvailabilityListener;
import org.flow.framework.IServiceConfigurator;
import org.flow.framework.IServiceController;
import org.flow.framework.IServiceDescriptorRepository;
import org.flow.framework.IServiceLifecycleAware;
import org.flow.framework.ServiceDescriptor;
import org.flow.framework.ServiceInstance;
import org.flow.framework.ServiceLifecycleException;
import org.flow.framework.IServiceDescriptorRepository.RepositoryMetadata;
import org.flow.framework.ServiceDescriptor.ServiceDependency;
import org.flow.framework.ServiceInstance.Dependencies;
import org.flow.framework.logging.FrameworkLogger;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Implementation of {@link IServiceController}
 * 
 * @author Daniel Meyer
 * @since 0.1
 * 
 */
public final class ServiceController implements IServiceController {

    /** singleton instance */
    private static ServiceController INSTANCE;

    /** {@link FrameworkLogger} instance */
    public static FrameworkLogger LOGGER;

    /** Allow static instantiation only to enforce singleton pattern */
    public static ServiceController getInstance(BundleContext context) {
        synchronized (IServiceController.class) {
            if (INSTANCE == null) {
                INSTANCE = new ServiceController(context);
            }
            return INSTANCE;
        }
    }

    /**
	 * Separate thread for orthogonal transactions to the current main
	 * transaction.
	 */
    private final Thread transactionsThread = new Thread() {

        public void run() {
            Runnable job = null;
            while (true) {
                if (isInterrupted()) break;
                try {
                    job = (Runnable) transactions.take();
                } catch (InterruptedException e) {
                    break;
                }
                if (job == null) continue;
                try {
                    job.run();
                } catch (Exception e) {
                    LOGGER.exception(e);
                }
            }
        }
    };

    private final BlockingQueue transactions = new LinkedBlockingQueue();

    /** used to lock up during lifecycle transitions */
    private final Object lifecycle_mutex = new Object();

    /** reference to bundle context */
    private final BundleContext bundleContext;

    /** used to generate intance ids */
    private final AtomicInteger runtimeIdGenerator = new AtomicInteger(1);

    /**
	 * service instance registry
	 */
    private final Map instancesByState = Collections.synchronizedMap(new HashMap());

    private Set serviceDescriptorRepositories = new CopyOnWriteArraySet();

    private IServiceConfigurator serviceConfigurator;

    private ServiceController(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
        this.transactionsThread.start();
        LOGGER = FrameworkActivator.LOGGER;
        instancesByState.put(new Integer(INACTIVE), new HashSet());
        instancesByState.put(new Integer(ACTIVE), new HashSet());
        ServiceTracker descriptorRepositoryTracker = new ServiceTracker(this.bundleContext, IServiceDescriptorRepository.class.getName(), null) {

            public Object addingService(ServiceReference reference) {
                IServiceDescriptorRepository reporitory = (IServiceDescriptorRepository) super.addingService(reference);
                addDescriptorRepository(reporitory);
                return reporitory;
            }

            public void removedService(ServiceReference reference, Object service) {
                removeDescriptorRepository((IServiceDescriptorRepository) service);
                super.removedService(reference, service);
            }
        };
        serviceConfigurator = new DefaultServiceConfigurator(this);
        descriptorRepositoryTracker.open(true);
    }

    /**
	 * Start 'autostart' services from new repository and try to re-satisfy
	 * dependencies of services in the {@link IServiceController#INACTIVE}
	 * state. Add to {@link #serviceDescriptorRepositories}.
	 * 
	 * @param repository
	 * @throws IllegalArgumentException
	 *             if repository is already registered
	 */
    private void addDescriptorRepository(IServiceDescriptorRepository repository) {
        synchronized (lifecycle_mutex) {
            if (!serviceDescriptorRepositories.add(repository)) {
                throw new IllegalArgumentException(repository + " is already registered!");
            }
            RepositoryMetadata metadata = repository.getRepositoryMetadata();
            LOGGER.debug("New ServiceDescriptorRepository discovered: " + metadata.getLocation() + ":" + metadata.getName());
            ServiceDescriptor[] descriptors = repository.getAvailableServices();
            List autoStartDescriptors = new ArrayList();
            for (int i = 0; i < descriptors.length; i++) {
                ServiceDescriptor descriptor = descriptors[i];
                resolve(descriptor);
                if (descriptor.isAutostart()) {
                    autoStartDescriptors.add(descriptor);
                }
            }
            for (Iterator iterator = autoStartDescriptors.iterator(); iterator.hasNext(); ) {
                ServiceDescriptor descriptor = (ServiceDescriptor) iterator.next();
                try {
                    autoStartService(descriptor);
                } catch (ServiceLifecycleException e) {
                    LOGGER.exception(e);
                }
            }
            repository.addServiceAvailablilityListener(this);
        }
    }

    /**
	 * Unregister {@link IServiceDescriptorRepository} instance
	 * <ul>
	 * <li>Unregister as {@link IServiceAvailabilityListener}</li>
	 * <li>Services from this repository in the
	 * {@link IServiceController#ACTIVE} state must transition to the
	 * {@link IServiceController#INACTIVE} state.</li>
	 * <li>Services declared in this repository that are currently in the
	 * {@link IServiceController#INACTIVE} state must be destroyed</li>
	 * <li>LifeCycle transitions must be performed as atomic transaction with (
	 * {@link #lifecycle_mutex} held)</li>
	 * <li>Remove instance from {@link #serviceDescriptorRepositories}</li>
	 * 
	 * </ul>
	 * 
	 * 
	 * @param repository
	 * @throws IllegalArgumentException
	 *             if repository is not contained in
	 *             {@link #serviceDescriptorRepositories}
	 * @see #addDescriptorRepository(IServiceDescriptorRepository)
	 * @see #uninstall(ServiceDescriptor)
	 */
    private void removeDescriptorRepository(IServiceDescriptorRepository repository) {
        repository.removeServiceAvailabilityListener(this);
        synchronized (lifecycle_mutex) {
            if (!serviceDescriptorRepositories.remove(repository)) throw new IllegalArgumentException(repository + " unknown");
            for (int i = 0; i < repository.getAvailableServices().length; i++) {
                ServiceDescriptor descriptor = repository.getAvailableServices()[i];
                uninstall(descriptor);
            }
        }
    }

    /**
	 * Installs a new {@link ServiceDescriptor}
	 * <ul>
	 * <li>
	 * Resolve the {@link ServiceDescriptor}</li>
	 * <li>Start if autostart</li>
	 * </ul>
	 * 
	 * @param descriptor
	 */
    private void install(ServiceDescriptor descriptor) {
        synchronized (lifecycle_mutex) {
            resolve(descriptor);
            if (!descriptor.isAutostart()) return;
            try {
                doStart(descriptor);
            } catch (ServiceLifecycleException e) {
                LOGGER.exception(e);
            }
        }
    }

    /**
	 * Un-installs a {@link ServiceDescriptor}.
	 * <ul>
	 * <li>ServiceInstances owned by this descriptor in the
	 * {@link IServiceController#ACTIVE} state must transition to the
	 * {@link IServiceController#INACTIVE} state.</li>
	 * <li>ServiceInstances in the {@link IServiceController#INACTIVE} state
	 * must be destroyed</li>
	 * <li>the {@link ServiceDescriptor} is unresolved</li>
	 * </ul>
	 * 
	 * @param descriptor
	 */
    private void uninstall(ServiceDescriptor descriptor) {
        synchronized (lifecycle_mutex) {
            ServiceInstance[] instances = descriptor.getAllInstances();
            for (int i = 0; i < instances.length; i++) {
                ServiceInstance serviceInstance = instances[i];
                try {
                    doStop(serviceInstance);
                } catch (ServiceLifecycleException e) {
                    LOGGER.exception(e);
                }
            }
            unResolve(descriptor);
        }
    }

    /**
	 * Resolves static dependencies of the descriptor
	 * <ul>
	 * <li>resolve dependencies for this descriptor</li>
	 * <li>update dependencies of services dependent on this descriptor</li>
	 * </ul>
	 * 
	 * @param descriptor
	 */
    private void resolve(ServiceDescriptor descriptor) {
        String serviceInterface = descriptor.getServiceInterface().getName();
        ServiceDescriptor[] dependantDescriptors;
        ServiceDependency[] dependencies;
        ServiceDependency serviceDependency;
        synchronized (lifecycle_mutex) {
            dependencies = descriptor.getDependencies();
            for (int i = 0; i < dependencies.length; i++) {
                serviceDependency = dependencies[i];
                ServiceDescriptor[] compatibleDescriptors = getDescriptors(serviceDependency.getServiceInterface());
                if (serviceDependency.getNamedReference() != null) {
                    for (int j = 0; j < compatibleDescriptors.length; j++) {
                        ServiceDescriptor serviceDescriptor = compatibleDescriptors[j];
                        if (serviceDependency.getNamedReference().equals(serviceDescriptor.getInstanceName())) {
                            serviceDependency.addResolvedDescriptor(serviceDescriptor);
                            break;
                        }
                    }
                } else {
                    serviceDependency.addResolvedDescriptors(compatibleDescriptors);
                }
            }
            dependantDescriptors = getDependentDescriptors(descriptor.getServiceInterface().getName());
            for (int j = 0; j < dependantDescriptors.length; j++) {
                dependencies = dependantDescriptors[j].getDependencies(serviceInterface);
                for (int k = 0; k < dependencies.length; k++) {
                    serviceDependency = dependencies[k];
                    if (serviceDependency.getNamedReference() != null) {
                        if (!serviceDependency.getNamedReference().equals(descriptor.getInstanceName())) continue;
                    }
                    serviceDependency.addResolvedDescriptor(descriptor);
                }
            }
            descriptor.checkResolved();
            for (int i = 0; i < dependantDescriptors.length; i++) {
                dependantDescriptors[i].checkResolved();
            }
        }
        LOGGER.debug("Resolved service " + descriptor);
    }

    /**
	 * Removes static dependencies of and on this descriptor
	 * <ul>
	 * <li>un resolve dependencies for this descriptor</li>
	 * <li>remove this descriptor form resolved dependencies of descriptors
	 * dependent on this descriptor</li>
	 * </ul>
	 * 
	 * @param descriptor
	 */
    private void unResolve(ServiceDescriptor descriptor) {
        ServiceDescriptor[] dependantDescriptors;
        ServiceDependency[] dependencies;
        ServiceDependency serviceDependency;
        synchronized (lifecycle_mutex) {
            dependencies = descriptor.getDependencies();
            for (int i = 0; i < dependencies.length; i++) {
                serviceDependency = dependencies[i];
                serviceDependency.unresolve();
            }
            dependantDescriptors = descriptor.getDependentDescriptors();
            for (int i = 0; i < dependantDescriptors.length; i++) {
                ServiceDescriptor serviceDescriptor = dependantDescriptors[i];
                dependencies = serviceDescriptor.getDependencies();
                for (int j = 0; j < dependencies.length; j++) {
                    serviceDependency = dependencies[j];
                    serviceDependency.removeResolvedDescriptor(descriptor);
                }
            }
        }
    }

    public ServiceInstance startService(ServiceDescriptor serviceDescriptor) throws ServiceLifecycleException {
        return doStart(serviceDescriptor);
    }

    private ServiceInstance doStart(ServiceDescriptor serviceDescriptor) throws ServiceLifecycleException {
        synchronized (lifecycle_mutex) {
            ServiceInstance instance = serviceDescriptor.getInstance();
            register(instance);
            activate(instance);
            return instance;
        }
    }

    /**
	 * {@link #doStart(ServiceDescriptor)} if not running
	 * 
	 * @param descriptor
	 * @throws ServiceLifecycleException
	 */
    private void autoStartService(ServiceDescriptor descriptor) throws ServiceLifecycleException {
        if (descriptor.getAllInstances().length == 0) doStart(descriptor);
    }

    private void register(ServiceInstance instance) {
        String instanceName = "";
        ServiceDescriptor descr = instance.getDescriptor();
        if (descr.getInstanceName().length() > 0) {
            instanceName = descr.getInstanceName();
        } else {
            String classname = descr.getImplementation().getName();
            classname = classname.substring(classname.lastIndexOf(".") + 1, classname.length());
            instanceName = classname + "#" + runtimeIdGenerator.getAndIncrement();
        }
        instance.setInstanceName(instanceName);
        Set inactiveInstances = (Set) instancesByState.get(new Integer(INACTIVE));
        inactiveInstances.add(instance);
    }

    /**
	 * Activate service
	 * <ul>
	 * <li>tries to satisfy dependencies. If dependencies cannot be satisfied,
	 * {@link ServiceLifecycleException} is thrown</li>
	 * <li>Removes instance from set of inactive instances (if applicable)</li>
	 * <li>Adds instance to set of active instances</li>
	 * </ul>
	 * 
	 * @param instance
	 * @throws ServiceLifecycleException
	 */
    private void activate(ServiceInstance instance) throws ServiceLifecycleException {
        if (!instance.getDependencies().isAllSatisfied()) {
            satDependencies(instance);
        }
        synchronized (instancesByState) {
            try {
                setState(instance, ACTIVE);
            } catch (Exception e) {
                throw new ServiceLifecycleException("Exception during state transition!", e);
            }
            Set inactiveInstances = (Set) instancesByState.get(new Integer(INACTIVE));
            inactiveInstances.remove(instance);
            Set activeInstances = (Set) instancesByState.get(new Integer(ACTIVE));
            activeInstances.add(instance);
            Hashtable properties = new Hashtable();
            properties.put(SERVICE_REGISTRATION_KEY, Boolean.toString(true));
            instance.register(properties);
        }
    }

    public void stopService(ServiceInstance instance) throws ServiceLifecycleException {
        ServiceLifecycleException exception = null;
        try {
            doStop(instance);
        } catch (ServiceLifecycleException e) {
            exception = e;
        }
        transactions.add(new Runnable() {

            public void run() {
                resatDependencies();
            }
        });
        if (exception != null) {
            throw exception;
        }
    }

    private void doStop(ServiceInstance instance) throws ServiceLifecycleException {
        synchronized (lifecycle_mutex) {
            deactivate(instance);
            unregister(instance);
        }
    }

    private void deactivate(ServiceInstance instance) throws ServiceLifecycleException {
        if (instance.getState() != ACTIVE) return;
        try {
            setState(instance, INACTIVE);
        } catch (Exception e) {
            LOGGER.exception(e);
        }
        Set activeSet = (Set) instancesByState.get(new Integer(ACTIVE));
        activeSet.remove(instance);
        Set inactiveSet = (Set) instancesByState.get(new Integer(INACTIVE));
        inactiveSet.add(instance);
        ServiceDescriptor[] descriptor = instance.getDescriptor().getDependentDescriptors();
        for (int i = 0; i < descriptor.length; i++) {
            ServiceDescriptor serviceDescriptor = descriptor[i];
            ServiceInstance[] instances = serviceDescriptor.getAllInstances();
            for (int j = 0; j < instances.length; j++) {
                ServiceInstance dependentServiceInstance = instances[j];
                if (dependentServiceInstance.getDependencies().unSatDependency(instance)) {
                    deactivate(dependentServiceInstance);
                }
            }
        }
        try {
            ServiceRegistration registration = instance.getRegistration();
            registration.unregister();
        } catch (IllegalStateException e) {
        }
    }

    private void unregister(ServiceInstance instance) throws ServiceLifecycleException {
        if (instance.getState() != INACTIVE) return;
        Set inactiveSet = (Set) instancesByState.get(new Integer(INACTIVE));
        inactiveSet.remove(instance);
        try {
            setState(instance, RESOLVED);
        } catch (Exception e) {
            LOGGER.exception(e);
        }
    }

    /**
	 * Satisfy dependencies of the referenced {@link ServiceInstance}
	 * 
	 * @throws ServiceLifecycleException
	 *             if dependencies cannot be satisfied
	 * */
    private void satDependencies(ServiceInstance instance) throws ServiceLifecycleException {
        synchronized (lifecycle_mutex) {
            Dependencies instanceDependencies = instance.getDependencies();
            ServiceDependency[] staticDependencies = instance.getDescriptor().getDependencies();
            for (int i = 0; i < staticDependencies.length; i++) {
                ServiceDependency serviceDependency = staticDependencies[i];
                if (instanceDependencies.isSatisfied(serviceDependency)) {
                    continue;
                }
                ServiceInstance[] instances = serviceConfigurator.chooseService(instance, serviceDependency);
                if (instances == null) throw new ServiceLifecycleException(instance, "Cannot satisfy " + serviceDependency + " of " + instance);
                if (instances.length == 0) throw new ServiceLifecycleException(instance, "Cannot satisfy " + serviceDependency + " of " + instance);
                for (int j = 0; j < instances.length; j++) {
                    ServiceInstance serviceInstance = instances[j];
                    switch(serviceInstance.getState()) {
                        case ACTIVE:
                            break;
                        case INACTIVE:
                            activate(serviceInstance);
                            break;
                        default:
                            register(serviceInstance);
                            activate(serviceInstance);
                            break;
                    }
                }
                instance.inject(serviceDependency, instances);
            }
        }
    }

    /**
	 * Re-satisfies dependencies of services in the
	 * {@link IServiceController#INACTIVE} state.
	 */
    private void resatDependencies() {
        synchronized (lifecycle_mutex) {
            ServiceInstance[] inactiveInstances = (ServiceInstance[]) ((Set) instancesByState.get(new Integer(INACTIVE))).toArray(new ServiceInstance[0]);
            for (int i = 0; i < inactiveInstances.length; i++) {
                ServiceInstance serviceInstance = inactiveInstances[i];
                try {
                    activate(serviceInstance);
                } catch (ServiceLifecycleException e) {
                }
            }
        }
    }

    protected void setState(ServiceInstance instance, int newState) throws Exception {
        int currentState = instance.getState();
        Object serviceObject = instance.getUnderlyingServiceObject();
        try {
            instance.setState(newState);
            if (serviceObject instanceof IServiceLifecycleAware) {
                IServiceLifecycleAware serviceLifecycleAware = (IServiceLifecycleAware) serviceObject;
                serviceLifecycleAware.onEnterState(newState);
            }
        } catch (Exception e) {
            instance.setState(currentState);
            throw (e);
        }
    }

    public ServiceInstance[] getActiveServices() {
        Set result = (Set) instancesByState.get(new Integer(ACTIVE));
        return (ServiceInstance[]) result.toArray(new ServiceInstance[0]);
    }

    public ServiceInstance[] getInActiveServices() {
        Set result = (Set) instancesByState.get(new Integer(INACTIVE));
        return (ServiceInstance[]) result.toArray(new ServiceInstance[0]);
    }

    /**
	 * @see IServiceAvailabilityListener#serviceAvailable(ServiceDescriptor)
	 * @see IServiceAvailabilityListener#serviceUnavailable(ServiceDescriptor)
	 * @see #resolve(ServiceDescriptor)
	 */
    public void serviceAvailable(ServiceDescriptor descriptor) {
        install(descriptor);
        transactions.add(new Runnable() {

            public void run() {
                resatDependencies();
            }
        });
    }

    /**
	 * @see IServiceAvailabilityListener#serviceUnavailable(ServiceDescriptor)
	 * @see IServiceAvailabilityListener#serviceAvailable(ServiceDescriptor)
	 * @see #uninstall(ServiceDescriptor)
	 */
    public void serviceUnavailable(ServiceDescriptor descriptor) {
        uninstall(descriptor);
        transactions.add(new Runnable() {

            public void run() {
                resatDependencies();
            }
        });
    }

    ServiceDescriptor[] getAvailableServices() {
        ArrayList result = new ArrayList();
        for (Iterator iterator = serviceDescriptorRepositories.iterator(); iterator.hasNext(); ) {
            IServiceDescriptorRepository repo = (IServiceDescriptorRepository) iterator.next();
            Collections.addAll(result, repo.getAvailableServices());
        }
        return (ServiceDescriptor[]) result.toArray(new ServiceDescriptor[0]);
    }

    ServiceDescriptor[] getDeclaredServices(Bundle bundle) {
        ArrayList result = new ArrayList();
        for (Iterator iterator = serviceDescriptorRepositories.iterator(); iterator.hasNext(); ) {
            IServiceDescriptorRepository repo = (IServiceDescriptorRepository) iterator.next();
            Collections.addAll(result, repo.getDeclaredServices(bundle));
        }
        return (ServiceDescriptor[]) result.toArray(new ServiceDescriptor[0]);
    }

    ServiceDescriptor[] getDependentDescriptors(String interfaceName) {
        ArrayList result = new ArrayList();
        for (Iterator iterator = serviceDescriptorRepositories.iterator(); iterator.hasNext(); ) {
            IServiceDescriptorRepository repo = (IServiceDescriptorRepository) iterator.next();
            Collections.addAll(result, repo.getDependentDescriptors(interfaceName));
        }
        return (ServiceDescriptor[]) result.toArray(new ServiceDescriptor[0]);
    }

    ServiceDescriptor[] getDescriptors(String interfaceName) {
        ArrayList result = new ArrayList();
        for (Iterator iterator = serviceDescriptorRepositories.iterator(); iterator.hasNext(); ) {
            IServiceDescriptorRepository repo = (IServiceDescriptorRepository) iterator.next();
            Collections.addAll(result, repo.getDescriptors(interfaceName));
        }
        return (ServiceDescriptor[]) result.toArray(new ServiceDescriptor[0]);
    }

    public void setServiceConfigrurator(IServiceConfigurator serviceConfigurator) {
        synchronized (lifecycle_mutex) {
            this.serviceConfigurator = serviceConfigurator;
        }
    }
}
