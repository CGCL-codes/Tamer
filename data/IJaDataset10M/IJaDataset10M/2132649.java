package org.bintrotter.apeer.sendstates;

import org.bintrotter.apeer.Peer;

public class WaitForHandshake implements SendState {

    private Peer m_peer;

    private org.bintrotter.apeer.receivestates.HandShake receiveStateHandshake;

    private long startWaitTime;

    public WaitForHandshake(Peer peer) {
        m_peer = peer;
        receiveStateHandshake = null;
        startWaitTime = 0;
    }

    @Override
    public void reset() {
        receiveStateHandshake = (org.bintrotter.apeer.receivestates.HandShake) m_peer.get(org.bintrotter.apeer.receivestates.States.HANDSHAKE);
        startWaitTime = System.currentTimeMillis();
    }

    @Override
    public States update() {
        if (System.currentTimeMillis() - startWaitTime > 3000) return States.SHUTDOWN;
        if (this.receiveStateHandshake.isHandshakeSuccessful()) return States.BITFIELD;
        return States.WAIT_FOR_HANDSHAKE;
    }
}
