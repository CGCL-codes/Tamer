package domain;

import java.util.ArrayList;

public class NodeManager {

    private ArrayList<Node> m_Nodes;

    private String m_MountPoint;

    public NodeManager() {
    }

    public NodeManager(String mountPoint, ArrayList<Node> hosts) {
        m_MountPoint = mountPoint;
        m_Nodes = hosts;
    }

    public boolean removeNode(String HostName) {
        Node tempHost;
        tempHost = null;
        for (Node Host : m_Nodes) {
            if (Host.getName().equals(HostName)) {
                tempHost = Host;
                break;
            }
        }
        if (tempHost != null) {
            tempHost.removeAllShares(m_MountPoint);
            m_Nodes.remove(tempHost);
            return true;
        }
        return false;
    }

    public void addNode(Node node) {
        m_Nodes.add(node);
    }

    public ArrayList<Node> getNodes() {
        return m_Nodes;
    }

    public Node getNode(String name) {
        Node foundHost = null;
        for (Node host : m_Nodes) {
            if (host.getName().equals(name)) {
                foundHost = host;
                break;
            }
        }
        return foundHost;
    }

    public void mountAllShares() {
        for (Node node : m_Nodes) {
            for (Share share : node.getShares()) {
                share.mountShare(m_MountPoint, node.getName());
            }
        }
    }

    public String getMountPoint() {
        return m_MountPoint;
    }

    public double getReservedMemory() {
        double reservedMemory = 0;
        for (Node node : m_Nodes) {
            for (Share share : node.getShares()) {
                reservedMemory += share.getReservedMemoryAsDouble();
            }
        }
        return reservedMemory;
    }
}
