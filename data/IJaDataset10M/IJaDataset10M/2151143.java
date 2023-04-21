package org.ct42.r42.structure;

/**
 * A node in the scene tree.
 * 
 * @author cthiele
 */
public interface Node {

    /**
	 * Set the name of this node. '/' are not allowed in nodes name.
	 * @param name to set.
	 */
    public void setName(String name);

    /**
	 * Get the name of this node.
	 * @return the name.
	 */
    public String getName();

    /**
	 * Return <code>true</code> while the children of this node are visible for
	 * the renderer, <code>false</code> otherwise.
	 * @return the visibility state for this node and its children.
	 */
    public boolean isVisible();

    /**
	 * Get the parent node of this node.
	 * @return the parent node or <code>null</code> while this node has no parent.
	 */
    public Node getParent();

    /**
	 * Moves this node and all children of this node.
	 * @param timepoint the point of the timeline in seconds.
	 */
    public void animate(double timepoint);
}
