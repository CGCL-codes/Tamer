package jcfs.core.serverside;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jcfs.core.fs.RServerInfo;

/**
 * handles everything about distribution of copies on peers
 * @author enrico
 */
public class ReplicaManager {

    private JCFSFileServer server;

    private static final Logger logger = Logger.getLogger("replica");

    public ReplicaManager(JCFSFileServer server) {
        this.server = server;
    }

    /**
     * replicates synchronously a file on other peers
     * @param path
     * @param minpeers
     */
    void replicate(String path, int minpeers, boolean allowoverwrite) throws IOException {
        minpeers = minpeers - 1;
        List<RServerInfo> peers = server.getPeers();
        if (peers.size() < minpeers) {
            server.updatePeerInfo();
        }
        if (peers.size() < minpeers) {
            throw new IOException("impossible to replicate file " + path + " on " + minpeers + " peers, only " + peers.size() + " peers are actually present");
        }
        for (RServerInfo peerserver : peers) {
            logger.log(Level.INFO, "replicate {0} on {1}", new Object[] { path, peerserver });
            server.copyFileOnPeer(path, peerserver, allowoverwrite);
        }
    }
}
