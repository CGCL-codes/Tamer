package jade.core;

import jade.imtp.leap.JICP.JICPProtocol;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Iterator;
import jade.util.leap.Properties;
import jade.util.Logger;
import jade.security.JADESecurityException;
import jade.core.behaviours.Behaviour;
import jade.core.event.ContainerEvent;
import jade.core.event.JADEEvent;
import jade.security.*;
import jade.util.ObjectManager;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;

/**
 @author Giovanni Caire - TILAB
 @author Jerome Picault - Motorola Labs
 */
class FrontEndContainer implements FrontEnd, AgentToolkit, Runnable {

    Logger logger = Logger.getMyLogger(this.getClass().getName());

    private Hashtable localAgents = new Hashtable(1);

    private Hashtable localServices = new Hashtable(1);

    private Vector feListeners = new Vector();

    private ContainerID myId;

    Vector platformAddresses;

    private AID amsAID;

    private AID dfAID;

    private Vector pending;

    private Vector senderAgents;

    private BackEndWrapper myBackEnd;

    private Properties configProperties;

    private boolean exiting = false;

    private boolean starting = true;

    /**
	 * Start this FrontEndContainer creating agents and services and connecting to the BackEnd.
	 */
    void start(Properties p) {
        configProperties = p;
        String agents = configProperties.getProperty(MicroRuntime.AGENTS_KEY);
        try {
            Vector specs = Specifier.parseSpecifierList(agents);
            Vector successfulAgents = new Vector();
            for (Enumeration en = specs.elements(); en.hasMoreElements(); ) {
                Specifier s = (Specifier) en.nextElement();
                try {
                    localAgents.put(s.getName(), initAgentInstance(s.getName(), s.getClassName(), s.getArgs()));
                    successfulAgents.addElement(s);
                } catch (Throwable t) {
                    logger.log(Logger.SEVERE, "Exception creating agent " + t);
                }
            }
            configProperties.setProperty(MicroRuntime.AGENTS_KEY, Specifier.encodeSpecifierList(successfulAgents));
        } catch (Exception e1) {
            configProperties.setProperty(MicroRuntime.AGENTS_KEY, null);
            logger.log(Logger.SEVERE, "Exception parsing agent specifiers " + e1);
            e1.printStackTrace();
        }
        String feServices = configProperties.getProperty(MicroRuntime.SERVICES_KEY);
        String beServices = null;
        Vector svcClasses = Specifier.parseList(feServices, ';');
        for (Enumeration en = svcClasses.elements(); en.hasMoreElements(); ) {
            String serviceClassName = (String) en.nextElement();
            try {
                FEService svc = (FEService) Class.forName(serviceClassName).newInstance();
                localServices.put(svc.getName(), svc);
                beServices = (beServices != null ? beServices + ';' + svc.getBEServiceClassName() : svc.getBEServiceClassName());
            } catch (Throwable t) {
                logger.log(Logger.SEVERE, "Exception creating service " + t);
            }
        }
        if (beServices != null) {
            configProperties.setProperty(MicroRuntime.BE_REQUIRED_SERVICES_KEY, beServices);
        }
        manageProtoOption(configProperties);
        try {
            myBackEnd = new BackEndWrapper(this, configProperties);
            logger.log(Logger.INFO, "--------------------------------------\nAgent container " + myId.getName() + " is ready.\n--------------------------------------------");
        } catch (IMTPException imtpe) {
            logger.log(Logger.SEVERE, "IMTP error " + imtpe);
            imtpe.printStackTrace();
            MicroRuntime.handleTermination(true);
            return;
        } catch (Exception e) {
            logger.log(Logger.SEVERE, "Unexpected error " + e);
            e.printStackTrace();
            MicroRuntime.handleTermination(true);
            return;
        }
        for (Enumeration en = localServices.elements(); en.hasMoreElements(); ) {
            FEService svc = (FEService) en.nextElement();
            svc.init(myBackEnd);
        }
        agents = configProperties.getProperty(MicroRuntime.AGENTS_KEY);
        try {
            Vector specs = Specifier.parseSpecifierList(agents);
            Enumeration e = specs.elements();
            while (e.hasMoreElements()) {
                Specifier sp = (Specifier) e.nextElement();
                Agent a = (Agent) localAgents.remove(sp.getName());
                if (a != null) {
                    Object[] args = sp.getArgs();
                    if ((args != null) && args.length > 0) {
                        logger.log(Logger.SEVERE, "Error starting agent " + sp.getName() + ". " + sp.getClassName() + " " + args[0]);
                    } else {
                        String actualName = sp.getClassName();
                        activateAgent(actualName, a);
                    }
                } else {
                    logger.log(Logger.WARNING, "Agent " + sp.getName() + " not found locally.");
                }
            }
        } catch (Exception e1) {
            logger.log(Logger.SEVERE, "Exception parsing agent specifiers " + e1);
            e1.printStackTrace();
        }
        configProperties.remove(MicroRuntime.AGENTS_KEY);
        notifyStarted();
    }

