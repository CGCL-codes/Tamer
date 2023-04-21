package org.rakiura.micro;

import java.util.Random;
import org.rakiura.transport.Message;
import org.rakiura.transport.TransportSystem;
import org.rakiura.util.ArrayList;
import org.rakiura.util.HashMap;
import org.rakiura.util.List;

/**
 * 
 * @author pmallet
 * Created: 16-Aug-05 2:24:14 PM
 */
public final class RemoteSystemAgentLoader {

    /** contains reference to every registered local agent in order to
	 forward messages */
    private HashMap localAgents = new HashMap();

    /** contains reference to every registered remote agent in order to
	 reference them */
    private HashMap RemoteAgentStubs = new HashMap();

    /** contains address of some known platform */
    private ArrayList knownPlatforms = new ArrayList();

    /** reference to the transport system */
    private TransportSystem transport = TransportSystem.getInstance();

    /** Constants used to inquiry on agents or roles */
    public static final String FIND_AGENTS = "findAgents";

    public static final String FIND_ROLES = "findRoles";

    /** static instance of the RemoteSystemAgentLoader */
    private static RemoteSystemAgentLoader instance = new RemoteSystemAgentLoader();

    /** 
	 * allows acces to the static instance of RemoteSystemAgentLoader
	 * @return
	 */
    public static RemoteSystemAgentLoader getInstance() {
        return instance;
    }

    /**
	 * This class broadcasts a request to each recheable platform for agents matching
	 * the given goal
	 * @param goalType
	 */
    public void findRemoteAgents(String goal, RemoteAgent agent) {
        String agentName = getAgentName(agent);
        String[] goalName = new String[1];
        goalName[0] = goal;
        Message message = new Message(Message.REQUEST);
        message.setSender(agentName);
        message.setAttributes(FIND_AGENTS, goalName);
        message.setReceiverName("RemoteSystemAgentLoader");
        this.transport.sendMessage(message);
    }

    /**
	 * This class broadcasts a request to each recheable platform for agents matching
	 * the given role
	 * @param goalType
	 */
    public void findRemoteRoles(String role, RemoteAgent agent) {
        String agentName = getAgentName(agent);
        String[] roleName = new String[1];
        roleName[0] = role;
        Message message = new Message(Message.REQUEST);
        message.setSender(agentName);
        message.setReceiverAddress(Message.BROADCAST);
        message.setAttributes(FIND_ROLES, roleName);
        message.setReceiverName("RemoteSystemAgentLoader");
        this.transport.sendMessage(message);
    }

    public String getAgentName(RemoteAgent agent) {
        String agentName = (String) this.localAgents.getKey(agent);
        if (agentName == null) {
            Random rand = new Random(System.currentTimeMillis());
            agentName = "agent" + (new Integer(rand.nextInt()).toString());
            this.localAgents.put(agentName, agent);
        }
        return agentName;
    }

    /**
	 * This is the method called when a message arrives.
	 * If this is a request for role, performs the search.
	 * Otherwise, forward the message to the correct agent
	 * @param incoming
	 */
    public void receivedMessage(Message incoming) {
        try {
            if (incoming.getReceiver().equals("RemoteSystemAgentLoader")) {
                if (incoming.getAttribute().equals(FIND_ROLES) && (incoming.getParams().length > 0)) {
                    Class.forName("org.rakiura.micro.RemoteSystemAgentLoader");
                    System.out.println(incoming.getParams()[0]);
                    Role[] results = SystemAgentLoader.findRoles(Class.forName(incoming.getParams()[0]));
                    if (results.length > 0) {
                        Message response = buildResponse(results, incoming.getParams()[0]);
                        response.setReceiver(incoming.getSender(), incoming.getSenderAddress());
                        response.setSender("RemoteSystemAgentLoader");
                        this.transport.sendMessage(response);
                    }
                }
            }
            if (incoming.getAttribute().equals(RemoteAgent.ROLES_FOUND)) {
                String[] rolesFound = incoming.getParams();
                System.out.println("first role name: " + rolesFound[1]);
                RemoteAgentStub[] results = new RemoteAgentStub[rolesFound.length - 1];
                String roleName = rolesFound[0];
                for (int i = 1; i < rolesFound.length; i++) {
                    results[i - 1] = new RemoteAgentStub(rolesFound[i], incoming.getSenderAddress(), roleName, (RemoteAgent) this.localAgents.get(incoming.getReceiver()));
                    this.RemoteAgentStubs.put(rolesFound[i], results[i - 1]);
                }
                ((RemoteAgent) this.localAgents.get(incoming.getReceiver())).results(results);
            } else if (this.localAgents.containsKey(incoming.getReceiver())) {
                RemoteAgent receiver = ((RemoteAgent) this.localAgents.get(incoming.getReceiver()));
                if (incoming.getPerformative().equals(Message.PROPOSE)) {
                    RemoteAgentStub sender = new RemoteAgentStub(incoming.getSender(), incoming.getSenderAddress(), incoming.getAttribute(), receiver);
                    this.RemoteAgentStubs.put(incoming.getSender(), sender);
                    receiver.propose(incoming.getAttribute(), sender);
                } else {
                    if (!this.RemoteAgentStubs.containsKey(incoming.getSender())) System.out.println("Warning, received message from an unknown agent");
                    RemoteAgentStub sender = (RemoteAgentStub) this.RemoteAgentStubs.get(incoming.getSender());
                    if (incoming.getPerformative().equals(Message.ACCEPT_PROPOSAL)) {
                        receiver.acceptProposal(incoming.getAttribute(), sender);
                    } else if (incoming.getPerformative().equals(Message.CANCEL)) {
                        receiver.cancel(sender);
                    } else if (incoming.getPerformative().equals(Message.INFORM)) {
                        receiver.inform(incoming.getAttribute(), incoming.getParams()[0], sender);
                    } else if (incoming.getPerformative().equals(Message.REQUEST)) {
                        receiver.request(incoming.getAttribute(), incoming.getParams(), sender);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Remove a stub registered by this RemoteSystemAgentLoader
	 * @param stub
	 */
    public void releaseStub(RemoteAgentStub stub) {
        this.RemoteAgentStubs.remove(stub.getName());
    }

    /**
	 * This function build the response message of a role search
	 * @param results
	 * @return
	 */
    private Message buildResponse(Role[] results, String role) {
        String roleName;
        String[] rolesName = new String[results.length + 1];
        Message response = new Message(Message.INFORM);
        rolesName[0] = role;
        Random rand = new Random(System.currentTimeMillis());
        for (int i = 0; i < results.length; i++) {
            roleName = (String) this.localAgents.getKey(results[i]);
            if (roleName == null) {
                roleName = "agent" + rand.nextInt();
                this.localAgents.put(roleName, results[i]);
            }
            rolesName[i + 1] = roleName;
        }
        response.setAttributes(RemoteAgent.ROLES_FOUND, rolesName);
        return response;
    }

    /**
	 * Add the address of a remote platform
	 * @param address
	 */
    public void addPlatform(String address) {
        this.knownPlatforms.add(address);
    }

    /**
	 * look for the addresses of known platform corrosponding to the given
	 * protocol
	 * @param protocol
	 * @return
	 */
    public List getPlatforms(String protocol) {
        ArrayList results = new ArrayList();
        for (int i = 0; i < knownPlatforms.size(); i++) {
            String tmp = (String) knownPlatforms.get(i);
            if (tmp.startsWith(protocol)) results.add(tmp);
        }
        return results;
    }
}
