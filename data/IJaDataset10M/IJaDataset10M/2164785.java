package com.javampire.util.gui;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * TODO: document this.
 *
 * @author <a href="mailto:cnagy@ecircle.de">Csaba Nagy</a>
 * @version $Revision: 1.5 $ $Date: 2007/05/28 15:12:26 $
 */
public class GUINodeTreeModel implements TreeModel {

    private final boolean VERBOSE = false;

    private final GUINode rootNode;

    private final List<TreeModelListener> listeners;

    public GUINodeTreeModel(GUINode rootNode) {
        this.rootNode = rootNode;
        listeners = new ArrayList<TreeModelListener>();
    }

    public GUINode getRoot() {
        if (VERBOSE) {
            System.out.println("getRoot -> " + rootNode);
        }
        return rootNode;
    }

    public Object getChild(Object parent, int index) {
        final Object result = (parent instanceof GUINode) ? ((GUINode) parent).getChild(index) : null;
        if (VERBOSE) {
            System.out.println("getChild(" + parent + ", " + index + ") -> " + result);
        }
        return result;
    }

    public int getChildCount(Object parent) {
        final int result = (parent instanceof GUINode) ? ((GUINode) parent).getChildCount() : 0;
        if (VERBOSE) {
            System.out.println("getChildCount(" + parent + ") -> " + result);
        }
        return result;
    }

    public boolean isLeaf(Object node) {
        final boolean result = !(node instanceof GUINode);
        if (VERBOSE) {
            System.out.println("isLeaf(" + node + ") -> " + result);
        }
        return result;
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
        throw new UnsupportedOperationException("Not supported");
    }

    public int getIndexOfChild(Object parent, Object child) {
        final int result = (parent instanceof GUINode) && (child instanceof GUINode) ? ((GUINode) parent).getIndexOfChild((GUINode) child) : -1;
        if (VERBOSE) {
            System.out.println("getIndexOfChild(" + parent + ", " + child + ") -> " + result);
        }
        return result;
    }

    /**
     * Adds a listener for the TreeModelEvent posted after the tree changes.
     */
    public void addTreeModelListener(TreeModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener previously added with addTreeModelListener().
     */
    public void removeTreeModelListener(TreeModelListener listener) {
        listeners.remove(listener);
    }

    public void structureChanged(GUINode node) {
        final TreePath nodePath = getPath(node);
        final TreeModelEvent event = new TreeModelEvent(node, nodePath);
        for (TreeModelListener listener : listeners) {
            listener.treeStructureChanged(event);
        }
    }

    public static TreePath getPath(GUINode node) {
        ArrayList<GUINode> result = new ArrayList<GUINode>();
        while (node != null) {
            result.add(0, node);
            node = node.getParent();
        }
        return new TreePath(result.toArray());
    }

    public static TreePath getRootPath(GUINode node) {
        GUINode rootNode = null;
        while (node != null) {
            rootNode = node;
            node = node.getParent();
        }
        if (rootNode == null) return null;
        return new TreePath(rootNode);
    }

    public TreePath findNext(TreePath start, String substring) {
        if (substring == null) return null;
        if (start == null) {
            start = new TreePath(rootNode);
        } else {
            Object startNode = start.getLastPathComponent();
            if (startNode instanceof GUINode) {
                start = getPath((GUINode) startNode);
            }
        }
        final String upperSubstring = substring.toUpperCase();
        NodeIterator nodeIterator = new NodeIterator(start);
        while (nodeIterator.hasNext()) {
            final TreePath crtPath = nodeIterator.next();
            final String crtName = String.valueOf(crtPath.getLastPathComponent()).toUpperCase();
            if (crtName.indexOf(upperSubstring) != -1) {
                return crtPath;
            }
        }
        return null;
    }

    public static <T> List<T> findAll(TreePath start, Class<T> nodeClass) {
        if (start == null) return null;
        List<T> result = new ArrayList<T>();
        NodeIterator nodeIterator = new NodeIterator(start);
        while (nodeIterator.hasNext()) {
            final TreePath crtPath = nodeIterator.next();
            Object crtNode = crtPath.getLastPathComponent();
            if (nodeClass.isAssignableFrom(crtNode.getClass())) {
                result.add(nodeClass.cast(crtNode));
            }
        }
        return result;
    }

    public static class NodeIterator implements Iterator<TreePath> {

        private TreePath parentPath;

        private TreePath childPath;

        private int crtChildIndex;

        private TreePath lastPath;

        public NodeIterator(TreePath startPath) {
            this.crtChildIndex = Integer.MAX_VALUE;
            if (startPath != null) {
                this.parentPath = startPath.getParentPath();
                if (parentPath != null) {
                    GUINode parentNode = ((GUINode) parentPath.getLastPathComponent());
                    crtChildIndex = parentNode.getIndexOfChild((GUINode) startPath.getLastPathComponent());
                }
            }
            this.childPath = null;
            this.lastPath = startPath;
        }

        public boolean hasNext() {
            if (childPath != null) return true;
            boolean descend = true;
            while (parentPath != null || lastPath != null) {
                GUINode crtParent;
                List children;
                Object crtChild;
                if (parentPath != null) {
                    crtParent = (GUINode) parentPath.getLastPathComponent();
                    children = crtParent.getChildren();
                    crtChild = children.get(crtChildIndex);
                } else {
                    crtParent = null;
                    children = null;
                    crtChild = lastPath.getLastPathComponent();
                }
                if (descend && crtChild instanceof GUINode) {
                    List newChildren = ((GUINode) crtChild).getChildren();
                    if (!newChildren.isEmpty()) {
                        crtChildIndex = 0;
                        parentPath = parentPath == null ? new TreePath(crtChild) : parentPath.pathByAddingChild(crtChild);
                        childPath = parentPath.pathByAddingChild(newChildren.get(0));
                        return true;
                    }
                    descend = false;
                }
                if (parentPath == null) return false;
                if (children.size() > ++crtChildIndex) {
                    childPath = parentPath.pathByAddingChild(children.get(crtChildIndex));
                    return true;
                }
                parentPath = parentPath.getParentPath();
                if (parentPath == null) return false;
                crtChildIndex = ((GUINode) parentPath.getLastPathComponent()).getIndexOfChild(crtParent);
            }
            return false;
        }

        public TreePath next() {
            if (!hasNext()) throw new NoSuchElementException();
            lastPath = childPath;
            childPath = null;
            return lastPath;
        }

        public void remove() {
            final Object crtNode = lastPath.getLastPathComponent();
            lastPath = null;
            if (crtNode instanceof GUINode) {
                ((GUINode) crtNode).delete();
            }
        }
    }
}
