package org.notify4b.im;

import java.util.Collection;

public abstract class Chat {

    private Collection<MessageListener> listeners;

    public Chat() {
    }

    public abstract void sentMessage(String msg) throws ImException;

    public abstract void sentMessage(Message msg) throws ImException;

    public void addMessageListener(MessageListener listener) {
        listeners.add(listener);
    }

    public void removeMessageListener(MessageListener listener) {
        if (listener != null) listeners.remove(listener);
    }

    public String getParcipant() {
        return null;
    }
}
