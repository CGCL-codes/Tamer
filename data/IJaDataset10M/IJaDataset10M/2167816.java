package com.agentfactory.infrastructure.fipa;

import com.agentfactory.rma.ReactiveMessageAgent;
import com.agentfactory.platform.AgentPlatform;
import com.agentfactory.platform.core.Agent;
import com.agentfactory.platform.core.AgentContainerEvent;
import com.agentfactory.platform.mts.Message;
import com.agentfactory.service.ams.AgentManagementService;
import com.agentfactory.platform.util.Logger;
import com.agentfactory.rma.Event;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;

/**
 * Implementation of the AMS agent for the robust FIPA-based infrastructure.
 * 
 * This agent is implemented using the ReactiveMessageAgent architecture.
 * @author remcollier
 */
public class AMSAgent extends ReactiveMessageAgent {

    private static final int CLASS_LOG_LEVEL = Logger.LEVEL_WARNING;

    public AMSAgent(String name, AgentPlatform platform) {
        super(name, platform);
        memory.put("lookups", new HashMap());
        memory.put("cache", new HashMap());
        memory.put("register", new HashMap());
        memory.put("amsRegister", new HashMap());
        agentPlatform.getAgentContainer().addObserver(this);
        bindToService(AgentManagementService.NAME);
        addMessageHandler(new CreateHandler(this, Message.REQUEST));
        addMessageHandler(new TerminateHandler(this, Message.REQUEST));
        addMessageHandler(new SuspendHandler(this, Message.REQUEST));
        addMessageHandler(new ResumeHandler(this, Message.REQUEST));
        addMessageHandler(new LookupHandler(this, Message.REQUEST));
        addMessageHandler(new PlatformDescriptionHandler(this, Message.REQUEST));
        addMessageHandler(new AMSRegistrationHandler(this, Message.REQUEST));
        addEventHandler(new RegistrationEventHandler(this));
        addMessageHandler(new AMSLookupHandler(this, Message.INFORM));
    }

    public void metaRegistration() {
        super.metaRegistration();
        initializeRegister();
    }

    /**
     * This method overrides the default initialize(...) method of the
     * ReactiveMessaging Agent architecture.  Specifically, it checks
     * each INITALISE statement to see if the key part is the string
     * "authority".  If this is the case, then the agent attempts to
     * register with the specified authority.
     * 
     * @param data the initialisation data
     */
    public void initialise(String data) {
        super.initialise(data);
        String[] vals = data.split("=");
        if (vals[0].equals("authority")) {
            Logger.detail("Authority: " + vals[1], CLASS_LOG_LEVEL);
            eventQueue.add(new Event("authorityRegistration"));
        }
    }

    /**
     * Implementation of the Observer Pattern.  This class monitors the
     * Agent Container class, watching for events that effect a change in
     * the state of the register.
     * 
     * @param source the source from which the event arose
     * @param event the event
     */
    public void update(Observable source, Object event) {
        if (event instanceof AgentContainerEvent) {
            AgentContainerEvent acEvent = (AgentContainerEvent) event;
            if (acEvent.getType().equals(AgentContainerEvent.CREATE)) {
                Agent agt = (Agent) acEvent.getData();
                Map register = (Map) memory.get("register");
                register.put(agt.getName(), agt.getAgentID());
                if (!agt.getAgentID().equals(getAgentID())) {
                    agt.addAgentID(getAgentID());
                }
            } else if (acEvent.getType().equals(AgentContainerEvent.TERMINATE)) {
                Agent agt = (Agent) acEvent.getData();
                Map register = (Map) memory.get("register");
                register.remove(agt.getName());
            }
        }
    }

    /**
     * Initializes the register to contain all agents currently exist on the
     * platform
     * 
     * @param register reference to the register
     */
    public void initializeRegister() {
        Map register = (Map) memory.get("register");
        Agent agt = null;
        Iterator it = agentPlatform.getAgentContainer().getAgents().iterator();
        while (it.hasNext()) {
            agt = (Agent) it.next();
            register.put(agt.getName(), agt.getAgentID());
            if (!agt.getAgentID().equals(getAgentID())) {
                agt.addAgentID(getAgentID());
            }
        }
    }
}
