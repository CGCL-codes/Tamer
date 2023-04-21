package net.sf.beenuts.apc;

import java.util.*;
import net.sf.beenuts.apc.net.APCConnection;
import net.sf.beenuts.apc.util.APCConfiguration;
import net.sf.beenuts.apr.APR;

/**
 * interface modeling a beenuts agent process cluster
 *  
 * @author Thomas Vengels
 *
 */
public interface AgentProcessCluster {

    /**
	 * returns a list of agents running locally on this agentProcessCluster
	 * 
	 * @return list of local agents
	 */
    public Collection<String> getLocalAgents();

    /**
	 * returns a list of all agents situated in an apsc
	 * 
	 * @return list of agents in an agentProcessCluster
	 */
    public Collection<String> getAgents();

    /**
	 * this method should setup an agentProcessCluster, and any submodule
	 * like connection server, apr, and agents.
	 * 
	 */
    public void initialize(APCConfiguration config, String uniqueName);

    /**
	 * this method shuts down an agentProcessCluster.  
	 */
    public void shutdown();

    /**
	 * this method sends a message to agents hosted on
	 * other agentProcessCluster's.
	 * 
	 * the method does not loopback messages to
	 * local agents, as this is handled by an apr. 
	 * 
	 * @param message
	 * @param recipients
	 */
    public void sendAgentMessage(Object message, Collection<String> recipients);

    /**
	 * this method is called by an agentProcessCluster connection if an agent message
	 * was received from a remote agentProcessCluster.
	 * 
	 * @param message
	 * @param recipients
	 */
    public void receiveAgentMessage(Object message, Collection<String> recipients);

    /**
	 * event handler, called by an AgentProcessCluster Server whenever a new connection
	 * to a remote agentProcessCluster been established.
	 * 
	 * @param con
	 * @param dstAPC TODO
	 */
    public void newAPCConnection(APCConnection con, String dstAPC);

    /**
	 * returns the unique name of an agentProcessCluster.
	 *  
	 * @return unique agentProcessCluster name
	 */
    public String getName();

    /**
	 * this method utilizes the agentProcessCluster network to forward
	 * an action committed by an agent to a simulation.
	 * this is used for situations where an apr cannot
	 * provide local environment connectivity.
	 * 
	 * @param agent agent identifier committing an action
	 * @param action action, represented as a prolog-like string
	 */
    public void sendAgentAction(String agent, String action);

    /**
	 * this method utilizes the agentProcessCluster network to forward
	 * a perception for a target agent. this is used by
	 * an LSR to reach non-local agents.
	 * 
	 * @param agent target agent
	 * @param perceptions array of prolog-like perceptions
	 */
    public void sendAgentPerception(String agent, String perception);

    /**
	 * this method declares an event handler that should
	 * forward a perception to a local agent from an LSR.
	 *  
	 * @param dstAgent recipient agent of perception
	 * @param action declarative prolog-like encoded perception  
	 */
    public void receiveAgentPerception(String dstAgent, String perceptions);

    /**
	 * this method declares an event handler for actions
	 * committed on agentProcessCluster nodes running an RSR-APR. the action
	 * should be handled if the agentProcessCluster node has an LSR-APR	 
	 * 
	 * @param srcAgent agent that committed an action
	 * @param action prolog-like representation of desired action by agent srcAgent   
	 */
    public void receiveAgentAction(String srcAgent, String action);

    /**
	 * blocks a caller until an agentProcessCluster has initialized.
	 * 
	 */
    public void waitReady();

    /**
	 * this method returns the apr instance running on this agentProcessCluster.
	 *  
	 * @return apr instance or null.
	 */
    public APR getAPR();
}
