package com.aelitis.azureus.core.clientmessageservice.impl;

import java.util.Map;
import com.aelitis.azureus.core.clientmessageservice.impl.ClientConnection;

/**
 * 
 */
public class ClientMessage {

    private final String message_id;

    private final ClientConnection client;

    private final Map payload;

    private ClientMessageHandler handler;

    private boolean outcome_reported;

    public ClientMessage(String msg_id, ClientConnection _client, Map msg_payload, ClientMessageHandler _handler) {
        this.message_id = msg_id;
        this.client = _client;
        this.payload = msg_payload;
        this.handler = _handler;
    }

    public String getMessageID() {
        return message_id;
    }

    public ClientConnection getClient() {
        return client;
    }

    public Map getPayload() {
        return payload;
    }

    public ClientMessageHandler getHandler() {
        return handler;
    }

    public void setHandler(ClientMessageHandler new_handler) {
        this.handler = new_handler;
    }

    public void reportComplete() {
        synchronized (this) {
            if (outcome_reported) {
                return;
            }
            outcome_reported = true;
        }
        handler.sendAttemptCompleted(this);
    }

    public void reportFailed(Throwable error) {
        synchronized (this) {
            if (outcome_reported) {
                return;
            }
            outcome_reported = true;
        }
        handler.sendAttemptFailed(this, error);
    }
}
