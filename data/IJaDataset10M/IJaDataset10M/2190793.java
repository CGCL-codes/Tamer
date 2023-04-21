package com.reserveamerica.elastica.appserver.jboss;

import java.util.HashMap;
import java.util.Map;
import com.reserveamerica.elastica.cluster.Node;
import com.reserveamerica.elastica.cluster.NodeKey;

/**
 * This map maintains relationships between the nodes and remote object targets.
 * It provides accessors to retrieve a node given a target or a target given a node.
 * 
 * @param V The {@link NodeView} implementation class
 * @param T The target class.
 * 
 * @author BStasyszyn
 */
public class NodeToTargetMap<V extends NodeView<T>, T> {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(NodeToTargetMap.class);

    private final long clusterConfigVersion;

    private final long clusterFamilyViewId;

    private final Map<NodeKey, V> nodeMap = new HashMap<NodeKey, V>();

    private final Map<T, V> targetToNodeMap = new HashMap<T, V>();

    private boolean connected;

    public NodeToTargetMap() {
        this.clusterConfigVersion = Long.MAX_VALUE;
        this.clusterFamilyViewId = Long.MAX_VALUE;
    }

    public NodeToTargetMap(long clusterConfigVersion, long clusterFamilyViewId) {
        this.clusterConfigVersion = clusterConfigVersion;
        this.clusterFamilyViewId = clusterFamilyViewId;
    }

    /**
   * The cluster version (view) ID.
   * 
   * @return long
   */
    public long getClusterConfigVersion() {
        return clusterConfigVersion;
    }

    /**
   * The cluster family view ID.
   * 
   * @return long
   */
    public long getClusterFamilyViewId() {
        return clusterFamilyViewId;
    }

    /**
   * Adds a new node-target relationship.
   * 
   * @param node Node
   * @param target RemoteObject
   */
    public void add(V nodeView) {
        nodeMap.put(NodeKey.getKey(nodeView), nodeView);
        targetToNodeMap.put(nodeView.getRemoteTarget(), nodeView);
    }

    /**
   * Retrieves the target that corresponds to the given node.
   * 
   * @param node Node
   * @return RemoteObject
   */
    public V getNodeView(Node node) {
        return getNodeView(NodeKey.getKey(node));
    }

    /**
   * Retrieves the node view that corresponds to the given node key.
   * 
   * @param nodeKey NodeKey
   * @return T
   */
    public V getNodeView(NodeKey nodeKey) {
        return nodeMap.get(nodeKey);
    }

    /**
   * Returns a map of node views.
   * 
   * @return The node map.
   */
    public Map<NodeKey, V> getNodeMap() {
        return nodeMap;
    }

    /**
   * The size of the map.
   * 
   * @return int
   */
    public int size() {
        return nodeMap.size();
    }

    /**
   * Returns true if the given node key exists.
   * 
   * @param nodeKey NodeKey
   * @return boolean - true if the given node key exists; otherwise false.
   */
    public boolean contains(NodeKey nodeKey) {
        return nodeMap.containsKey(nodeKey);
    }

    /**
   * Returns the node view for the given remote target.
   * 
   * @param remoteTarget
   * @return
   */
    public V getNodeView(T remoteTarget) {
        return targetToNodeMap.get(remoteTarget);
    }
}
