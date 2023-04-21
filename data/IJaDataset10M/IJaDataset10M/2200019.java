package net.sf.doolin.app.sc.engine.delegate.support;

import net.sf.doolin.app.sc.engine.InstanceState;
import net.sf.doolin.app.sc.engine.client.Client;
import net.sf.doolin.app.sc.engine.client.ClientID;
import net.sf.doolin.app.sc.engine.common.Message;
import net.sf.doolin.app.sc.engine.delegate.ClientDelegateResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AsyncClientDelegate<T extends InstanceState, P extends ClientDelegateResponse> extends AbstractClientDelegate<T, P> {

    private static final Log log = LogFactory.getLog(AsyncClientDelegate.class);

    private final Client<T, P> client;

    public AsyncClientDelegate(ClientID clientID, Client<T, P> client) {
        super(clientID);
        this.client = client;
    }

    public Client<T, P> getClient() {
        return this.client;
    }

    @Override
    public P play(T instanceState) {
        int turnNumber = instanceState.getTurnNumber();
        log.debug(String.format("[%s] Sending instance state for turn %d", getClientID(), turnNumber));
        this.client.setInstanceState(instanceState);
        log.debug(String.format("[%s] Waiting response for turn %d", getClientID(), turnNumber));
        P response = this.client.getResponse();
        log.debug(String.format("[%s] Response has been received for turn %d", getClientID(), turnNumber));
        return response;
    }

    @Override
    public void sendMessages(Message... messages) {
        this.client.sendMessages(messages);
    }

    @Override
    public String toString() {
        return String.format("Client %s", this.client);
    }
}