    private void manageProtoOption(Properties pp) {
        String proto = pp.getProperty(MicroRuntime.PROTO_KEY);
        if (proto != null) {
            if (CaseInsensitiveString.equalsIgnoreCase(MicroRuntime.SOCKET_PROTOCOL, proto)) {
                pp.setProperty(MicroRuntime.CONN_MGR_CLASS_KEY, "jade.imtp.leap.JICP.FrontEndDispatcher");
            } else if (CaseInsensitiveString.equalsIgnoreCase(MicroRuntime.SSL_PROTOCOL, proto)) {
                pp.setProperty(MicroRuntime.CONN_MGR_CLASS_KEY, "jade.imtp.leap.JICP.FrontEndSDispatcher");
            } else if (CaseInsensitiveString.equalsIgnoreCase(MicroRuntime.HTTP_PROTOCOL, proto)) {
                pp.setProperty(MicroRuntime.CONN_MGR_CLASS_KEY, "jade.imtp.leap.http.HTTPFEDispatcher");
                pp.setProperty(JICPProtocol.MEDIATOR_CLASS_KEY, "jade.imtp.leap.nio.NIOHTTPBEDispatcher");
            } else if (CaseInsensitiveString.equalsIgnoreCase(MicroRuntime.HTTPS_PROTOCOL, proto)) {
                pp.setProperty(MicroRuntime.CONN_MGR_CLASS_KEY, "jade.imtp.leap.http.HTTPFESDispatcher");
                pp.setProperty(JICPProtocol.MEDIATOR_CLASS_KEY, "jade.imtp.leap.nio.NIOHTTPBEDispatcher");
            }
        }
    }

    void detach() {
        myBackEnd.detach();
    }

    void addListener(FEListener l) {
        if (!feListeners.contains(l)) {
            feListeners.add(l);
        }
    }

    void removeListener(FEListener l) {
        feListeners.remove(l);
    }

    private void notifyListeners(JADEEvent ev) {
        synchronized (feListeners) {
            for (int i = 0; i < feListeners.size(); ++i) {
                FEListener l = (FEListener) feListeners.get(i);
                l.handleEvent(ev);
            }
        }
    }

    /**
	 * Request the FrontEnd to return a local agent reference by his local name
	 */
    final Agent getLocalAgent(String localName) {
        return (Agent) localAgents.get(localName);
    }

    /**
	 Request the FrontEnd container to create a new agent.
	 @param name The name of the new agent.
	 @param className The class of the new agent.
	 @param args The arguments to be passed to the new agent.
	 */
    public final void createAgent(String name, String className, String[] args) throws IMTPException {
        try {
            Agent a = initAgentInstance(name, className, (Object[]) args);
            String newName = myBackEnd.bornAgent(name);
            activateAgent(newName, a);
        } catch (Exception e) {
            String msg = "Exception creating new agent. ";
            logger.log(Logger.SEVERE, msg + e);
            throw new IMTPException(msg, e);
        }
    }

    /**
	 Request the FrontEnd container to create a new agent.
	 @param name The name of the new agent.
	 @param className The class of the new agent.
	 @param args The arguments to be passed to the new agent.
	 */
    public final void createAgent(String name, String className, Object[] args) throws IMTPException {
        try {
            Agent a = initAgentInstance(name, className, args);
            String newName = myBackEnd.bornAgent(name);
            activateAgent(newName, a);
        } catch (Exception e) {
            String msg = "Exception creating new agent. ";
            logger.log(Logger.SEVERE, msg + e);
            throw new IMTPException(msg, e);
        }
    }

    /**
	 Request the FrontEnd container to kill an agent.
	 @param name The name of the agent to kill.
	 */
    public final void killAgent(String name) throws NotFoundException, IMTPException {
        waitUntilStarted();
        Agent agent = (Agent) localAgents.get(name);
        if (agent == null) {
            System.out.println("FrontEndContainer killing: " + name + " NOT FOUND");
            throw new NotFoundException("KillAgent failed to find " + name);
        }
        agent.doDelete();
    }

    /**
	 Request the FrontEnd container to suspend an agent.
	 @param name The name of the agent to suspend.
	 */
    public final void suspendAgent(String name) throws NotFoundException, IMTPException {
        waitUntilStarted();
        Agent agent = (Agent) localAgents.get(name);
        if (agent == null) {
            throw new NotFoundException("SuspendAgent failed to find " + name);
        }
        agent.doSuspend();
    }

