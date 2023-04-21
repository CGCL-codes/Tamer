package org.dllearner.algorithms.el;

import org.dllearner.core.owl.ObjectProperty;

/**
 * A (directed) edge in an EL description tree. It consists of an edge
 * label, which is an object property, and the EL description tree
 * the edge points to.
 * 
 * @author Jens Lehmann
 *
 */
public class ELDescriptionEdge {

    private ObjectProperty label;

    private ELDescriptionNode node;

    /**
	 * Constructs and edge given a label and an EL description tree.
	 * @param label The label of this edge.
	 * @param tree The tree the edge points to (edges are directed).
	 */
    public ELDescriptionEdge(ObjectProperty label, ELDescriptionNode tree) {
        this.label = label;
        this.node = tree;
    }

    /**
	 * @param label the label to set
	 */
    public void setLabel(ObjectProperty label) {
        this.label = label;
    }

    /**
	 * @return The label of this edge.
	 */
    public ObjectProperty getLabel() {
        return label;
    }

    /**
	 * @return The EL description tree 
	 */
    public ELDescriptionNode getNode() {
        return node;
    }

    @Override
    public String toString() {
        return "--" + label + "--> " + node.toDescriptionString();
    }
}
