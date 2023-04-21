package tr.edu.metu.srdc.model;

import java.awt.Component;
import java.util.EventObject;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.w3c.dom.Node;
import tr.edu.metu.srdc.view.XStance;

public class XmlTreeEditor extends DefaultTreeCellEditor {

    private XStance viewer = null;

    public XmlTreeEditor(XStance xmlViewer, JTree arg0, DefaultTreeCellRenderer arg1) {
        super(arg0, arg1);
        this.viewer = xmlViewer;
    }

    public XStance getViewer() {
        return this.viewer;
    }

    public boolean isCellEditable(EventObject event) {
        Node node = this.viewer.getSelectedNode();
        if (node != null && node.getNodeType() == Node.TEXT_NODE) return super.isCellEditable(event); else return false;
    }

    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
        if (value instanceof XStanceNode) viewer.setEditingNode(((XStanceNode) value).getXmlNode());
        return super.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);
    }
}
