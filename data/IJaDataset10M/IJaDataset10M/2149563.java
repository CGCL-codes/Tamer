package org.instedd.smsreminder.messaging.impl;

import org.instedd.smsreminder.messaging.core.IOutboundMessage;

/**
 * Simple implementation of {@link IOutboundMessage}
 * @see IOutboundMessage
 *
 */
class OutboundMessage implements IOutboundMessage {

    private String to;

    private byte[] body;

    public OutboundMessage(String to, byte[] body) {
        this.to = to;
        this.body = body;
    }

    @Override
    public byte[] getBody() {
        return body;
    }

    @Override
    public String getTo() {
        return to;
    }
}
