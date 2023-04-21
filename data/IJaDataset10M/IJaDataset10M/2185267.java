package org.dreamspeak.lib.protocol.packets.outbound;

import org.dreamspeak.lib.helper.IsCRC32Signed;
import org.dreamspeak.lib.protocol.packets.PacketClass;
import org.dreamspeak.lib.protocol.packets.PacketClass.Kind;

/**
 * TODO: Proper documentation
 * 
 * @author avithan
 */
@PacketClass(Kind.Connection)
public abstract class OutboundPacketConnection extends OutboundPacket implements IsCRC32Signed {

    public OutboundPacketConnection(int sessionKey, int clientId) {
        super(sessionKey, clientId);
    }
}
