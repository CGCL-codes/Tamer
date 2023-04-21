package no.ugland.utransprod.service.impl;

import java.io.Serializable;
import java.util.List;
import no.ugland.utransprod.dao.SupplierTypeDAO;
import no.ugland.utransprod.model.SupplierType;
import no.ugland.utransprod.service.SupplierTypeManager;

/**
 * Implementasjon av serviceklasse for tabell SUPPLIER_TYPE.
 * @author atle.brekka
 */
public class SupplierTypeManagerImpl extends ManagerImpl<SupplierType> implements SupplierTypeManager {

    /**
     * @see no.ugland.utransprod.service.OverviewManager#findAll()
     */
    public final List<SupplierType> findAll() {
        return dao.getObjects("supplierTypeName");
    }

    /**
     * Finner basert p� eksempel.
     * @param supplierType
     * @return leverand�rtyper
     */
    public final List<SupplierType> findBySupplierType(final SupplierType supplierType) {
        return dao.findByExampleLike(supplierType);
    }

    /**
     * @param object
     * @return leverand�rtyper
     * @see no.ugland.utransprod.service.OverviewManager#findByObject(java.lang.Object)
     */
    public final List<SupplierType> findByObject(final SupplierType object) {
        return findBySupplierType(object);
    }

    /**
     * @param object
     * @see no.ugland.utransprod.service.OverviewManager#refreshObject(java.lang.Object)
     */
    public final void refreshObject(final SupplierType object) {
        ((SupplierTypeDAO) dao).refreshObject(object);
    }

    /**
     * Fjerner leverand�rtype.
     * @param supplierType
     */
    public final void removeSupplierType(final SupplierType supplierType) {
        dao.removeObject(supplierType.getSupplierTypeId());
    }

    /**
     * @param object
     * @see no.ugland.utransprod.service.OverviewManager#removeObject(java.lang.Object)
     */
    public final void removeObject(final SupplierType object) {
        removeSupplierType(object);
    }

    /**
     * Lagrer leverand�rtype.
     * @param supplierType
     */
    public final void saveSupplierType(final SupplierType supplierType) {
        dao.saveObject(supplierType);
    }

    /**
     * @param object
     * @see no.ugland.utransprod.service.OverviewManager#saveObject(java.lang.Object)
     */
    public final void saveObject(final SupplierType object) {
        saveSupplierType(object);
    }

    public void lazyLoad(SupplierType object, Enum[] enums) {
    }

    @Override
    protected Serializable getObjectId(SupplierType object) {
        return object.getSupplierTypeId();
    }
}
