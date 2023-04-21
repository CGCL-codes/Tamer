package nodomain.applewhat.torrentdemonio.protocol;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;
import nodomain.applewhat.torrentdemonio.metafile.MalformedMetadataException;
import nodomain.applewhat.torrentdemonio.metafile.TorrentMetadata;
import nodomain.applewhat.torrentdemonio.protocol.messages.PendingHandshake;
import nodomain.applewhat.torrentdemonio.protocol.tracker.TrackerEventListener;
import nodomain.applewhat.torrentdemonio.protocol.tracker.TrackerManager;
import nodomain.applewhat.torrentdemonio.storage.TorrentStorage;
import nodomain.applewhat.torrentdemonio.util.ConfigManager;

/**
 * @author Alberto Manzaneque
 *
 */
public class TorrentDownloadManager implements IncomingConnectionListener {

    private static Logger logger = Logger.getLogger(TorrentDownloadManager.class.getName());

    private TorrentMetadata metadata;

    private TrackerManager tracker;

    private DownloadProcess process;

    private boolean start, stop, destroy;

    private State state;

    private List<Peer> remainingPeers;

    private List<PeerConnection> connectedPeers;

    private Selector sockSelector;

    private TorrentStorage storage;

    private enum State {

        INITIALIZED, STARTED, STOPPED, DESTROYED
    }

    ;

    public TorrentDownloadManager(File torrentFile) throws IOException, MalformedMetadataException {
        metadata = TorrentMetadata.createFromFile(torrentFile);
        storage = new TorrentStorage(metadata, torrentFile);
        tracker = new TrackerManager(metadata);
        remainingPeers = new Vector<Peer>();
        connectedPeers = new Vector<PeerConnection>();
        tracker.addTrackerEventListener(new PeerAdder());
        process = new DownloadProcess();
        start = false;
        stop = false;
        destroy = false;
        state = State.INITIALIZED;
        sockSelector = null;
        new Thread(process, metadata.getName()).start();
    }

    private class DownloadProcess implements Runnable {

        public void run() {
            tracker.start();
            try {
                while (state != State.DESTROYED) {
                    switch(state) {
                        case INITIALIZED:
                            logger.fine("Torrent " + metadata.getName() + " initialized");
                            synchronized (this) {
                                if (!start) wait();
                                if (start) {
                                    doStart();
                                }
                            }
                            break;
                        case STOPPED:
                            synchronized (this) {
                                if (!start && !destroy) wait();
                                if (start) {
                                    doStart();
                                }
                                if (destroy) {
                                    doDestroy();
                                }
                            }
                            break;
                        case STARTED:
                            synchronized (this) {
                                if (stop) doStop();
                                if (destroy) {
                                    doStop();
                                    doDestroy();
                                }
                            }
                            makeNewConnections();
                            if (stop || destroy) break;
                            processSocketEvents();
                            if (stop || destroy) break;
                            findNewActionsToDo();
                            break;
                        case DESTROYED:
                            break;
                    }
                }
            } catch (InterruptedException e) {
                logger.warning(e.getMessage());
                doStop();
                doDestroy();
            } catch (IOException e) {
                logger.severe("Unrecoverable IO exception in torrent " + metadata.getName() + ". " + e.getMessage());
                doStop();
                doDestroy();
            }
        }
    }

    protected void doStart() throws IOException {
        sockSelector = Selector.open();
        state = State.STARTED;
        start = false;
        logger.fine("Torrent " + metadata.getName() + " started");
    }

    protected void doStop() {
        for (PeerConnection connectedPeer : connectedPeers) {
            connectedPeer.kill();
        }
        try {
            sockSelector.close();
        } catch (IOException e) {
            logger.warning("Error closing socket selector for " + metadata.getName());
        }
        state = State.STOPPED;
        stop = false;
        logger.fine("Torrent " + metadata.getName() + " stopped");
    }

    protected void doDestroy() {
        state = State.DESTROYED;
        destroy = false;
        logger.fine("Torrent " + metadata.getName() + " destroyed");
    }

    public void start() {
        synchronized (process) {
            start = true;
            process.notify();
        }
    }

