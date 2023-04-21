package de.schmaller.apps.TreeDataViewer.data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public abstract class ADataAdapter implements TreeModel {

    public static final int DATA_ACCESS_NO_DATA = 0;

    public static final int DATA_ACCESS_DIRECT = 1;

    public static final int DATA_ACCESS_DEFERRED = 2;

    public static final int NAME_SOURCE_KEY = 0;

    public static final int NAME_SOURCE_NEXT_TO_KEY = 1;

    public static final int NAME_SOURCE_DATA = 2;

    public static final int TYPE_TREE = 0;

    public static final int TYPE_FLAT = 1;

    /**
	 * The name of this DataAdapter.
	 * Used to reference in the collection of the ParmManager 
	 */
    protected String name = null;

    /**
	 * The dispName of this DataAdapter. 
	 * Used to display in GUI selections 
	 */
    protected String dispName = null;

    /**
	 * The keys are the parent nodes.
	 * The values are <code>List</code>s of their corresponding children. 
	 */
    protected LinkedHashMap relation = new LinkedHashMap();

    /**
	 * The keys are all the child nodes.
	 * This set can be used to determine the "root" nodes.
	 */
    protected HashSet allChildren = new HashSet();

    /**
	 * The root nodes are calculated after all relations are read.
	 * It's every key element from <code>relation</code> which is not an element of <code>allChildren</code>
	 */
    protected LinkedHashSet allRoots = new LinkedHashSet();

    /**
	 * The leaf nodes are calculated after all relations are read.
	 * It's every node from <code>allChildren</code> which is not a key node in <code>relation</code>
	 */
    protected HashSet allLeaves = new HashSet();

    /**
	 * The root node is the one and only topmost dummy node.
	 */
    private DefaultTreeNode root = new DefaultTreeNode("Root");

    /**
	 * How is the node's data accessed?
	 * Default: there's no special data source
	 */
    private int dataAccess = DATA_ACCESS_NO_DATA;

    /**
    * Where does the display name of the node come from?
    */
    private int nameSource = NAME_SOURCE_KEY;

    /**
    * Is the data hierarchically structured or just flat? 
    */
    private int type = TYPE_TREE;

    /**
	 * @see <code>allRoots</code>
	 */
    protected final void calcRootNodes() {
        this.allRoots.clear();
        for (Iterator iParent = relation.keySet().iterator(); iParent.hasNext(); ) {
            Object parent = iParent.next();
            if (!allChildren.contains(parent)) {
                this.allRoots.add(parent);
            }
        }
    }

    /**
	 * @see <code>allLeaves</code>
	 */
    protected final void calcLeaves() {
        this.allLeaves.clear();
        for (Iterator iChild = allChildren.iterator(); iChild.hasNext(); ) {
            Object child = iChild.next();
            if (!relation.containsKey(child)) {
                this.allLeaves.add(child);
            }
        }
    }

    /**
	 * getter for private attribute root
	 */
    public Object getRoot() {
        return this.root;
    }

    /**
	 * @implements TreeModel.getChild
	 */
    public Object getChild(Object parent, int index) {
        Object res = null;
        List children;
        if (parent == this.root) children = Arrays.asList(allRoots.toArray()); else children = (List) relation.get(parent);
        if (children != null) res = children.get(index);
        return res;
    }

    /**
	 * @implements TreeModel.getChildCount
	 */
    public int getChildCount(Object parent) {
        int res = 0;
        List children;
        if (parent == this.root) children = Arrays.asList(allRoots.toArray()); else children = (List) relation.get(parent);
        if (children != null) res = children.size();
        return res;
    }

    /**
	 * @implements TreeModel.getIndexOfChild
	 */
    public int getIndexOfChild(Object parent, Object child) {
        int res = -1;
        List children;
        if (parent == this.root) children = Arrays.asList(allRoots.toArray()); else children = (List) relation.get(parent);
        if (children != null) res = children.indexOf(child);
        return res;
    }

    public boolean isLeaf(Object node) {
        return relation.get(node) == null && node != this.root;
    }

    /**
	 * other not implemented methods form TreeModel
	 */
    public void addTreeModelListener(TreeModelListener l) {
    }

    public void removeTreeModelListener(TreeModelListener l) {
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    /**
    * This method can be used as an iterator over all TreeNodes.
    * The order of traversal is preorder.  
    * @param p path to start from
    * @return the path to the next TreeNode, or the first root node if p is null,
    * or null if p is the last node (i.e. has no children an no further siblings)
    */
    public TreePath getNext(TreePath p) {
        TreePath rootpath = new TreePath(root);
        if (relation.size() == 0 || allRoots.size() == 0) return null;
        if (p == null || p.getLastPathComponent() == root) {
            return rootpath.pathByAddingChild(allRoots.toArray()[0]);
        }
        Object node = p.getLastPathComponent();
        List list = (List) (relation.get(node));
        if (list != null && list.size() > 0) {
            return p.pathByAddingChild(list.get(0));
        }
        TreePath parentPath = p.getParentPath();
        Object parent = null;
        while (node != root) {
            parent = parentPath.getLastPathComponent();
            if (parent == root) {
                list = Arrays.asList(allRoots.toArray());
            } else {
                list = (List) (relation.get(parent));
            }
            int i = list.indexOf(node);
            if (list.size() > i + 1) {
                return parentPath.pathByAddingChild(list.get(i + 1));
            }
            node = parent;
            parentPath = parentPath.getParentPath();
        }
        return null;
    }

    /**
	 * each sub-class must implement this to fill the key data.
	 * the calc... methods must be called when filling is finished.
	 */
    public abstract void fill() throws Exception;

    public abstract void fillData(DefaultTreeNode n) throws Exception;

    /**
	 * @return the dataAccess
	 */
    public int getDataAccess() {
        return dataAccess;
    }

    /**
	 * @param dataAccess the dataAccess to set
	 */
    public void setDataAccess(int dataAccess) {
        this.dataAccess = dataAccess;
    }

    /**
	 * @return the nameSource
	 */
    public int getNameSource() {
        return nameSource;
    }

    /**
	 * @param nameSource the nameSource to set
	 */
    public void setNameSource(int nameSource) {
        this.nameSource = nameSource;
    }

    /**
	 * @return the dispName
	 */
    public String getDispName() {
        return dispName;
    }

    /**
	 * @param dispName the dispName to set
	 */
    public void setDispName(String dispName) {
        this.dispName = dispName;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public String toString() {
        return dispName;
    }

    /**
    * @return the type
    */
    public int getType() {
        return type;
    }

    /**
    * @param type the type to set
    */
    public void setType(int type) {
        this.type = type;
    }
}
