package com.guzzservices.manager.impl.ip;

import java.util.ArrayList;
import org.omg.CORBA.BooleanHolder;

public class DefaultBSTree<E extends Comparable<E>> implements BSTree<E> {

    protected static class BSTNode<E extends Comparable<E>> {

        protected BSTNode<E> left;

        protected BSTNode<E> right;

        protected E key;

        BSTNode(E key) {
            this.key = key;
            left = right = null;
        }
    }

    protected BSTNode<E> root;

    public void insert(E ele) {
        if (root == null) {
            root = new BSTNode<E>(ele);
        } else _insert(root, ele);
    }

    private final void _insert(BSTNode<E> pointer, E ele) {
        int cmp = pointer.key.compareTo(ele);
        if (cmp == 0) {
            throw new IllegalArgumentException();
        }
        if (cmp > 0) {
            if (pointer.left == null) {
                pointer.left = new BSTNode<E>(ele);
                return;
            }
            _insert(pointer.left, ele);
        } else {
            if (pointer.right == null) {
                pointer.right = new BSTNode<E>(ele);
                return;
            }
            _insert(pointer.right, ele);
        }
    }

    public boolean search(E ele) {
        return _search(root, ele);
    }

    public CityMark searchMatchedIP(long ipValue) {
        if (root == null) return null;
        return _searchMatchedIP(root, ipValue, 1);
    }

    protected CityMark _searchMatchedIP(BSTNode<E> pointer, long ipValue, int searchLevel) {
        if (pointer == null) return null;
        CityMark currentCity = (CityMark) pointer.key;
        boolean matchOK = ipValue >= currentCity.getStartIPSeq() && ipValue <= currentCity.getEndIPSeq();
        if (matchOK) {
            if (pointer.right == null) return currentCity;
            CityMark biggerCity = (CityMark) pointer.right.key;
            if (ipValue >= biggerCity.getStartIPSeq()) {
                CityMark biggerMachtedCity = _searchMatchedIP(pointer.right, ipValue, ++searchLevel);
                return biggerMachtedCity == null ? currentCity : biggerMachtedCity;
            } else {
                return currentCity;
            }
        } else {
            if (ipValue >= currentCity.getStartIPSeq()) {
                if (pointer.right == null) return null;
                return _searchMatchedIP(pointer.right, ipValue, ++searchLevel);
            } else {
                if (pointer.left == null) return null;
                return _searchMatchedIP(pointer.left, ipValue, ++searchLevel);
            }
        }
    }

    private boolean _search(BSTNode<E> pointer, E ele) {
        if (pointer == null) return false;
        int cmp = pointer.key.compareTo(ele);
        if (cmp == 0) {
            return true;
        }
        if (cmp > 0) {
            return _search(pointer.left, ele);
        } else {
            return _search(pointer.right, ele);
        }
    }

    public boolean delete(E ele) {
        BooleanHolder mark = new BooleanHolder(false);
        root = _delete(root, ele, mark);
        return mark.value;
    }

    private BSTNode<E> _delete(BSTNode<E> pointer, E ele, BooleanHolder mark) {
        if (pointer == null) return null;
        int cmp = pointer.key.compareTo(ele);
        if (cmp == 0) {
            mark.value = true;
            if (pointer.left == null) {
                return pointer.right;
            } else if (pointer.right == null) {
                return pointer.left;
            } else {
                BSTNode<E> ret = pointer.left;
                BSTNode<E> retP = null;
                while (ret.right != null) {
                    retP = ret;
                    ret = ret.right;
                }
                retP.right = ret.left;
                ret.right = pointer.right;
                ret.left = pointer.left;
                return ret;
            }
        }
        if (cmp > 0) {
            pointer.left = _delete(pointer.left, ele, mark);
        } else {
            pointer.right = _delete(pointer.right, ele, mark);
        }
        return pointer;
    }

    public void inOrder(Visitor<E> v) {
        _inOrder(root, v);
    }

    private final void _inOrder(BSTNode<E> p, Visitor<E> v) {
        if (p == null) {
            return;
        }
        _inOrder(p.left, v);
        v.visit(p.key);
        _inOrder(p.right, v);
    }

    public void postOrder(Visitor<E> v) {
        _postOrder(root, v);
    }

    private final void _postOrder(BSTNode<E> p, Visitor<E> v) {
        if (p == null) {
            return;
        }
        _postOrder(p.left, v);
        _postOrder(p.right, v);
        v.visit(p.key);
    }

    public void preOrder(Visitor<E> v) {
        _preOrder(root, v);
    }

    private final void _preOrder(BSTNode<E> p, Visitor<E> v) {
        if (p == null) {
            return;
        }
        v.visit(p.key);
        _preOrder(p.left, v);
        _preOrder(p.right, v);
    }

    public void levelOrder(Visitor<E> v) {
        if (root == null) {
            return;
        }
        ArrayList<BSTNode<E>> queue = new ArrayList<BSTNode<E>>();
        queue.add(root);
        while (!queue.isEmpty()) {
            BSTNode<E> p = queue.remove(0);
            v.visit(p.key);
            if (p.left != null) queue.add(p.left);
            if (p.right != null) queue.add(p.right);
        }
    }

    public boolean isEmpty() {
        return root == null;
    }
}
