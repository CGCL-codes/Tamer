package edu.gsbme.gyoza2d.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;

/**
 * Graph traversal utility. This utility allows us to get ancestors or child nodes
 * @author David
 *
 */
public class GraphUtility {

    /**
	 * Get the Vertexs connected to this vertex
	 * @param vertex
	 * @return
	 */
    public static ArrayList<DefaultGraphCell> getTargetVertex(DefaultGraphCell vertex) {
        ArrayList<DefaultGraphCell> result = new ArrayList<DefaultGraphCell>();
        List children = vertex.getChildren();
        for (int i = 0; i < children.size(); i++) {
            DefaultPort port = (DefaultPort) children.get(i);
            Set edges = port.getEdges();
            Object[] edgeArray = edges.toArray(new Object[edges.size()]);
            for (int j = 0; j < edgeArray.length; j++) {
                DefaultEdge edge = (DefaultEdge) edgeArray[j];
                result.add((DefaultGraphCell) ((DefaultPort) edge.getTarget()).getParent());
            }
        }
        result.remove(vertex);
        return result;
    }

    public static ArrayList<DefaultGraphCell> getSourceVertex(DefaultGraphCell vertex) {
        ArrayList<DefaultGraphCell> result = new ArrayList<DefaultGraphCell>();
        List children = vertex.getChildren();
        for (int i = 0; i < children.size(); i++) {
            DefaultPort port = (DefaultPort) children.get(i);
            Set edges = port.getEdges();
            Object[] edgeArray = edges.toArray(new Object[edges.size()]);
            for (int j = 0; j < edgeArray.length; j++) {
                DefaultEdge edge = (DefaultEdge) edgeArray[j];
                if (!((DefaultPort) edge.getSource()).getParent().equals(vertex)) result.add((DefaultGraphCell) ((DefaultPort) edge.getSource()).getParent());
            }
        }
        result.remove(vertex);
        return result;
    }

    public static ArrayList<DefaultGraphCell> getTargetDescendants(DefaultGraphCell vertex) {
        ArrayList<DefaultGraphCell> result = getTargetVertex(vertex);
        ArrayList<DefaultGraphCell> descendants = new ArrayList<DefaultGraphCell>();
        for (int i = 0; i < result.size(); i++) {
            descendants.addAll(getTargetDescendants(result.get(i)));
        }
        result.addAll(descendants);
        return result;
    }

    public static ArrayList<DefaultGraphCell> getAncestors(DefaultGraphCell vertex) {
        ArrayList<DefaultGraphCell> result = getSourceVertex(vertex);
        ArrayList<DefaultGraphCell> ancestor = new ArrayList<DefaultGraphCell>();
        for (int i = 0; i < result.size(); i++) {
            ancestor.addAll(getAncestors(result.get(i)));
        }
        result.addAll(ancestor);
        return result;
    }

    public static ArrayList<DefaultGraphCell> getAncestorBranchExcludingSelf(ArrayList<DefaultGraphCell> all_vertex, DefaultGraphCell vertex) {
        ArrayList<DefaultGraphCell> result = new ArrayList<DefaultGraphCell>();
        result.addAll(all_vertex);
        ArrayList<DefaultGraphCell> descendants = getTargetDescendants(vertex);
        result.removeAll(descendants);
        result.remove(vertex);
        return result;
    }
}
