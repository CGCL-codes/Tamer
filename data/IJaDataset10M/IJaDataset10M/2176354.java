package org.ws4d.java.configuration;

import org.ws4d.java.structures.HashMap;

/**
 * @author mspies
 */
public class ServicesPropertiesHandler implements PropertiesHandler {

    private HashMap servProps = new HashMap();

    private ServiceProperties buildUpProperties = null;

    /** default properties for all services */
    private ServiceProperties defaultProperties = null;

    ServicesPropertiesHandler() {
        super();
    }

    /**
	 * Returns instance of service properties handler.
	 * 
	 * @return the singleton instance of the service properties
	 */
    public static ServicesPropertiesHandler getInstance() {
        return (ServicesPropertiesHandler) Properties.forClassName(Properties.SERVICES_PROPERTIES_HANDLER_CLASS);
    }

    /**
	 * Gets service properties.
	 * 
	 * @param configurationId
	 * @return service properties
	 */
    public ServiceProperties getServiceProperties(Integer configurationId) {
        return (ServiceProperties) servProps.get(configurationId);
    }

    public void setProperties(PropertyHeader header, Property property) {
        if (Properties.HEADER_SECTION_SERVICES.equals(header)) {
            if (defaultProperties == null) {
                defaultProperties = new ServiceProperties();
            }
            defaultProperties.addProperty(property);
        } else if (Properties.HEADER_SUBSECTION_SERVICE.equals(header)) {
            if (buildUpProperties == null) {
                if (defaultProperties != null) {
                    buildUpProperties = new ServiceProperties(defaultProperties);
                } else {
                    buildUpProperties = new ServiceProperties();
                }
            }
            buildUpProperties.addProperty(property);
        }
    }

    public void finishedSection(int depth) {
        if (depth == 2 && buildUpProperties != null) {
            if (!buildUpProperties.getConfigurationId().equals(ServiceProperties.DEFAULT_CONFIGURATION_ID)) {
                Integer id = buildUpProperties.getConfigurationId();
                servProps.put(id, buildUpProperties);
            }
            buildUpProperties = null;
        } else if (depth <= 1) {
            defaultProperties = null;
            buildUpProperties = null;
        }
    }
}
