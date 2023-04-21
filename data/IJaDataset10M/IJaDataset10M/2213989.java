package org.broadleafcommerce.core.offer.dao;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.broadleafcommerce.core.offer.domain.OfferAudit;
import org.broadleafcommerce.persistence.EntityConfiguration;
import org.springframework.stereotype.Repository;

@Repository("blOfferAuditDao")
public class OfferAuditDaoImpl implements OfferAuditDao {

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Resource(name = "blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    public OfferAudit create() {
        return ((OfferAudit) entityConfiguration.createEntityInstance(OfferAudit.class.getName()));
    }

    public void delete(final OfferAudit offerAudit) {
        OfferAudit loa = offerAudit;
        if (!em.contains(loa)) {
            loa = readAuditById(offerAudit.getId());
        }
        em.remove(loa);
    }

    public OfferAudit save(final OfferAudit offerAudit) {
        return em.merge(offerAudit);
    }

    public OfferAudit readAuditById(final Long offerAuditId) {
        return (OfferAudit) em.find(entityConfiguration.lookupEntityClass(OfferAudit.class.getName()), offerAuditId);
    }
}
