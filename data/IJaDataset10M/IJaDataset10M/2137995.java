package symbiosis.message;

import symbiosis.util.Util;
import java.util.LinkedList;
import java.util.Iterator;

public class MessageChannel {

    private String name = null;

    private Object owner = null;

    private boolean oneWayChannel = false;

    private LinkedList listeners = null;

    public MessageChannel(String name) {
        this.name = name;
        listeners = new LinkedList();
    }

    public MessageChannel(String name, Object owner) {
        this.name = name;
        this.owner = owner;
        listeners = new LinkedList();
    }

    public String getName() {
        return name;
    }

    public boolean getOneWayChannel() {
        return oneWayChannel;
    }

    public void setOneWayChannel(boolean oneWayChannel, Object owner) {
        if (this.owner != null) {
            if (this.owner == owner) {
                this.oneWayChannel = oneWayChannel;
            }
        } else {
            Util.debug("Permission denied to set one way channel, not owner");
        }
    }

    public LinkedList getListeners() {
        return (LinkedList) listeners.clone();
    }

    public synchronized void addMessageListener(SymMessageListener listener) {
        if (listener != null) listeners.add(listener);
    }

    public synchronized void removeMessageListener(SymMessageListener listener) {
        if (listener != null) listeners.remove(listener);
    }

    public synchronized void sendMessage(SymMessage message) {
        sendMessage(message, null);
    }

    public synchronized void sendMessage(SymMessage message, Object owner) {
        if (this.owner != null && oneWayChannel) {
            if (this.owner != owner) {
                Util.debug("Denied permission to send message: " + message);
            }
        }
        message.setMessageID("MID:" + Math.random());
        message.setTimestamp(System.currentTimeMillis());
        Iterator iter = listeners.iterator();
        while (iter.hasNext()) {
            ((SymMessageListener) iter.next()).handleSymMessage(message);
        }
    }
}
