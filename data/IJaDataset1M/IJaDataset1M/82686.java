package com.buschmais.maexo.framework.commons.mbean.lifecycle;

import javax.management.ObjectName;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

/**
 * Represents an abstract service event listener to manage the life cycle of the
 * associated service MBeans.
 * <p>
 * The <code>ServiceMBeanLifeCycleSupport</code> instance will be registered as
 * a service listener to receive service-changed-events.
 */
public abstract class ServiceMBeanLifeCycleSupport extends MBeanLifeCycleSupport implements ServiceListener {

    /**
	 * Constructor.
	 *
	 * @param bundleContext
	 *            The bundle context of the exporting bundle.
	 */
    public ServiceMBeanLifeCycleSupport(BundleContext bundleContext) {
        super(bundleContext);
    }

    /**
	 * Returns the service interface supported by the life cycle event listener.
	 *
	 * @return The service interface.
	 */
    protected abstract Class<?> getServiceInterface();

    /**
	 * Returns the filter which will be used to track services in the OSGi
	 * service registry.
	 * <p>
	 * If <code>null</code> is returned all service instances for the interface
	 * provided by {@link #getServiceInterface()} will be used to create MBeans.
	 *
	 * @return The service filter or <code>null</code>.
	 */
    protected abstract String getServiceFilter();

    /**
	 * Returns the object name of service which will be used for the MBean.
	 *
	 * @param serviceReference
	 *            The service reference.
	 * @param service
	 *            The service instance.
	 * @return The object name.
	 */
    protected abstract ObjectName getObjectName(ServiceReference serviceReference, Object service);

    /**
	 * Returns the MBean instance for the given service.
	 *
	 * @param serviceReference
	 *            The service reference.
	 * @param service
	 *            The service instance.
	 * @return The MBean instance which represents the service.
	 */
    protected abstract Object getMBean(ServiceReference serviceReference, Object service);

    /**
	 * Releases the MBean instance for the given service.
	 *
	 * @param serviceReference
	 *            The service reference.
	 * @param service
	 *            The service instance.
	 * @param mbean
	 *            The MBean instance which represents the service.
	 */
    protected abstract void releaseMBean(ServiceReference serviceReference, Object service, Object mbean);

    /**
	 * Returns the MBean interface which will be used for registration.
	 *
	 * @return The MBean interface.
	 */
    protected abstract Class<?> getMBeanInterface();

    /**
	 * Returns a filter using the interface from {@link #getServiceInterface()}
	 * as object class and the service filter obtained from
	 * {@link #getServiceFilter()}.
	 *
	 * @return The filter or <code>null</code> if there is no service interface
	 *         declared.
	 */
    private String getFilter() {
        Class<?> serviceInterface = this.getServiceInterface();
        String serviceFilter = this.getServiceFilter();
        StringBuilder filter = new StringBuilder();
        if (serviceInterface != null) {
            filter.append(String.format("(objectClass=%s)", serviceInterface.getName()));
        }
        if (serviceFilter != null) {
            filter.append(serviceFilter);
        }
        if (filter.length() == 0) {
            return null;
        }
        return String.format("(&%s)", filter);
    }

    /**
	 * Returns the currently registered service references for the interface
	 * provided by {@link #getServiceInterface()}.
	 *
	 * @return The service references.
	 */
    private ServiceReference[] getServices() {
        try {
            return super.getBundleContext().getServiceReferences(null, this.getFilter());
        } catch (InvalidSyntaxException e) {
            throw new IllegalStateException("cannot get services from bundle context using service filter " + this.getFilter(), e);
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public final synchronized void serviceChanged(ServiceEvent serviceEvent) {
        ServiceReference serviceReference = serviceEvent.getServiceReference();
        Object service = super.getBundleContext().getService(serviceReference);
        ObjectName objectName = this.getObjectName(serviceReference, service);
        Object mbean = null;
        switch(serviceEvent.getType()) {
            case ServiceEvent.REGISTERED:
                mbean = this.getMBean(serviceReference, service);
                super.registerMBeanService(this.getMBeanInterface(), objectName, mbean);
                break;
            case ServiceEvent.UNREGISTERING:
                try {
                    mbean = super.unregisterMBeanService(objectName);
                    this.releaseMBean(serviceReference, service, mbean);
                } finally {
                    super.getBundleContext().ungetService(serviceReference);
                }
                break;
            default:
                assert false : "Unexpected ServiceEvent";
        }
    }

    /**
	 * Starts the life cycle listener.
	 * <p>
	 * The existing services are looked up to be published as MBeans. Later the
	 * life cycle listener is registered with the bundle context.
	 */
    public final void start() {
        ServiceReference[] serviceReferences = this.getServices();
        if (serviceReferences != null) {
            for (ServiceReference serviceReference : serviceReferences) {
                this.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, serviceReference));
            }
        }
        try {
            super.getBundleContext().addServiceListener(this, this.getFilter());
        } catch (InvalidSyntaxException e) {
            throw new IllegalStateException("cannot add service listener to bundle context using service filter " + this.getFilter(), e);
        }
    }

    /**
	 * Stops the life cycle listener.
	 * <p>
	 * The existing services are looked up, their MBeans get unpublished and the
	 * life cycle listener is unregistered from the bundle context.
	 */
    public final void stop() {
        super.getBundleContext().removeServiceListener(this);
        ServiceReference[] serviceReferences = this.getServices();
        if (serviceReferences != null) {
            for (ServiceReference serviceReference : serviceReferences) {
                this.serviceChanged(new ServiceEvent(ServiceEvent.UNREGISTERING, serviceReference));
            }
        }
    }
}
