package subsearch.graph;

import java.util.List;

public interface Graph {

    public List<Node> nodes();

    public List<Edge> edges();

    public Node getNode(int i);

    public int getNodeCount();

    public int getEdgeCount();

    public boolean hasEdge(Node u, Node v);

    public Edge getEdge(Node u, Node v);
}
