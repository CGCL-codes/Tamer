package playground.mmoyo.PTRouter;

import java.util.Iterator;
import java.util.List;
import org.matsim.interfaces.core.v01.Link;
import org.matsim.interfaces.core.v01.Node;
import org.matsim.network.NetworkLayer;
import org.matsim.network.NetworkWriter;

public class PTNetwork2View extends NetworkLayer {

    private static final String NETWORKFILENAME = "c://PTnetwork.xml";

    private NetworkLayer cityNet;

    public PTNetwork2View(NetworkLayer cityNet) {
        super();
        this.cityNet = cityNet;
    }

    public PTNetwork2View() {
        super();
    }

    public void createPTNView(List<PTLine> ptLineList) {
        for (Iterator<PTLine> iterPTLines = ptLineList.iterator(); iterPTLines.hasNext(); ) {
            PTLine ptLine = iterPTLines.next();
            boolean firstLink = true;
            for (Iterator<String> iter = ptLine.getRoute().iterator(); iter.hasNext(); ) {
                Link l = this.cityNet.getLink(iter.next());
                if (firstLink) {
                    addNode(l.getFromNode());
                }
                addNode(l.getToNode());
                addLink(l);
                firstLink = false;
            }
        }
        writePTNetwork();
    }

    private void addNode(Node node) {
        if (this.getNode(node.getId().toString()) == null) {
            this.createNode(node.getId(), node.getCoord()).setType(node.getType());
        }
    }

    private void addLink(Link l) {
        if (this.getLink(l.getId()) == null) {
            this.createLink(l.getId(), l.getFromNode(), l.getToNode(), l.getLength(), 1.0, 1.0, 1.0, l.getOrigId(), l.getType());
        }
    }

    public void writePTNetwork() {
        NetworkWriter networkWriter = new NetworkWriter(this, NETWORKFILENAME);
        networkWriter.write();
    }

    public void printLinks() {
        for (org.matsim.interfaces.core.v01.Link l : this.getLinks().values()) {
            System.out.println("(" + l.getFromNode().getId().toString() + ")----" + l.getId().toString() + "--->(" + l.getToNode().getId().toString() + ")   " + "      (" + ((PTNode) l.getFromNode()).getIdFather().toString() + ")----" + l.getId().toString() + "--->(" + ((PTNode) l.getToNode()).getIdFather().toString() + ")");
        }
    }
}
