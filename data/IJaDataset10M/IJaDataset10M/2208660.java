package ebiNeutrinoSDK.utils;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.Iterator;

/**
 * This dialog allow to view a XML file in JTreeView component
 *
 */
public class EBIJXMLTreePanel extends JTree {

    private Document xmlDoc;

    DefaultMutableTreeNode tn;

    private String treeName = null;

    public EBIJXMLTreePanel(Document doc, String treeName) {
        super();
        this.treeName = treeName;
        this.xmlDoc = doc;
        setSize(600, 450);
        tn = new DefaultMutableTreeNode(treeName);
        initialize();
    }

    private void initialize() {
        setName(this.treeName);
        setExpandsSelectedPaths(true);
        ImageIcon tutorialIcon = EBIConstant.ACTION_SUCCESS;
        ImageIcon errorIcon = EBIConstant.ACTION_FAILED;
        if (tutorialIcon != null) {
            setCellRenderer(new MyRenderer(tutorialIcon, errorIcon));
        }
        processElement(xmlDoc.getRootElement(), tn);
        ((DefaultTreeModel) getModel()).setRoot(tn);
        expandTree();
        setVisible(true);
    }

    public void resetUIProtocol(Document doc) {
        this.xmlDoc = doc;
        tn = new DefaultMutableTreeNode(treeName);
        initialize();
    }

    private void processElement(Element el, DefaultMutableTreeNode dmtn) {
        DefaultMutableTreeNode currentNode = new DefaultMutableTreeNode(processAttributes(el));
        String text = el.getTextNormalize();
        if ((text != null) && (!text.equals(""))) currentNode.add(new DefaultMutableTreeNode(text));
        Iterator children = el.getChildren().iterator();
        while (children.hasNext()) processElement((Element) children.next(), currentNode);
        dmtn.add(currentNode);
    }

    private String processAttributes(Element el) {
        Iterator atts = el.getAttributes().iterator();
        String attributes = el.getName() + " ";
        while (atts.hasNext()) {
            Attribute att = (Attribute) atts.next();
            attributes += " " + att.getName() + ":" + att.getValue() + "";
        }
        return attributes;
    }

    private void expandTree() {
        expandAPath(getPathForRow(0));
    }

    private void expandAPath(TreePath p) {
        expandPath(p);
        DefaultMutableTreeNode currNode = (DefaultMutableTreeNode) p.getLastPathComponent();
        int numChildren = currNode.getChildCount();
        for (int i = 0; i < numChildren; ++i) {
            TreePath newPath = p.pathByAddingChild(currNode.getChildAt(i));
            expandAPath(newPath);
        }
    }

    private class MyRenderer extends DefaultTreeCellRenderer {

        Icon tutorialIcon;

        Icon errorIcon;

        public MyRenderer(Icon icon, Icon icon1) {
            tutorialIcon = icon;
            errorIcon = icon1;
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (leaf) {
                String error = value.toString().substring(value.toString().length() - 1, value.toString().length());
                if (error.equals(" ")) {
                    setIcon(errorIcon);
                } else {
                    setIcon(tutorialIcon);
                }
            } else {
            }
            return this;
        }
    }
}
