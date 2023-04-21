package net.sf.javagimmicks.swing.model;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import javax.swing.tree.TreeNode;

public class ListTreeNode<E> implements TreeNode {

    protected ArrayList<ListTreeNode<E>> _children;

    protected ChildrenListView _childrenListView;

    protected ChildrenValueListView _childrenValueListView;

    protected ListTreeModel<E> _model;

    protected ListTreeNode<E> _parent;

    protected E _value;

    public ListTreeNode(E value, boolean leaf) {
        this(null, null, leaf, value);
    }

    protected ListTreeNode(ListTreeModel<E> model, boolean leaf, E value) {
        this(model, null, leaf, value);
    }

    protected ListTreeNode(ListTreeNode<E> parent, boolean leaf, E value) {
        this(parent._model, parent, leaf, value);
    }

    private ListTreeNode(ListTreeModel<E> model, ListTreeNode<E> parent, boolean leaf, E value) {
        _model = model;
        _parent = parent;
        setLeaf(leaf);
        _value = value;
        _childrenListView = new ChildrenListView();
        _childrenValueListView = new ChildrenValueListView();
    }

    public ListTreeNode<E> addChildAt(int index, E value, boolean leaf) {
        ListTreeNode<E> result = new ListTreeNode<E>(value, leaf);
        getChildListView().add(index, result);
        return result;
    }

    public ListTreeNode<E> addChildAt(int index, E value) {
        return addChildAt(index, value, false);
    }

    public ListTreeNode<E> addChild(E value, boolean leaf) {
        return addChildAt(getChildCount(), value, leaf);
    }

    public ListTreeNode<E> addChild(E value) {
        return addChild(value, false);
    }

    public ListTreeNode<E> removeChildAt(int index) {
        return getChildListView().remove(index);
    }

    public List<ListTreeNode<E>> getChildListView() {
        return _childrenListView;
    }

    public List<E> getChildValueListView() {
        return _childrenValueListView;
    }

    public E getValue() {
        return _value;
    }

    public void setValue(E value) {
        _value = value;
        if (_model != null) {
            _model.fireNodeChanged(_parent, _parent == null ? 0 : _parent._childrenListView.indexOf(this));
        }
    }

    public void setLeaf(boolean leaf) {
        if (isLeaf() == leaf) {
            return;
        }
        if (!leaf) {
            _children = new ArrayList<ListTreeNode<E>>();
        } else if (_children.isEmpty()) {
            _children = null;
        } else {
            throw new IllegalStateException("Node still has children. Remove them before setting the node to leaf mode.");
        }
    }

    @SuppressWarnings("unchecked")
    public Enumeration children() {
        return Collections.enumeration(isLeaf() ? Collections.EMPTY_LIST : _children);
    }

    public boolean getAllowsChildren() {
        return !isLeaf();
    }

    public ListTreeNode<E> getChildAt(int childIndex) {
        if (!getAllowsChildren()) {
            throw new ArrayIndexOutOfBoundsException("Node allows no children!");
        }
        return _children.get(childIndex);
    }

    public int getChildCount() {
        return isLeaf() ? 0 : _children.size();
    }

    public int getIndex(TreeNode node) {
        return getAllowsChildren() ? _children.indexOf(node) : -1;
    }

    public ListTreeNode<E> getParent() {
        return _parent;
    }

    public boolean isLeaf() {
        return _children == null;
    }

    public void detach() {
        if (_parent == null && _model == null) {
            throw new IllegalStateException("This node cannot be detached. It has no parent and is not a root node!");
        }
        if (_parent != null) {
            _parent._childrenListView.remove(this);
        } else if (_model != null) {
            updateModel(null);
            _model.fireNodesRemoved(null, 0, Collections.singleton(this));
        }
    }

    public String toString() {
        return _value == null ? null : _value.toString();
    }

