package net.sf.f2s.service;

import net.sf.f2s.service.config.ServiceConfiguration;

/**
 * Base interface for all the services. Whenever the operation suported by the
 * {@link Service} it must implement this interface. Most of implementations
 * will benefit from abstract instances of this interface:
 * <ul>
 * <li>{@link AbstractHttpService}: provides an basic support for HTTP services.
 * </li>
 * </ul>
 * 
 * @author Rogiel
 * @since 1.0
 */
public interface Service {

    /**
	 * Get the ServiceID.
	 * 
	 * @return the id of the service
	 */
    String getId();

    /**
	 * Get Major version of this service
	 * 
	 * @return the major version
	 */
    int getMajorVersion();

    /**
	 * Get the minor version of this service
	 * 
	 * @return the minor version
	 */
    int getMinorVersion();

    /**
	 * Returns this {@link ServiceConfiguration} instance
	 * 
	 * @return the {@link ServiceConfiguration} instance
	 */
    ServiceConfiguration getServiceConfiguration();
}
