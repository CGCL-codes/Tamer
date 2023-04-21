package issrg.utils.xml;

import org.w3c.dom.*;
import java.awt.AWTEvent;

public class NodeSelectionEvent extends AWTEvent {

    XMLEditor xmlED;

    private Node selectedNode;

    public NodeSelectionEvent(XMLEditor xmlED, Object source, Node selectedNode) {
        super(source, 1);
        this.xmlED = xmlED;
        setSelectedNode(selectedNode);
    }

    public void setSelectedNode(Node selNode) {
        selectedNode = selNode;
    }

    public Node getSelectedNode() {
        return selectedNode;
    }
}
