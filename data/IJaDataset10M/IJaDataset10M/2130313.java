package no.ugland.utransprod.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import no.ugland.utransprod.ProTransException;
import no.ugland.utransprod.dao.DeviationDAO;
import no.ugland.utransprod.gui.handlers.CheckObject;
import no.ugland.utransprod.model.ApplicationUser;
import no.ugland.utransprod.model.Deviation;
import no.ugland.utransprod.model.JobFunction;
import no.ugland.utransprod.model.Order;
import no.ugland.utransprod.service.DeviationManager;
import no.ugland.utransprod.service.enums.LazyLoadDeviationEnum;
import no.ugland.utransprod.util.excel.ExcelReportSetting;

/**
 * Implementasjon av manager for tabell DEVIATION.
 * @author atle.brekka
 */
public class DeviationManagerImpl extends ManagerImpl<Deviation> implements DeviationManager {

    /**
     * @see no.ugland.utransprod.service.OverviewManager#findAll()
     */
    public final List<Deviation> findAll() {
        return dao.getObjects();
    }

    /**
     * @param object
     * @return avvik
     * @see no.ugland.utransprod.service.OverviewManager#findByObject(java.lang.Object)
     */
    public final List<Deviation> findByObject(final Deviation object) {
        return ((DeviationDAO) dao).findByDeviation(object);
    }

    /**
     * @param object
     * @see no.ugland.utransprod.service.OverviewManager#refreshObject(java.lang.Object)
     */
    public final void refreshObject(final Deviation object) {
        ((DeviationDAO) dao).refreshObject(object);
    }

    /**
     * Sletter avvik.
     * @param deviation
     */
    public final void removeDeviation(final Deviation deviation) {
        if (deviation.getDeviationId() != null) {
            dao.removeObject(deviation.getDeviationId());
        }
    }

    /**
     * @param object
     * @see no.ugland.utransprod.service.OverviewManager#removeObject(java.lang.Object)
     */
    public final void removeObject(final Deviation object) {
        removeDeviation(object);
    }

    /**
     * Lagrer avvik.
     * @param deviation
     */
    public final void saveDeviation(final Deviation deviation) {
        dao.saveObject(deviation);
    }

    /**
     * @param object
     * @see no.ugland.utransprod.service.OverviewManager#saveObject(java.lang.Object)
     */
    public final void saveObject(final Deviation object) {
        saveDeviation(object);
    }

    /**
     * @see no.ugland.utransprod.service.DeviationManager#
     * findByJobFunction(no.ugland.utransprod.model.JobFunction)
     */
    public final List<Deviation> findByJobFunction(final JobFunction jobFunction) {
        return ((DeviationDAO) dao).findByJobFunction(jobFunction);
    }

    /**
     * @see no.ugland.utransprod.service.DeviationManager#lazyLoad(no.ugland.utransprod.model.Deviation,
     *      no.ugland.utransprod.service.enums.LazyLoadDeviationEnum[])
     */
    public final void lazyLoad(final Deviation deviation, final LazyLoadDeviationEnum[] enums) {
        ((DeviationDAO) dao).lazyLoad(deviation, enums);
    }

    /**
     * @see no.ugland.utransprod.service.DeviationManager#
     * findByManager(no.ugland.utransprod.model.ApplicationUser)
     */
    public final List<Deviation> findByManager(final ApplicationUser applicationUser) {
        return ((DeviationDAO) dao).findByManager(applicationUser);
    }

    /**
     * @see no.ugland.utransprod.service.DeviationManager#findByOrder(no.ugland.utransprod.model.Order)
     */
    public final List<Deviation> findByOrder(final Order order) {
        return ((DeviationDAO) dao).findByOrder(order);
    }

    /**
     * @see no.ugland.utransprod.service.DeviationManager#findAllAssembly()
     */
    public final List<Deviation> findAllAssembly() {
        return ((DeviationDAO) dao).findAllAssembly();
    }

    public void lazyLoad(Deviation object, Enum[] enums) {
        lazyLoad(object, (LazyLoadDeviationEnum[]) enums);
    }

    @Override
    protected Serializable getObjectId(Deviation object) {
        return object.getDeviationId();
    }
}
