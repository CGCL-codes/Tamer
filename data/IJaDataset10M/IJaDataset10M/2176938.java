package org.fudaa.fudaa.crue.common.helper;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import org.apache.commons.lang.ArrayUtils;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.BeanTreeView;
import org.openide.nodes.Node;

/**
 * Utiliser pour selectionner un noeud.
 *
 * @author deniger ( genesis)
 */
public class DisplayerView extends JPanel implements ExplorerManager.Provider {

    private ExplorerManager em = new ExplorerManager();

    public DisplayerView(Node rootNode) {
        setLayout(new BorderLayout());
        em.setRootContext(rootNode);
    }

    public void expandAll(BeanTreeView view) {
        expandAll(view, em.getRootContext());
    }

    private void expandAll(BeanTreeView view, Node node) {
        Node[] children = node.getChildren().getNodes();
        if (ArrayUtils.isNotEmpty(children)) {
            for (Node child : children) {
                view.expandNode(child);
                expandAll(view, child);
            }
        }
    }

    public BeanTreeView installTreeView(boolean rootVisible) {
        final BeanTreeView beanTreeView = new BeanTreeView();
        beanTreeView.setRootVisible(rootVisible);
        add(beanTreeView);
        return beanTreeView;
    }

    public Node getSelectedNode() {
        Node[] selectedNodes = em.getSelectedNodes();
        if (selectedNodes != null && selectedNodes.length == 1) {
            return selectedNodes[0];
        }
        return null;
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }
}
