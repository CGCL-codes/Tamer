package playground.ou.intersectiongroup;

import java.util.Map;
import java.util.TreeMap;
import org.matsim.core.api.network.Node;

public class Signalgroup {

    private Map<String, Node> signalgroup = new TreeMap<String, Node>();

    public void addNodeToGroup(String nodeid, Node node) {
        signalgroup.put(nodeid, node);
    }

    public void createSignalgroup(Map<String, Node> nodes) {
        signalgroup.putAll(nodes);
    }

    public Map<String, Node> getSignalgroup() {
        return signalgroup;
    }
}
