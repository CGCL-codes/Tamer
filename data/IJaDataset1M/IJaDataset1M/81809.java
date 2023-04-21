package net.sourceforge.combean.samples.simplegraphs;

import java.util.Iterator;
import net.sourceforge.combean.graph.NumberNode;
import net.sourceforge.combean.interfaces.graph.Graph;
import net.sourceforge.combean.interfaces.graph.prop.NumberedNodesGraphProp;
import net.sourceforge.combean.interfaces.graph.prop.OutgoingNeighborhoodGraphProp;
import net.sourceforge.combean.interfaces.graph.prop.IncidentNeighborhoodGraphProp;
import net.sourceforge.combean.util.Twins;
import net.sourceforge.combean.util.except.IllegalParameterException;

/**
 * This class represents a graph where every node is identified by a long-value.
 * 
 * @author schickin
 *
 */
public abstract class NumberGraph implements Graph<NumberNode, Twins<NumberNode>>, IncidentNeighborhoodGraphProp<NumberNode, Twins<NumberNode>>, OutgoingNeighborhoodGraphProp<NumberNode, Twins<NumberNode>>, NumberedNodesGraphProp<NumberNode, Twins<NumberNode>> {

    /**
	 * The number of the first node in the graph.
	 */
    public static final int FIRSTNODE = 0;

    /**
	 * The virtual node number if no such node exists.
	 */
    public static final int NOSUCHNODE = -1;

    /**
	 *
	 */
    public NumberGraph() {
        super();
    }

    /**
	 * @see net.sourceforge.combean.interfaces.graph.prop.GlobalNodesGraphProp#getAllNodesIterator()
	 */
    public Iterator<NumberNode> getAllNodesIterator() {
        return new NumberNodeIterator(FIRSTNODE, getNumNodes() - 1);
    }

    public NumberNode getNode(int index) {
        if (index < 0 || index >= getNumNodes()) {
            throw new IllegalParameterException("node index out of bounds. " + "must be in interval [0..number of nodes-1]");
        }
        return new NumberNode(index);
    }

    public int getNodeNumber(NumberNode v) {
        return ((NumberNode) v).getNodeNum();
    }

    /**
	 * Create a node in the graph.given its number
	 * 
	 * @param num the number of the node to be created
	 * @return the node with the given number
	 * or null if the node does not exist in the graph or num is NOSUCHNODE.
	 */
    public final NumberNode convertNumToNode(int num) {
        NumberNode result = new NumberNode(num);
        if (!contains(result)) {
            result = null;
        }
        return result;
    }

    public final Iterator<Twins<NumberNode>> getIncidentEdges(NumberNode v) {
        return new NumberNeighborIterator(this, (NumberNode) v);
    }

    public Iterator<Twins<NumberNode>> getOutgoingEdges(NumberNode v) {
        return getIncidentEdges(v);
    }

    public final NumberNode getFirstNode(Twins<NumberNode> e) {
        return e.getFirst();
    }

    public final NumberNode getSecondNode(Twins<NumberNode> e) {
        return e.getSecond();
    }

    public final NumberNode getOtherNode(Twins<NumberNode> e, NumberNode v) {
        if (e.getFirst().equals(v)) {
            return e.getSecond();
        }
        return e.getFirst();
    }

    /**
	 * Helps NumberNodeIterators to calculate the next node when iterating
	 * through neighbors.
	 * 
	 * @param startNode the start node of the NumberNeighborIterator
	 * @param currNode the current node of the NumberNeighborIterator or null is this
	 * is the first iteration
	 * @return the next node of the NumberNeighborIterator or
	 * NOSUCHNODE if no next node exists
	 */
    protected int nextNode(NumberNode startNode, NumberNode currNode) {
        if (empty() || !contains(startNode)) {
            return NOSUCHNODE;
        }
        int currNodeNum = 0;
        if (currNode != null) {
            currNodeNum = currNode.getNodeNum() + 1;
        }
        return calcNextNode(startNode.getNodeNum(), currNodeNum);
    }

    /**
	 * Template method for calculating neighbors.
	 * 
	 * Calculates the smallest number >= minNodeNum of a node in the neighborhood of
	 * the node with the number sourceNumNode or NOSUCHNODE if no next node exists.
	 * It is guaranteed that the graph in non-empty and that the node number startNodeNum
	 * is contained in the graph.
	 * Note that minNodeNum may also be negative.
	 * 
	 * @param sourceNodeNum
	 * @param minNodeNum
	 * @return the next node in the neighborhood of the given node
	 * @see net.sourceforge.combean.samples.simplegraphs.NumberGraph#nextNode(NumberNode, NumberNode)
	 */
    protected abstract int calcNextNode(int sourceNodeNum, int minNodeNum);

    /**
	 * Check whether a given node is contained in the graph.
	 * 
	 * @param v the node to be checked
	 * @return true if v is contained in the graph
	 */
    public final boolean contains(NumberNode v) {
        return FIRSTNODE <= v.getNodeNum() && v.getNodeNum() < getNumNodes();
    }

    /**
	 * Check if the graph is empty.
	 * 
	 * @return true if the graph does not contain any node.
	 */
    public final boolean empty() {
        return getNumNodes() == 0;
    }
}
