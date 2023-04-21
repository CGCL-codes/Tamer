package net.confex.schema.layout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;

/**
 * Creates dummy edges between nodes, to be used with NodeJoiningDirectedGraphLayout
 * @author Phil Zoio
 */
public class ClusterEdgeCreator {

    private static final int INITIAL_RECURSION_DEPTH = 3;

    NodeList nodeList;

    EdgeList edgeList;

    DirectedGraph graph;

    List edgesAdded;

    List encountered = new ArrayList();

    List clusters = new ArrayList();

    Cluster currentCluster = null;

    /**
	 * @param graph
	 */
    public ClusterEdgeCreator() {
        super();
    }

    public void visit(DirectedGraph graph) {
        try {
            this.graph = graph;
            this.nodeList = graph.nodes;
            this.edgeList = graph.edges;
            edgesAdded = new ArrayList();
            for (Iterator iter = nodeList.iterator(); iter.hasNext(); ) {
                Node node = (Node) iter.next();
                if (!encountered.contains(node)) {
                    currentCluster = new Cluster();
                    clusters.add(currentCluster);
                    encountered.add(node);
                    currentCluster.set.add(node);
                    int depth = INITIAL_RECURSION_DEPTH;
                    recursivelyAddToCluster(node, depth);
                } else {
                }
            }
            for (Iterator iter = clusters.iterator(); iter.hasNext(); ) {
                Cluster cluster = (Cluster) iter.next();
            }
            coalesceRemainingClusters();
            joinClusters();
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
	 * If recursion fails to join all the remaining
	 */
    private void coalesceRemainingClusters() {
    }

    /**
	 * Joins the clusters together
	 */
    private void joinClusters() {
        if (clusters.size() > 1) {
            Node sourceNode = null;
            Node targetNode = null;
            for (Iterator iter = clusters.iterator(); iter.hasNext(); ) {
                Cluster cluster = (Cluster) iter.next();
                if (sourceNode != null) {
                    targetNode = (Node) cluster.set.get(0);
                    newDummyEdge(sourceNode, targetNode);
                }
                sourceNode = (Node) cluster.set.get(cluster.set.size() - 1);
            }
        }
    }

    private void recursivelyAddToCluster(Node node, int depth) {
        if (depth > 3) return; else {
            depth++;
            EdgeList incoming = node.incoming;
            for (Iterator iter = incoming.iterator(); iter.hasNext(); ) {
                Edge edge = (Edge) iter.next();
                Node incomingNode = edge.source;
                if (!encountered.contains(incomingNode)) {
                    encountered.add(incomingNode);
                    currentCluster.set.add(incomingNode);
                    recursivelyAddToCluster(incomingNode, depth);
                } else {
                }
            }
            EdgeList outgoing = node.outgoing;
            for (Iterator iter = outgoing.iterator(); iter.hasNext(); ) {
                Edge edge = (Edge) iter.next();
                Node outgoingNode = edge.target;
                if (!encountered.contains(outgoingNode)) {
                    encountered.add(outgoingNode);
                    currentCluster.set.add(outgoingNode);
                    recursivelyAddToCluster(outgoingNode, depth);
                } else {
                }
            }
        }
    }

    /**
	 * creates a new dummy edge to be used in the graph
	 */
    private Edge newDummyEdge(Node sourceNode, Node targetNode) {
        boolean addedEdge;
        DummyEdgePart edgePart = new DummyEdgePart();
        Edge edge = new Edge(edgePart, sourceNode, targetNode);
        edge.weight = 2;
        edgeList.add(edge);
        targetNode = sourceNode;
        addedEdge = true;
        return edge;
    }

    /**
	 * Very thin wrapper around List
	 */
    private class Cluster {

        List set = new ArrayList();

        public String toString() {
            return set.toString();
        }
    }
}
