package org.jul.warp.impl;

import static org.jul.warp.impl.Debug.DEBUG;
import static org.jul.warp.impl.Debug.logger;
import java.util.*;
import java.util.concurrent.*;
import org.jul.dsm.RepresentationException;
import org.jul.warp.*;
import org.jul.warp.messaging.*;

public class NodeImpl extends MemoryManager implements Node, ThreadFactory, Runnable {

    private class RunnableAgent implements Runnable {

        private final Agent agent;

        private final Runnable runnable;

        public void run() {
            runnable.run();
        }

        private RunnableAgent(Agent agent, Runnable runnable) {
            this.agent = agent;
            this.runnable = runnable;
        }
    }

    private final Environment environment;

    private final AgentFactory agentFactory;

    private final MessageFactory messageFactory;

    private final NodeId id;

    private final ExecutorService executorService;

    private final Thread messageHandler;

    private final AgentResolver agentResolver;

    private final NodeMessageHandler nodeMessageHandler;

    private final Set<Agent> localAgents = new HashSet<Agent>();

    private final Map<Agent, Long> lastPatchTime = new HashMap<Agent, Long>();

    private final Set<Agent> handledAgents = new HashSet<Agent>();

    private final BlockingQueue<AgentMessageWrapper> agentMessageQueue = new LinkedBlockingQueue<AgentMessageWrapper>();

    private final Object agentsLock = new Object();

    private final Set<Agent> executedAgents = Collections.synchronizedSet(new HashSet<Agent>());

    private volatile boolean execute = true;

    private void handleUnexpectedException(Exception e) {
        System.err.println("FATAL ERROR: " + this);
        e.printStackTrace();
        System.exit(-1);
    }

    private void initializeAgent(Agent agent, ExecutableWrapper executableWrapper) {
        agent.setExecutableWrapper(executableWrapper);
        if (!localAgents.add(agent)) {
            throw new WarpException(agent + " has already been initialized!");
        }
        agent.initialize(this);
    }

    private boolean isHandledAgent(Agent agent) {
        return handledAgents.contains(agent);
    }

