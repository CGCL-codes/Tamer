package com.limegroup.bittorrent.handshaking;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.limewire.nio.AbstractNBSocket;
import com.limegroup.bittorrent.BTConnectionFactory;
import com.limegroup.bittorrent.ManagedTorrent;
import com.limegroup.bittorrent.TorrentLocation;

class OutgoingBTHandshaker extends BTHandshaker {

    private static final Log LOG = LogFactory.getLog(OutgoingBTHandshaker.class);

    /**
	 * creates an outgoing handshaker to the given location for
	 * the given torrent.
	 */
    public OutgoingBTHandshaker(TorrentLocation loc, ManagedTorrent torrent, AbstractNBSocket sock, BTConnectionFactory factory) {
        super(loc, sock, factory);
        this.torrent = torrent;
        this.observer = torrent.getFetcher();
    }

    public void startHandshaking() {
        if (shutdown) return;
        initOutgoingHandshake();
        initIncomingHandshake();
        setWriteInterest();
        setReadInterest();
    }

    protected void initIncomingHandshake() {
        incomingHandshake = new ByteBuffer[5];
        incomingHandshake[0] = ByteBuffer.allocate(1);
        incomingHandshake[1] = ByteBuffer.allocate(19);
        incomingHandshake[2] = ByteBuffer.wrap(loc.getExtBytes());
        incomingHandshake[3] = ByteBuffer.allocate(20);
        incomingHandshake[4] = ByteBuffer.wrap(loc.getPeerID());
    }

    protected boolean verifyIncoming() {
        for (; currentBufIndex < incomingHandshake.length && !incomingHandshake[currentBufIndex].hasRemaining(); currentBufIndex++) {
            ByteBuffer current = incomingHandshake[currentBufIndex];
            switch(currentBufIndex) {
                case 0:
                    if (current.get(0) != (byte) 19) return false;
                    break;
                case 1:
                    if (!Arrays.equals(current.array(), BTConnectionFetcher.BITTORRENT_PROTOCOL_BYTES)) return false;
                    break;
                case 2:
                    break;
                case 3:
                    if (!Arrays.equals(current.array(), torrent.getInfoHash())) return false;
                    break;
                case 4:
                    break;
            }
        }
        return true;
    }

    public void handleIOException(IOException iox) {
        if (LOG.isDebugEnabled()) LOG.debug("Connection failed: " + loc);
        loc.strike();
        if (!loc.isOut()) torrent.addEndpoint(loc);
        super.handleIOException(iox);
    }
}
