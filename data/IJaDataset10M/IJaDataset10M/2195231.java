package no.ugland.utransprod.service.impl;

import java.io.Serializable;
import java.util.List;
import no.ugland.utransprod.dao.ColliDAO;
import no.ugland.utransprod.model.Colli;
import no.ugland.utransprod.model.Order;
import no.ugland.utransprod.model.PostShipment;
import no.ugland.utransprod.service.ColliManager;

/**
 * Implementasjon av manager for kolli.
 * 
 * @author atle.brekka
 */
public class ColliManagerImpl extends ManagerImpl<Colli> implements ColliManager {

    /**
	 * @see no.ugland.utransprod.service.ColliManager#findByNameAndOrder(java.lang.String,
	 *      no.ugland.utransprod.model.Order)
	 */
    public final Colli findByNameAndOrder(final String colliName, final Order order) {
        return ((ColliDAO) dao).findByNameAndOrder(colliName, order);
    }

    /**
	 * @see no.ugland.utransprod.service.ColliManager#saveColli(no.ugland.utransprod.model.Colli)
	 */
    public final void saveColli(final Colli colli) {
        dao.saveObject(colli);
    }

    /**
	 * @see no.ugland.utransprod.service.OverviewManager#findAll()
	 */
    public final List<Colli> findAll() {
        return dao.getObjects();
    }

    /**
	 * @param object
	 * @return kollier
	 * @see no.ugland.utransprod.service.OverviewManager#findByObject(java.lang.Object)
	 */
    public final List<Colli> findByObject(final Colli object) {
        return dao.findByExampleLike(object);
    }

    /**
	 * @param object
	 * @see no.ugland.utransprod.service.OverviewManager#refreshObject(java.lang.Object)
	 */
    public final void refreshObject(final Colli object) {
        ((ColliDAO) dao).refreshObject(object);
    }

    /**
	 * @param object
	 * @see no.ugland.utransprod.service.OverviewManager#removeObject(java.lang.Object)
	 */
    public final void removeObject(final Colli object) {
        if (object.getColliId() != null) {
            dao.removeObject(object.getColliId());
        }
    }

    /**
	 * @param object
	 * @see no.ugland.utransprod.service.OverviewManager#saveObject(java.lang.Object)
	 */
    public final void saveObject(final Colli object) {
        saveColli(object);
    }

    /**
	 * @see no.ugland.utransprod.service.ColliManager#findByNameAndPostShipment(java.lang.String,
	 *      no.ugland.utransprod.model.PostShipment)
	 */
    public final Colli findByNameAndPostShipment(final String colliName, final PostShipment postShipment) {
        return ((ColliDAO) dao).findByNameAndPostShipment(colliName, postShipment);
    }

    @Override
    protected Serializable getObjectId(Colli object) {
        return object.getColliId();
    }

    public void lazyLoadAll(Colli colli) {
        ((ColliDAO) dao).lazyLoadAll(colli);
    }
}
