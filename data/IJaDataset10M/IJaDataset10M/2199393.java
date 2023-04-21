package org.personalsmartspace.pss_sm_composition.impl;

import org.personalsmartspace.sre.api.pss3p.IServiceIdentifier;

public interface IServiceMatcher {

    /**
     * 
     * @param rankedServiceCollection
     */
    public IServiceIdentifier matchService(Object rankedServiceCollection);
}
