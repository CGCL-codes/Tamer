package de.grogra.imp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import de.grogra.graph.impl.GraphManager;
import de.grogra.graph.impl.Node;
import de.grogra.graph.impl.Node.NType;
import de.grogra.pf.ui.Context;
import de.grogra.pf.ui.swing.Job;
import de.grogra.reflect.MemberBase;

public abstract class ObjectInspector implements TreeModel, TreeSelectionListener {

    public static class TreeNode implements javax.swing.tree.TreeNode {

        private Object object;

        private TreeNode parent;

        private ArrayList<TreeNode> children;

        public TreeNode(Object object, TreeNode parent) {
            this.object = object;
            this.parent = parent;
        }

        public void addChild(TreeNode treeNode) {
            if (children == null) children = new ArrayList<TreeNode>();
            children.add(treeNode);
        }

        public String toString() {
            if (object instanceof Node) {
                Node node = (Node) object;
                if (node.getName() == null) return node.getNType().getSimpleName() + " [" + node.getId() + "]";
                return node.getName() + " [" + node.getId() + "]";
            } else if (object instanceof MemberBase) {
                MemberBase mb = (MemberBase) object;
                return mb.getSimpleName();
            }
            if (object != null) return object.toString();
            return "";
        }

        public Object getObject() {
            return object;
        }

        public int getChildCount() {
            if (children == null) return 0;
            return children.size();
        }

        public TreeNode getChildAt(int childIndex) {
            if (children == null) return null;
            return children.get(childIndex);
        }

        public int getIndex(javax.swing.tree.TreeNode node) {
            if (children == null) return 0;
            return children.indexOf(node);
        }

        public Enumeration<TreeNode> children() {
            if (children == null) return null;
            return Collections.enumeration(children);
        }

        public boolean getAllowsChildren() {
            return true;
        }

        public TreeNode getParent() {
            return parent;
        }

        public boolean isLeaf() {
            return getChildCount() == 0 ? true : false;
        }
    }

    protected final Context ctx;

    protected final GraphManager graph;

    protected TreeNode rootNode;

    protected HashSet<NType> filter = null;

    protected boolean hierarchicFilter = false;

    private boolean activeTreeSelection = false;

    private boolean activeGISelection = false;

    public ObjectInspector(Context ctx, GraphManager graph) {
        this.ctx = ctx;
        this.graph = graph;
        initialize();
        buildTree();
    }

    public void setFilter(TreePath[] paths, boolean hierarchic) {
        this.hierarchicFilter = hierarchic;
        if (paths == null) {
            filter = null;
            return;
        }
        if (filter == null) filter = new HashSet<NType>(); else filter.clear();
        for (TreePath path : paths) {
            Object o = ((TreeNode) path.getLastPathComponent()).getObject();
            if (o instanceof Node) filter.add(((Node) o).getNType());
        }
    }

    public void removeFilter() {
        this.filter = null;
    }

    public Object getChild(Object parent, int index) {
        return ((TreeNode) parent).getChildAt(index);
    }

    public int getChildCount(Object parent) {
        return ((TreeNode) parent).getChildCount();
    }

    public int getIndexOfChild(Object parent, Object child) {
        return ((TreeNode) parent).getIndex((TreeNode) child);
    }

    public Object getRoot() {
        return rootNode;
    }

    public boolean isLeaf(Object node) {
        return ((TreeNode) node).isLeaf();
    }

    public void valueChanged(final TreeSelectionEvent e) {
        if (activeGISelection) return;
        new Job(ctx) {

            @Override
            protected void runImpl(Object arg, Context ctx) {
                Object source = e.getSource();
                if (source instanceof JTree) {
                    JTree tree = (JTree) source;
                    TreePath[] paths = tree.getSelectionPaths();
                    ArrayList<Node> nodes = new ArrayList<Node>();
                    int i = 0;
                    for (TreePath path : paths) {
                        Object o = path.getLastPathComponent();
                        Object n = ((TreeNode) o).getObject();
                        if (n instanceof Node) {
                            nodes.add((Node) n);
                            i++;
                        }
                    }
                    Node[] nodeArray = new Node[i];
                    nodes.toArray(nodeArray);
                    activeTreeSelection = true;
                    ctx.getWorkbench().select(nodeArray);
                }
            }
        }.execute();
    }

    public void addTreeModelListener(TreeModelListener l) {
    }

    public void removeTreeModelListener(TreeModelListener l) {
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    public TreeNode getTreeNodeForNode(Node node) {
        return findNode(rootNode, node);
    }

    private TreeNode findNode(TreeNode treeNode, Node node) {
        if (treeNode.getObject() == node) return treeNode;
        int size = treeNode.getChildCount();
        for (int i = 0; i < size; i++) {
            TreeNode child = findNode(treeNode.getChildAt(i), node);
            if (child != null) return child;
        }
        return null;
    }

    public void getPathToTreeNode(TreeNode treeNode, LinkedList<TreeNode> path) {
        path.addFirst(treeNode);
        while (treeNode != rootNode) {
            treeNode = treeNode.getParent();
            path.addFirst(treeNode);
        }
    }

    /**
	 * Use this method for declarations etc. Only called once in constructor.
	 */
    public abstract void initialize();

    /**
	 * Implement this method to set {@link rootNode} and its children.
	 * The method is called in the constructor of ObjectInspector and every time
	 * the GroIMP scene graph changes.
	 */
    public abstract void buildTree();

    public boolean isActiveTreeSelection() {
        return activeTreeSelection;
    }

    public void setActiveTreeSelection(boolean activeTreeSelection) {
        this.activeTreeSelection = activeTreeSelection;
    }

    public boolean isActiveGISelection() {
        return activeGISelection;
    }

    public void setActiveGISelection(boolean activeGISelection) {
        this.activeGISelection = activeGISelection;
    }
}
