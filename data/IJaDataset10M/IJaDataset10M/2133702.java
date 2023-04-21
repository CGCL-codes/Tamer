package com.yahoo.zookeeper.server.util;

import java.util.HashSet;
import java.util.Set;
import com.yahoo.zookeeper.server.DataTree;
import com.yahoo.zookeeper.server.ServerCnxn;
import com.yahoo.zookeeper.server.ZooKeeperObserverNotifier;
import com.yahoo.zookeeper.server.quorum.QuorumPeer;

/**
 * Zookeeper specific implementation of ObserverManager. It implements a mapping
 * of observer classes to a set of observer instances.
 */
public class ZooKeeperObserverManager extends ObserverManager {

    private ZooKeeperObserverManager() {
    }

    /**
     * Explicitly set this class as a concrete instance of ObserverManager.
     */
    public static void setAsConcrete() {
        setInstance(new ZooKeeperObserverManager());
    }

    protected Set<Object> getObserverList(Object ob) {
        if (ob instanceof ConnectionObserver || ob instanceof ServerCnxn) return connectionObservers; else if (ob instanceof ServerObserver || ob instanceof ZooKeeperObserverNotifier) return serverObservers; else if (ob instanceof QuorumPeerObserver || ob instanceof QuorumPeer) return quorumPeerObservers; else if (ob instanceof DataTreeObserver || ob instanceof DataTree) return dataTreeObservers;
        assert false;
        return null;
    }

    private Set<Object> serverObservers = new HashSet<Object>();

    private Set<Object> connectionObservers = new HashSet<Object>();

    private Set<Object> dataTreeObservers = new HashSet<Object>();

    private Set<Object> quorumPeerObservers = new HashSet<Object>();
}
