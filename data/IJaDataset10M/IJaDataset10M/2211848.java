package org.eclipse.core.internal.databinding.observable.tree;

/**
 * Describes the difference between two trees as a tree of tree diff nodes.
 * 
 * @since 1.1
 * 
 */
public abstract class TreeDiff extends TreeDiffNode {

    /**
	 * Returns the tree path (possibly empty) of the parent, or
	 * <code>null</code> if the underlying tree is not lazy and never contains
	 * duplicate elements.
	 * 
	 * @return the tree path (possibly empty) of the unchanged parent, or
	 *         <code>null</code>
	 */
    public abstract TreePath getParentPath();

    /**
	 * @param visitor
	 */
    public void accept(TreeDiffVisitor visitor) {
        doAccept(visitor, getParentPath());
    }
}
