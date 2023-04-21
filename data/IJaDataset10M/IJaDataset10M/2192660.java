package org.personalsmartspace.pss_sm_locator.api;

import java.util.Set;
import org.personalsmartspace.pss_sm_api.impl.PssService;
import org.personalsmartspace.sre.api.pss3p.IServiceIdentifier;

public interface IServiceLocator {

    public Set<PssService> findInstalledServices();

    /**
	 * Returns the persisted metadata for the PssService with the provided Id, 
	 * or null if not available.
	 */
    public PssService getInternalServiceMetadata(IServiceIdentifier serviceID);
}
