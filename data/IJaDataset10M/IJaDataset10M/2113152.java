package no.ugland.utransprod.service.impl;

import java.io.Serializable;
import java.util.List;
import no.ugland.utransprod.dao.PostShipmentDAO;
import no.ugland.utransprod.model.PostShipment;
import no.ugland.utransprod.service.PostShipmentManager;
import no.ugland.utransprod.service.enums.LazyLoadPostShipmentEnum;
import no.ugland.utransprod.util.Periode;

/**
 * Implementasjon av serviceklasse for tabell POST_SHIPMENT.
 * @author atle.brekka
 */
public class PostShipmentManagerImpl extends ManagerImpl<PostShipment> implements PostShipmentManager {

    /**
     * @see no.ugland.utransprod.service.PostShipmentManager#findAllWithoutTransport()
     */
    public final List<PostShipment> findAllWithoutTransport() {
        return ((PostShipmentDAO) dao).findAllWithoutTransport();
    }

    /**
     * @see no.ugland.utransprod.service.PostShipmentManager#
     * savePostShipment(no.ugland.utransprod.model.PostShipment)
     */
    public final void savePostShipment(final PostShipment postShipment) {
        dao.saveObject(postShipment);
    }

    /**
     * @see no.ugland.utransprod.service.PostShipmentManager#
     * removePostShipment(no.ugland.utransprod.model.PostShipment)
     */
    public final void removePostShipment(final PostShipment postShipment) {
        dao.removeObject(postShipment.getPostShipmentId());
    }

    /**
     * @see no.ugland.utransprod.service.PostShipmentManager#findAllNotSent()
     */
    public final List<PostShipment> findAllNotSent() {
        return ((PostShipmentDAO) dao).findAllNotSent();
    }

    /**
     * @see no.ugland.utransprod.service.PostShipmentManager#
     * lazyLoad(no.ugland.utransprod.model.PostShipment,
     *      no.ugland.utransprod.service.enums.LazyLoadPostShipmentEnum[])
     */
    public final void lazyLoad(final PostShipment postShipment, final LazyLoadPostShipmentEnum[] enums) {
        ((PostShipmentDAO) dao).lazyLoad(postShipment, enums);
    }

    /**
     * @see no.ugland.utransprod.service.OverviewManager#findAll()
     */
    public final List<PostShipment> findAll() {
        return ((PostShipmentDAO) dao).findAll();
    }

    /**
     * @param object
     * @return etterleveringer
     * @see no.ugland.utransprod.service.OverviewManager#findByObject(java.lang.Object)
     */
    public final List<PostShipment> findByObject(final PostShipment object) {
        return dao.findByExampleLike(object);
    }

    /**
     * @param object
     * @see no.ugland.utransprod.service.OverviewManager#refreshObject(java.lang.Object)
     */
    public final void refreshObject(final PostShipment object) {
        ((PostShipmentDAO) dao).refreshPostShipment(object);
    }

    /**
     * @param object
     * @see no.ugland.utransprod.service.OverviewManager#removeObject(java.lang.Object)
     */
    public final void removeObject(final PostShipment object) {
        removePostShipment(object);
    }

    /**
     * @param object
     * @see no.ugland.utransprod.service.OverviewManager#saveObject(java.lang.Object)
     */
    public final void saveObject(final PostShipment object) {
        savePostShipment(object);
    }

    /**
     * @see no.ugland.utransprod.service.PostShipmentManager#
     * lazyLoadTree(no.ugland.utransprod.model.PostShipment)
     */
    public final void lazyLoadTree(final PostShipment postShipment) {
        ((PostShipmentDAO) dao).lazyLoadTree(postShipment);
    }

    /**
     * @see no.ugland.utransprod.service.PostShipmentManager#findByOrderNr(java.lang.String)
     */
    public final List<PostShipment> findByOrderNr(final String orderNr) {
        return ((PostShipmentDAO) dao).findByOrderNr(orderNr);
    }

    /**
     * @see no.ugland.utransprod.service.PostShipmentManager#findByCustomerNr(java.lang.Integer)
     */
    public final List<PostShipment> findByCustomerNr(final Integer customerNr) {
        return ((PostShipmentDAO) dao).findByCustomerNr(customerNr);
    }

    /**
     * @see no.ugland.utransprod.service.PostShipmentManager#load(no.ugland.utransprod.model.PostShipment)
     */
    public final void load(final PostShipment postShipment) {
        ((PostShipmentDAO) dao).load(postShipment);
    }

    public final List<PostShipment> findSentInPeriod(final Periode periode, final String productAreaGroupName) {
        return ((PostShipmentDAO) dao).findSentInPeriod(periode, productAreaGroupName);
    }

    public final PostShipment loadById(final Integer postShipmentId) {
        return dao.getObject(postShipmentId);
    }

    public void lazyLoad(PostShipment object, Enum[] enums) {
        lazyLoad(object, (LazyLoadPostShipmentEnum[]) enums);
    }

    @Override
    protected Serializable getObjectId(PostShipment object) {
        return object.getPostShipmentId();
    }
}
