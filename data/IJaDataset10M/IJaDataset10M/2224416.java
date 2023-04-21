package ru.amse.baltijsky.javascheme.nodeshape;

/**
 * A node that can be visualized using its node traits.
 */
public interface IVisualizableNode {

    /**
     * Gets visual traits for the node.
     *
     * @return visual traits
     */
    public NodeVisualTraits getVisualTraits();
}
