package de.iteratec.graph;

/**
 * A simple interface for nodes.
 * 
 * These nodes extend the standard notion of a mathematical graph by having
 * coordinates and a size in 2-dimensional space. This is used by the layouting
 * algorithms found in the {@link de.iteratec.graph.layout} package.
 */
public interface Node {

    /**
   * A string that uniquely identifies the node.
   */
    String getId();

    /**
   * The x coordinate the node is located.
   * 
   * This is assumed to be the center of the node.
   */
    double getX();

    /**
   * The y coordinate the node is located.
   * 
   * This is assumed to be the center of the node.
   */
    double getY();

    /**
   * Moves the node to a new position, if it is movable (see {@link #setMovable(boolean)}).
   */
    void setPosition(double x, double y);

    /**
   * The horizontal extension of the node. 
   */
    double getWidth();

    /**
   * The vertical extension of the node. 
   */
    double getHeight();

    /**
   * Marks whether the node can be assigned a new position or not.
   */
    void setMovable(boolean movable);

    /**
   * Whether the node is movable or not.
   */
    boolean isMovable();
}
