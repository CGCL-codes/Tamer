package fedora.utilities.policyEditor;

/**
 * @author diglib
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FedoraSystemModel extends AbstractTreeTableModel implements TreeTableModel {

    protected static String[] cNames = { "Name", "Allow Access", "Exception" };

    protected static Class[] cTypes = { TreeTableModel.class, String.class, String.class };

    public static final Integer ZERO = new Integer(0);

    /**
     * @param root
     */
    public FedoraSystemModel(Object root) {
        super(root);
        FedoraNode.model = this;
    }

    protected Object[] getChildren(Object node) {
        FedoraNode fedoraNode = ((FedoraNode) node);
        return fedoraNode.getChildren();
    }

    public int getChildCount(Object node) {
        Object[] children = getChildren(node);
        return (children == null) ? 0 : children.length;
    }

    public Object getChild(Object node, int i) {
        return getChildren(node)[i];
    }

    public int getColumnCount() {
        return cNames.length;
    }

    public String getColumnName(int column) {
        return cNames[column];
    }

    public Class getColumnClass(int column) {
        return cTypes[column];
    }

    public Object getValueAt(Object node, int column) {
        FedoraNode fnode = (FedoraNode) node;
        if (column == 0) {
            return fnode.toString();
        } else {
            return fnode.getValue(column - 1);
        }
    }

    public void setValueAt(Object aValue, Object node, int column) {
        FedoraNode fnode = (FedoraNode) node;
        if (column > 0) {
            Object prev = fnode.getValue(column - 1);
            if (prev != aValue) {
                PolicyEditor.mainWin.setDirty();
            }
            fnode.setValue(column - 1, aValue);
        }
    }
}
