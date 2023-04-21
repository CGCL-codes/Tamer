package org.carabiner.infopanel;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 * An InfoPanel that looks for common GUI design flaws and show the component
 * hierarchy with appropriate warnings.
 * 
 * FIXME add event listener support to this panel
 * 
 * <p>
 * Copyright: <a href="http://www.gnu.org/licenses/gpl.html">GNU Public License</a>
 * </p>
 * 
 * @author Ben Rady (benrady@gmail.com)
 * 
 */
public class InspectorInfoPanel extends JPanel implements AWTEventListener {

    private Inspector inspector;

    private JTree tree;

    public InspectorInfoPanel(Inspector inspectorWithRules) {
        Toolkit.getDefaultToolkit().addAWTEventListener(this, Long.MAX_VALUE);
        inspector = inspectorWithRules;
        setLayout(new BorderLayout());
        init();
    }

    private void init() {
        tree = new JTree(inspector.getTreeModel());
        tree.setCellRenderer(new IconNodeRenderer());
        TreeSelectionListener treeListener = new ComponentWarningTreeSelectionListener(inspector.getComponent());
        tree.addTreeSelectionListener(treeListener);
        tree.addTreeSelectionListener(new ComponentSelectionListener());
        checkForExpansion((CarabinerComponentNode) tree.getModel().getRoot());
        add(new JScrollPane(tree), BorderLayout.CENTER);
        revalidate();
    }

    private void checkForExpansion(CarabinerComponentNode node) {
        if (node.autoExpand()) {
            tree.expandPath(inspector.getPathToRoot(node));
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            CarabinerComponentNode child = (CarabinerComponentNode) node.getChildAt(i);
            checkForExpansion(child);
        }
    }

    JTree getTree() {
        return tree;
    }

    public void eventDispatched(AWTEvent event) {
        inspector.addEvent(event);
    }

    private class ComponentSelectionListener implements TreeSelectionListener {

        public void valueChanged(TreeSelectionEvent e) {
            if (tree.isSelectionEmpty()) inspector.setSelectedComponent(null);
            CarabinerComponentNode node = (CarabinerComponentNode) tree.getSelectionPath().getLastPathComponent();
            inspector.setSelectedComponent(node.getComponent());
        }
    }
}
