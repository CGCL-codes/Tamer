package com.loribel.tools.xml.template.node;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.tree.TreeNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.GB_MenusOwner;
import com.loribel.commons.abstraction.GB_NodeOwner;
import com.loribel.commons.abstraction.swing.GB_ViewManager;
import com.loribel.commons.abstraction.swing.GB_ViewManagerOwner;
import com.loribel.commons.gui.GB_ErrorVM;
import com.loribel.commons.gui.GB_VMTools;
import com.loribel.commons.swing.GB_Menu;
import com.loribel.commons.swing.GB_MenuItem;
import com.loribel.commons.swing.GB_MenuItemAction;
import com.loribel.commons.swing.tree.GB_DefaultTreeNode;
import com.loribel.commons.swing.tree.GB_TreeNodeWithChildren;
import com.loribel.commons.xml.GB_ElementTools;
import com.loribel.commons.xml.GB_NodeList;
import com.loribel.commons.xml.transform.GB_ElementTransformToAtt;
import com.loribel.tools.xa.GB_XAUtils;
import com.loribel.tools.xa.GB_XSUtils;
import com.loribel.tools.xml.template.GB_XmlGuiTemplate;
import com.loribel.tools.xml.template.GB_XmlGuiTools;
import com.loribel.tools.xml.vm.GB_NodeOrderChoiceVM;
import com.loribel.tools.xml.vm.GB_NodeTransformerChoiceVM;

/**
 * Tree Node to represent an Xml Node.
 *
 * @author Gr�gory Borelli
 */
public class GB_XmlTreeNodeDefault extends GB_TreeNodeWithChildren implements GB_XmlTreeNode, GB_ViewManagerOwner, GB_MenusOwner, GB_NodeOwner {

    /**
     * MenuItemCopy
     */
    private class MenuItemCopy extends GB_MenuItemAction {

        public MenuItemCopy() {
            super(AA.MNU_EDIT_COPY, AA.MNU_EDIT_COPY_IMG, AA.MNU_EDIT_COPY_C, AA.MNU_EDIT_COPY_DESC);
        }

        public void doAction() {
            actionCopy();
        }
    }

    /**
     * MenuItemCut
     */
    private class MenuItemCut extends GB_MenuItemAction implements ActionListener {

        public MenuItemCut() {
            super(AA.MNU_EDIT_CUT, AA.MNU_EDIT_CUT_IMG, AA.MNU_EDIT_CUT_C, AA.MNU_EDIT_CUT_DESC);
        }

        public void doAction() {
            actionCut();
        }
    }

    /**
     * MenuItemMoveDown
     */
    private class MenuItemMoveDown extends GB_MenuItem implements ActionListener {

        public MenuItemMoveDown() {
            super("D�pacer vers le bas", AA.ICON.X16_ARROW_DOWN);
            Node l_nextNode = GB_ElementTools.getNextSiblingElementSameLevel(node);
            if (l_nextNode == null) {
                setEnabled(false);
            } else {
                this.addActionListener(this);
            }
        }

        public void actionPerformed(ActionEvent e) {
            actionMoveDown();
        }
    }

    /**
     * MenuItemMoveUp
     */
    private class MenuItemMoveUp extends GB_MenuItem implements ActionListener {

        public MenuItemMoveUp() {
            super("D�placer vers haut", AA.ICON.X16_ARROW_UP);
            Node l_previousNode = GB_ElementTools.getPreviousSiblingElementSameLevel(node);
            if (l_previousNode == null) {
                setEnabled(false);
            } else {
                this.addActionListener(this);
            }
        }

        public void actionPerformed(ActionEvent e) {
            actionMoveUp();
        }
    }

    /**
     * MenuItemOrder
     */
    private class MenuItemOrder extends GB_MenuItem implements ActionListener {

        public MenuItemOrder() {
            super("Trier...");
            this.setIcon(AA.ICON.X16_ORDER);
            this.addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            actionOrder();
        }
    }

    /**
     * MenuItemPasteAfter
     */
    private class MenuItemPasteAfter extends GB_MenuItemAction {

        public MenuItemPasteAfter() {
            super("Coller Apr�s", AA.ICON.X16_PASTE, 'a', null);
            if (nodeClipboard == null) {
                setEnabled(false);
            }
        }

        public void doAction() {
            actionPasteAfter();
        }
    }

    /**
     * MenuItemPasteBefore
     */
    private class MenuItemPasteBefore extends GB_MenuItem implements ActionListener {

        public MenuItemPasteBefore() {
            super("Coller Avant", AA.ICON.X16_PASTE);
            if (nodeClipboard == null) {
                setEnabled(false);
            } else {
                this.addActionListener(this);
            }
        }

        public void actionPerformed(ActionEvent e) {
            actionPasteBefore();
        }
    }

    /**
     * MenuItemPasteBefore
     */
    private class MenuItemPasteEnd extends GB_MenuItem implements ActionListener {

