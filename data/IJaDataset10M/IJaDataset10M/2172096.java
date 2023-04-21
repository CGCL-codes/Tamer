package no.ugland.utransprod.service;

import java.util.List;
import no.ugland.utransprod.model.Assembly;
import no.ugland.utransprod.model.Supplier;

/**
 * Interface for serviceklasse mot tabell ASSEMBLY
 * @author atle.brekka
 */
public interface AssemblyManager extends OverviewManager<Assembly> {

    String MANAGER_NAME = "assemblyManager";

    /**
     * Finner monteringer for gitt team,�r og uke
     * @param supplier 
     * @param year
     * @param week
     * @return monteringer
     */
    List<Assembly> findBySupplierYearWeek(Supplier supplier, Integer year, Integer week);

    /**
     * Lagrer montering
     * @param assembly
     */
    void saveAssembly(Assembly assembly);
}
