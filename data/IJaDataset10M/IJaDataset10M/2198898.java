package treemap;

import java.util.Enumeration;

/**
 * The TMNode interface should be implemented by 
 * object that are node of the tree that want to be 
 * displayed in the TreeMap.
 * <P>
 * If you have already a tree structure, just implements
 * this interface in node of the tree.
 */
public interface TMNode {

    /**
     * Returns the children of this node
     * in an Enumeration.
     * If this object does not have children,
     * it should return an empty Enumeration,
     * not <CODE>null</CODE>.
     * All objects contained in the Enumeration
     * should implements TMNode.
     *
     * @return    an Enumeration containing childs of this node
     */
    public Enumeration children();

    /**
     * Checks if this node is a leaf or not.
     * A node could have no children and still not
     * be a leaf.
     *
     * @return    <CODE>true</CODE> if this node is a leaf;
     *            <CODE>false</CODE> otherwise
     */
    public boolean isLeaf();

    /**
     * Called by the TMUpdater constructor. 
     * Gives to this node a reference to a TMUpdater object.
     * This node should use this reference
     * to notify treemap that something has changed.
     * See the differents update methods of the TMUpdater interface.
     * <P>
     * As this method is called by the constructor
     * of TMUpdater, don't call methods of TMUpdater
     * in this method.
     *
     * @param updater    the TMUpdater to be called when something has changed
     */
    public void setUpdater(TMUpdater updater);
}
