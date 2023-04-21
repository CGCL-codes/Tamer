package org.vikamine.swing.subgroup.visualization.graph;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.vikamine.kernel.subgroup.SG;
import org.vikamine.kernel.subgroup.SGDescription;
import org.vikamine.kernel.subgroup.selectors.SGNominalSelector;

/**
 * @author Tobias Vogele
 */
public class SGNode {

    private SG subgroup;

    private List fromEdges = new LinkedList();

    private List toEdges = new LinkedList();

    private int level = 0;

    public SGNode() {
        super();
    }

    public List getFromEdges() {
        return fromEdges;
    }

    public void setFromEdges(List fromEdges) {
        this.fromEdges = fromEdges;
    }

    public SG getSubgroup() {
        return subgroup;
    }

    public void setSubgroup(SG subgroup) {
        this.subgroup = subgroup;
    }

    public List getToEdges() {
        return toEdges;
    }

    public void setToEdges(List toEdges) {
        this.toEdges = toEdges;
    }

    public SGEdge connectTo(SGNode toNode) {
        SGEdge edge = new SGEdge(this, toNode);
        getFromEdges().add(edge);
        toNode.getToEdges().add(edge);
        return edge;
    }

    public boolean isConnectedTo(SGNode toNode) {
        for (Iterator iter = getFromEdges().iterator(); iter.hasNext(); ) {
            SGEdge edge = (SGEdge) iter.next();
            assert (edge.getFromNode() == this);
            if (edge.getToNode() == toNode) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return String.valueOf(subgroup);
    }

    public SGNode(SG subgroup) {
        this.subgroup = subgroup;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (o == this) {
            return true;
        } else if (o.getClass() != getClass()) {
            return false;
        } else {
            SGNode n = (SGNode) o;
            if (subgroup == null) {
                return n.subgroup == null;
            } else {
                return subgroup.equals(n.subgroup);
            }
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * returns true, of this Node is a SuperNode of subNode, which means, if the
     * SGDescription of this node is a superset of the SGDescription of subNode;
     * 
     * @param subNode
     * @return
     */
    public boolean isSuperNode(SGNode subNode) {
        SGDescription superDesc = getSubgroup().getSGDescription();
        for (Iterator iter = subNode.getSubgroup().getSGDescription().iterator(); iter.hasNext(); ) {
            SGNominalSelector selector = (SGNominalSelector) iter.next();
            if (!superDesc.contains(selector)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true, if there is a line of edges from this node to toNode.
     * <p>
     * Note, that this method can result in infinite loops, if there are any
     * circles in the graph.
     * 
     * @param toNode
     * @return
     */
    public boolean isIndirectlyConnectedTo(SGNode toNode) {
        if (this == toNode) {
            return true;
        }
        for (Iterator iter = fromEdges.iterator(); iter.hasNext(); ) {
            SGEdge edge = (SGEdge) iter.next();
            if (edge.getToNode().isIndirectlyConnectedTo(toNode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param i
     */
    public void setLevel(int i) {
        level = i;
    }
}
