package gridrm.util;

import jade.core.AID;
import jade.util.Logger;
import jade.wrapper.AgentController;
import jade.wrapper.AgentState;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * ContainerKiller kills container when all agents terminated. ContainerKiller
 * knows what agents are running within container. When agent is about to terminate
 * it notifies ContainerKiller, which checks whether all agents it has knowledge
 * about are killed, if so it kills the container.
 *  
 * @author Mateusz Dominiak <madomini@gmail.com>
 */
public class ContainerKiller implements Runnable {

    /**
	 * JADE logger.
	 */
    private static Logger log = Logger.getMyLogger(ContainerKiller.class.getName());

    /**
	 * Singleton instance of ContainerKiller.
	 */
    private static ContainerKiller instance = new ContainerKiller();

    /**
	 * Map of AgentControllers running within container.
	 */
    private Map agents = new HashMap();

    /**
	 * List of AgentControllers that notified about termination.
	 */
    private List killedAgents = new LinkedList();

    /**
	 * ContainerController that is to be killed.
	 */
    private ContainerController conControl;

    /**
	 * Checks whether all agents are killed by investigating agents' states
	 * via their proxies.
	 * @return true/false
	 * @throws StaleProxyException
	 */
    private synchronized boolean checkAllAgentsKilled() {
        Iterator i = killedAgents.iterator();
        while (i.hasNext()) {
            AgentController agentControl = (AgentController) i.next();
            try {
                if (agentControl.getState().getCode() != AgentState.cAGENT_STATE_DELETED) {
                    return false;
                }
            } catch (StaleProxyException e) {
                log.fine(e.getMessage());
            }
        }
        return true;
    }

    /**
	 * Kill container if all agents are deleted.
	 * @return
	 * @throws StaleProxyException
	 */
    private synchronized boolean kill() throws StaleProxyException {
        if (checkAllAgentsKilled()) {
            conControl.kill();
            return true;
        }
        return false;
    }

    /**
	 * Sets container that will be checked for termination by ContainerKiller.
	 * @param cc
	 */
    private synchronized void setContainer(ContainerController cc) {
        conControl = cc;
    }

    /**
	 * Returns container monitored by ContainerKiller.
	 * @return
	 */
    private synchronized ContainerController getContainer() {
        return conControl;
    }

    /**
	 * Return singleton instance of ContainerKiller.
	 * @return
	 */
    public static ContainerKiller instance() {
        return instance;
    }

    /**
	 * Adds agent to ContainerKiller so ContainerKiller is aware about it.
	 * @param a Agent from which notification about termination is to be received
	 * before killing container.
	 * @throws StaleProxyException
	 */
    public synchronized void addAgent(AgentController a) throws StaleProxyException {
        String aName = a.getName();
        agents.put(aName, a);
    }

    public synchronized void notifyAgentTerminating(AID aid) {
        synchronized (instance) {
            String aName = aid.getName();
            if (log.isLoggable(Logger.FINE)) log.fine("Received termination notification from " + aName);
            Object agentControl = agents.remove(aName);
            if (agentControl != null) {
                killedAgents.add(agentControl);
            } else {
                log.warning("Not aware of existence of " + aName);
            }
            if (agents.size() == 0) {
                instance.notify();
            }
        }
    }

    /**
	 * Start ContainerKiller thread.
	 * @param cc Container to be killed.
	 */
    public static void start(ContainerController cc) {
        if (instance.getContainer() == null) {
            instance.setContainer(cc);
            Thread t = new Thread(instance);
            t.start();
        } else {
            log.warning("ContainerKiller thread already started!");
        }
    }

    public void run() {
        log.info("ContainerKiller thread started.");
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        log.info("All agents notified about termination. Killing container...");
        while (true) {
            try {
                if (kill()) {
                    log.info("Container killed.");
                    break;
                }
            } catch (StaleProxyException e) {
                log.severe("Could not kill container: " + e.getMessage());
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
}
