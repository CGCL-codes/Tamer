package org.bissa;

import org.bissa.tuple.TupleIdFactory;
import org.bissa.util.BissaUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import rice.environment.Environment;
import rice.p2p.commonapi.IdFactory;
import rice.pastry.NodeHandle;
import rice.pastry.NodeIdFactory;
import rice.pastry.PastryNode;
import rice.pastry.PastryNodeFactory;
import rice.pastry.socket.SocketPastryNodeFactory;
import rice.pastry.standard.IPNodeIdFactory;
import rice.persistence.*;
import java.io.IOException;
import java.net.InetSocketAddress;

@Deprecated
public class BissaNode {

    private NodeInfo nodeInfo = null;

    private static boolean started = false;

    private static BissaPastImpl pastApp = null;

    private static final Log log = LogFactory.getLog(BissaNode.class);

    public BissaNode(NodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;
    }

    public void start() throws IOException {
        log.info("Creating environment...");
        Environment env = new Environment(nodeInfo.getConfigurationFile());
        NodeIdFactory nidFactory = new IPNodeIdFactory(BissaUtils.convertToInetAddress(nodeInfo.getLocalAddress()), nodeInfo.getLocalPort(), env);
        PastryNodeFactory factory = new SocketPastryNodeFactory(nidFactory, nodeInfo.getBootPort(), env);
        InetSocketAddress bootaddress = new InetSocketAddress(nodeInfo.getBootAddress(), nodeInfo.getBootPort());
        log.info("Creating Pastry node bootstrapping to IP: " + nodeInfo.getBootAddress() + " and port: " + nodeInfo.getBootPort());
        PastryNode node = factory.newNode();
        node.boot(bootaddress);
        try {
            synchronized (node) {
                while (!node.isReady() && !node.joinFailed()) {
                    node.wait(500);
                    if (node.joinFailed()) {
                        throw new IOException("Could not join the FreePastry ring.  Reason:" + node.joinFailedReason());
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Successfully created pastry node " + node);
        IdFactory idf = new TupleIdFactory(env);
        Storage stor = new MemoryStorage(idf);
        pastApp = new BissaPastImpl(node, new StorageManagerImpl(idf, stor, new LRUCache(new MemoryStorage(idf), 512 * 1024, node.getEnvironment())), 0, "");
        started = true;
        log.info("Successfully started pastry node " + node);
    }

    public static BissaPastImpl getPastApp() throws Exception {
        if (started) {
            return pastApp;
        } else {
            throw new Exception("Can't return past Application, node is not started yet");
        }
    }
}
