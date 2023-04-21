package no.ugland.utransprod.service.impl;

import java.util.List;
import no.ugland.utransprod.dao.GavlProductionVDAO;
import no.ugland.utransprod.model.Produceable;
import no.ugland.utransprod.model.ProductAreaGroup;
import no.ugland.utransprod.service.GavlProductionVManager;

/**
 * Implementasjon av serviceklasse for view GAVL_PRODUCTION_V.
 * @author atle.brekka
 */
public class GavlProductionVManagerImpl extends AbstractApplyListManager<Produceable> implements GavlProductionVManager {

    private GavlProductionVDAO dao;

    /**
     * @param aDao
     */
    public final void setGavlProductionVDAO(final GavlProductionVDAO aDao) {
        this.dao = aDao;
    }

    /**
     * @see no.ugland.utransprod.service.GavlProductionVManager#findAllApplyable()
     */
    public final List<Produceable> findAllApplyable() {
        return dao.findAll();
    }

    /**
     * @see no.ugland.utransprod.service.GavlProductionVManager#findApplyableByOrderNr(java.lang.String)
     */
    public final List<Produceable> findApplyableByOrderNr(final String orderNr) {
        return dao.findByOrderNr(orderNr);
    }

    /**
     * @see no.ugland.utransprod.service.GavlProductionVManager#
     * refresh(no.ugland.utransprod.model.Produceable)
     */
    public final void refresh(final Produceable productionV) {
        dao.refresh(productionV);
    }

    /**
     * @see no.ugland.utransprod.service.GavlProductionVManager#findApplyableByCustomerNr(java.lang.Integer)
     */
    public final List<Produceable> findApplyableByCustomerNr(final Integer customerNr) {
        return dao.findByCustomerNr(customerNr);
    }

    public List<Produceable> findApplyableByCustomerNrAndProductAreaGroup(Integer customerNr, ProductAreaGroup productAreaGroup) {
        return dao.findByCustomerNrAndProductAreaGroup(customerNr, productAreaGroup);
    }

    public List<Produceable> findApplyableByOrderNrAndProductAreaGroup(String orderNr, ProductAreaGroup productAreaGroup) {
        return dao.findByOrderNrAndProductAreaGroup(orderNr, productAreaGroup);
    }
}
