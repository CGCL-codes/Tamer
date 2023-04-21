package org.tcpfile.net.protocols;

import java.util.Vector;
import org.apache.mina.common.ByteBuffer;

/**
 * This is the description of the lowlevel Protocol.
 * Every byte[] sent is wrapped with this protocol, so that the receiver can tell
 * whether the full byte[] has been received or just parts of it.
 * It is only used to determine whether a byte array has fully arrived yet or not.
 * @author Stivo
 *
 */
public abstract class LowLevelProtocol {

    public abstract ByteBuffer outgoing(byte[] b);

    public abstract Vector<byte[]> incoming(ByteBuffer bb);

    public static LowLevelProtocol getCurrentLowLevel() {
        return new LowLevelProtocol2();
    }
}
