package net.sf.jimo.platform.service;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Map;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

/**
 * ServiceContexts are created by the registry provider.  They are used by clients
 * to access the service registry.  All invocations on this class are passed 
 * through the service filter chains. 
 * 
 * @author logicfish
 *
 */
public interface ServiceContext {

    public ServiceReference getServiceReference(String clazz) throws ServiceRegistryException;

    public ServiceReference[] getServiceReferences(String objectClass, String filter) throws ServiceRegistryException;

    public ServiceReference[] getAllServiceReferences(String objectClass, String filter) throws ServiceRegistryException;

    public ServiceRegistration registerService(String[] clazzes, Object service, Dictionary<?, ?> properties) throws ServiceRegistryException;

    public <O> Object getService(ServiceReference reference) throws ServiceRegistryException;

    public <O> Collection<O> getServices(ServiceReference[] serviceReferences) throws ServiceRegistryException;

    /**
	 * Only to be called inside a <b>ServiceFilter</b> method.
	 * @return The current filter chain
	 */
    public ServiceFilterChain getChain();

    public Map<String, Object> getProperties();

    public void unregisterService(ServiceRegistration serviceEntry) throws ServiceRegistryException;

    public void updateService(ServiceRegistration serviceEntry, Map<?, ?> properties) throws ServiceRegistryException;

    public boolean ungetService(ServiceReference reference) throws ServiceRegistryException;
}
