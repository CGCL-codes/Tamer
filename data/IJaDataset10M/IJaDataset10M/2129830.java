package cashRegister;

import java.util.ArrayList;
import java.util.List;

/**
 * A basic binary search tree that uses a linked data structure. This tree does
 * not self-balance or do any of that fancy stuff.
 * 
 * @author David Birti
 * @param <E> data type for objects stored in the tree.
 */
public class LinkedBST<E extends Comparable<? super E>> {

    private Node root = new Node(null, null, null);

    private int size = 0;

    public boolean add(E data) {
        if (root.left == null) {
            root.left = new Node(data);
            size++;
            return true;
        }
        Node curr = root.left;
        Node prev = root;
        while (true) {
            if (data.compareTo(curr.data) < 0) {
                prev = curr;
                curr = curr.left;
                if (curr == null) {
                    prev.left = new Node(data);
                    size++;
                    return true;
                }
            } else if (data.compareTo(curr.data) > 0) {
                prev = curr;
                curr = curr.right;
                if (curr == null) {
                    prev.right = new Node(data);
                    size++;
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    public E get(E key) {
        if (root.left == null) return null;
        Node curr = root.left;
        while (curr != null) {
            if (key.compareTo(curr.data) == 0) return curr.data;
            if (key.compareTo(curr.data) < 0) curr = curr.left; else if (key.compareTo(curr.data) > 0) curr = curr.right;
        }
        return null;
    }

    public boolean search(E data) {
        if (root.left == null) return false;
        Node curr = root.left;
        while (curr != null) {
            if (data.compareTo(curr.data) == 0) return true;
            if (data.compareTo(curr.data) < 0) curr = curr.left; else if (data.compareTo(curr.data) > 0) curr = curr.right;
        }
        return false;
    }

    /**
	 * Deletes a node from the tree.
	 */
    public boolean remove(E data) {
        Node prev = root;
        Node curr = root.left;
        boolean sentinel = true;
        boolean left = true;
        while (curr != null && sentinel) {
            if (data.compareTo(curr.data) == 0) sentinel = false;
            if (data.compareTo(curr.data) < 0) {
                prev = curr;
                curr = curr.left;
                left = true;
            } else if (data.compareTo(curr.data) > 0) {
                prev = curr;
                curr = curr.right;
                left = false;
            }
        }
        if (curr == null) return false;
        if (curr.isLeaf()) {
            if (left) {
                prev.left = null;
            } else {
                prev.right = null;
            }
            return true;
        }
        if (curr.hasLeft() && !curr.hasRight()) {
            if (left) {
                prev.left = curr.left;
            } else {
                prev.right = curr.left;
            }
            return true;
        }
        if (curr.hasRight() && !curr.hasLeft()) {
            if (left) {
                prev.left = curr.right;
            } else {
                prev.right = curr.right;
            }
            return true;
        }
        Node replacement = curr;
        replacement = replacement.left;
        while (replacement.right != null) {
            replacement = replacement.right;
        }
        remove(replacement.data);
        curr.data = replacement.data;
        return true;
    }

    public void add(E[] arr) {
        recursiveAdd(0, arr.length - 1, arr);
    }

    private void recursiveAdd(int start, int end, E[] arr) {
        if (end < start) return;
        if (end == start) {
            add(arr[end]);
            return;
        }
        if (end - start == 1) {
            add(arr[start]);
            add(arr[end]);
            return;
        }
        int mid = (start + end) / 2;
        add(arr[mid]);
        recursiveAdd(start, mid - 1, arr);
        recursiveAdd(mid + 1, end, arr);
    }

    public ArrayList<E> toList() {
        ArrayList<E> list = new ArrayList<E>(size);
        if (root.left == null) return list;
        recursiveToList(list, root.left);
        return list;
    }

    private void recursiveToList(List<E> l, Node n) {
        l.add(n.data);
        if (n.hasLeft()) recursiveToList(l, n.left);
        if (n.hasRight()) recursiveToList(l, n.right);
    }

    /**
	 * The node class for binary trees.
	 * 
	 * @author David Birti
	 */
    private class Node {

        Node left;

        Node right;

        E data;

        /**
		 * Constructs a node with a left and right child.
		 * @param data the data for this node.
		 * @param left pointer to the left node.
		 * @param right pointer to the right node.
		 */
        @SuppressWarnings("unused")
        Node(E data, Node left, Node right) {
            this.left = left;
            this.right = right;
            this.data = data;
        }

        /**
		 * Constructs a node with no children.
		 * @param data the data for this node.
		 */
        Node(E data) {
            left = right = null;
            this.data = data;
        }

        /**
		 * Checks if this node is a leaf node.
		 * @return true if this node is a leaf.
		 */
        boolean isLeaf() {
            if (left == null && right == null) return true;
            return false;
        }

        /**
		 * Checks for a left child.
		 * @return true if a left child exists.
		 */
        boolean hasLeft() {
            if (left != null) return true;
            return false;
        }

        /**
		 * Checks for a right child.
		 * @return true if a right child exists.
		 */
        boolean hasRight() {
            if (right != null) return true;
            return false;
        }
    }
}
