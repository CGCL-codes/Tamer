package hu.aitia.qcg.statnetds.demo.griddemomodel;

import hu.aitia.qcg.statnetds.core.NodeOfWorker;
import hu.aitia.qcg.statnetds.core.StatNetWorker;

/**
 * 
 * @author Gabor Szemes & Attila Szabo
 * @version 1.0
 */
public class Worker extends StatNetWorker {

    protected boolean useBigAgents = false;

    @Override
    public NodeOfWorker getInitNode(int i) {
        Agent agent = new Agent();
        if (useBigAgents) agent.setBigAgent(true);
        return agent;
    }

    @Override
    public void init() {
        Object useBigAgentsVal = getMasterParameter("useBigAgents");
        if (useBigAgentsVal != null) useBigAgents = (Boolean) useBigAgentsVal;
    }
}
