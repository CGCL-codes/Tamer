package gov.sns.apps.jeri.apps.blmbrowser;

import gov.sns.tools.swing.treetable.TreeTableModel;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

public class ModulesTableModel implements TreeTableModel {

    public ModulesTableModel() {
    }

    /**
     * Returns the number ofs availible column.
     */
    public int getColumnCount() {
        return 0;
    }

    /**
     * Returns the name for column number <code>column</code>.
     */
    public String getColumnName(int column) {
        return null;
    }

    /**
     * Returns the type for column number <code>column</code>.
     */
    public Class getColumnClass(int column) {
        return null;
    }

    /**
     * Returns the value to be displayed for node <code>node</code>, 
     * at column number <code>column</code>.
     */
    public Object getValueAt(Object node, int column) {
        return null;
    }

    /**
     * Indicates whether the the value for node <code>node</code>, 
     * at column number <code>column</code> is editable.
     */
    public boolean isCellEditable(Object node, int column) {
        return false;
    }

    /**
     * Sets the value for node <code>node</code>, 
     * at column number <code>column</code>.
     */
    public void setValueAt(Object aValue, Object node, int column) {
    }

    /**
   * Returns the root of the iocTree.  Returns <code>null</code>
   * only if the iocTree has no nodes.
   * 
   * @return the root of the iocTree
   */
    public Object getRoot() {
        return null;
    }

    /**
   * Returns the child of <code>parent</code> at index <code>index</code>
   * in the parent's
   * child array.  <code>parent</code> must be a node previously obtained
   * from this data source. This should not return <code>null</code>
   * if <code>index</code>
   * is a valid index for <code>parent</code> (that is <code>index >= 0 &&
   * index < getChildCount(parent</code>)).
   * 
   * @param parent a node in the iocTree, obtained from this data source
   * @return the child of <code>parent</code> at index <code>index</code>
   */
    public Object getChild(Object parent, int index) {
        return null;
    }

    /**
   * Returns the number of children of <code>parent</code>.
   * Returns 0 if the node
   * is a leaf or if it has no children.  <code>parent</code> must be a node
   * previously obtained from this data source.
   * 
   * @param parent a node in the iocTree, obtained from this data source
   * @return the number of children of the node <code>parent</code>
   */
    public int getChildCount(Object parent) {
        return 0;
    }

    /**
   * Returns <code>true</code> if <code>node</code> is a leaf.
   * It is possible for this method to return <code>false</code>
   * even if <code>node</code> has no children.
   * A directory in a filesystem, for example,
   * may contain no files; the node representing
   * the directory is not a leaf, but it also has no children.
   * 
   * @param node a node in the iocTree, obtained from this data source
   * @return true if <code>node</code> is a leaf
   */
    public boolean isLeaf(Object node) {
        return false;
    }

    /**
      * Messaged when the user has altered the value for the item identified
      * by <code>path</code> to <code>newValue</code>. 
      * If <code>newValue</code> signifies a truly new value
      * the model should post a <code>treeNodesChanged</code> event.
      *
      * @param path path to the node that the user has altered
      * @param newValue the new value from the TreeCellEditor
      */
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    /**
   * Returns the index of child in parent.  If either <code>parent</code>
   * or <code>child</code> is <code>null</code>, returns -1.
   * If either <code>parent</code> or <code>child</code> don't
   * belong to this iocTree model, returns -1.
   * 
   * @param parent a note in the iocTree, obtained from this data source
   * @param child the node we are interested in
   * @return the index of the child in the parent, or -1 if either
   * <code>child</code> or <code>parent</code> are <code>null</code>
   * or don't belong to this iocTree model
   */
    public int getIndexOfChild(Object parent, Object child) {
        return 0;
    }

    /**
   * Adds a listener for the <code>TreeModelEvent</code>
   * posted after the iocTree changes.
   * 
   * @param l the listener to add
   * @see #removeTreeModelListener
   */
    public void addTreeModelListener(TreeModelListener l) {
    }

    /**
     * Removes a listener previously added with
     * <code>addTreeModelListener</code>.
     *
     * @see     #addTreeModelListener
     * @param   l       the listener to remove
     */
    public void removeTreeModelListener(TreeModelListener l) {
    }
}
