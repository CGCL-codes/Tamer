package org.vikamine.gui.introductoryDialog;

import java.util.HashMap;
import java.util.Map;
import org.vikamine.app.event.OntologyChangeListener;
import org.vikamine.app.event.OntologyChangedEvent;
import org.vikamine.kernel.data.Ontology;
import org.vikamine.kernel.subgroup.SG;
import org.vikamine.kernel.subgroup.SGDescription;
import org.vikamine.kernel.subgroup.SGStatistics;
import org.vikamine.kernel.subgroup.selectors.SGNominalSelector;
import org.vikamine.kernel.subgroup.target.SGTarget;

/**
 * @author lemmerich
 * @date 03/2009
 */
public class SGStatsCache implements OntologyChangeListener {

    public static SGStatsCache getInstance() {
        if (instance == null) {
            instance = new SGStatsCache();
        }
        return instance;
    }

    private SGTarget target;

    private final Map<SGNominalSelector, SG> selToSGMap;

    private Ontology om;

    private static SGStatsCache instance;

    public SGStatsCache() {
        selToSGMap = new HashMap<SGNominalSelector, SG>();
    }

    public void clear() {
        this.target = null;
        this.selToSGMap.clear();
    }

    public SGStatistics getStatistics(SGNominalSelector selector) {
        if (om != null) {
            SG sg = selToSGMap.get(selector);
            if (sg == null) {
                SGDescription description = new SGDescription();
                description.add(selector);
                sg = new SG(om.getPopulation(), target, description);
                sg.createStatistics(null);
                selToSGMap.put(selector, sg);
            }
            return sg.getStatistics();
        }
        return null;
    }

    public SGTarget getTarget() {
        return target;
    }

    @Override
    public void ontologyChanged(OntologyChangedEvent eve) {
        om = eve.getNewOntology();
        clear();
    }

    public void setTarget(SGTarget target) {
        clear();
        this.target = target;
    }
}
