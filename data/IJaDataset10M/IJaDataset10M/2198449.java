package org.eaiframework.support;

import java.util.ArrayList;
import java.util.Collection;
import org.eaiframework.LifecycleEnum;
import org.eaiframework.Message;
import org.eaiframework.MessageConsumer;
import org.eaiframework.MessageListener;

/**
 * 
 */
public abstract class AbstractMessageConsumer implements MessageConsumer {

    protected String endpoint;

    protected LifecycleEnum state;

    protected MessageListener listener;

    protected long pollingInterval;

    public void setMessageListener(MessageListener listener) {
        this.listener = listener;
    }

    public long getPollingInterval() {
        return pollingInterval;
    }

    public void setPollingInterval(long pollingInterval) {
        this.pollingInterval = pollingInterval;
    }

    public LifecycleEnum getState() {
        return state;
    }

    public String getEndpoint() {
        return this.endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    protected void notifyListeners(Message message) throws Exception {
        if (listener != null) {
            listener.onMessage(message);
        }
    }

    protected String getLogHead() {
        return "[endpoing=" + endpoint + "] ";
    }
}
