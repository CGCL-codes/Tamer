package playground.christoph.withinday.replanning.identifiers.interfaces;

import java.util.List;
import org.matsim.core.mobsim.framework.PersonAgent;
import playground.christoph.withinday.replanning.WithinDayReplanner;

public abstract class AgentsToReplanIdentifier implements Cloneable {

    protected boolean checkAllAgents = true;

    public abstract List<PersonAgent> getAgentsToReplan(double time, WithinDayReplanner withinDayReplanner);

    public boolean checkAllAgents() {
        return this.checkAllAgents;
    }

    public void checkAllAgents(boolean checkAllAgents) {
        this.checkAllAgents = checkAllAgents;
    }

    @Override
    public abstract AgentsToReplanIdentifier clone();

    protected void cloneBasicData(AgentsToReplanIdentifier clone) {
        clone.checkAllAgents = this.checkAllAgents;
    }
}
