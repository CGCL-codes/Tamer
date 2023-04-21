package gg.arkehion.network.packet;

import gg.arkehion.network.exception.ArkProtocolPacketException;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * Shutdown Message class for packet
 *
 * 1 string: spassword(or key)
 *
 * @author frederic bregier
 */
public class ShutdownPacket extends AbstractLocalPacket {

    private final byte[] key;

    /**
     * @param headerLength
     * @param middleLength
     * @param endLength
     * @param buf
     * @return the new ShutdownPacket from buffer
     * @throws OpenR66ProtocolPacketException
     */
    public static ShutdownPacket createFromBuffer(int headerLength, int middleLength, int endLength, ChannelBuffer buf) throws ArkProtocolPacketException {
        if (headerLength - 1 <= 0) {
            throw new ArkProtocolPacketException("Not enough data");
        }
        final byte[] bpassword = new byte[headerLength - 1];
        if (headerLength - 1 > 0) {
            buf.readBytes(bpassword);
        }
        return new ShutdownPacket(bpassword);
    }

    /**
     * @param spassword
     */
    public ShutdownPacket(byte[] spassword) {
        key = spassword;
    }

    @Override
    public void createEnd() throws ArkProtocolPacketException {
        end = ChannelBuffers.EMPTY_BUFFER;
    }

    @Override
    public void createHeader() throws ArkProtocolPacketException {
        if (key != null) {
            header = ChannelBuffers.wrappedBuffer(key);
        }
    }

    @Override
    public void createMiddle() throws ArkProtocolPacketException {
        middle = ChannelBuffers.EMPTY_BUFFER;
    }

    @Override
    public String toString() {
        return "ShutdownPacket";
    }

    @Override
    public byte getType() {
        return LocalPacketFactory.SHUTDOWNPACKET;
    }

    /**
     * @return the key
     */
    public byte[] getKey() {
        return key;
    }
}
