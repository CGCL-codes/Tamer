package com.parfumball;

import org.eclipse.core.runtime.IAdaptable;

/**
 * 
 * Represents a simple object (a node) in a tree.
 *  
 * @author prasanna
 */
public class TreeObject implements IAdaptable {

    /**
     * The object for which this node is the UI.
     */
    protected Object payload;

    /**
	 * The parent for this node.
	 */
    private TreeParent parent;

    /**
	 * Create a new TreeObject for the given payload.
	 * 
	 * @param payload
	 */
    public TreeObject(Object payload) {
        this.payload = payload;
    }

    /**
	 * Get the name of this node. Returns the String representation
	 * of the underlying payload.
	 * 
	 * @return
	 */
    public String getName() {
        return payload.toString();
    }

    /**
	 * Set the parent for this node.
	 * 
	 * @param parent
	 */
    public void setParent(TreeParent parent) {
        this.parent = parent;
    }

    /**
	 * Returns the parent node for this node.
	 * 
	 * @return
	 */
    public TreeParent getParent() {
        return parent;
    }

    /**
	 * Returns the String representation of the underlying payload. 
	 */
    public String toString() {
        return getName();
    }

    /**
	 * Returns the underlying payload.
	 * 
	 * @return
	 */
    public Object getPayload() {
        return payload;
    }

    /**
	 * Always returns null.
	 */
    public Object getAdapter(Class key) {
        return null;
    }
}
