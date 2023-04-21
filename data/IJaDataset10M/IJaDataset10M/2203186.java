package com.codemonster.surinam.mgmt.impl;

import com.codemonster.surinam.export.lifecycle.ServiceAdapter;
import com.codemonster.surinam.export.meta.ProviderImplementationInfo;

/**
 * All registered service contracts are required to have implementations. So in the event that a service
 * contract does not have an implementation binding, one is provided by the framework in the form of a
 * placeholder that simply says that the service is currently 'unavailable' which affords any client
 * at runtime the opportunity to continue to function, provided that it is designed to suffer the lack
 * of a resource gracefully.<br/>
 */
@ProviderImplementationInfo(author = "Sam Provencher", organization = "Surinam Project", version = "0.01", releaseDate = "9/24/2006", webAddress = "http://tempweb.com", description = "This is the placeholder for a registered service implementation.")
public class PlaceholderProviderImplementation extends ServiceAdapter {

    public PlaceholderProviderImplementation() {
    }
}
