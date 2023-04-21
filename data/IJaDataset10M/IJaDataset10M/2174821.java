package no.ugland.utransprod.service.impl;

import java.util.List;
import no.ugland.utransprod.dao.ProductAreaGroupDAO;
import no.ugland.utransprod.model.ProductAreaGroup;
import no.ugland.utransprod.service.ProductAreaGroupManager;

/**
 * Implementasjon av serviceklasse for tabell PRODUCT_AREA_GROUP.
 * @author atle.brekka
 */
public class ProductAreaGroupManagerImpl implements ProductAreaGroupManager {

    private ProductAreaGroupDAO dao;

    public final void setProductAreaGroupDAO(final ProductAreaGroupDAO aDao) {
        this.dao = aDao;
    }

    /**
     * @see no.ugland.utransprod.service.ProductAreaGroupManager#findAll()
     */
    public final List<ProductAreaGroup> findAll() {
        return dao.getObjects();
    }

    public final ProductAreaGroup findByName(final String name) {
        return dao.findByName(name);
    }
}
