package org.opensourcephysics.controls;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Enumeration;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * This is an XML tree in a scroller.
 *
 * @author Douglas Brown
 */
public class XMLTree {

    protected static Icon hiliteIcon;

    protected XMLTreeNode root;

    protected JTree tree;

    protected JScrollPane scroller;

    protected XMLControl control;

    protected java.util.List selectedProps = new java.util.ArrayList();

    protected Class hilite = Object.class;

    /**
   * Contructs a tree view of an XMLControl
   *
   * @param control the XMLControl
   */
    public XMLTree(XMLControl control) {
        this.control = control;
        createGUI();
    }

    /**
   * Gets the tree.
   *
   * @return the tree
   */
    public JTree getTree() {
        return tree;
    }

    /**
   * Gets the selected xml properties.
   *
   * @return a list of currently selected properties
   */
    public java.util.List getSelectedProperties() {
        selectedProps.clear();
        TreePath[] paths = tree.getSelectionPaths();
        if (paths != null) {
            for (int i = 0; i < paths.length; i++) {
                XMLTreeNode node = (XMLTreeNode) paths[i].getLastPathComponent();
                selectedProps.add(node.getProperty());
            }
        }
        return selectedProps;
    }

    /**
   * Gets the scroll pane with view of the tree
   *
   * @return the scroll pane
   */
    public JScrollPane getScrollPane() {
        return scroller;
    }

    /**
   * Sets the highlighted class.
   *
   * @param type the class to highlight
   */
    public void setHighlightedClass(Class type) {
        if (type != null) {
            hilite = type;
            scroller.repaint();
        }
    }

    /**
   * Gets the highlighted class.
   *
   * @return the highlighted class
   */
    public Class getHighlightedClass() {
        return hilite;
    }

    /**
   * Selects the highlighted properties.
   */
    public void selectHighlightedProperties() {
        Enumeration e = root.breadthFirstEnumeration();
        while (e.hasMoreElements()) {
            XMLTreeNode node = (XMLTreeNode) e.nextElement();
            XMLProperty prop = node.getProperty();
            Class type = prop.getPropertyClass();
            if (type != null && hilite.isAssignableFrom(type)) {
                TreePath path = new TreePath(node.getPath());
                tree.addSelectionPath(path);
                tree.scrollPathToVisible(path);
            }
        }
    }

    /**
   * Shows the highlighted properties.
   */
    public void showHighlightedProperties() {
        Enumeration e = root.breadthFirstEnumeration();
        while (e.hasMoreElements()) {
            XMLTreeNode node = (XMLTreeNode) e.nextElement();
            XMLProperty prop = node.getProperty();
            Class type = prop.getPropertyClass();
            if (type != null && hilite.isAssignableFrom(type)) {
                TreePath path = new TreePath(node.getPath());
                tree.scrollPathToVisible(path);
            }
        }
    }

    /**
   * Creates the GUI and listeners.
   */
    protected void createGUI() {
        String imageFile = "/org/opensourcephysics/resources/controls/images/hilite.gif";
        hiliteIcon = new ImageIcon(XMLTree.class.getResource(imageFile));
        root = new XMLTreeNode(control);
        tree = new JTree(root);
        tree.setCellRenderer(new HighlightRenderer());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        scroller = new JScrollPane(tree);
        scroller.setPreferredSize(new Dimension(200, 200));
    }

    /**
   * A cell renderer to show launchable nodes.
   */
    private class HighlightRenderer extends DefaultTreeCellRenderer {

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            XMLTreeNode node = (XMLTreeNode) value;
            XMLProperty prop = node.getProperty();
            Class type = prop.getPropertyClass();
            if (type != null && hilite.isAssignableFrom(type)) {
                setIcon(hiliteIcon);
            }
            return this;
        }
    }
}
