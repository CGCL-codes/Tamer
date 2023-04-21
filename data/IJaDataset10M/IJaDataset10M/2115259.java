package org.impalaframework.service;

import java.util.List;
import java.util.Map;

/**
 * Interface for shared registry for services used by Impala to share beans between modules.
 * 
 * @author Phil Zoio
 */
public interface ServiceEntryRegistry {

    /**
     * Registers a service in the Impala service registry, with options provide a list of export types and a map of attributes.
     * @param beanName the name under which the service is registered. Can be null
     * @param moduleName the module from which the service is registered. Cannot be null
     * @param service the service instance
     * @param exportTypes the export types against which the service will be registered
     * @param attributes attributes associated with the service instance. Can be used to filter services when 
     * looking up services from the service registry
     * @param classLoader the classloader associated with the loading module
     */
    ServiceRegistryEntry addService(String beanName, String moduleName, ServiceBeanReference service, List<Class<?>> exportTypes, Map<String, ?> attributes, ClassLoader classLoader);

    /**
     * Removes a service instance from the service registry. Returns true if service was actually removed.
     */
    boolean remove(ServiceRegistryEntry entry);

    /**
     * Evicts the services contributing from a particular module
     */
    List<ServiceRegistryEntry> evictModuleServices(String moduleName);

    /**
     * Retrieves a service from the service registry
     * @param beanName the name under which the service was registered
     * @param supportedTypes the possible types for the service. The service
     * must be class compatible with all of these types to be returned.
     * @param exportTypesOnly if true, then the services returned must all be
     * explicitly registered using the types. Otherwise, it is sufficient that
     * each service must be type compatible with the supplied types
     * @return a {@link ServiceRegistryEntry} instance
     */
    ServiceRegistryEntry getService(String beanName, Class<?>[] supportedTypes, boolean exportTypesOnly);

    /**
     * Retrieves services from the service registry registered under a
     * particular bean name. Typically, only a single bean will be registered in
     * the service registry under a given bean name. However, it is possible for
     * more than one bean to be registered under a single key name.
     * @param beanName the name under which the services were registered
     * @param supportedTypes the possible types for the service. The service
     * must be class compatible with all of these types to be returned.
     * @param exportTypesOnly if true, then the services returned must all be
     * explicitly registered using the types. Otherwise, it is sufficient that
     * each service must be type compatible with the supplied types
     * @return a {@link List} of {@link ServiceRegistryEntry} instance
     */
    List<ServiceRegistryEntry> getServices(String beanName, Class<?>[] supportedTypes, boolean exportTypesOnly);

    /**
     * Gets all services from the service registry which match the provided
     * filter
     * @param filter a {@link ServiceReferenceFilter} instance
     * @param types the possible for the service. The service must be class
     * compatible with all of these types to be returned.
     * @param exportTypesOnly if true, then the services returned must all be
     * explicitly registered using the types. Otherwise, it is sufficient that
     * each service must be type compatible with the supplied types
     * @return a list of service references.
     */
    List<ServiceRegistryEntry> getServices(ServiceReferenceFilter filter, Class<?>[] types, boolean exportTypesOnly);

    /**
     * Returns non-null reference if service reference is present in 
     * @param entry the {@link ServiceRegistryEntry} under examination
     * @param exportTypes an array of {@link Class} instances
     * @return true if service reference is present in service registry against all of the passed in export types
     */
    public boolean isPresentInExportTypes(ServiceRegistryEntry entry, Class<?>[] exportTypes);
}