        public MenuItemPasteEnd() {
            super("Coller en dernier", AA.ICON.X16_PASTE);
            if (nodeClipboard == null) {
                setEnabled(false);
            } else {
                this.addActionListener(this);
            }
        }

        public void actionPerformed(ActionEvent e) {
            actionPasteEnd();
        }
    }

    /**
     * MenuItemTransformToAtt
     */
    private class MenuItemTransformToAtt extends GB_MenuItem implements ActionListener {

        public MenuItemTransformToAtt() {
            super("Cet Element en Attribut");
            this.setIcon(AA.ICON.X16_ORDER);
            this.addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            actionTransformToAtt();
        }
    }

    /**
     * MenuItemTransformToAtt2
     */
    private class MenuItemTransformToAtt2 extends GB_MenuItem implements ActionListener {

        public MenuItemTransformToAtt2() {
            super("Transformer en Attribut(s)...");
            this.setIcon(AA.ICON.X16_ORDER);
            this.addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            actionTransformToAtt2();
        }
    }

    /**
     * MenuItemUpdate
     */
    private class MenuItemUpdate extends GB_MenuItem implements ActionListener {

        public MenuItemUpdate() {
            super("Rafraichir", AA.ICON.X16_REFRESH);
            this.addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            actionUpdate();
        }
    }

    /**
     * MenuItemXA
     */
    private class MenuItemXA extends GB_MenuItemAction {

        public MenuItemXA() {
            super(AA.MNU_XML_ACTIONS, AA.MNU_XML_ACTIONS_IMG, AA.MNU_XML_ACTIONS_C, AA.MNU_XML_ACTIONS_DESC);
        }

        public void doAction() {
            actionXA();
        }
    }

    /**
     * MenuItemXS
     */
    private class MenuItemXS extends GB_MenuItemAction {

        public MenuItemXS() {
            super(AA.MNU_XML_SELECTOR, AA.MNU_XML_SELECTOR_IMG, AA.MNU_XML_SELECTOR_C, AA.MNU_XML_SELECTOR_DESC);
        }

        public void doAction() {
            actionXS();
        }
    }

    private Node node;

    private GB_ViewManager viewManager;

    private static Node nodeClipboard;

    GB_XmlGuiTemplate guiTemplate;

    public GB_XmlTreeNodeDefault(Node a_node, GB_XmlGuiTemplate a_guiTemplate) {
        super(a_node);
        guiTemplate = a_guiTemplate;
        node = a_node;
    }

    protected void actionCopy() {
        nodeClipboard = node;
        System.out.println("Copy");
    }

    public void actionCut() {
        nodeClipboard = node;
        node.getParentNode().removeChild(node);
        updateTreeFromParent();
    }

    public void actionMoveDown() {
        Node l_parent = node.getParentNode();
        Node l_refChild = GB_ElementTools.getNextSiblingElementSameLevel(node);
        Node l_refChild2 = GB_ElementTools.getNextSiblingElementSameLevel(l_refChild);
        l_parent.removeChild(node);
        if (l_refChild2 != null) {
            l_parent.insertBefore(node, l_refChild2);
        } else {
            l_parent.appendChild(node);
        }
        updateTreeFromParent();
    }

    public void actionMoveUp() {
        Node l_parent = node.getParentNode();
        Node l_refChild = GB_ElementTools.getPreviousSiblingElementSameLevel(node);
        l_parent.removeChild(node);
        l_parent.insertBefore(node, l_refChild);
        updateTreeFromParent();
    }

    protected void actionOrder() {
        GB_NodeOrderChoiceVM l_vm = new GB_NodeOrderChoiceVM(node);
        Dimension d = new Dimension(750, 450);
        GB_VMTools.showDialog(null, l_vm, "Order...", d, true, true);
        if (l_vm.isUpdateRoot()) {
            updateTreeFromParent();
        } else if (l_vm.isUpdateParent()) {
            updateTreeFromParent();
        } else if (l_vm.isUpdateCurrent()) {
            updateTree();
        }
    }

    protected void actionPasteAfter() {
        if (nodeClipboard == null) {
            return;
        }
        Node l_newNode = nodeClipboard.cloneNode(true);
        Node l_parent = node.getParentNode();
        Node l_nextChild = node.getNextSibling();
        if (l_nextChild == null) {
            l_parent.appendChild(l_newNode);
        } else {
            l_parent.insertBefore(l_newNode, l_nextChild);
        }
        updateTreeFromParent();
    }

    protected void actionPasteBefore() {
        if (nodeClipboard == null) {
            return;
        }
        Node l_newNode = nodeClipboard.cloneNode(true);
        Node l_parent = node.getParentNode();
        l_parent.insertBefore(l_newNode, node);
        updateTreeFromParent();
    }

    protected void actionPasteEnd() {
        if (nodeClipboard == null) {
            return;
        }
        Node l_newNode = nodeClipboard.cloneNode(true);
        node.appendChild(l_newNode);
        actionUpdate();
    }

