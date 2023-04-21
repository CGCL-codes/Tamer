package com.sptci.rwt.webui.tree;

import com.sptci.rwt.MetaData;

/**
 * A {@link echopointng.tree.TreeNode} that represents a leaf in a tree.
 *
 * <p>&copy; Copyright 2007 Sans Pareil Technologies, Inc.</p>
 * @author Rakesh Vidyadharan 2007-09-28
 * @version $Id: LeafNode.java 2 2007-10-19 21:06:36Z rakesh.vidyadharan $
 */
public class LeafNode<S extends MetaData> extends AbstractNode<S> {

    /**
   * Create a new tree node using the specified metadata object.  The
   * object represents a column in the database.
   *
   * @param metadata The metadata object to use as the model for this node.
   */
    public LeafNode(final S metadata) {
        super(metadata);
    }

    /**
   * Determine whether this node holds children or not.  Over-ridden
   * to always indicate that there are no children.
   *
   * @return Returns <code>true</code> if the receiver is a leaf.  Always
   *   returns <code>true</code>.
   */
    @Override
    public boolean isLeaf() {
        return true;
    }

    /**
   * Create the child nodes for this node.  Not applicable.
   */
    @Override
    protected void createChildren() {
    }
}
