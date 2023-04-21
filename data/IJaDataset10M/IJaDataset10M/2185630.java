package com.corratech.opensuite.persist.filters;

import com.corratech.opensuite.api.businesscomponent.Endpoint;
import com.corratech.opensuite.api.businesscomponent.Property;
import com.corratech.opensuite.persist.api.Filter;

/**
 * @author aleksandr.kryzhak
 *
 */
public class EndpointPropertyFilter extends Filter {

    public EndpointPropertyFilter() {
    }

    public void setEndpoint(Endpoint endpoint) {
        put("endpoint", endpoint);
    }

    /**
	 * @param property
	 */
    public void setProperty(Property property) {
        put("property", property);
    }
}
