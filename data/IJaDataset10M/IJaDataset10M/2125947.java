package com.mysolution.app.manager.impl;

import com.mysolution.app.manager.CredentialsManager;
import com.mysolution.persistence.PersistenceService;
import com.mysolution.persistence.DomainID;
import com.mysolution.persistence.domain.Credentials;
import java.util.Set;

/**
 * DKu
 * Date: Apr 1, 2009
 * Time: 9:58:16 PM
 */
public class CredentialsManagerImpl implements CredentialsManager {

    PersistenceService persistenceService;

    public CredentialsManagerImpl(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public Set<Credentials> getCredentialsBySiteDomain(DomainID siteId) {
        return persistenceService.loadDomains(Credentials.class, new Credentials.SiteIndex.Value(siteId));
    }
}
