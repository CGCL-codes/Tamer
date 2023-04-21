package cz.cuni.mff.ksi.jinfer.functionalDependencies;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Node;

/**
 * Class representing tree tuple.
 * @author sviro
 */
public class Tuple {

    private final int tupleId;

    private final RXMLTree tree;

    private List<Node> nodes = null;

    /**
   * Constructor of tuple for particular tree, with provided ID.
   * @param tree Tree for which is tuple created.
   * @param tupleId Tuple ID.
   */
    public Tuple(final RXMLTree tree, final int tupleId) {
        this.tree = tree;
        this.tupleId = tupleId;
    }

    /**
   * Get ID of this tuple.
   * @return ID of this tuple.
   */
    public int getId() {
        return tupleId;
    }

    /**
   * Check if this tuple contains all nodes from provided tuple.
   * @param tuple Tuple to be check if is subset of this tuple.
   * @return true if this tuple contains all nodes from provided tuple.
   */
    public boolean contains(final Tuple tuple) {
        final List<Node> tupleNodes = tuple.getNodes();
        if (this.getNodes().containsAll(tupleNodes)) {
            return true;
        }
        return false;
    }

    /**
   * Get list of all nodes contained in this tuple.
   * @return List of all nodes contained in this tuple. 
   */
    public List<Node> getNodes() {
        if (nodes == null) {
            nodes = computeNodes();
        }
        return nodes;
    }

    private List<Node> computeNodes() {
        final List<Node> result = new ArrayList<Node>();
        final Map<Node, NodeAttribute> nodesMap = tree.getNodesMap();
        for (Node node : nodesMap.keySet()) {
            if (nodesMap.get(node).isInTuple(this)) {
                result.add(node);
            }
        }
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof Tuple)) {
            return false;
        }
        final Tuple tuple = (Tuple) obj;
        return this.getId() == tuple.getId();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this.tupleId;
        hash = 89 * hash + (this.tree != null ? this.tree.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Tuple:" + tupleId;
    }
}
