package org.flow.framework;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * This is the core Interface of the FlowFramework. An implementation is
 * responsible for
 * <em>service discovery, lifecycle and dependency management</em>.
 * 
 * <h2>Service discovery</h2> IServiceController extends
 * IServiceAvailibillityListener and the implementation must implement the
 * provided methods by that interface. The implementation must track all
 * available services implementing {@link IServiceDescriptorRepository} at
 * runtime. The implementation must further register itself as
 * ServiceAvailibilityListener with these IServiceDescriptorRepositories.
 * Whenever services become available they enter the service lifecycle described
 * in the next section.
 * 
 * <h2>Lifecycle and dependency management:</h2>
 * <p />
 * We distinguish 2 states for {@link ServiceDescriptor}s
 * <ul>
 * <li><strong>the {@link #INSTALLED} state:</strong> A
 * {@link ServiceDescriptor} is in the installed state, if it is available in
 * exactly one {@link IServiceDescriptorRepository} and recognized by the
 * {@link IServiceController} implementation.</li>
 * 
 * <li><strong>the {@link #RESOLVED} state:</strong> A {@link ServiceDescriptor}
 * is in the resolved state if it is {@link #INSTALLED} and all Dependencies are
 * resolved. A Dependency is resolved, if one or more {@link ServiceDescriptor}
 * instances describing the required service are available and at least one of
 * them is in the {@link #RESOLVED} state.</li>
 * </ul>
 * 
 * We distinguish 2 states for {@link ServiceInstance}s
 * <ul>
 * <li><strong>the {@link #INACTIVE} state,</strong> A {@link ServiceInstance}
 * is initially in the {@link #INACTIVE} state. In this state not all
 * dependencies are resolved and the service object is not registered with the
 * corresponding {@link BundleContext}. The controller must activate
 * {@link ServiceInstance}s in the {@link #INACTIVE} state whenever possible.</li>
 * 
 * <li><strong>the {@link #ACTIVE} state</strong> A {@link ServiceInstance} is
 * in the {@link #ACTIVE} state when all dependencies are satisfied and if it is
 * registered with the OSGi framework.</li>
 * </ul>
 * 
 * Furthermore, we notice the following transitions between these states:
 * <ul>
 * 
 * <li><strong>Discovery:</strong> As soon as a {@link ServiceDescriptor}
 * becomes available it enters the {@link #INSTALLED} state.</li>
 * 
 * <li><strong>Resolution:</strong> The framework tries to resolve services in
 * the {@link #INSTALLED} state. During resolution, the framework tries to
 * locate {@link ServiceDescriptor}s matching the declared dependencies of a
 * particular service. If such descriptors can be found and are themselves in
 * the {@link #RESOLVED} state, we consider the dependencies <em>resolved</em>.
 * In a second phase the framework locates {@link ServiceDescriptor} which
 * depend on the freshly discovered {@link ServiceDescriptor} and updates their
 * dependencies.</li>
 * 
 * <li><strong>Instantiation:</strong> {@link ServiceDescriptor}s in the
 * {@link #RESOLVED} state it can be instantiated and returns a fresh
 * {@link ServiceInstance}. This instance enters the {@link #INACTIVE} state.</li>
 * 
 * <li><strong>Activation:</strong> If all dependencies can be satisfied, the
 * service enters the {@link #ACTIVE} state. In this state it is fully
 * functionable. Only services in the {@link #ACTIVE} state must be used by
 * other services.</li>
 * 
 * <li><strong>De-activation:</strong> A "initiated transition" form the
 * {@link #ACTIVE} state to the {@link #INACTIVE} state occurs when the
 * {@link IServiceController} is advised to shutdown (unregister) this service.
 * This transition signals to dependent services that the service is about to
 * become unavailable. A service in the {@link #ACTIVE} state enters the
 * {@link #INACTIVE} state when one of the services it depends on enters the
 * {@link #INACTIVE} state. We call this a "propagated transition". If this is
 * the case the {@link IServiceController} attempts to re-satisfy this
 * particular dependency.</li>
 * </ul>
 * </p>
 * 
 * <h2>Configuration and extensibility</h2> The two main extension points are
 * the {@link IServiceConfigurator} and {@link IServiceDescriptorRepository}
 * interfaces. A {@link IServiceConfigurator} implementation represents a
 * strategy in dependency election of {@link ServiceInstance}s. See
 * {@link #setServiceConfigrurator(IServiceConfigurator)}
 * 
 * 
 * <h2>Implementation Notes:</h2> There must only be one instance of this
 * service at runtime.
 * <p />
 * 
 * @author Daniel Meyer
 * @TheadSafe
 * @since 0.1
 * @Singleton
 * @see IServiceDescriptorRepository
 * @see IServiceConfigurator
 */
public interface IServiceController extends IServiceAvailabilityListener {

    /**
	 * Used to qualify {@link ServiceReference}s as 'managed-services'. The
	 * value is 'true'
	 */
    static final String SERVICE_REGISTRATION_KEY = "managed-service";

    /**
	 * The INSTALLED state.
	 * <p />
	 * A {@link ServiceDescriptor} is in the installed state, if it is available
	 * in exactly one {@link IServiceDescriptorRepository} and recognized by the
	 * {@link IServiceController} implementation.
	 */
    public static final int INSTALLED = 0x1;

    /**
	 * The RESOLVED state.
	 * <p/>
	 * A {@link ServiceDescriptor} is in the resolved state if it is
	 * {@link #INSTALLED} and all Dependencies are resolved. A Dependency is
	 * resolved, if one or more {@link ServiceDescriptor} instances describing
	 * the required service are available and at least one of them is in the
	 * {@link #RESOLVED} state.
	 */
    public static final int RESOLVED = 0x2;

    /**
	 * The INACTIVE state.
	 * <p/>
	 * A {@link ServiceInstance} is initially in the {@link #INACTIVE} state. In
	 * this state not all dependencies are resolved and the service object is
	 * not registered with the corresponding {@link BundleContext}. Services in
	 * the {@link #INACTIVE} state must not be used.
	 */
    public static final int INACTIVE = 0x3;

    /**
	 * The ACTIVE state.
	 * <p/>
	 * A {@link ServiceInstance} is in the {@link #ACTIVE} state when all
	 * dependencies are satisfied and if it is registered with the OSGi
	 * framework.
	 */
    public static final int ACTIVE = 0x4;

    /**
	 * Starts and registers a new Service, defined by the referenced
	 * {@link ServiceDescriptor}.
	 * <ul>
	 * <li>Performs transition from the {@link #INACTIVE} state to the
	 * {@link #ACTIVE} state</li>
	 * <li>Note that the Service is registered on behalf of the defining
	 * {@link Bundle}.</li>
	 * <li>The returned {@link ServiceInstance} can be used to bind the
	 * services, see {@link ServiceInstance#getReference()}
	 * </ul>
	 * <p>
	 * <strong>NOTE:</strong> if not all dependencies can be satisfied, the
	 * service stays in the {@link #INACTIVE} state and
	 * {@link ServiceLifecycleException} is thrown. The
	 * {@link IServiceController} implementation tries to re-satisfy
	 * dependencies of services in the {@link #INACTIVE} state as new (more)
	 * services become available. Make sure to call
	 * {@link #stopService(ServiceInstance)} at some point to remove service
	 * from the inactive state.
	 * </p>
	 * 
	 * @param instance
	 * @throws ServiceLifecycleException
	 *             if the {@link ServiceDescriptor#isSingleton()} flag is set
	 *             and singleton is already running.
	 * @throws ServiceLifecycleException
	 *             if the service cannot be started because not all dependencies
	 *             can be satisfied
	 */
    public ServiceInstance startService(ServiceDescriptor serviceDescriptor) throws ServiceLifecycleException;

    /**
	 * Stops the referenced service
	 * <ul>
	 * <li>Performs transition from the {@link #ACTIVE} state to the
	 * {@link #INACTIVE} state (if applicable)</li>
	 * <li>destroys service instance</li>
	 * </ul>
	 * 
	 * @param instance
	 * @throws ServiceLifecycleException
	 */
    public void stopService(ServiceInstance instance) throws ServiceLifecycleException;

    /**
	 * Returns a set of running services
	 * <p>
	 * State must be {@link #ACTIVE}
	 * </p>
	 * 
	 * @return
	 * @see #ACTIVE
	 */
    public ServiceInstance[] getActiveServices();

    /**
	 * Returns a set of services in the {@link #INACTIVE} state.
	 * <p>
	 * State must be {@link #INACTIVE}
	 * </p>
	 * 
	 * @return
	 * @see #INACTIVE
	 */
    ServiceInstance[] getInActiveServices();

    /**
	 * Sets the used {@link IServiceConfigurator} to the referenced instance.
	 * 
	 * @param serviceConfigurator
	 */
    void setServiceConfigrurator(IServiceConfigurator serviceConfigurator);
}