    protected void actionTransformToAtt() {
        if (node.getChildNodes().getLength() > 1) {
            String l_nodeName = "<tt>" + node.getNodeName() + "</tt>";
            showWarning(null, "<html>L'�l�ment " + l_nodeName + " ne peut �tre transform� en attribut. Sa valeur est nulle !");
            return;
        }
        Element l_parent = (Element) node.getParentNode();
        String l_eltName = node.getNodeName();
        GB_ElementTransformToAtt l_transformer = new GB_ElementTransformToAtt(node);
        l_transformer.transformtoAttribute(l_parent, l_eltName);
        updateTreeFromParent();
    }

    protected void actionTransformToAtt2() {
        GB_NodeTransformerChoiceVM l_vm = new GB_NodeTransformerChoiceVM(node);
        Dimension d = new Dimension(750, 450);
        GB_VMTools.showDialog(null, l_vm, "Order...", d, true, true);
        if (l_vm.isUpdateRoot()) {
            updateTreeFromParent();
        } else if (l_vm.isUpdateParent()) {
            updateTreeFromParent();
        } else if (l_vm.isUpdateCurrent()) {
            updateTree();
        }
    }

    public void actionUpdate() {
        this.nodeStructureChanged();
    }

    public void actionXA() {
        GB_XAUtils.showXAToolsFrame(this);
    }

    public void actionXS() {
        GB_XSUtils.showXSToolsFrame(this);
    }

    public boolean buildChildren2(boolean a_flagDeep) {
        GB_NodeList l_children = GB_ElementTools.getChildNodesByType(node, Node.ELEMENT_NODE, 1);
        if (l_children == null) {
            return false;
        }
        Node[] l_elements = l_children.toArrayOfNode();
        int len = l_elements.length;
        Node l_child;
        for (int i = 0; i < len; i++) {
            l_child = l_elements[i];
            if (!l_child.getNodeName().equals("html")) {
                this.add(new GB_XmlTreeNodeDefault(l_child, guiTemplate));
            }
        }
        return true;
    }

    protected JMenu buildMenuMove() {
        JMenu retour = new JMenu("D�placer");
        retour.add(new MenuItemMoveUp());
        retour.add(new MenuItemMoveDown());
        return retour;
    }

    protected JMenu buildMenuPaste() {
        GB_Menu retour = new GB_Menu("Coller");
        retour.setIcon(AA.ICON.X16_PASTE);
        if (nodeClipboard == null) {
            retour.setEnabled(false);
        } else {
            retour.add(new MenuItemPasteBefore());
            retour.add(new MenuItemPasteAfter());
            retour.add(new MenuItemPasteEnd());
        }
        return retour;
    }

    protected JMenu buildMenuTransform() {
        JMenu retour = new JMenu("Transformer");
        retour.add(new MenuItemTransformToAtt());
        retour.add(new MenuItemTransformToAtt2());
        return retour;
    }

    public GB_LabelIcon getLabelIcon() {
        return GB_XmlGuiTools.getLabelIcon(node);
    }

    public JMenu[] getMenus() {
        JMenu[] retour = new JMenu[2];
        JMenu l_menu = new JMenu("Edit");
        retour[0] = l_menu;
        l_menu.add(new MenuItemCut());
        l_menu.add(new MenuItemCopy());
        l_menu.add(buildMenuPaste());
        l_menu.addSeparator();
        l_menu.add(buildMenuMove());
        l_menu.add(buildMenuTransform());
        l_menu.add(new MenuItemOrder());
        l_menu.addSeparator();
        l_menu.add(new MenuItemXA());
        l_menu.add(new MenuItemXS());
        l_menu.addSeparator();
        l_menu.add(new MenuItemUpdate());
        return retour;
    }

    public Node getNode() {
        return node;
    }

    public GB_ViewManager getViewManager() {
        if (viewManager == null) {
            try {
                viewManager = guiTemplate.newViewManagers(node)[0];
            } catch (Exception ex) {
                return new GB_ErrorVM(this, ex);
            }
        }
        return viewManager;
    }

    public void nodeStructureChanged() {
        rebuildChildren(false);
        super.nodeStructureChanged();
    }

    protected void showWarning(String a_title, Object a_message) {
        if (a_title == null) {
            a_title = "Warning";
        }
        JOptionPane.showMessageDialog(null, a_message, a_title, JOptionPane.WARNING_MESSAGE);
    }

    public String toString() {
        return getLabelIcon().getLabel();
    }

    public void updateTree() {
        this.nodeStructureChanged();
    }

    protected void updateTreeFromParent() {
        TreeNode l_parent = this.getParent();
        GB_DefaultTreeNode l_treeNode = (GB_DefaultTreeNode) l_parent;
        l_treeNode.nodeStructureChanged();
    }

    protected void updateTreeFromRoot() {
        TreeNode l_parent = this.getRoot();
        GB_DefaultTreeNode l_treeNode = (GB_DefaultTreeNode) l_parent;
        l_treeNode.nodeStructureChanged();
    }
}
