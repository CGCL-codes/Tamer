package com.patientis.framework.controls.tree;

import java.util.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import com.patientis.framework.concurrency.SwingWorker;

/**
 * ISTreeLoader registers listeners on a tree to dynamically populate
 * the tree in a background thread as the tree is expanded.
 *
 * Design Patterns: <a href="/functionality/rm/1000059.html">Trees</a>
 * <br/>
 */
public class ISTreeLoader {

    /**
		 * Tree being loaded
		 */
    private ISTree tree = null;

    /**
		 * Tree Model being loaded
		 */
    private ISTreeModel treeModel = null;

    /**
		 * Interface to populate tree methods
		 */
    private IPopulateTree populateTree = null;

    /**
		 * Create the tree loader.
		 * 
		 * @param populateTree interface to get the child nodes
		 * @param treeModel tree model
		 * @param tree tree to populate
		 * @throws Exception
		 */
    public ISTreeLoader(final ISTree tree, final ISTreeModel treeModel, final IPopulateTree populateTree) throws Exception {
        this.tree = tree;
        this.treeModel = treeModel;
        this.populateTree = populateTree;
    }

    /**
		 * Adds the listeners and initalize the load
		 */
    public void initialize() throws Exception {
        if (treeModel != null && tree != null) {
            addListeners();
            initialLoad();
            this.tree.setModel(treeModel);
        }
    }

    /**
		 * Add listeners to dynamically trigger loading tree
		 */
    private void addListeners() {
        if (this.tree != null) {
            this.tree.addTreeExpansionListener(new TreeExpansionListener() {

                public void treeCollapsed(TreeExpansionEvent event) {
                }

                public void treeExpanded(TreeExpansionEvent e) {
                    if (e.getPath() != null) {
                        if (e.getPath().getLastPathComponent() instanceof ISTreeNode) {
                            ISTreeNode treeNode = (ISTreeNode) e.getPath().getLastPathComponent();
                            if (populateTree.hasChildNodes(treeNode)) {
                                backgroundLoad(treeNode);
                            }
                        }
                    }
                }
            });
        }
    }

    /**
		 * Load the top level of children
		 * 
		 * @throws Exception
		 */
    private void initialLoad() throws Exception {
        Enumeration e1 = ((ISTreeNode) treeModel.getRoot()).breadthFirstEnumeration();
        while (e1.hasMoreElements()) {
            ISTreeNode node = (ISTreeNode) e1.nextElement();
            if (node.getChildCount() == 0) {
                loadChildren(node, false);
            }
        }
    }

    /**
		 * Load the children of the parent node
		 * 
		 * @param parentNode node to load children on
		 */
    private void backgroundLoad(final ISTreeNode parentNode) {
        SwingWorker sw = new SwingWorker(tree.getRootPane()) {

            @Override
            protected void doNonUILogic() throws Exception {
                Enumeration e1 = parentNode.children();
                while (e1.hasMoreElements()) {
                    ISTreeNode node = (ISTreeNode) e1.nextElement();
                    if (node.getChildCount() == 0) {
                        loadChildren(node, false);
                    }
                }
            }

            @Override
            protected void doUIUpdateLogic() throws Exception {
            }
        };
        sw.start();
    }

    /**
		 * Load the children of the parent node and optionally load the next level
		 * down as well to increase the speed of opening a child node. 
		 * 
		 * @param parent parent node
		 * @param loadNextLevelAsWell if true single recursive call
		 * @throws Exception
		 */
    public synchronized void loadChildren(final ISTreeNode parent, boolean loadNextLevelAsWell) throws Exception {
        if (parent != null && populateTree.hasChildNodes(parent)) {
            List<ISTreeNode> nodes = populateTree.getChildNodes(parent);
            final int[] childIndicies = new int[nodes.size()];
            int i = 0;
            for (final ISTreeNode node : nodes) {
                parent.add(node);
                childIndicies[i] = i;
                i++;
            }
            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    treeModel.nodesWereInserted(parent, childIndicies);
                }
            });
            Thread.yield();
            if (loadNextLevelAsWell) {
                for (ISTreeNode node : nodes) {
                    loadChildren(node, false);
                }
            }
        }
    }
}
