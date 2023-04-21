package com.lts.util.tree;

/**
 * A class that provides "do nothing" implementations for all the methods required 
 * by the {@link TreeListener} interface.
 * 
 * @author cnh
 */
public class TreeAdaptor<N extends TreeNode> implements TreeListener<N> {

    @Override
    public void allChanged() {
    }

    @Override
    public void nodeAdded(N parent, N child) {
    }

    @Override
    public void nodeChanged(N node) {
    }

    @Override
    public void nodeRemoved(N parent, N child) {
    }
}
