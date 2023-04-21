package org.apache.bailey.ddb;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.bailey.util.SetValuedMap;

/**
 * Not synchronized (yet).
 */
class NodeHostMap {

    private Map<NodeID, HostID> nodeToHost = new HashMap<NodeID, HostID>();

    private SetValuedMap<HostID, NodeID> hostToNodes = new SetValuedMap<HostID, NodeID>();

    public NodeHostMap() {
    }

    public NodeHostMap(NodeHostMap map) {
        nodeToHost.putAll(map.nodeToHost);
        hostToNodes.putAll(map.hostToNodes);
    }

    public boolean contains(NodeID nodeID) {
        return nodeToHost.containsKey(nodeID);
    }

    public HostID getHost(NodeID nodeID) {
        return nodeToHost.get(nodeID);
    }

    public Set<NodeID> getNodes(HostID hostID) {
        return hostToNodes.get(hostID);
    }

    public boolean addNode(NodeID nodeID, HostID hostID) {
        if (!nodeToHost.containsKey(nodeID)) {
            nodeToHost.put(nodeID, hostID);
            hostToNodes.add(hostID, nodeID);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeNode(NodeID nodeID) {
        if (nodeToHost.containsKey(nodeID)) {
            HostID hostID = nodeToHost.remove(nodeID);
            hostToNodes.remove(hostID, nodeID);
            return true;
        } else {
            return false;
        }
    }
}
