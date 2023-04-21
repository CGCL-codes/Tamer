package com.agentfactory.service.ams;

import com.agentfactory.platform.core.AgentCreationException;
import com.agentfactory.platform.core.DuplicateAgentNameException;
import com.agentfactory.platform.core.IAgent;
import com.agentfactory.platform.core.IAgentPlatform;
import com.agentfactory.platform.core.NoSuchAgentException;
import com.agentfactory.platform.core.NoSuchArchitectureException;
import com.agentfactory.platform.impl.AbstractPlatformService;
import java.util.ArrayList;
import java.util.List;

/**
 * Agent Management Service
 *
 * @author Rem Collier
 * @author daithi
 */
public class AgentManagementService extends AbstractPlatformService {

    public static final String NAME = "fipa.std.service.ams";

    public static final String DESCRIPTION = "fipa.std.service.ams com.agentfactory.service.ams.AgentManagementService";

    public AgentManagementService() {
        setName(NAME);
    }

    public synchronized List<String> getAgentNames() {
        List<String> list = new ArrayList<String>();
        IAgentPlatform platform = manager.getAgentPlatform();
        for (IAgent agent : platform.getAgentContainer().agents()) {
            list.add(agent.getName());
        }
        return list;
    }

    public synchronized boolean resumeAgent(String name) {
        manager.getAgentPlatform().getScheduler().resume(name);
        return true;
    }

    public synchronized boolean suspendAgent(String name) {
        manager.getAgentPlatform().getScheduler().suspend(name);
        return true;
    }

    public synchronized boolean terminateAgent(String name) {
        try {
            manager.getAgentPlatform().getAgentContainer().getAgentByName(name).setState(IAgent.TERMINATED);
        } catch (NoSuchAgentException e) {
            return false;
        }
        return true;
    }

    /**
     * Create an agent based on an agent description file.
     *
     * Currently, agents can be created from .afapl code files. .agt is no
     * longer directly supported.
     *
     * @param inName -
     *            A String Object. The name of the agent that is to be
     *            constructed.sout
     * @param inType -
     *            A String Object, the name of the code file that is to be used
     *            to construct the agent.
     * @throws NoSuchArchitectureException -
     *             thrown if there is a problem constructing an agent.
     * @throws AgentCreationException 
     */
    public synchronized IAgent createAgent(String name, Object design) throws NoSuchArchitectureException, DuplicateAgentNameException, AgentCreationException {
        IAgentPlatform platform = manager.getAgentPlatform();
        IAgent agent = platform.getArchitectureService().createAgent(name, design);
        platform.getAgentContainer().add(agent);
        platform.getScheduler().register(agent);
        return agent;
    }
}
