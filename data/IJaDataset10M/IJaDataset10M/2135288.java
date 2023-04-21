package net.sourceforge.combean.graph.util;

import java.util.Iterator;
import net.sourceforge.combean.interfaces.graph.Graph;
import net.sourceforge.combean.interfaces.graph.containers.FixedPath;
import net.sourceforge.combean.interfaces.graph.prop.GlobalNodesGraphProp;
import net.sourceforge.combean.interfaces.graph.prop.OutgoingNeighborhoodGraphProp;

/**
 * Utilitiy class which converts graphs and substructures of graphs into
 * human-readable string representations (for debugging purposes)
 * 
 * @author schickin
 *
 */
public final class GraphStringifier {

    /**
     * Convert a path to a string
     * 
     * @param path the path to be converted
     * @return a string representation of path
     */
    public static <Node, Edge> String convertToString(FixedPath<Node, Edge> path) {
        StringBuffer result = new StringBuffer();
        result.append("path {");
        Iterator<Node> itPathNodes = path.getNodeIterator();
        Iterator<Edge> itPathEdges = path.getEdgeIterator();
        while (itPathEdges.hasNext()) {
            result.append("node {");
            result.append(itPathNodes.next());
            result.append("} edge {");
            result.append(itPathEdges.next());
            result.append("} ");
        }
        result.append("node {");
        result.append(itPathNodes.next());
        result.append("}");
        result.append("}");
        return result.toString();
    }

    /**
     * Convert the edges in an edge iteration to a String
     * 
     * @param itEdges the iterator for which all edges of the iteration shall
     * be converted to a string.
     * @return the string representing the edges in the iteration.
     */
    public static <Edge> String convertEdgeIterationToString(Iterator<Edge> itEdges) {
        StringBuffer result = new StringBuffer();
        result.append("edge iterator contents {");
        while (itEdges.hasNext()) {
            result.append(" edge {");
            result.append(itEdges.next());
            result.append("}");
        }
        return result.toString();
    }

    /**
     * Convert a graph to a string consisting of all adjacency lists
     * of outgoing edges
     * 
     * @param g the graph to be converted
     * @return the adjacency lists of outgoing edges of g
     */
    @SuppressWarnings("unchecked")
    public static <Node, Edge> String convertOutgoingAdjacencyListsToString(Graph g) {
        OutgoingNeighborhoodGraphProp<Node, Edge> outG = (OutgoingNeighborhoodGraphProp<Node, Edge>) GraphPropertyInitializer.requireGraphProperty(g, OutgoingNeighborhoodGraphProp.class);
        GlobalNodesGraphProp globNodes = (GlobalNodesGraphProp) GraphPropertyInitializer.requireGraphProperty(g, GlobalNodesGraphProp.class);
        StringBuffer result = new StringBuffer();
        result.append("graph outgoing edges {");
        Iterator<Node> itAllNodes = globNodes.getAllNodesIterator();
        while (itAllNodes.hasNext()) {
            Node v = itAllNodes.next();
            result.append("node {");
            result.append(v);
            result.append("} outgoing edges {");
            Iterator<Edge> outEdges = outG.getOutgoingEdges(v);
            result.append(convertEdgeIterationToString(outEdges));
            result.append("} ");
        }
        result.append("}");
        return result.toString();
    }
}
