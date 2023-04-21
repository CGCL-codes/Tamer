package org.rascalli.framework.net.tcp;

/**
 * A PacketFactory wraps raw message data into {@link Packet}s. The resulting
 * packet might, for example, contain header information such as the length of
 * the message, in addition to the raw message data.
 * 
 * @author cschollum
 */
public interface PacketFactory {

    IncomingPacket acquireIncomingPacket();

    void releaseIncomingPacket(IncomingPacket incomingPacket);

    OutgoingPacket acquireOutgoingPacket(byte[] data);

    void releaseOutgoingPacket(OutgoingPacket outgoingPacket);
}
