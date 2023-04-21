package org.bintrotter.apeer.sendstates;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import org.bintrotter.apeer.Peer;

public class Have implements SendState {

    private Peer m_peer;

    private ByteBuffer bb;

    private SocketChannel sc;

    public Have(Peer peer) {
        m_peer = peer;
    }

    @Override
    public void reset() {
        int have = m_peer.outHaveQueue.poll();
        sc = m_peer.channel();
        if (bb == null) {
            bb = ByteBuffer.allocate(9);
            bb.putInt(5).put((byte) 4).putInt(have);
        } else {
            bb.position(5);
            bb.putInt(have);
        }
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
        return States.HAVE;
    }
}
