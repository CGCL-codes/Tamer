package org.fudaa.fudaa.crue.study.node;

import java.util.List;
import org.fudaa.dodico.crue.metier.etude.ManagerEMHScenario;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 * Used to create scenario node
 * @author genesis
 */
public class ManagerEMHScenarioChildFactory extends ChildFactory<ManagerEMHScenario> {

    private List<ManagerEMHScenario> resultList;

    public ManagerEMHScenarioChildFactory(List<ManagerEMHScenario> resultList) {
        this.resultList = resultList;
    }

    @Override
    protected boolean createKeys(List<ManagerEMHScenario> list) {
        for (ManagerEMHScenario scenario : resultList) {
            list.add(scenario);
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(ManagerEMHScenario c) {
        return null;
    }
}
