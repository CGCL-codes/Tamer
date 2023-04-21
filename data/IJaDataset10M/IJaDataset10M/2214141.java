package org.personalsmartspace.pss_2_ipojo_Gui.impl;

import java.util.Hashtable;
import org.personalsmartspace.sre.api.pss3p.IServiceIdentifier;
import org.personalsmartspace.ipojo.pssintegrator.api.pss2ipojo.IPss2IpojoEventinfo;

/**
 * The information to be included in local event when SLM installs a new
 * service.
 */
public class EventInfo implements IPss2IpojoEventinfo {

    /**
    * 
    */
    private static final long serialVersionUID = 1L;

    public IServiceIdentifier getServiceID() {
        return serviceID;
    }

    public void setServiceID(IServiceIdentifier serviceID) {
        this.serviceID = serviceID;
    }

    public Hashtable getProperties() {
        return properties;
    }

    public void setProperties(Hashtable properties) {
        this.properties = properties;
    }

    /**
     * Service ID
     */
    private IServiceIdentifier serviceID;

    /**
     * New Properties
     */
    private Hashtable properties;

    public EventInfo(IServiceIdentifier serviceID, Hashtable properties) {
        this.serviceID = serviceID;
        this.properties = properties;
    }
}
