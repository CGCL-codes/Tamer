package com.sri.emo.controller;

import com.jcorporate.expresso.core.controller.ExpressoResponse;
import com.sri.common.taglib.TreeNode;
import com.sri.emo.dbobj.Node;
import com.sri.emo.dbobj.model_tree.DefaultModelFactory;
import com.sri.emo.dbobj.model_tree.Model;

/**
 * A factory for building the data structure needed for a tree view.  It does
 * so by first building a 'tree' model of the database node, and then using
 * a visitor to transform it into something useful for the TreeView
 * control.
 *
 * @author Michael Rimov
 */
public class TreeViewFactory {

    /**
     * Root node instance to the hierarchy.
     */
    private final Node root;

    /**
     * ControllerResponse used to set for Transitions.
     */
    private final ExpressoResponse response;

    /**
     * Maximum levels to recurse.
     */
    private final int MAX_LEVELS;

    /**
     * Constructs a tree view factory.
     *
     * @param rootNode         Node
     * @param response         ControllerResponse
     * @param maxNestingLevels int
     */
    public TreeViewFactory(final Node rootNode, final ExpressoResponse response, int maxNestingLevels) {
        super();
        assert maxNestingLevels > 0;
        assert rootNode != null;
        assert response != null;
        MAX_LEVELS = maxNestingLevels;
        this.response = response;
        root = rootNode;
    }

    /**
     * Builds a Tree for the Tree node.  It is the callers responsibility to
     * save the Nodes to the request, session or wherever.
     *
     * @return TreeNode the tree node.
     */
    public TreeNode buildTree() {
        DefaultModelFactory modelFactory = new DefaultModelFactory(root, response, MAX_LEVELS);
        Model model = modelFactory.buildModel();
        TreeViewVisitor visitor = new TreeViewVisitor(response);
        return visitor.traverseModelTree(model);
    }
}
