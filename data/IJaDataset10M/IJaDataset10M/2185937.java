package net.sf.rcpforms.widgetwrapper.wrapper.advanced.tree;

public interface ITreeBuilder<NODE_TYPE extends TreeNode3> {

    /**
	 * @return recursively created Node, every node receives children, if
	 *   object type is supported for multiple sub-objects (by this treebuilder)
	 */
    public NODE_TYPE createNode(NODE_TYPE parent, Object object);

    /**
	
	 * @return non-recursivly creates a new node
	 */
    public NODE_TYPE newNode(final NODE_TYPE parent, final Object object);

    /**
	 * @return array of recursively created Nodes, every node receives children, if
	 *   its object type is supported for multiple sub-objects (by this treebuilder)
	 */
    public NODE_TYPE[] createNodes(NODE_TYPE parent, Object... objects);

    /**
	 * 
	 * @return non-recursively creates a new array of new nodes
	 */
    public NODE_TYPE[] newArray(final NODE_TYPE parent, final Object... objects);

    public boolean needsRefreshOnProperty(String propertyName);
}
