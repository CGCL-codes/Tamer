package de.fzi.mapaco.strategy.acs;

import java.util.HashMap;
import java.util.Map;
import de.fzi.mapaco.PheromoneMatrix;
import de.fzi.mapaco.ontoutils.OntologyManager;

/**
 * @author bock
 *
 */
public final class ACSAntFactory {

    private static Map<OntologyManager, Map<PheromoneMatrix, ACSAntFactory>> factoryStore;

    static {
        factoryStore = new HashMap<OntologyManager, Map<PheromoneMatrix, ACSAntFactory>>();
    }

    private OntologyManager om;

    private PheromoneMatrix pm;

    private Map<Integer, ACSAnt> ants;

    private ACSAntFactory(OntologyManager om, PheromoneMatrix pm) {
        ants = new HashMap<Integer, ACSAnt>();
        this.om = om;
        this.pm = pm;
    }

    public static ACSAntFactory getInstance(OntologyManager om, PheromoneMatrix pm) {
        if (factoryStore.containsKey(om)) {
            Map<PheromoneMatrix, ACSAntFactory> omMap = factoryStore.get(om);
            if (!omMap.containsKey(pm)) omMap.put(pm, new ACSAntFactory(om, pm));
            return omMap.get(pm);
        } else {
            Map<PheromoneMatrix, ACSAntFactory> omMap = new HashMap<PheromoneMatrix, ACSAntFactory>();
            ACSAntFactory antFactory = new ACSAntFactory(om, pm);
            omMap.put(pm, antFactory);
            factoryStore.put(om, omMap);
            return antFactory;
        }
    }

    public ACSAnt getAnt(int i) {
        if (!ants.containsKey(i)) ants.put(i, new ACSAnt(i, om, pm));
        return ants.get(i);
    }
}
