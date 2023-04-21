package be.vds.jtbdive.client.view.core.logbook.material;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.core.material.MaterialTypable;

public class MaterialTreePanel extends DetailPanel {

    private static final long serialVersionUID = -1272164829412136704L;

    private MaterialTree materialTree;

    private JButton collapseButton;

    private JButton expandButton;

    public MaterialTreePanel() {
        init();
    }

    private void init() {
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        this.add(createNorthPanel(), BorderLayout.NORTH);
        this.add(createTree(), BorderLayout.CENTER);
    }

    private Component createNorthPanel() {
        JPanel buttonHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
        buttonHeader.setOpaque(false);
        Dimension d = new Dimension(18, 18);
        collapseButton = new JButton(new AbstractAction(null, UIAgent.getInstance().getIcon(UIAgent.ICON_BTN_COLLAPSE_ALL_16)) {

            private static final long serialVersionUID = 7592442717029155431L;

            @Override
            public void actionPerformed(ActionEvent e) {
                collapseTree();
            }
        });
        collapseButton.setBorderPainted(false);
        collapseButton.setContentAreaFilled(false);
        collapseButton.setText(null);
        collapseButton.setPreferredSize(d);
        collapseButton.setEnabled(false);
        buttonHeader.add(collapseButton);
        expandButton = new JButton(new AbstractAction(null, UIAgent.getInstance().getIcon(UIAgent.ICON_BTN_EXPAND_ALL_16)) {

            private static final long serialVersionUID = 1990893338485891500L;

            @Override
            public void actionPerformed(ActionEvent e) {
                expandTree();
            }
        });
        expandButton.setBorderPainted(false);
        expandButton.setContentAreaFilled(false);
        expandButton.setText(null);
        expandButton.setPreferredSize(d);
        expandButton.setEnabled(false);
        buttonHeader.add(expandButton);
        return buttonHeader;
    }

    private Component createTree() {
        materialTree = new MaterialTree();
        JScrollPane scroll = new JScrollPane(materialTree);
        return scroll;
    }

    public void addTreeSelectionListener(TreeSelectionListener treeSelectionListener) {
        materialTree.addTreeSelectionListener(treeSelectionListener);
    }

    public Material getSelectedMaterial() {
        TreePath path = materialTree.getSelectionPath();
        if (null == path) return null;
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (node != null && node.getUserObject() instanceof Material) {
            return (Material) node.getUserObject();
        }
        return null;
    }

    public void removeMaterial(Material material) {
        materialTree.removeMaterial(material);
        adaptExpandButtons(materialTree.hasElements());
    }

    public void replaceMaterial(Material oldValue, Material newValue) {
        materialTree.replaceMaterial(oldValue, newValue);
    }

    public void clear() {
        materialTree.clear();
        adaptExpandButtons(materialTree.hasElements());
    }

    private void adaptExpandButtons(boolean b) {
        expandButton.setEnabled(b);
        collapseButton.setEnabled(b);
    }

    public void addMaterial(MaterialTypable materialTypable, boolean scrollPathToVisible) {
        materialTree.addMaterial(materialTypable, scrollPathToVisible);
        adaptExpandButtons(materialTree.hasElements());
    }

    public void expandTree() {
        materialTree.expandAll();
    }

    public void collapseTree() {
        materialTree.collapseAll();
    }

    public DefaultMutableTreeNode getSelectedNode() {
        return (DefaultMutableTreeNode) materialTree.getLeadSelectionPath().getLastPathComponent();
    }
}
