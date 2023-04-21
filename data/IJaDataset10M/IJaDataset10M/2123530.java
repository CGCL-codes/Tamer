package com.touchgraph.graphlayout.graphelements;

import java.util.Hashtable;
import java.util.Vector;
import com.touchgraph.graphlayout.Edge;
import com.touchgraph.graphlayout.Node;

/**
 * GESUtils is a set of functions that return information about a GraphEltSet
 * 
 * @author Alexander Shapiro
 * @version 1.22-jre1.1 $Id: GESUtils.java,v 1.1 2003/05/05 01:25:44 sauerf Exp $
 */
public class GESUtils {

    /**
	 * Returns a hashtable of Node-Integer pairs, where integers are the minimum distance from the focusNode to any given Node, and the Nodes in the Hashtable are all within radius distance from the focusNode.
	 * 
	 * Nodes with edge degrees of more then maxAddEdgeCount are not added to the hashtable, and those with edge degrees of more then maxExpandEdgeCount are added but not expanded.
	 * 
	 * If unidirectional is set to true, then edges are only follewed in the forward direction.
	 * 
	 */
    public static Hashtable<Node, Integer> calculateDistances(GraphEltSet ges, Node focusNode, int radius, int maxAddEdgeCount, int maxExpandEdgeCount, boolean unidirectional) {
        Hashtable<Node, Integer> distHash = new Hashtable<Node, Integer>();
        distHash.put(focusNode, Integer.valueOf(0));
        TGNodeQueue nodeQ = new TGNodeQueue();
        nodeQ.push(focusNode);
        while (!nodeQ.isEmpty()) {
            Node n = nodeQ.pop();
            int currDist = (distHash.get(n)).intValue();
            if (currDist >= radius) {
                break;
            }
            for (int i = 0; i < n.edgeCount(); i++) {
                Edge e = n.edgeAt(i);
                if (n != n.edgeAt(i).getFrom() && unidirectional) {
                    continue;
                }
                Node adjNode = e.getOtherEndpt(n);
                if (ges.contains(e) && !distHash.containsKey(adjNode) && adjNode.edgeCount() <= maxAddEdgeCount) {
                    if (adjNode.edgeCount() <= maxExpandEdgeCount) {
                        nodeQ.push(adjNode);
                    }
                    distHash.put(adjNode, Integer.valueOf(currDist + 1));
                }
            }
        }
        return distHash;
    }

    public static Hashtable<Node, Integer> calculateDistances(GraphEltSet ges, Node focusNode, int radius) {
        return calculateDistances(ges, focusNode, radius, 1000, 1000, false);
    }

    public static Hashtable<Node, Integer> getLargestConnectedSubgraph(GraphEltSet ges) {
        int nodeCount = ges.nodeCount();
        if (nodeCount == 0) {
            return null;
        }
        Vector<Hashtable<Node, Integer>> subgraphVector = new Vector<Hashtable<Node, Integer>>();
        for (int i = 0; i < nodeCount; i++) {
            Node n = ges.nodeAt(i);
            boolean skipNode = false;
            for (int j = 0; j < subgraphVector.size(); j++) {
                if ((subgraphVector.elementAt(j)).contains(n)) {
                    skipNode = true;
                }
            }
            Hashtable<Node, Integer> subgraph = calculateDistances(ges, n, 1000);
            if (subgraph.size() > nodeCount / 2) {
                return subgraph;
            }
            if (!skipNode) {
                subgraphVector.addElement(subgraph);
            }
        }
        int maxSize = 0;
        int maxIndex = 0;
        for (int j = 0; j < subgraphVector.size(); j++) {
            int localSize = subgraphVector.elementAt(j).size();
            if (localSize > maxSize) {
                maxSize = localSize;
                maxIndex = j;
            }
        }
        return subgraphVector.elementAt(maxIndex);
    }
}