    protected void updateModel(ListTreeModel<E> model) {
        _model = model;
        if (!isLeaf()) {
            for (ListTreeNode<E> child : _children) {
                child.updateModel(model);
            }
        }
    }

    protected class ChildrenListView extends AbstractList<ListTreeNode<E>> {

        public boolean addAll(int index, Collection<? extends ListTreeNode<E>> c) {
            checkLeaf();
            for (ListTreeNode<E> newChild : c) {
                if (newChild._model != null) {
                    throw new IllegalArgumentException("Cannot add a node which already belongs to a model!");
                } else if (newChild._parent != null) {
                    throw new IllegalArgumentException("Cannot add a non-detached node!");
                }
                newChild._parent = ListTreeNode.this;
                newChild.updateModel(_model);
            }
            _children.addAll(index, c);
            if (_model != null) {
                _model.fireNodesInserted(ListTreeNode.this, index, c);
            }
            return true;
        }

        public boolean addAll(Collection<? extends ListTreeNode<E>> c) {
            return addAll(_children.size(), c);
        }

        public void add(int index, ListTreeNode<E> element) {
            addAll(index, Collections.singleton(element));
        }

        public void clear() {
            checkLeaf();
            clear(true);
        }

        private void clear(boolean isTop) {
            if (isLeaf()) {
                return;
            }
            for (ListTreeNode<E> newChild : _children) {
                newChild._childrenListView.clear(false);
                newChild._model = null;
                newChild._parent = null;
            }
            if (isTop && _model != null) {
                ArrayList<ListTreeNode<E>> oldChildren = new ArrayList<ListTreeNode<E>>(_children);
                _children.clear();
                _model.fireNodesRemoved(ListTreeNode.this, 0, oldChildren);
            } else {
                _children.clear();
            }
        }

        public ListTreeNode<E> get(int index) {
            checkLeaf();
            return _children.get(index);
        }

        public ListTreeNode<E> remove(int index) {
            checkLeaf();
            ListTreeNode<E> removedNode = _children.remove(index);
            removedNode.updateModel(null);
            removedNode._parent = null;
            if (_model != null) {
                _model.fireNodesRemoved(ListTreeNode.this, index, Collections.singleton(removedNode));
            }
            return removedNode;
        }

        public ListTreeNode<E> set(int index, ListTreeNode<E> element) {
            checkLeaf();
            ListTreeNode<E> result = _children.set(index, element);
            if (_model != null) {
                _model.fireNodeChanged(ListTreeNode.this, index);
            }
            return result;
        }

        public int size() {
            if (isLeaf()) {
                return 0;
            }
            return _children.size();
        }

        private void checkLeaf() {
            if (isLeaf()) {
                throw new UnsupportedOperationException("Node is a leaf!");
            }
        }
    }

    protected class ChildrenValueListView extends AbstractList<E> {

        public void add(int index, E element) {
            ListTreeNode<E> newNode = new ListTreeNode<E>(element, false);
            _childrenListView.add(index, newNode);
        }

        public boolean addAll(Collection<? extends E> c) {
            return addAll(_childrenListView.size(), c);
        }

        public boolean addAll(int index, Collection<? extends E> c) {
            ArrayList<ListTreeNode<E>> newNodeCollection = new ArrayList<ListTreeNode<E>>(c.size());
            for (E element : c) {
                newNodeCollection.add(new ListTreeNode<E>(element, false));
            }
            return _childrenListView.addAll(index, newNodeCollection);
        }

        public void clear() {
            _childrenListView.clear();
        }

        public E get(int index) {
            return _childrenListView.get(index).getValue();
        }

        public E remove(int index) {
            return _childrenListView.remove(index).getValue();
        }

        public E set(int index, E element) {
            ListTreeNode<E> childNode = _childrenListView.get(index);
            E oldValue = childNode.getValue();
            childNode.setValue(element);
            return oldValue;
        }

        public int size() {
            return _childrenListView.size();
        }
    }
}
