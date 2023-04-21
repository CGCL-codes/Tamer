package org.happy.collections.trees;

/**
 * TreeFactory is a factory class which is used for creating resources for tree, 
 * for example creating TreeIterator or TreeNode
 * @param E type of the data, which is stored inside of the TreeNode
 */
public interface TreeFactory_1x0<E> extends TreeNodeFactory_1x0<E> {

    /**
	 * creates new TreeNode
	 * @param node TreeNode, from which the iterator start the iteration
	 * @return
	 */
    public TreeIterator_1x0<E> createTreeIterator(TreeNode_1x0<E> node, Tree_1x0<E> tree);
}
