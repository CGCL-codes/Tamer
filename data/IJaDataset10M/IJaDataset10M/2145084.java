package pcgen.gui2.util.treetable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 *
 * @author Connor Petty <mistercpp2000@gmail.com>
 */
public class DefaultTreeTableNode extends DefaultMutableTreeNode implements TreeTableNode {

    private List<Object> data;

    public DefaultTreeTableNode() {
        this(Collections.emptyList());
    }

    public DefaultTreeTableNode(List<?> data) {
        setValues(data);
    }

    public DefaultTreeTableNode(TreeNode node) {
        this();
        if (node instanceof TreeTableNode) {
            TreeTableNode treeTableNode = (TreeTableNode) node;
            setValues(treeTableNode.getValues());
        }
        for (int x = 0; x < node.getChildCount(); x++) {
            add(new DefaultTreeTableNode(node.getChildAt(x)));
        }
    }

    public Object getValueAt(int column) {
        if (data.size() > column) {
            return data.get(column);
        }
        return null;
    }

    public void setValueAt(Object value, int column) {
        if (data.isEmpty()) {
            data = new ArrayList<Object>(column + 1);
        }
        while (data.size() <= column) {
            data.add(null);
        }
        data.set(column, value);
    }

    public List<Object> getValues() {
        return data;
    }

    protected void setValues(List<?> values) {
        this.data = new ArrayList<Object>(values);
    }

    @Override
    public String toString() {
        if (!data.isEmpty()) {
            Object name = data.get(0);
            if (name != null) {
                return name.toString();
            }
        }
        return super.toString();
    }
}
