package no.ugland.utransprod.service;

import java.util.List;
import no.ugland.utransprod.model.ApplicationUser;
import no.ugland.utransprod.model.Deviation;
import no.ugland.utransprod.model.JobFunction;
import no.ugland.utransprod.model.Order;
import no.ugland.utransprod.service.enums.LazyLoadDeviationEnum;
import no.ugland.utransprod.util.excel.ExcelManager;

/**
 * Interface for manager mor tabell DEVIATION
 * @author atle.brekka
 */
public interface DeviationManager extends OverviewManager<Deviation> {

    public static final String MANAGER_NAME = "deviationManager";

    /**
     * Finner alle avvik for en gitt funksjon
     * @param jobFunction
     * @return avvik
     */
    List<Deviation> findByJobFunction(JobFunction jobFunction);

    /**
     * Lazy laster avvik
     * @param deviation
     * @param enums
     */
    void lazyLoad(Deviation deviation, LazyLoadDeviationEnum[] enums);

    /**
     * Finner alle avvik for en gitt leder
     * @param applicationUser
     * @return avvik
     */
    List<Deviation> findByManager(ApplicationUser applicationUser);

    /**
     * Finner avvik for gitt ordre
     * @param order
     * @return avvik
     */
    List<Deviation> findByOrder(Order order);

    /**
     * Finner alle avvik med montering
     * @return avvik
     */
    List<Deviation> findAllAssembly();

    /**
     * Lagrer avvik
     * @param deviation
     */
    void saveDeviation(Deviation deviation);
}
