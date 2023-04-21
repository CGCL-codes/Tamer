package net.sf.echopm.panel.editor.tree;

import java.util.Map;
import java.util.WeakHashMap;
import nextapp.echo2.app.Component;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.Row;
import echopointng.Tree;
import echopointng.tree.DefaultMutableTreeNode;
import echopointng.tree.TreeCellRenderer;
import echopointng.tree.TreeNode;
import echopointng.xhtml.XhtmlFragment;

/**
 * @author ron
 */
public class EditorTreeCellRenderer implements TreeCellRenderer {

    static final long serialVersionUID = 0L;

    private final Map<Object, Row> wrapperMap = new WeakHashMap<Object, Row>();

    /**
	 * @param enableDragAndDrop
	 */
    public EditorTreeCellRenderer() {
    }

    public Component getTreeCellRendererComponent(Tree tree, Object node, boolean selected, boolean expanded, boolean leaf) {
        if (node instanceof EditorTreeNodeAbstractDecorator) {
            EditorTreeNodeAbstractDecorator decorator = (EditorTreeNodeAbstractDecorator) node;
            return decorator.getRenderComponent();
        }
        return getComponent((TreeNode) node);
    }

    public Label getTreeCellRendererText(Tree tree, Object node, boolean sel, boolean expanded, boolean leaf) {
        return null;
    }

    public XhtmlFragment getTreeCellRendererXhtml(Tree tree, Object value, boolean selected, boolean expanded, boolean leaf) {
        return null;
    }

    private Component getComponent(TreeNode node) {
        Object value;
        if (node instanceof EditorTreeNode) {
            value = ((EditorTreeNode) node).getEditor();
        } else if (node instanceof DefaultMutableTreeNode) {
            value = ((DefaultMutableTreeNode) node).getUserObject();
        } else {
            value = null;
        }
        if (value != null) {
            if (value instanceof Component) {
                return (Component) value;
            } else {
                return new Label(value.toString());
            }
        }
        return new Label(node.toString());
    }
}
