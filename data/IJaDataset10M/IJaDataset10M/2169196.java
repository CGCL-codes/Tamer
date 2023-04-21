package org.bintrotter.apeer.receivestates;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import org.bintrotter.Main;
import org.bintrotter.apeer.Peer;

public class HandShake implements ReceiveState {

    private Peer m_peer;

    private ByteBuffer bb;

    private SocketChannel sc;

    private boolean handshakeSuccessful;

    public HandShake(Peer peer) {
        m_peer = peer;
        handshakeSuccessful = false;
    }

    public boolean isHandshakeSuccessful() {
        return handshakeSuccessful;
    }

    @Override
    public void reset() {
        handshakeSuccessful = false;
        sc = m_peer.channel();
        bb = ByteBuffer.allocate(68);
        bb.clear();
    }

    public static final byte[] PID = new byte[] { 'B', 'i', 't', 'T', 'o', 'r', 'r', 'e', 'n', 't', ' ', 'p', 'r', 'o', 't', 'o', 'c', 'o', 'l' };

    @Override
    public States update() {
        try {
            if (sc.isOpen() == false) Main.log.info("not open!");
            if (sc.socket().isConnected() == false) Main.log.info("not connected!");
            if (sc.socket().isInputShutdown()) Main.log.info("isInputShutdown()==true !");
            sc.read(bb);
            if (bb.hasRemaining() == false) {
                bb.rewind();
                int pidLen = bb.get();
                if (pidLen != PID.length) {
                    bb = null;
                    Main.log.warn("bad protocol id length == " + pidLen);
                    return States.SHUTDOWN;
                }
                byte[] buf = new byte[19];
                bb.position(1);
                bb.get(buf, 0, pidLen);
                if (Arrays.equals(buf, PID) == false) {
                    bb = null;
                    Main.log.warn("bad protocol id: " + new String(buf) + " | " + new String(PID));
                    return States.SHUTDOWN;
                }
                buf = new byte[20];
                bb.position(1 + pidLen + 8);
                bb.get(buf, 0, 20);
                if (Arrays.equals(m_peer.metafile().infoHash.SHA1Bytes, buf) == false) {
                    bb = null;
                    Main.log.warn("bad info hash");
                    return States.SHUTDOWN;
                }
                handshakeSuccessful = true;
                return States.LENGTH;
            }
            return States.HANDSHAKE;
        } catch (IOException e) {
            e.printStackTrace();
            bb = null;
            return States.SHUTDOWN;
        }
    }
}