    /**
	 Request the FrontEnd container to resume an agent.
	 @param name The name of the agent to resume.
	 */
    public final void resumeAgent(String name) throws NotFoundException, IMTPException {
        waitUntilStarted();
        Agent agent = (Agent) localAgents.get(name);
        if (agent == null) {
            throw new NotFoundException("ResumeAgent failed to find " + name);
        }
        agent.doActivate();
    }

    /**
	 Pass an ACLMessage to the FrontEnd for posting.
	 @param msg The message to be posted.
	 @param sender The name of the receiver agent.
	 */
    public final void messageIn(ACLMessage msg, String receiver) throws NotFoundException, IMTPException {
        waitUntilStarted();
        if (receiver != null) {
            Agent agent = (Agent) localAgents.get(receiver);
            if (agent == null) {
                throw new NotFoundException("Receiver " + receiver + " not found");
            }
            agent.postMessage(msg);
        }
    }

    /**
	 Request the FrontEnd container to exit.
	 */
    public final void exit(boolean self) throws IMTPException {
        if (!exiting) {
            exiting = true;
            logger.log(Logger.INFO, "Container shut down activated");
            Vector toBeKilled = new Vector();
            synchronized (localAgents) {
                Enumeration e = localAgents.elements();
                while (e.hasMoreElements()) {
                    toBeKilled.addElement(e.nextElement());
                }
            }
            Enumeration e = toBeKilled.elements();
            while (e.hasMoreElements()) {
                Agent a = (Agent) e.nextElement();
                a.doDelete();
                a.join();
                a.resetToolkit();
            }
            localAgents.clear();
            logger.log(Logger.FINE, "Local agents terminated");
            myBackEnd.detach();
            logger.log(Logger.FINE, "Connection manager closed");
            MicroRuntime.handleTermination(self);
            TimerDispatcher.getTimerDispatcher().stop();
        }
    }

