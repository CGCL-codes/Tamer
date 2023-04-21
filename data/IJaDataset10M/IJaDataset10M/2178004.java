package com.gaurav.tree;

/**
 * This is a BST implementation and requires its nodes to implement {@link Comparable}. 
 * <br><br>
 * Also add(E, E) and
 * add(E, E, int) throw {@link UnsupportedOperationException} because this implementation requires identification
 * of parents on its own. Thus, only add(E) works. The parent is figured out automatically based on BST rules.
 * The first node always becomes the root
 * 
 * @author Gaurav Saxena
 *
 * @param <E>
 */
public class BinarySearchTree<E extends Comparable<E>> extends ArrayTree<E> implements Cloneable {

    public BinarySearchTree() {
        super(2);
    }

    @Override
    public boolean add(E child) {
        try {
            if (size() == 0) return super.add(null, child); else {
                E parent = findParent(root(), child);
                return super.add(parent, child, parent.compareTo(child) > 0 ? 0 : 1);
            }
        } catch (NodeNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean add(E parent, E child) throws NodeNotFoundException {
        throw new UnsupportedOperationException("A binary search tree determines parent of a child on its own and hence it is not possible to add the child to any given parent. Please use add(child)");
    }

    @Override
    public boolean add(E parent, E child, int index) throws NodeNotFoundException {
        throw new UnsupportedOperationException("A binary search tree determines parent of a child on its own and hence it is not possible to add the child to any given parent or index. Please use add(child)");
    }

    /**
	 * @param parent
	 * @return the left child if present, or null otherwise
	 * @throws NodeNotFoundException
	 */
    public E left(E parent) throws NodeNotFoundException {
        return child(parent, 0);
    }

    /**
	 * @param parent
	 * @return the right child if present, or null otherwise
	 * @throws NodeNotFoundException
	 */
    public E right(E parent) throws NodeNotFoundException {
        return child(parent, 1);
    }

    private E findParent(E root, E child) throws NodeNotFoundException {
        if (child.compareTo(root) > 0) {
            E right = right(root);
            if (right != null) return findParent(right, child); else return root;
        } else {
            E left = left(root);
            if (left != null) return findParent(left, child); else return root;
        }
    }
}
