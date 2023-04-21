package info.goldenorb.graph;

import java.util.Hashtable;

public class Graph {

    private Hashtable<String, Node> nodeList;

    private Hashtable<String, Edge> edgeList;

    private String id;

    private boolean directed;

    private String nodeOnClickAction;

    private NodeMenu nodeMenu;

    public Graph() {
        nodeList = new Hashtable<String, Node>();
        edgeList = new Hashtable<String, Edge>();
        directed = false;
        id = "G";
    }

    public void setNodeOnClickAction(String action) {
        this.nodeOnClickAction = action;
        this.nodeMenu = null;
    }

    public void setNodeOnClickAction(NodeMenu action) {
        this.nodeMenu = action;
        this.nodeOnClickAction = null;
    }

    public String getNodeOnClickAction() {
        return this.nodeOnClickAction;
    }

    public NodeMenu getNodeMenuOnClick() {
        return this.nodeMenu;
    }

    public void addNode(Node node) {
        if (!nodeList.contains(node.getId())) nodeList.put(node.getId(), node);
    }

    public Edge addEdge(Edge edge) {
        String edgeKey = getEdgeKey(edge.getSource().getId(), edge.getTarget().getId());
        Edge edgeAux = edgeList.get(edgeKey);
        if (edgeAux == null) {
            addNode(edge.getSource());
            addNode(edge.getTarget());
            edgeList.put(edgeKey, edge);
        }
        return edge;
    }

    private String getEdgeKey(String source, String target) {
        if (source.compareTo(target) < 0) return source + "|" + target; else return target + "|" + source;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setDirected(boolean value) {
        this.directed = value;
    }

    public boolean isDirected() {
        return this.directed;
    }

    public Hashtable<String, Node> getNodeList() {
        return this.nodeList;
    }

    public Hashtable<String, Edge> getEdgeList() {
        return this.edgeList;
    }
}
