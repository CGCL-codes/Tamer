package org.nbrowse.views;

import java.util.List;

/** JNetMan has inner class that implements to listen for selects from locate gene
 * @author mgibson */
interface SelectionListener {

    public void selectNode(NodeViewI n);

    public void selectNodes(List<NodeViewI> nodes);

    public void selectEdge(EdgeViewI e);
}
