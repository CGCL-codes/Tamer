package gridrm.util;

import jade.core.AID;
import java.util.List;

/**
 * An abstract collection to store information about agents.
 * 
 * @author Mehrdad Senobari <senobari@gmail.com>
 * 
 */
public abstract class AgentRegistry {

    public AgentRegistry() {
    }

    public abstract int getLength();

    public abstract AID getAgent(int index);

    public abstract boolean addAgent(AID agentID);

    public abstract boolean removeAgent(AID agentID);

    public abstract void merge(AgentRegistry agentReg);

    public abstract boolean hasAgent(AID agentID);

    public abstract List<AID> getAll();
}
