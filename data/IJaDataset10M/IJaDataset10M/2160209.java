package com.aelitis.azureus.plugins.extseed;

public class ExternalSeedManualPeer {

    private ExternalSeedPeer peer;

    protected ExternalSeedManualPeer(ExternalSeedPeer _peer) {
        peer = _peer;
    }

    public String getIP() {
        return (peer.getIp());
    }

    public byte[] read(int piece_number, int offset, int length, int timeout) throws ExternalSeedException {
        return (peer.getReader().read(piece_number, offset, length, timeout));
    }

    public ExternalSeedPeer getDelegate() {
        return (peer);
    }
}
