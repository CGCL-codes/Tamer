package net.sf.thyvin.node;

import java.util.Collection;

/**
 * A simple node in the board.  For chess this would be one
 * of the squares, where the pieces are placed on.
 *
 * The meaning of the coordinates are game dependent and IDs
 * are not required to be related to coordinates.  A given
 * ID must be unique among all nodes in the same {@link INodeMap}
 * (or any other collection they appear in) and positive (or zero).
 *
 * Nodes from two different games or two different sessions of the
 * same game should generally not be intermixed.
 *
 * Self-edges are not allowed, meaning that the neighbours of a node
 * cannot include the node itself.
 */
public interface INode {

    /**
	 * @return An unmodifiable collection of the IDs of neighbour nodes.
	 */
    public Collection<Integer> getNeighbourIds();

    /**
	 * @return The id of this node.
	 */
    public int getId();

    /**
	 * @return The X-coordinate of the node.
	 */
    public int getX();

    /**
	 * @return The Y-coordinate of the node.
	 */
    public int getY();
}
