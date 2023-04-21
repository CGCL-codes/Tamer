package net.sf.jgcs.spread;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import net.sf.jgcs.Message;

class SpMessage implements Message {

    private byte[] payload;

    private String sender;

    SpMessage() {
    }

    SpMessage(ByteBuffer buf) {
        payload = new byte[buf.remaining()];
        buf.get(payload);
    }

    public void setPayload(byte[] buffer) {
        payload = buffer;
    }

    public byte[] getPayload() {
        return payload;
    }

    public SocketAddress getSenderAddress() {
        return new SpGroup(sender);
    }

    public void setSenderAddress(SocketAddress sender) {
        this.sender = ((SpGroup) sender).getGroup();
    }
}
