package org.mbari.vars.knowledgebase.ui;

import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * @author achase
 *
 *
 */
public class ConceptTreeLazyLoader implements TreeExpansionListener {

    private final DefaultTreeModel treeModel;

    /**
     * Constructs ...
     *
     *
     * @param model
     */
    public ConceptTreeLazyLoader(DefaultTreeModel model) {
        treeModel = model;
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param event
     */
    public void treeCollapsed(TreeExpansionEvent event) {
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param event
     */
    public void treeExpanded(final TreeExpansionEvent event) {
        final DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
        final TreeConcept treeConcept = (TreeConcept) node.getUserObject();
        Thread lazyLoader = new Thread() {

            public void run() {
                if ((treeConcept != null) && treeConcept.lazyExpand(node)) {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            treeModel.reload(node);
                        }
                    });
                }
            }
        };
        lazyLoader.start();
    }
}
