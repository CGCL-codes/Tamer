package ch.cern.lhcb.xmleditor.gui;

import java.util.EventObject;
import javax.swing.tree.DefaultTreeCellEditor;
import ch.cern.lhcb.xmleditor.specific.IconsManager;
import ch.cern.lhcb.xmleditor.specific.XmlDisplay;

/**
 * This extends the DefaultTreeCellEditor to allow or not the edition depending
 * on Xml specific conditions
 * 
 * @author Sebastien Ponce
 */
public class TreeCellEditor extends DefaultTreeCellEditor {

    /**
     * an icon manager to be used by this editor
     */
    private IconsManager iconsManager;

    /**
     * the underlying XmlTree
     */
    private XmlTree jTree;

    /**
     * a error message displayer
     */
    private MessageDisplayer msgDis;

    /**
     * default constructor, calls the ancestor one
     * 
     * @param jTree the XmlTree on which this editor will work
     */
    public TreeCellEditor(XmlTree jTree, MessageDisplayer msgDis) {
        super(jTree, (TreeCellRenderer) jTree.getCellRenderer());
        this.jTree = jTree;
        this.msgDis = msgDis;
        this.iconsManager = jTree.getTreeView().getEditor().getIconsManager();
    }

    /**
     * overrides the ancestor's method to avoid edition of node with no editable
     * name
     * 
     * @param event the EventObject corresponding to the begining edition
     */
    public boolean isCellEditable(EventObject event) {
        boolean result = super.isCellEditable(event);
        if (result) {
            XmlTreeNode treeNode = (XmlTreeNode) jTree.getPathForRow(this.lastRow).getLastPathComponent();
            if (!treeNode.isNameEditable()) {
                msgDis.popupMessage("Not editable since it has no name");
                msgDis.displayMessage("The node you tried to edit does not display an " + "editable name but the default one, computed " + "from its type.\nThus, you cannot change it.\n");
                return false;
            }
            String name = treeNode.getNode().getNodeName();
            if (treeNode.getOldNode() != null && !treeNode.isReference()) {
                name = name + "deref";
            }
            XmlDisplay xmlDisplay = treeNode.getXmlDisplay();
            if (xmlDisplay != null) {
                if (!xmlDisplay.isInDtd(treeNode.getNode().getNodeName())) {
                    if (!name.startsWith("#")) {
                        name = "notindtd";
                    }
                }
            }
            this.editingIcon = iconsManager.getIcon(name);
        }
        return result;
    }
}
