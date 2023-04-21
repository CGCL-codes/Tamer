package org.helianto.core.service;

import java.util.List;
import org.helianto.core.Node;

/**
 * Interface to create and retrive a tree from
 * a list of <code>Node</code>s.
 * 
 * @author Mauricio Fernandes de Castro
 */
public interface TreeBuilder {

    /**
	 * Build a tree of <code>Node</code>s.
	 * 
	 * @param root
	 */
    public void buildTree(Node root);

    /**
	 * List the tree.
	 */
    public List<Node> getTree();
}
