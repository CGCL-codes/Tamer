package DE.FhG.IGD.semoa.compat.jade;

import DE.FhG.IGD.semoa.service.*;
import DE.FhG.IGD.semoa.server.*;
import DE.FhG.IGD.semoa.agent.*;
import DE.FhG.IGD.util.*;
import jade.security.*;
import jade.lang.acl.*;
import jade.core.*;
import java.util.*;
import java.security.*;
import java.lang.reflect.*;
import java.net.InetAddress;

/**
 * This <code>Lifecycle</code> is applied to objects that extend 
 * JADE's <code>Agent</code> class. 
 *
 * @author  Ulrich Pinsdorf
 * @version "$Id: JadeLifecycle.java 573 2002-03-27 10:17:09Z upinsdor $"
 */
public class JadeLifecycle extends AbstractLifecycle implements AgentToolkit {

    /**
     * The reference to the agent's <code>AgentContext</code>.
     */
    private AgentContext context_;

    /**
     * The reference to the agent itself.
     */
    private Agent agent_;

    private AID dfID_;

    private AID amsID_;

    private AID agentID_;

    private String nickname_;

    private MessageRouter router_;

    /**
     * Creates an instance with the given <code>AgentContext
     * </code> and <code>Runnable</code>. The latter is the
     * actual agent implementation.
     *
     * @param context The <code>AgentContext</code> of the
     *   given <code>agent</code>.
     */
    public JadeLifecycle(AgentContext context) {
        super(context);
    }

    /**
     * Runs the lifecycle, and hence the agent. This method
     * completes when the agent's <code>run()</code> method
     * completes.
     */
    public void start() {
        ResourceManager resourceMgr;
        AgentContainer container;
        ThreadGroup threadGroup;
        AgentContext ctx;
        String hostname;
        String nickname;
        AgentCard card;
        String key;
        ctx = getAgentContext();
        try {
            agent_ = (Agent) unmarshall();
            key = WhatIs.stringValue("JADE_MESSAGE_ROUTER");
            router_ = (MessageRouter) Environment.getEnvironment().lookup(key);
            if (router_ == null) {
                System.out.println("Message Router not found at " + key + ".");
                return;
            }
            card = (AgentCard) ctx.get(FieldType.CARD);
            dfID_ = new AID("df", false);
            amsID_ = new AID("ams", false);
            agentID_ = new AID(card.toString(), false);
            threadGroup = (ThreadGroup) ctx.get(FieldType.THREADGROUP);
            resourceMgr = new SemoaResourceManager(threadGroup);
            agent_.initReservedAIDs(amsID_, dfID_);
            agent_.setToolkit((AgentToolkit) this);
            agent_.powerUp(agentID_, resourceMgr);
            nickname = card.getNickname();
            if (agent_ instanceof jade.domain.df) {
                router_.registerDF(agentID_);
            } else if (agent_ instanceof jade.domain.ams) {
                router_.registerAMS(agentID_);
            } else if (nickname != null) {
                router_.registerNickname(agentID_, nickname);
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();
            return;
        }
    }

    /**
     * Delivers a message to excactly one receiver. This methods
     * checks if the receiver ID is correct and then reaches the
     * message to the agent.
     */
    public void unicastPostMessage(ACLMessage msg, AID receiverID) {
        System.out.println("\nDelivering incoming message:" + "\n----------------------------" + "\nReceiver: " + receiverID.getName() + "\nMessage :\n" + msg);
        agent_.postMessage(msg);
    }

    public Location here() {
        return router_.getPlatformID();
    }

    public void handleStart(String localName, Agent instance) {
        System.out.println("AgentToolkit.handleStart() called.");
        throw new UnsupportedOperationException();
    }

    public void handleEnd(AID agentID) {
        agent_.doDelete();
        stop();
        throw new UnsupportedOperationException();
    }

    public void handleMove(AID agentID, Location where) {
        System.out.println("AgentToolkit.handleMove() called.");
        throw new UnsupportedOperationException();
    }

    public void handleClone(AID agentID, Location where, String newName) {
        System.out.println("AgentToolkit.handleClone() called.");
        throw new UnsupportedOperationException();
    }

    public void handleSend(ACLMessage msg) throws AuthException {
        ACLMessage replyMsg;
        Iterator i;
        AID recv;
        i = msg.getAllReceiver();
        while (i.hasNext()) {
            recv = (AID) i.next();
            if (recv.getName().startsWith("ams@")) {
                replyMsg = new ACLMessage(ACLMessage.INFORM);
                replyMsg.setSender(amsID_);
                replyMsg.addReceiver(msg.getSender());
                replyMsg.setContent("FIXME");
                replyMsg.setLanguage(msg.getLanguage());
                replyMsg.setOntology(msg.getOntology());
                replyMsg.setConversationId(msg.getConversationId());
                replyMsg.setInReplyTo(msg.getReplyWith());
                replyMsg.setReplyWith(msg.getSender().toString());
                agent_.postMessage(replyMsg);
                return;
            }
            router_.dispatch(msg);
        }
    }

    public void handlePosted(AID agentID, ACLMessage msg) {
        System.out.println("AgentToolkit.handlePosted() called.");
        throw new UnsupportedOperationException();
    }

    public void handleReceived(AID agentID, ACLMessage msg) throws AuthException {
        System.out.println("AgentToolkit.handleReceived() called.");
        throw new UnsupportedOperationException();
    }

    public void handleChangedAgentState(AID agentID, jade.core.AgentState from, jade.core.AgentState to) {
        System.out.println("AgentToolkit.handleChangedAgentState() called.");
        throw new UnsupportedOperationException();
    }

    public void handleChangedAgentPrincipal(AID agentID, AgentPrincipal from, AgentPrincipal to, IdentityCertificate identity) {
        System.out.println("AgentToolkit.handleChangedAgentPrincip() called.");
        throw new UnsupportedOperationException();
    }

    public Authority getAuthority() {
        System.out.println("AgentToolkit.getAuthority() called.");
        throw new UnsupportedOperationException();
    }

    public void setPlatformAddresses(AID id) {
        System.out.println("AgentToolkit.setPlatformAddress() called.");
        throw new UnsupportedOperationException();
    }
}
