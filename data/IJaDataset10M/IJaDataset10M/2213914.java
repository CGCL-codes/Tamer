package no.ugland.utransprod.service;

import no.ugland.utransprod.model.CostType;

/**
 * Interface for manger for kostnadstype
 * @author atle.brekka
 */
public interface CostTypeManager extends OverviewManager<CostType> {

    String MANAGER_NAME = "costTypeManager";

    /**
     * Finner basert p� navn
     * @param name
     * @return kostnadstype
     */
    CostType findByName(String name);
}
