package com.jiexplorer.model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import com.jiexplorer.ui.ISafeRunnable;
import com.jiexplorer.util.SafeRunnable;

public abstract class AbstractTreeNode implements ITreeNode {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AbstractTreeNode.class);

    protected List<INodeChangedListener> modelChangeListeners = new ArrayList<INodeChangedListener>();

    protected Vector<ITreeNode> children;

    protected ITreeNode parent;

    protected Object nodeObject;

    protected String uid = null;

    protected String name = null;

    protected boolean explored = false;

    protected AbstractTreeNode() {
    }

    public abstract boolean hasChildren();

    public abstract boolean isLeaf();

    public ITreeNode getParent() {
        return parent;
    }

    public Object getNodeObject() {
        return nodeObject;
    }

    public List<ITreeNode> getChildren() {
        if (children != null) {
            return children;
        }
        createChildren();
        return children;
    }

    public int getChildCount() {
        if (children != null) {
            return children.size();
        }
        createChildren();
        return children.size();
    }

    public Enumeration<ITreeNode> children() {
        if (children != null) {
            return children.elements();
        }
        createChildren();
        return children.elements();
    }

    public boolean getAllowsChildren() {
        return true;
    }

    public ITreeNode getChildAt(final int childIndex) {
        if (children != null && childIndex < children.size()) {
            return children.get(childIndex);
        }
        return null;
    }

    public int getIndex(final ITreeNode node) {
        if (children != null) {
            return children.indexOf(node);
        }
        return -1;
    }

    public int getIndex(final TreeNode node) {
        if (children != null) {
            return children.indexOf(node);
        }
        return -1;
    }

    protected abstract void createChildren();

    public void addChildren(final ITreeNode[] nodes) {
        if (!isLeaf()) {
            for (final ITreeNode node : nodes) {
                addNode(node);
            }
            fireNodesAdded(nodes);
        }
    }

    public void addChild(final ITreeNode node) {
        addNode(node);
        fireNodesAdded(new ITreeNode[] { node });
    }

    protected synchronized void addNode(final ITreeNode node) {
        if (children == null) {
            children = new Vector<ITreeNode>();
        }
        if (children.contains(node)) {
            log.info("DUPLICATES ENTRY");
        } else {
            children.add(node);
        }
    }

    public void removeChildren(final ITreeNode[] nodes) {
        for (final ITreeNode node : nodes) {
            removeNode(node);
        }
        fireNodesRemoved(nodes);
    }

    public void refresh() {
        explored = false;
        if (children != null) {
            final ITreeNode[] tnodes = new ITreeNode[children.size()];
            children.toArray(tnodes);
            children = null;
            fireNodesRemoved(tnodes);
            children = null;
            createChildren();
        }
    }

    public void removeChild(final ITreeNode node) {
        log.debug("removeChild - " + this.getName() + " remove " + node.getName());
        removeNode(node);
        fireNodesRemoved(new ITreeNode[] { node });
    }

    protected synchronized void removeNode(final ITreeNode node) {
        children.remove(node);
    }

    public void addNodeChangeListener(final INodeChangedListener listener) {
        if (modelChangeListeners == null) {
            modelChangeListeners = new ArrayList<INodeChangedListener>();
        }
        if (!modelChangeListeners.contains(listener)) modelChangeListeners.add(listener);
    }

    public void removeNodeChangeListener(final INodeChangedListener listener) {
        modelChangeListeners.remove(listener);
    }

    public List<INodeChangedListener> getModelChangedListeners() {
        return modelChangeListeners;
    }

    /** Fire methods need to be called in appropriate subclasses of treenode */
    public void fireNodesUpdated(final ITreeNode[] children) {
        final Iterator<INodeChangedListener> listenerIter = getModelChangedListeners().iterator();
        SafeRunnable.run(new ISafeRunnable() {

            public void handleException(final Throwable exception) {
                exception.printStackTrace();
            }

            public void run() throws Exception {
                while (listenerIter.hasNext()) {
                    listenerIter.next().onUpdate(AbstractTreeNode.this, children);
                }
            }
        });
    }

    public void fireNodesAdded(final ITreeNode[] children) {
        final Iterator<INodeChangedListener> listenerIter = getModelChangedListeners().iterator();
        SafeRunnable.run(new ISafeRunnable() {

            public void handleException(final Throwable exception) {
                exception.printStackTrace();
            }

            public void run() throws Exception {
                while (listenerIter.hasNext()) {
                    listenerIter.next().onAdd(AbstractTreeNode.this, children);
                }
            }
        });
    }

    public void fireNodesRemoved(final ITreeNode[] children) {
        final Iterator<INodeChangedListener> listenerIter = getModelChangedListeners().iterator();
        SafeRunnable.run(new ISafeRunnable() {

            public void handleException(final Throwable exception) {
                exception.printStackTrace();
            }

            public void run() throws Exception {
                while (listenerIter.hasNext()) {
                    final INodeChangedListener obj = listenerIter.next();
                    final StringBuffer strBuf = new StringBuffer();
                    for (final ITreeNode n : children) {
                        strBuf.append(n.getName() + ", ");
                    }
                    log.debug("fireNodesRemoved - " + obj.getClass().getName() + " remove " + strBuf.toString());
                    obj.onRemove(AbstractTreeNode.this, children);
                }
            }
        });
    }

    public void fireNodeExpand() {
    }

    public void fireNodeCollapse() {
    }

    public synchronized String getUid() {
        return uid;
    }

    public synchronized void setUid(final String uuid) {
        this.uid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setParent(final ITreeNode parent) {
        this.parent = parent;
    }

    public TreePath getTreePath() {
        final List<? super ITreeNode> ancestors = getAncestors();
        ancestors.add(this);
        return new TreePath(ancestors.toArray());
    }

    public List<ITreeNode> getAncestors() {
        if (parent != null) {
            final List<ITreeNode> ancestors = parent.getAncestors();
            ancestors.add(parent);
            return ancestors;
        } else {
            return new ArrayList<ITreeNode>(0);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
