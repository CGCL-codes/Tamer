package com.mysolution.app.manager.impl;

import com.mysolution.app.manager.SiteManager;
import com.mysolution.persistence.PersistenceService;
import com.mysolution.persistence.DomainID;
import com.mysolution.persistence.domain.Site;
import com.mysolution.core.resources.resourcesImpl.SiteResource;
import java.util.*;
import java.text.CollationElementIterator;
import org.apache.commons.lang.StringUtils;

/**
 * DKu
 * Date: Apr 2, 2009
 * Time: 4:15:50 PM
 */
public class SiteManagerImpl implements SiteManager {

    PersistenceService persistenceService;

    public SiteManagerImpl(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public List<Site> getAllSites() {
        List<Site> siteList = new ArrayList<Site>(persistenceService.loadDomains(Site.class, Site.AllIndex.Value.INSTANCE));
        Collections.sort(siteList, siteComparator);
        return siteList;
    }

    public Site getSiteById(DomainID siteId) {
        return persistenceService.loadDomain(Site.class, siteId);
    }

    public void save(Site site) {
        if (site.getId() != null) persistenceService.update(Site.class, site); else persistenceService.save(Site.class, site);
    }

    public void delete(DomainID siteId) {
        persistenceService.remove(Site.class, siteId);
    }

    public SiteResource createResource(Site site) {
        return SiteResource.create(persistenceService, site);
    }

    Comparator<Site> siteComparator = new Comparator<Site>() {

        public int compare(Site s1, Site s2) {
            String str1 = s1.getSiteDomain();
            String str2 = s2.getSiteDomain();
            if (StringUtils.isNotEmpty(str1) && StringUtils.isNotEmpty(str2)) return str1.compareToIgnoreCase(str2); else return 0;
        }
    };
}
