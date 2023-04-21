package uk.ac.ebi.intact.util.simplegraph;

import uk.ac.ebi.intact.model.BasicObject;
import uk.ac.ebi.intact.model.Interactor;
import uk.ac.ebi.intact.model.util.CvObjectUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * A simple graph class for temporary processing, for example to prepare output for graph analysis packages.
 */
@Deprecated
public class Graph extends uk.ac.ebi.intact.util.simplegraph.BasicGraph implements GraphI {

    /**
     * This is used in toString() in order to be platform compatible.
     */
    private static final String NEW_LINE = System.getProperty("line.separator");

    private HashMap<String, BasicGraphI> nodes = new HashMap<String, BasicGraphI>();

    private ArrayList<EdgeI> edges = new ArrayList<EdgeI>();

    private HashSet visited = new HashSet();

    public void addNode(BasicGraphI aNode) {
        nodes.put(aNode.getAc(), aNode);
    }

    public BasicGraphI addNode(Interactor anInteractor) {
        BasicGraphI node = nodes.get(anInteractor.getAc());
        if (null == node) {
            node = new Node(anInteractor);
            this.addNode(node);
        }
        return node;
    }

    public void addEdge(EdgeI anEdge) {
        if (!edges.contains(anEdge)) edges.add(anEdge);
    }

    public HashMap<String, BasicGraphI> getNodes() {
        return nodes;
    }

    public Collection<EdgeI> getEdges() {
        return edges;
    }

    /** record that a Component has been visited during
     *  graph exploration.
     */
    public void addVisited(BasicObject anElement) {
        visited.add(anElement.getAc());
    }

    /** return true if a Component has been visited during graph exploration.
     *
     */
    public boolean isVisited(BasicObject anElement) {
        return visited.contains(anElement.getAc());
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        final int count = edges.size();
        s.append("Graph[" + count + "]").append(NEW_LINE);
        for (int i = 0; i < count; i++) {
            EdgeI e = edges.get(i);
            s.append(e.getNode1().getAc());
            s.append('(');
            s.append(CvObjectUtils.createRoleInfo(e.getComponent1().getCvExperimentalRole(), e.getComponent1().getCvBiologicalRole()).getRelevantName());
            s.append(')');
            s.append("-> ");
            s.append(e.getNode2().getAc());
            s.append('(');
            s.append(CvObjectUtils.createRoleInfo(e.getComponent2().getCvExperimentalRole(), e.getComponent2().getCvBiologicalRole()).getRelevantName());
            s.append(')');
            s.append(NEW_LINE);
        }
        return s.toString();
    }
}
