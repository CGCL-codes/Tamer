package monitor.layer3logic;

import java.util.Vector;
import monitor.mib.DiscoveryType;
import monitor.mib.L3MIBHandler;
import monitor.mib.NamedOID;
import monitor.mib.RouterMIB;

public class L3PropetryHandler {

    private Vector<L3MIBHandler> handlers;

    public L3PropetryHandler() {
        this.reloadRegisteredHandlers();
    }

    public void reloadRegisteredHandlers() {
        handlers = new Vector<L3MIBHandler>();
        RouterMIB rmib = new RouterMIB();
        handlers.add(rmib);
    }

    public Vector<NamedOID> getOIDs(DiscoveryType dt) {
        Vector<NamedOID> ret = new Vector<NamedOID>();
        Vector<NamedOID> tmp;
        for (L3MIBHandler vh : handlers) {
            tmp = vh.getOids(dt);
            if (tmp != null) {
                ret.addAll(tmp);
            }
        }
        return ret;
    }

    public void addNodes(Vector<Router> nodes, Vector<Router> newNodes) {
        Vector<Router> tmp = new Vector<Router>();
        boolean found = false;
        for (Router newNode : newNodes) {
            found = false;
            for (Router node : nodes) {
                if (node.getAttribute("managementIP").equals(newNode.getAttribute("managementIP"))) {
                    node.addResult(newNode.getResults());
                    found = true;
                }
            }
            if (!found) {
                tmp.add(newNode);
            }
        }
        nodes.addAll(tmp);
    }

    public void processNodes(Vector<Router> nodes) {
        ProcessNodes pn = new ProcessNodes();
        pn.handlers = handlers;
        pn.nodes = nodes;
        pn.run();
    }

    class ProcessNodes implements Runnable {

        public Vector<Router> nodes;

        public Vector<L3MIBHandler> handlers;

        public void run() {
            for (L3MIBHandler handler : handlers) {
                nodes = handler.setProperties(nodes);
            }
        }
    }
}
