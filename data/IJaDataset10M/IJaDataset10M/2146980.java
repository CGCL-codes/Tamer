package org.criticalfailure.anp.core.persistence.vo.translator.impl;

import org.criticalfailure.anp.core.application.services.AlertService;
import org.criticalfailure.anp.core.domain.entity.Resource;
import org.criticalfailure.anp.core.domain.factory.IResourceFactory;
import org.criticalfailure.anp.core.persistence.storage.IDomainObjectPersister;
import org.criticalfailure.anp.core.persistence.vo.ResourceVO;
import org.criticalfailure.anp.core.persistence.vo.translator.IResourceValueObjectTranslator;
import org.criticalfailure.anp.core.persistence.vo.translator.ValueObjectTranslationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author pauly
 * 
 */
@Component("resourceValueObjectTranslator")
public class ResourceValueObjectTranslatorImpl implements IResourceValueObjectTranslator {

    private Logger logger;

    @Autowired
    private IDomainObjectPersister domainObjectPersister;

    @Autowired
    private AlertService alertService;

    @Autowired
    private IResourceFactory resourceFactory;

    public ResourceValueObjectTranslatorImpl() {
        logger = LoggerFactory.getLogger(this.getClass());
    }

    public Resource translateFrom(ResourceVO vo) throws ValueObjectTranslationException {
        Resource r = (Resource) domainObjectPersister.getCachedObject(vo.getId());
        if (r == null) {
            r = resourceFactory.createResource();
            r.setId(vo.getId());
            domainObjectPersister.cacheObject(r);
            translateFromWithMerge(vo, r);
        }
        return r;
    }

    public void translateFromWithMerge(ResourceVO vo, Resource obj) throws ValueObjectTranslationException {
        obj.setName(vo.getName());
        obj.setNotes(vo.getNotes());
        obj.setWebsite(vo.getWebsite());
    }

    public ResourceVO translateTo(Resource obj) {
        ResourceVO vo = new ResourceVO();
        vo.setId(obj.getId());
        vo.setName(obj.getName());
        vo.setNotes(obj.getNotes());
        vo.setWebsite(obj.getWebsite());
        return vo;
    }

    /**
     * @return the domainObjectPersister
     */
    public IDomainObjectPersister getDomainObjectPersister() {
        return domainObjectPersister;
    }

    /**
     * @param domainObjectPersister
     *            the domainObjectPersister to set
     */
    public void setDomainObjectPersister(IDomainObjectPersister domainObjectPersister) {
        this.domainObjectPersister = domainObjectPersister;
    }

    /**
     * @return the alertService
     */
    public AlertService getAlertService() {
        return alertService;
    }

    /**
     * @param alertService
     *            the alertService to set
     */
    public void setAlertService(AlertService alertService) {
        this.alertService = alertService;
    }

    /**
     * @return the resourceFactory
     */
    public IResourceFactory getResourceFactory() {
        return resourceFactory;
    }

    /**
     * @param resourceFactory
     *            the resourceFactory to set
     */
    public void setResourceFactory(IResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
    }
}
