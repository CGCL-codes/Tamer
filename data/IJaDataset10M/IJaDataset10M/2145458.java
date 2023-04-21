package ru.amse.baltijsky.javascheme.tree;

/**
 * A node with a label bound to it that can be used in a labeled break or continue statement enclosed by this node.
 */
public interface ILabeledNode {

    /**
     * Getter for the label.
     *
     * @return node label
     */
    String getLabel();

    /**
     * Setter fpr the label.
     *
     * @param label the new label value
     */
    void setLabel(String label);
}
