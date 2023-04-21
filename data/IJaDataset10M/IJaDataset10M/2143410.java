package com.loribel.tools.xml.template.vm;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.GB_SimpleNode;
import com.loribel.commons.abstraction.swing.GB_ViewManager;
import com.loribel.commons.gui.GB_TreeNodeVMImpl;
import com.loribel.commons.gui.GB_TreeVMDefault;
import com.loribel.commons.swing.GB_MenuItem;
import com.loribel.commons.util.GB_IconTools;
import com.loribel.commons.util.GB_LabelIconTools;
import com.loribel.commons.util.impl.GB_SimpleNodeImpl;
import com.loribel.commons.xml.GB_ElementTools;
import com.loribel.commons.xml.GB_NodeList;
import com.loribel.tools.xml.template.GB_XmlGuiTemplate;
import com.loribel.tools.xml.template.node.GB_NodeTreeCellRenderer;
import com.loribel.tools.xml.template.node.GB_XmlTreeNodeDefault;

/**
 * ViewManager to represent a node in a treeViewManager.
 *
 * @author Gr�gory Borelli
 * @version 2003/01/24 - manu
 */
public class GB_TreeXmlVM extends GB_TreeVMDefault {

    /**
     * Attribute node. <br />
     */
    private Node node;

    GB_XmlTreeNodeDefault myRoot;

    GB_XmlGuiTemplate guiTemplate;

    /**
     * Constructor.
     *
     * @param a_node Node -
     */
    public GB_TreeXmlVM(Node a_node, GB_XmlGuiTemplate a_guiTemplate) {
        super();
        guiTemplate = a_guiTemplate;
        setNode(a_node);
    }

    /**
     * Property getter of {@link #node}.
     *
     * @return Node - {@link #node}
     */
    public Node getNode() {
        return node;
    }

    /**
     * Property setter of {@link #node}.
     *
     * @param a_node Node -
     */
    public void setNode(Node a_node) {
        node = (Element) a_node;
    }

    /**
     * Return the label icon associated to this ViewManager
     */
    public GB_LabelIcon getLabelIcon() {
        Icon l_icon = GB_IconTools.get(AA.ICON.X16_TREE);
        GB_LabelIcon retour = GB_LabelIconTools.newLabelIcon("Arbre", l_icon);
        return retour;
    }

    /**
     * Define a style for the tree.
     */
    protected void initTreeStyle() {
        JTree l_tree = this.getMainTree();
        l_tree.setBackground(new Color(200, 200, 200));
        DefaultTreeCellRenderer l_treeCellRenderer = new GB_NodeTreeCellRenderer();
        l_tree.setCellRenderer(l_treeCellRenderer);
    }

    public void initialize() {
        myRoot = new GB_XmlTreeNodeDefault(node, guiTemplate);
        myRoot.buildChildren(false);
        this.setRoot(myRoot);
        this.setRootVisible(true);
    }

    /**
     * Init style.
     */
    public void loadDataToView() {
        super.loadDataToView();
        initTreeStyle();
    }

    /**
     * A partir d'un node construit une hi�rarchie (GB_SimpleNode) de tous les
     * �l�ments :
     *  - excluant les �l�ments avec le nom html
     */
    protected GB_SimpleNode buildSimpleNode(Node a_node) {
        GB_SimpleNodeImpl retour = new GB_SimpleNodeImpl(null, a_node);
        GB_NodeList l_elements = GB_ElementTools.getChildElements(a_node);
        if (l_elements == null) {
            return retour;
        }
        int len = l_elements.size();
        Node l_child;
        for (int i = 0; i < len; i++) {
            l_child = l_elements.getNode(i);
            if (!l_child.getNodeName().equals("html")) {
                retour.addChild(buildSimpleNode(l_child));
            }
        }
        return retour;
    }

    /**
     * Add update to all popup menu.
     */
    protected JPopupMenu buildPopupMenu(GB_ViewManager a_viewManager, TreePath a_treePath) {
        JPopupMenu retour = super.buildPopupMenu(a_viewManager, a_treePath);
        if (retour == null) {
            retour = new JPopupMenu();
        } else {
            retour.addSeparator();
        }
        GB_MenuItem l_menuItem = new GB_MenuItem("Update");
        final DefaultMutableTreeNode l_treeNode = (DefaultMutableTreeNode) a_treePath.getLastPathComponent();
        ActionListener l_listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                update(l_treeNode);
            }
        };
        l_menuItem.addActionListener(l_listener);
        retour.add(l_menuItem);
        return retour;
    }

    protected void update(DefaultMutableTreeNode a_treeNode) {
        a_treeNode.setUserObject("Update");
        GB_TreeNodeVMImpl l_myTreeNode = (GB_TreeNodeVMImpl) a_treeNode;
        GB_TabbedXmlVM l_vm = (GB_TabbedXmlVM) l_myTreeNode.getViewManager();
        l_vm = new GB_TabbedXmlVM(l_vm.getNode(), guiTemplate);
        l_myTreeNode.setViewManager(l_vm);
        DefaultTreeModel l_model = (DefaultTreeModel) getMainTree().getModel();
        l_model.nodeStructureChanged(getRoot());
    }
}
