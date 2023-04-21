package org.nuclearbunny.icybee.protocol;

import java.net.ProtocolException;

public class StatusPacket extends Packet {

    public StatusPacket(final String rawPacket) throws ProtocolException {
        super(rawPacket);
    }

    public String getStatusHeader() {
        return getField(0);
    }

    public String getStatusText() {
        return getField(1);
    }
}
