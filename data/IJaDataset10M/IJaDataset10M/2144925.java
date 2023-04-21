package openfield.appgestor;

import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import openfield.panels.explotacion.*;

/**
 *
 * @author Francis
 */
public class ArbolExplotaciones {

    private TreeModel arbol;

    private ArbolExplotaciones() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Explotaciones");
        DefaultMutableTreeNode child = new DefaultMutableTreeNode(new PanelElement("Información", ExplotacionPanel.class));
        root.add(child);
        child = new DefaultMutableTreeNode(new PanelElement("Capital Territorial", JPanel.class));
        root.add(child);
        child = new DefaultMutableTreeNode(new PanelElement("Edificaciones", JPanel.class));
        root.add(child);
        arbol = new DefaultTreeModel(root);
    }

    private static ArbolExplotaciones instance;

    public static ArbolExplotaciones getInstance() {
        if (instance == null) instance = new ArbolExplotaciones();
        return instance;
    }

    public TreeModel getArbol() {
        return arbol;
    }
}
