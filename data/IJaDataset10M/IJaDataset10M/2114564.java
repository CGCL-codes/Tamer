package org.bintrotter.apeer.sendstates;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import org.bintrotter.apeer.Peer;

public class Choke implements SendState {

    private static final byte[] msgData = new byte[] { 0, 0, 0, 1, 0 };

    private Peer m_peer;

    private ByteBuffer bb;

    private SocketChannel sc;

    public Choke(Peer peer) {
        m_peer = peer;
    }

    @Override
    public void reset() {
        sc = m_peer.channel();
        if (bb == null) bb = ByteBuffer.wrap(msgData);
        bb.rewind();
    }

    @Override
    public States update() {
        try {
            sc.write(bb);
            if (bb.hasRemaining() == false) return States.IDLE;
        } catch (IOException ioe) {
            return States.SHUTDOWN;
        }
        return States.CHOKE;
    }
}
