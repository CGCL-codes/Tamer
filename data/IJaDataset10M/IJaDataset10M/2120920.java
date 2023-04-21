package logbeam.model.persistence;

import java.util.List;
import logbeam.model.Agent;

public interface AgentDao {

    public void insert(Agent agent);

    public void update(Agent agent);

    public Agent getByName(String name);

    public List<Agent> getAllAgents();
}
