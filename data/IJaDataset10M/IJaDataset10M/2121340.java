package treeview.action;

import treeview.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.Action;
import org.w3c.dom.DOMException;
import net.sourceforge.jsxe.jsXe;
import net.sourceforge.jsxe.LocalizedAction;
import net.sourceforge.jsxe.gui.DocumentView;
import net.sourceforge.jsxe.gui.Messages;
import net.sourceforge.jsxe.gui.TabbedView;
import net.sourceforge.jsxe.dom.AdapterNode;
import net.sourceforge.jsxe.dom.XMLDocument;
import net.sourceforge.jsxe.dom.completion.ElementDecl;

/**
 * An action that edits the current selected node.
 *
 * @author Ian Lewis (<a href="mailto:IanLewis@member.fsf.org">IanLewis@member.fsf.org</a>)
 * @version $Id: EditNodeAction.java 1125 2006-08-08 20:39:34Z ian_lewis $
 */
public class EditNodeAction extends LocalizedAction {

    /**
     * Creates a action that brings up an EditTagDialog for the selected
     * Node
     */
    public EditNodeAction() {
        super("treeview.edit.node");
    }

    public void invoke(TabbedView view, ActionEvent evt) {
        DocumentView docView = view.getDocumentView();
        if (docView instanceof DefaultView) {
            DefaultView defView = (DefaultView) docView;
            TreeViewTree tree = defView.getTree();
            AdapterNode selectedNode = tree.getSelectedNode();
            AdapterNode addedNode = null;
            if (selectedNode != null && selectedNode.getNodeType() == AdapterNode.ELEMENT_NODE) {
                try {
                    XMLDocument document = selectedNode.getOwnerDocument();
                    ElementDecl element = document.getElementDecl(selectedNode.getNodeName());
                    if (element != null) {
                        try {
                            document.beginCompoundEdit();
                            EditTagDialog dialog = new EditTagDialog(jsXe.getActiveView(), element, new HashMap(), element.empty, element.completionInfo.getEntityHash(), new ArrayList(), selectedNode.getOwnerDocument(), selectedNode);
                            dialog.show();
                        } finally {
                            document.endCompoundEdit();
                        }
                        tree.updateUI();
                    }
                } catch (DOMException dome) {
                    JOptionPane.showMessageDialog(tree, dome, "XML Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }
}
