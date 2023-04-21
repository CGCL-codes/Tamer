package net.sourceforge.fluxion.graph;

import net.sourceforge.fluxion.graph.listener.GraphListener;
import net.sourceforge.fluxion.graph.listener.GraphChangeEvent;
import net.sourceforge.fluxion.graph.visitor.GraphVisitor;
import java.util.*;

/**
 * Javadocs go here.
 *
 * @author Tony Burdett
 * @version 1.0
 * @date 23-Apr-2007
 */
public abstract class AbstractGraph implements Graph {

    private String name;

    private Map<Integer, Node> nodeMap;

    private Map<Integer, Edge> edgeMap;

    private Map<Integer, Node> rootNodeMap;

    private List<GraphListener> listeners = new ArrayList<GraphListener>();

    /**
   * Constructs a new DefaultGraph.
   */
    public AbstractGraph() {
        nodeMap = new HashMap<Integer, Node>();
        edgeMap = new HashMap<Integer, Edge>();
        rootNodeMap = new HashMap<Integer, Node>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void add(Node node) {
        nodeMap.put(node.hashCode(), node);
        rootNodeMap.put(node.hashCode(), node);
        GraphChangeEvent event = new GraphChangeEvent(GraphChangeEvent.NODE_ADDED, this, System.currentTimeMillis());
        for (GraphListener listener : listeners) {
            listener.nodeAdded(event);
        }
    }

    public void remove(Node node) {
        if (nodeMap.containsValue(node)) {
            if (rootNodeMap.containsValue(node)) {
                rootNodeMap.remove(node.hashCode());
            }
            nodeMap.remove(node.hashCode());
            GraphChangeEvent event = new GraphChangeEvent(GraphChangeEvent.NODE_REMOVED, this, System.currentTimeMillis());
            for (GraphListener listener : listeners) {
                listener.nodeRemoved(event);
            }
        }
    }

    public void add(Edge edge) {
        edgeMap.put(edge.hashCode(), edge);
        if (rootNodeMap.containsValue(edge.getTailNode())) {
            rootNodeMap.remove(edge.getTailNode().hashCode());
        }
        GraphChangeEvent event = new GraphChangeEvent(GraphChangeEvent.EDGE_ADDED, this, System.currentTimeMillis());
        for (GraphListener listener : listeners) {
            listener.edgeAdded(event);
        }
    }

    public void remove(Edge edge) {
        if (edgeMap.containsValue(edge)) {
            edgeMap.remove(edge.hashCode());
            GraphChangeEvent event = new GraphChangeEvent(GraphChangeEvent.EDGE_REMOVED, this, System.currentTimeMillis());
            for (GraphListener listener : listeners) {
                listener.edgeRemoved(event);
            }
        }
    }

    public void removeAll() {
        for (Node node : nodeMap.values()) {
            remove(node);
        }
        for (Edge edge : edgeMap.values()) {
            remove(edge);
        }
    }

    public boolean contains(Node node) {
        if (nodeMap.containsKey(node.hashCode())) {
            if (nodeMap.get(node.hashCode()) == node) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(Edge edge) {
        if (nodeMap.containsKey(edge.hashCode())) {
            if (nodeMap.get(edge.hashCode()) == edge) {
                return true;
            }
        }
        return false;
    }

    public void setAsRoot(Node node) {
        if (nodeMap.containsKey(node.hashCode())) {
            rootNodeMap.put(node.hashCode(), node);
        }
    }

    public Set<Node> getRootNodes() {
        Set<Node> rootNodes = new HashSet<Node>();
        for (Node n : rootNodeMap.values()) {
            rootNodes.add(n);
        }
        return rootNodes;
    }

    public Collection<Edge> getLeadingEdges(Node node) {
        Set<Edge> leadEdges = new HashSet<Edge>();
        for (Edge e : edgeMap.values()) {
            if (e.getTailNode().equals(node)) {
                leadEdges.add(e);
            }
        }
        return leadEdges;
    }

    public Set<Edge> getTailingEdges(Node node) {
        Set<Edge> tailEdges = new HashSet<Edge>();
        for (Edge e : edgeMap.values()) {
            if (e.getHeadNode().equals(node)) {
                tailEdges.add(e);
            }
        }
        return tailEdges;
    }

    public Node getNode(String name) {
        Node node = null;
        for (Node n : this.getAllNodes()) {
            if (n.getLabel().equals(name)) {
                node = n;
            }
        }
        return node;
    }

    public Set<Node> getAllNodes() {
        Set<Node> nodes = new HashSet<Node>();
        for (Node n : nodeMap.values()) {
            nodes.add(n);
        }
        return nodes;
    }

    public void cascadeChanges() {
        GraphChangeEvent evt = new GraphChangeEvent(GraphChangeEvent.RENAMING_EVENT, this, System.currentTimeMillis());
        for (GraphListener listener : listeners) {
            listener.graphChanged(evt);
        }
    }

    public void accept(GraphVisitor visitor) {
        visitor.visit(this);
    }

    public void addGraphListener(GraphListener listener) {
        listeners.add(listener);
    }

    public void removeGraphListener(GraphListener listener) {
        listeners.remove(listener);
    }
}