    public void run() {
        try {
            while (execute) {
                AgentMessageWrapper message = agentMessageQueue.take();
                if (message.getSender() == null) {
                    throw new WarpException("Message \"" + message + "\" has no sender node!");
                } else if (id.equals(message.getSender())) {
                    if (message.isGlobalMessage()) {
                        synchronized (agentsLock) {
                            nodeMessageHandler.sendMessage(message);
                            messageFactory.send(message.getMessage(), handledAgents);
                        }
                    } else {
                        for (Iterator<Agent> iterator = message.getReceivers().iterator(); iterator.hasNext(); ) {
                            Agent agent = iterator.next();
                            if (isHandledAgent(agent)) {
                                messageFactory.send(message.getMessage(), agent);
                                iterator.remove();
                            }
                        }
                        if (!message.getReceivers().isEmpty()) {
                            Set<NodeId> receiverNodes = new HashSet<NodeId>();
                            for (Agent agent : message.getReceivers()) {
                                receiverNodes.add(agentResolver.resolve(agent));
                            }
                            nodeMessageHandler.sendMessage(message, receiverNodes);
                        }
                    }
                } else {
                    if (message.isGlobalMessage()) {
                        synchronized (agentsLock) {
                            messageFactory.send(message.getMessage(), handledAgents);
                        }
                    } else {
                        for (Iterator<Agent> iterator = message.getReceivers().iterator(); iterator.hasNext(); ) {
                            Agent agent = iterator.next();
                            if (!isHandledAgent(agent)) {
                                iterator.remove();
                            }
                        }
                        messageFactory.send(message.getMessage(), message.getReceivers());
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(AgentMessage message) {
        agentMessageQueue.add(new AgentMessageWrapper(environment, id, message));
    }

    public void sendMessage(AgentMessage message, Agent receiver) {
        agentMessageQueue.add(new AgentMessageWrapper(environment, id, receiver, message));
    }

    public void sendMessage(AgentMessage message, Set<Agent> receivers) {
        agentMessageQueue.add(new AgentMessageWrapper(environment, id, new HashSet<Agent>(receivers), message));
    }

    public void deferMessage(AgentMessage message, Agent agent) {
        sendMessage(message, agent);
    }

    public Agent addAgent(Executable executable, AgentType type) {
        Agent agent = agentFactory.createAgent(type);
        ExecutableWrapper executableWrapper = new ExecutableWrapperImpl(executable);
        if (DEBUG) {
            logger.info(this + " Created: " + agent + " (" + executable.getClass() + ", type = " + type + ")");
        }
        switch(type) {
            case NORMAL:
                {
                    try {
                        registerReferencedAgent(agent, 0);
                        agent.setExecutableWrapper(executableWrapper);
                        createPatch(agent);
                        IdLease lease = getLease(agent);
                        HandlingRequest request = new HandlingRequest(environment, environment.getIdFactory().getNullNodeId(), agent, 0, lease.getExternalLeaseStart() + lease.getCurrentId(), createClone(agent));
                        nodeMessageHandler.sendMessage(request);
                    } catch (RepresentationException e) {
                        handleUnexpectedException(e);
                    }
                }
                break;
            case FIXED:
                {
                    try {
                        synchronized (agentsLock) {
                            handledAgents.add(agent);
                            agentResolver.agentAdded(this, agent);
                            registerReferencedAgent(agent, 0);
                            agent.setExecutableWrapper(executableWrapper);
                            createPatch(agent);
                            initializeAgent(agent, executableWrapper);
                        }
                    } catch (RepresentationException e) {
                        handleUnexpectedException(e);
                    }
                }
                break;
            case OBSERVER:
                {
                    synchronized (agentsLock) {
                        handledAgents.add(agent);
                        agentResolver.agentAdded(this, agent);
                        initializeAgent(agent, executableWrapper);
                    }
                }
                break;
        }
        return agent;
    }

    public ExecutionCompleted updateLocal(Agent agent, long time) {
        if (agent.getType() == AgentType.OBSERVER) {
            return new ExecutionCompleted(environment, agent, time, null, null);
        } else {
            try {
                if (DEBUG) {
                    logger.finest(this + " Updating local " + agent + "@" + time);
                }
                return new ExecutionCompleted(environment, agent, time, createClone(agent), createPatch(agent));
            } catch (RepresentationException e) {
                handleUnexpectedException(e);
                return null;
            }
        }
    }

    public void process(AgentMessageWrapper message) {
        agentMessageQueue.add(message);
    }

    public void process(HandlingRequest message) {
        try {
            if (DEBUG) {
                logger.info(this + " " + message);
            }
            synchronized (agentsLock) {
                Agent agent = message.getAgent();
                handledAgents.add(agent);
                agentResolver.agentAdded(this, agent);
                registerReferencedAgent(agent, message.getExternalId());
                initializeAgent(agent, (ExecutableWrapper) applyPatch(message.getClone()));
            }
        } catch (RepresentationException e) {
            handleUnexpectedException(e);
        }
    }

    public boolean updateRemote(ExecutionCompleted message) {
        synchronized (lastPatchTime) {
            Long lastTime = lastPatchTime.get(message.getSender());
            if (lastTime != null && lastTime <= message.getTime()) {
                return false;
            }
        }
        if (isHandledAgent(message.getSender())) {
            if (DEBUG) {
                logger.finest(this + " Ignoring update of handled " + message.getSender() + "@" + message.getTime());
            }
        } else {
            if (DEBUG) {
                logger.finest(this + " Updating remote " + message.getSender() + "@" + message.getTime());
            }
            try {
                if (message.getClone() != null) {
                    message.getSender().setExecutableWrapper((ExecutableWrapper) applyPatch(message.getClone()));
                }
                applyPatch(message.getPatch());
            } catch (RepresentationException e) {
                handleUnexpectedException(e);
            }
        }
        return true;
    }

    public void close() {
        if (DEBUG) {
            logger.warning(this + " Closing node...");
        }
        execute = false;
        executorService.shutdown();
        messageHandler.interrupt();
    }

    public Thread newThread(Runnable r) {
        return new Thread(r, this + " Executor");
    }

    public boolean isAgentAvailable(Agent agent) {
        return localAgents.contains(agent);
    }

    public void execute(Agent agent, Runnable runnable) {
        executorService.execute(new RunnableAgent(agent, runnable));
    }

    public void call(Agent agent, Runnable runnable) {
        executorService.execute(runnable);
    }

    public NodeId getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Node  [" + id + "]";
    }

    public NodeImpl(Environment environment, NodeId id, NodeMessageHandler nodeMessageHandler) {
        super(environment);
        this.environment = environment;
        this.agentFactory = environment.getAgentFactory();
        this.messageFactory = environment.getMessageFactory();
        this.id = id;
        this.executorService = Executors.newCachedThreadPool(this);
        this.messageHandler = new Thread(this, this + " Message handler");
        this.nodeMessageHandler = nodeMessageHandler;
        this.agentResolver = environment.getAgentResolver();
        if (DEBUG) {
            logger.info(this + " Starting node...");
        }
        messageHandler.start();
    }
}