    /**
	 Request the FrontEnd container to synch.
	 */
    public final void synch() throws IMTPException {
        synchronized (localAgents) {
            Enumeration e = localAgents.keys();
            while (e.hasMoreElements()) {
                String name = (String) e.nextElement();
                logger.log(Logger.INFO, "Resynching agent " + name);
                try {
                    myBackEnd.bornAgent(name);
                } catch (IMTPException imtpe) {
                    logger.log(Logger.WARNING, "IMTPException resynching. " + imtpe);
                    throw imtpe;
                } catch (Exception ex) {
                    logger.log(Logger.SEVERE, "Exception resynching agent " + name + ". " + ex);
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
	 * It may happen that a message for a bootstrap agent using wildcards in its name is received before 
	 * the actual agent name is assigned --> This method is called in messageIn() and other agent-related 
	 * methods to avoid that. 
	 */
    private synchronized void waitUntilStarted() {
        try {
            while (starting) {
                wait();
            }
        } catch (Exception e) {
        }
    }

    private synchronized void notifyStarted() {
        starting = false;
        notifyAll();
    }

    public jade.wrapper.AgentContainer getContainerController(JADEPrincipal principal, Credentials credentials) {
        return null;
    }

    public final Location here() {
        return myId;
    }

    public final void handleEnd(AID agentID) {
        String name = agentID.getLocalName();
        if (pending != null) {
            synchronized (pending) {
                while (senderAgents.contains(name)) {
                    try {
                        pending.wait();
                    } catch (Exception e) {
                    }
                }
            }
        }
        notifyListeners(new ContainerEvent(ContainerEvent.DEAD_AGENT, agentID, myId));
        if (!exiting) {
            try {
                localAgents.remove(name);
                myBackEnd.deadAgent(name);
                if ("true".equals(configProperties.getProperty("exitwhenempty"))) {
                    if (localAgents.isEmpty()) {
                        exit(true);
                    }
                }
            } catch (IMTPException re) {
                logger.log(Logger.SEVERE, re.toString());
            }
        }
    }

    public final void handleChangedAgentState(AID agentID, int from, int to) {
    }

    public final void handleSend(ACLMessage msg, AID sender, boolean needClone) {
        Iterator it = msg.getAllIntendedReceiver();
        boolean hasRemoteReceivers = false;
        while (it.hasNext()) {
            AID id = (AID) it.next();
            Agent a = (Agent) localAgents.get(id.getLocalName());
            if (a != null) {
                ACLMessage m = (ACLMessage) msg.clone();
                a.postMessage(m);
            } else {
                hasRemoteReceivers = true;
            }
        }
        if (hasRemoteReceivers) {
            post(msg, sender.getLocalName());
        }
    }

    public final void setPlatformAddresses(AID id) {
        id.clearAllAddresses();
        for (int i = 0; i < platformAddresses.size(); ++i) {
            id.addAddresses((String) platformAddresses.elementAt(i));
        }
    }

    public final AID getAMS() {
        return amsAID;
    }

    public final AID getDefaultDF() {
        return dfAID;
    }

    public String getProperty(String key, String aDefault) {
        String ret = configProperties.getProperty(key);
        return (ret != null ? ret : aDefault);
    }

    public Properties getBootProperties() {
        return null;
    }

    public void handleMove(AID agentID, Location where) throws JADESecurityException, IMTPException, NotFoundException {
    }

    public void handleClone(AID agentID, Location where, String newName) throws JADESecurityException, IMTPException, NotFoundException {
    }

    public void handleSave(AID agentID, String repository) throws ServiceException, NotFoundException, IMTPException {
    }

    public void handleReload(AID agentID, String repository) throws ServiceException, NotFoundException, IMTPException {
    }

    public void handleFreeze(AID agentID, String repository, ContainerID bufferContainer) throws ServiceException, NotFoundException, IMTPException {
    }

    public void handlePosted(AID agentID, ACLMessage msg) {
    }

    public void handleReceived(AID agentID, ACLMessage msg) {
    }

    public void handleBehaviourAdded(AID agentID, Behaviour b) {
    }

    public void handleBehaviourRemoved(AID agentID, Behaviour b) {
    }

    public void handleChangeBehaviourState(AID agentID, Behaviour b, String from, String to) {
    }

    public ServiceHelper getHelper(Agent a, String serviceName) throws ServiceException {
        FEService svc = (FEService) localServices.get(serviceName);
        if (svc != null) {
            return svc.getHelper(a);
        } else {
            throw new ServiceNotActiveException(serviceName);
        }
    }

    final void initInfo(Properties pp) {
        myId = new ContainerID(pp.getProperty(MicroRuntime.CONTAINER_NAME_KEY), null);
        AID.setPlatformID(pp.getProperty(MicroRuntime.PLATFORM_KEY));
        platformAddresses = Specifier.parseList(pp.getProperty(MicroRuntime.PLATFORM_ADDRESSES_KEY), ';');
        amsAID = new AID("ams", AID.ISLOCALNAME);
        setPlatformAddresses(amsAID);
        dfAID = new AID("df", AID.ISLOCALNAME);
        setPlatformAddresses(dfAID);
    }

    private final Agent initAgentInstance(String name, String className, Object[] args) throws Exception {
        Agent agent = null;
        agent = (Agent) ObjectManager.load(className, ObjectManager.AGENT_TYPE);
        if (agent == null) {
            agent = (Agent) Class.forName(className).newInstance();
        }
        agent.setArguments(args);
        agent.setToolkit(this);
        agent.initMessageQueue();
        return agent;
    }

    private void activateAgent(String name, Agent a) {
        localAgents.put(name, a);
        AID id = new AID(name, AID.ISLOCALNAME);
        notifyListeners(new ContainerEvent(ContainerEvent.BORN_AGENT, id, myId));
        a.powerUp(id, new Thread(a));
    }

    private void post(ACLMessage msg, String sender) {
        if (pending == null) {
            pending = new Vector(4);
            senderAgents = new Vector(1);
            Thread t = new Thread(this);
            t.start();
        }
        synchronized (pending) {
            if (!senderAgents.contains(sender)) {
                senderAgents.addElement(sender);
            }
            pending.addElement(msg.clone());
            pending.addElement(sender);
            int size = pending.size();
            if (size > 100 && size < 110) {
                logger.log(Logger.INFO, size + " pending messages");
            }
            pending.notifyAll();
        }
    }

    public void run() {
        ACLMessage msg = null;
        String sender = null;
        while (true) {
            synchronized (pending) {
                while (pending.size() == 0) {
                    try {
                        pending.wait();
                    } catch (InterruptedException ie) {
                        logger.log(Logger.SEVERE, ie.toString());
                    }
                }
                msg = (ACLMessage) pending.elementAt(0);
                sender = (String) pending.elementAt(1);
                pending.removeElementAt(1);
                pending.removeElementAt(0);
            }
            try {
                myBackEnd.messageOut(msg, sender);
            } catch (Exception e) {
                logger.log(Logger.SEVERE, e.toString());
            }
            synchronized (pending) {
                if (!pending.contains(sender)) {
                    senderAgents.removeElement(sender);
                    pending.notifyAll();
                }
            }
        }
    }
}
