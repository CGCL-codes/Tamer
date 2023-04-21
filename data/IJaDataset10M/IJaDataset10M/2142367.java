package com.extentech.luminet;

import java.util.HashMap;
import com.extentech.comm.*;
import com.extentech.security.User;

/**
 * Provides clustering for FT/LB Luminet Services
 *  
 *  This class manages inter-instance communications re:
 *  
 *  session replication
 *  REST api replication/broadcast
 *  server startup, shutdown
 *  load balancing
 *  scalability
 *  
 *  
 *  Broadcasting is peer-to-peer, instance type allows different instance sites/apps
 *  
 *  
 *  
 *  --- NODE ---
 *  - per CPU/compute resource
 *  - contains clustered instances
 *  - manages load monitoring/balancing
 *  - manages pulse-check of instances (restarts the dead)
 *  - cleansup stale resources
 *  - provides management REST api
 *  - provides management
 *  
 *  
 *  --- Cluster Manager Servlet (aka: ServletCommunicator ---
 *  - 
 *  
 *  
 *  
 *  ------------------------------------------------------------
 * 
 * @author John McMahon :: [ Nov 25, 2009 ] :: Copyright &copy;2009 <a href = "http://www.extentech.com">Extentech Inc.</a>
 *
 */
public class ClusterManager extends GenericMessageManager implements Runnable, MessageListener {

    AbstractMessageListener listener = null;

    /**
	 * Default constructor
	 * ------------------------------------------------------------
	 * 
	 * @author John McMahon [ Nov 25, 2008 ]
	 * @param theKey
	 */
    public ClusterManager(Object theKey) {
        super(theKey);
    }

    /**
	 * Add a server instance to this cluster
	 * ------------------------------------------------------------
	 * 
	 * @author John McMahon [ Nov 25, 2008 ]
	 * @param s
	 * @param u
	 */
    public void addInstance(Serve s, User u) {
    }

    /**
	 * remove a server instance from this cluster
	 * ------------------------------------------------------------
	 * 
	 * @author John McMahon [ Nov 25, 2008 ]
	 * @param s
	 * @param u
	 */
    public void removeInstance(Serve s, User u) {
    }

    public void run() {
    }

    public void addMessageSession(Object sessionId, MessageManager messageSession) {
        listener.addMessageSession(sessionId, messageSession);
    }

    public void addMessageToQueue(Object sessionId, Message mesg) {
        listener.addMessageToQueue(sessionId, mesg);
    }

    public void clearMessages(Object sessionId) {
        listener.clearMessages(sessionId);
    }

    public boolean equals(Object obj) {
        return listener.equals(obj);
    }

    public MessageManager getMessageSession(Object sessionId) {
        return listener.getMessageSession(sessionId);
    }

    public int getMessagingType() {
        return listener.getMessagingType();
    }

    public Message[] getPendingMessages(Object sessionId) {
        return listener.getPendingMessages(sessionId);
    }

    public HashMap getSessions() {
        return listener.getSessions();
    }

    public int hashCode() {
        return listener.hashCode();
    }

    public void removeMessageSession(Object sessionId) {
        listener.removeMessageSession(sessionId);
    }

    public boolean sendMessageToAll(Object sessionId, Message msg) throws MessageFormatException {
        return listener.sendMessageToAll(sessionId, msg);
    }

    public boolean sendMessageToOthers(Object sessionId, Message msg) throws MessageFormatException {
        return listener.sendMessageToOthers(sessionId, msg);
    }

    public void setMessagingType(int m) {
        listener.setMessagingType(m);
    }

    public String toString() {
        return listener.toString();
    }
}
