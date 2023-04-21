package org.coos.examples.chat;

import org.coos.actorframe.ActorSM;
import org.coos.javaframe.ActorAddress;
import org.coos.javaframe.messages.AFPropertyMsg;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author by geir Tellu AS.
 *         Date: Feb 23, 2010
 *         Time: 9:29:19 AM
 *         Copy right Tellu AS
 */
public class ChatRoomSM extends ActorSM {

    HashMap<String, ActorAddress> clients;

    /**
     * This class is created by class.forname(). Constructur with no
     * names is therefor called.  Super is implicit called by the container;
     */
    public ChatRoomSM() {
        setBehaviorClass(new ChatRoomCS("ChatRoom"));
    }

    /**
     * Initiate state data for this instance. This method is called
     * by the framework before any signal is received by the state machine.
     */
    public void initInstance() {
        super.initInstance();
        clients = new HashMap<String, ActorAddress>();
    }

    /**
     * Adds a client to the chat
     * @param alias
     * @param address
     */
    void addClient(String alias, ActorAddress address) {
        clients.put(alias, address);
        trace.traceTask("User: " + alias + " is now connected to the chat room");
    }

    /**
     * Sends the message to all clients except the sender of this message
     * @param message is the message to send to clients
     * @param sender is the actor address of the sender of the message
     */
    void send(String message, ActorAddress sender) {
        Collection<ActorAddress> en = clients.values();
        Vector<ActorAddress> receivers = new Vector();
        for (Iterator<ActorAddress> addressIterator = en.iterator(); addressIterator.hasNext(); ) {
            ActorAddress actorAddress = addressIterator.next();
            if (actorAddress.equals(sender)) {
                receivers.addElement(actorAddress);
                AFPropertyMsg pm = new AFPropertyMsg("Message").setProperty("text", message);
                sendMessage(pm, actorAddress);
            }
        }
        trace.traceTask("Message: " + message + " is send to " + receivers);
    }

    /**
     * Removes a client from the chat room
     * @param alias is the alias of the user
     */
    public void remove(String alias) {
        clients.remove(alias);
        trace.traceTask("User: " + alias + " is removed");
    }

    /**
     * 
     * @return
     */
    public boolean hasClients() {
        return !clients.isEmpty();
    }
}
