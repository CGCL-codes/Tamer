package de.fhdarmstadt.fbi.dtree.model;

/**
 * A classification node is a leaf node in the decision tree. All data
 * reaching such a node will inherit the classification contained in this
 * node.
 */
public final class ClassificationNode extends DTreeNode {

    /** The classification assigned to this node. */
    private Classification classification;

    /**
   * Creates a new classification node.
   *
   * @param classification the classification, never null
   * @throws NullPointerException if the given classification is null.
   */
    public ClassificationNode(final Classification classification) {
        if (classification == null) {
            throw new NullPointerException();
        }
        this.classification = classification;
    }

    /**
   * Returns the classification for this node.
   *
   * @return the classification for this node.
   */
    public final Classification getClassification() {
        return classification;
    }

    /**
   * Returns true to indicate that this node is a leaf node.
   * @return true.
   */
    public final boolean isLeaf() {
        return true;
    }

    /**
   * Returns a string representation of this node.
   * @return a string describing this node.
   */
    public final String toString() {
        return "ClassificationNode={classification=" + getClassification() + "}";
    }
}
