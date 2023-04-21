package org.limewire.mojito.messages.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.net.SocketAddress;
import org.limewire.mojito.Context;
import org.limewire.mojito.io.MessageInputStream;
import org.limewire.mojito.io.MessageOutputStream;
import org.limewire.mojito.messages.MessageID;
import org.limewire.mojito.messages.PingResponse;
import org.limewire.mojito.routing.Contact;
import org.limewire.mojito.routing.Version;

/**
 * An implementation of PingResponse (Pong)
 */
public class PingResponseImpl extends AbstractResponseMessage implements PingResponse {

    private final SocketAddress externalAddress;

    private final BigInteger estimatedSize;

    public PingResponseImpl(Context context, Contact contact, MessageID messageId, SocketAddress externalAddress, BigInteger estimatedSize) {
        super(context, OpCode.PING_RESPONSE, contact, messageId, Version.ZERO);
        this.externalAddress = externalAddress;
        this.estimatedSize = estimatedSize;
    }

    public PingResponseImpl(Context context, SocketAddress src, MessageID messageId, Version msgVersion, MessageInputStream in) throws IOException {
        super(context, OpCode.PING_RESPONSE, src, messageId, msgVersion, in);
        this.externalAddress = in.readSocketAddress();
        this.estimatedSize = in.readDHTSize();
    }

    /** My external address */
    public SocketAddress getExternalAddress() {
        return externalAddress;
    }

    public BigInteger getEstimatedSize() {
        return estimatedSize;
    }

    protected void writeBody(MessageOutputStream out) throws IOException {
        out.writeSocketAddress(externalAddress);
        out.writeDHTSize(estimatedSize);
    }

    public String toString() {
        return "PingResponse: externalAddress=" + externalAddress + ", estimatedSize=" + estimatedSize;
    }
}
