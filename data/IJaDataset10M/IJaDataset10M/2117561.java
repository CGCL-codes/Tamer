package org.slasoi.gslam.pac;

import org.slasoi.gslam.pac.config.AgentConfiguration;

/**
 * Interface for an agent. Contains methods to configure, start and stop an agent.
 * 
 * @author Beatriz Fuentes
 * 
 */
public interface IAgent {

    /**
     * Ask the agent to configure itself.
     */
    public void configure(AgentConfiguration config);

    /**
     * start the agent
     */
    public void start();

    /**
     * stop the agent
     */
    public void stop();
}
