package com.plus.fcentre.jobfilter.dao;

import com.plus.fcentre.jobfilter.domain.Agency;
import com.plus.fcentre.jobfilter.domain.Agent;

/**
 * Data Access Object interface providing {@link Agent} persistence operations.
 * 
 * @author Steve Jefferson
 */
public interface AgentDAO {

    /**
	 * Lookup an agent by its id.
	 * 
	 * @param agentId id of agent to lookup.
	 * @return agent with given id or null if no such agent.
	 */
    public Agent findAgentById(long agentId);

    /**
	 * Lookup a named agent, belonging to a given agency.
	 * 
	 * @param agency agency for which agent belongs.
	 * @param agentName name of agent to lookup.
	 * @return agent with given name and agency or null if no such agent.
	 */
    public Agent findAgentByName(Agency agency, String agentName);

    /**
	 * Remove the provided agent from persistent store.
	 * <p>
	 * This method is included for ORMs (ie. JPA) which do not implement
	 * persistence-by-reachability. For more <i>enlightened</i> ORMs which deign
	 * to provide this basic functionality, this method can be implemented as a
	 * null operation.</p>
	 * 
	 * @param agent agent to remove.
	 */
    public void removeAgent(Agent agent);

    /**
   * Detach the provided persistent agent from the current transaction.
   * 
   * @param agent agent to detach.
   * @return detached agent.
   */
    public Agent detachAgent(Agent agent);

    /**
	 * Attach the provided (detached, potentially modified) agent to the
	 * current transaction.
	 * <p>
	 * The provided agent may have been previously detached via
	 * {@link #detachAgent(Agent)} been modified outside of a transaction or
	 * it may have merely been retrieved in another transaction.
	 * </p>
	 * 
	 * @param agent agent to attach.
	 * @return attached agent.
	 */
    public Agent attachAgent(Agent agent);

    /**
	 * Restore the provided (detached, potentially modified) agent to its
	 * current persisted state, discarding any pending updates.
	 * <p>
	 * The provided agent will have been previously obtained via
	 * {@link #detachAgent(Agent)} and may have been modified.
	 * </p>
	 * 
	 * @param agent agent to restore.
	 * @return restored agent.
	 */
    public Agent restoreAgent(Agent agent);
}
