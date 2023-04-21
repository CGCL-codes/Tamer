package org.neteng;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class NetworkComposed extends Network {

    private List<Network> networks = new ArrayList<Network>();

    public void addNetwork(Network network) {
        networks.add(network);
    }

    @Override
    public List<Node> getMasters() {
        List<Node> nodes = new ArrayList<Node>();
        for (Network network : networks) {
            nodes.addAll(network.getMasters());
        }
        return nodes;
    }

    @Override
    public Node getPrimaryMaster() {
        for (Network network : networks) {
            Node node = network.getPrimaryMaster();
            if (node != null) return node;
        }
        return null;
    }

    @Override
    public List<Node> getSlaves() {
        List<Node> nodes = new ArrayList<Node>();
        for (Network network : networks) {
            nodes.addAll(network.getSlaves());
        }
        return nodes;
    }

    @Override
    public InputStream syncSend(Node node, InputStream message) {
        return null;
    }
}
