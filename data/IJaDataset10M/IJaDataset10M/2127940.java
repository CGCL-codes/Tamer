package edu.upc.lsi.kemlg.aws.agentshell;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AgentShell {

    protected Set<IDecisionMaker> dm;

    public AgentShell() {
        dm = new HashSet<IDecisionMaker>();
        dm.add(new AsyncDecisionMaker(new DecisionMaker()));
        initAgentShell();
    }

    private void initAgentShell() {
        Iterator<IDecisionMaker> it;
        BehaviourList bl;
        it = dm.iterator();
        while (it.hasNext()) {
            bl = new BehaviourList();
            it.next().initDecisionMaker(bl);
        }
    }

    public static void main(String args[]) {
        AgentShell as;
        as = new AgentShell();
    }
}