    public void stop() {
        synchronized (process) {
            stop = true;
            process.notify();
        }
    }

    public void destroy() {
        synchronized (process) {
            destroy = true;
            process.notify();
        }
    }

    private void makeNewConnections() {
        synchronized (remainingPeers) {
            while (connectedPeers.size() < ConfigManager.getMaxConnections() && remainingPeers.size() > 0) {
                Peer peer = null;
                peer = remainingPeers.get(0);
                logger.fine("Trying to connect to peer " + peer);
                remainingPeers.remove(peer);
                try {
                    SocketChannel sock = SocketChannel.open();
                    sock.configureBlocking(false);
                    sock.connect(new InetSocketAddress(peer.getAddress(), peer.getPort()));
                    sock.register(sockSelector, SelectionKey.OP_CONNECT, peer);
                } catch (IOException e) {
                    logger.fine("Could not connect to peer " + peer);
                }
            }
        }
    }

    private void processSocketEvents() {
        try {
            if (sockSelector.select(5000) > 0) {
                Set<SelectionKey> keys = sockSelector.selectedKeys();
                Iterator<SelectionKey> i = keys.iterator();
                while (i.hasNext()) {
                    SelectionKey key = i.next();
                    i.remove();
                    if (key.isConnectable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        Peer peer = (Peer) key.attachment();
                        try {
                            if (channel.isConnectionPending()) {
                                channel.finishConnect();
                                PeerConnection conn = new PeerConnection(peer, channel, metadata.getInfoHash());
                                conn.enqueue(new PendingHandshake(metadata.getInfoHash(), ConfigManager.getClientId().getBytes()));
                                connectedPeers.add(conn);
                                key.attach(conn);
                                key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                                logger.fine("Connected to new peer: " + peer);
                            }
                        } catch (Exception e) {
                            logger.fine("Can not connect to peer " + peer + ". " + e.getMessage());
                            key.attach(null);
                            key.cancel();
                        }
                    }
                    if (key.isValid() && key.isWritable()) {
                        PeerConnection conn = (PeerConnection) key.attachment();
                        try {
                            if (!conn.doWrite()) {
                                key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
                            }
                        } catch (Exception e) {
                            logger.fine("Error sending message to " + conn.getPeer() + ". Closing connection: " + e.getMessage());
                            conn.kill();
                            key.attach(null);
                            key.cancel();
                            connectedPeers.remove(conn);
                        }
                    }
                    if (key.isValid() && key.isReadable()) {
                        PeerConnection conn = (PeerConnection) key.attachment();
                        try {
                            if (!conn.getChannel().isOpen()) {
                                logger.fine("Socket closed. Connection with " + conn.getPeer() + " dropped");
                                conn.kill();
                                key.cancel();
                            } else {
                                conn.doRead();
                            }
                        } catch (Exception e) {
                            logger.fine("Error reading from socket (" + e.getMessage() + "). Closing connection with " + conn.getPeer());
                            conn.kill();
                            key.attach(null);
                            key.cancel();
                            connectedPeers.remove(conn);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void findNewActionsToDo() {
    }

    private class PeerAdder implements TrackerEventListener {

        public void peerAddedEvent(Peer peer) {
            synchronized (remainingPeers) {
                if (!remainingPeers.contains(peer) && !connectedPeers.contains(peer)) {
                    logger.fine("New peer for download " + metadata.getName() + ", " + peer);
                    remainingPeers.add(peer);
                }
            }
        }
    }

    @Override
    public byte[] getInfoHash() {
        return metadata.getInfoHash();
    }

    @Override
    public void incomingConnectionReceived(PeerConnection peer) {
        synchronized (remainingPeers) {
            if (!connectedPeers.contains(peer)) {
                if (remainingPeers.contains(peer)) {
                    remainingPeers.remove(peer);
                }
                connectedPeers.add(peer);
                peer.enqueue(new PendingHandshake(metadata.getInfoHash(), ConfigManager.getClientId().getBytes()));
                logger.fine("New peer (incoming connection) for download " + metadata.getName() + ", " + peer.getPeer());
                try {
                    peer.getChannel().configureBlocking(false);
                    peer.getChannel().register(sockSelector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, peer);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
