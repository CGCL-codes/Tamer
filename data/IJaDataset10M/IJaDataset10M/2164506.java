package com.mechup.mediaserver.service.contentdirectory.state;

import org.osgi.service.upnp.UPnPStateVariable;

/**
 * Content Directory Service variable, look at the Service Templates
 * on the UPnP Forum web site for more details.
 * 
 * @author Stefan Berntheisel
 */
public class AArgTypeSearchCriteria implements UPnPStateVariable {

    public final String NAME = "A_ARG_TYPE_SearchCriteria";

    public final String DEFAULT_VALUE = "";

    /**
	 * Constructor
	 */
    public AArgTypeSearchCriteria() {
    }

    /**
	 * Get the name of the variable
	 * @see org.osgi.service.upnp.UPnPStateVariable#getName()
	 */
    public String getName() {
        return NAME;
    }

    /**
	 * Get the Java data type
	 * @see org.osgi.service.upnp.UPnPStateVariable#getJavaDataType()
	 */
    public Class getJavaDataType() {
        return String.class;
    }

    /**
	 * Get the UPnP data type
	 * @see org.osgi.service.upnp.UPnPStateVariable#getUPnPDataType()
	 */
    public String getUPnPDataType() {
        return TYPE_STRING;
    }

    /**
	 * Get the default value
	 * @see org.osgi.service.upnp.UPnPStateVariable#getDefaultValue()
	 */
    public Object getDefaultValue() {
        return DEFAULT_VALUE;
    }

    /**
	 * Get the allowed values
	 * @see org.osgi.service.upnp.UPnPStateVariable#getAllowedValues()
	 */
    public String[] getAllowedValues() {
        return null;
    }

    /**
	 * Get the minimum value
	 * @see org.osgi.service.upnp.UPnPStateVariable#getMinimum()
	 */
    public Number getMinimum() {
        return null;
    }

    /**
	 * Get the maximum value
	 * @see org.osgi.service.upnp.UPnPStateVariable#getMaximum()
	 */
    public Number getMaximum() {
        return null;
    }

    /**
	 * Get the step count
	 * @see org.osgi.service.upnp.UPnPStateVariable#getStep()
	 */
    public Number getStep() {
        return null;
    }

    /**
	 * Is variable allowed to send events
	 * @see org.osgi.service.upnp.UPnPStateVariable#sendsEvents()
	 */
    public boolean sendsEvents() {
        return false;
    }
}
