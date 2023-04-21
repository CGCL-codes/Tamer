package algs.model.tree;

import java.util.Iterator;

/**
 * Standard unbalanced binary tree.
 * 
 * Duplicates are allowed. The right child of a node in the tree is guaranteed to 
 * have its value be greater than or equal to its parent.
 * 
 * @param <T>     the base type of the values stored by the BinaryTree. Must be
 *                Comparable.
 * @author George Heineman
 * @version 1.0, 6/15/08
 * @since 1.0
 */
@SuppressWarnings("unchecked")
public class BinaryTree<T extends Comparable<T>> implements Iterable<T> {

    /** Root of the tree. */
    private BinaryNode<T> root;

    /** Empty iterator object, as needed. */
    private Iterator<T> empty;

    /**
	 * Singleton instance of the empty iterator, available for use.
	 */
    private Iterator<T> empty() {
        if (empty == null) {
            empty = new Iterator<T>() {

                public boolean hasNext() {
                    return false;
                }

                public T next() {
                    throw new java.util.NoSuchElementException("Empty Iterator");
                }

                public void remove() {
                    throw new UnsupportedOperationException("Empty Iterator can't be modified");
                }
            };
        }
        return empty;
    }

    /** Default BinaryTree constructor. */
    public BinaryTree() {
        root = null;
    }

    /**
	 * Helper method to construct appropriately typed BinaryNode for this value.
	 * 
	 * Specialized Trees may override this behavior, as needed.
	 */
    BinaryNode<T> construct(T value) {
        return new BinaryNode<T>(value);
    }

    /**
	 * Helper method to properly set the root for the tree.
	 * 
	 * @param newRoot
	 */
    protected void setRoot(BinaryNode<T> newRoot) {
        root = newRoot;
    }

    /**
	 * Expose the root of the tree.
	 */
    public BinaryNode<T> getRoot() {
        return root;
    }

    /**
	 * Determine if the given value occurs in the tree
	 * 
	 * @param value  non-null desired value to search for
	 * @return       true if the value is stored in the Binary Tree
	 * @exception    IllegalArgumentException if value is null
     * @exception    ClassCastException if the specified object's type prevents it
     *               from being compared to this object.
	 */
    public boolean member(T value) {
        if (value == null) {
            throw new IllegalArgumentException("BinaryTree cannot store 'null' values.");
        }
        if (root == null) {
            return false;
        }
        BinaryNode<T> node = root;
        while (node != null) {
            int c = value.compareTo(node.value);
            if (c == 0) {
                return true;
            }
            if (c < 0) {
                node = node.getLeftSon();
            } else {
                node = node.getRightSon();
            }
        }
        return false;
    }

    /**
	 * Remove the value from the tree.
	 * 
	 * @param value  non-null value to be removed
	 * @return true  if the value existed and was removed; otherwise return false
	 * @exception    IllegalArgumentException if value is null
     * @exception    ClassCastException if the specified object's type prevents it
     *               from being compared to this object.
	 */
    public boolean remove(T value) {
        if (value == null) {
            throw new IllegalArgumentException("BinaryTree cannot store 'null' values.");
        }
        if (root == null) {
            return false;
        }
        BinaryNode<T> node = root;
        BinaryNode<T> parent = null;
        BinaryNode<T> n;
        boolean returnVal = false;
        while (!returnVal) {
            int c = value.compareTo(node.value);
            if (c < 0) {
                if ((n = node.getLeftSon()) == null) {
                    break;
                } else {
                    parent = node;
                    node = n;
                }
            } else if (c > 0) {
                if ((n = node.getRightSon()) == null) {
                    break;
                } else {
                    parent = node;
                    node = n;
                }
            } else {
                removeHelper(node, parent);
                returnVal = true;
                break;
            }
        }
        return returnVal;
    }

    /**
	 * Helper method to properly handle the multiple subcases when removing a node
	 * from the tree.
	 * <p>
	 * The key is to find the successor-value (pv) to the target-value (tv). You can 
	 * set the value of 'tv' to be 'pv' and then delete the original 'pv' node. Find the 
	 * minimum value in the right sub-tree and remove it. Use that value as the replacement 
	 * value for node tv.
	 * 
	 * @param target    Node to be removed
	 * @param parent    parent of target node (or null if target is the root).
	 */
    void removeHelper(BinaryNode<T> target, BinaryNode<T> parent) {
        BinaryNode<T> lnode = target.getLeftSon();
        BinaryNode<T> rnode = target.getRightSon();
        if (lnode == null && rnode == null) {
            if (parent == null) {
                setRoot(null);
                return;
            }
            if (parent.getLeftSon() == target) {
                parent.left = null;
            } else {
                parent.right = null;
            }
            return;
        }
        if (lnode != null && rnode == null) {
            if (parent == null) {
                setRoot(lnode);
                return;
            }
            if (parent.getLeftSon() == target) {
                parent.left = lnode;
            } else {
                parent.right = lnode;
            }
            return;
        }
        if (rnode != null && lnode == null) {
            if (parent == null) {
                setRoot(rnode);
                return;
            }
            if (parent.getLeftSon() == target) {
                parent.left = rnode;
            } else {
                parent.right = rnode;
            }
            return;
        }
        BinaryNode<T> minNode = rnode;
        BinaryNode<T> rparent = null;
        while (minNode.getLeftSon() != null) {
            rparent = minNode;
            minNode = minNode.getLeftSon();
        }
        if (rparent == null) {
            target.value = minNode.value;
            target.right = minNode.right;
            return;
        }
        T minValue = minNode.value;
        removeHelper(minNode, rparent);
        target.value = minValue;
    }

    /**
	 * Insert the value into its proper location in the Binary tree.
	 * 
	 * No balancing is performed.
	 * 
	 * @param value   non-null value to be added into the tree.
	 * @exception    IllegalArgumentException if value is null
     * @exception    ClassCastException if the specified object's type prevents it
     *               from being compared to this object.
	 */
    public void insert(T value) {
        if (value == null) {
            throw new IllegalArgumentException("BinaryTree cannot store 'null' values.");
        }
        BinaryNode<T> newNode = construct(value);
        if (root == null) {
            setRoot(newNode);
            return;
        }
        BinaryNode<T> node = root;
        BinaryNode<T> n;
        while (true) {
            int c = value.compareTo(node.value);
            if (c < 0) {
                if ((n = node.getLeftSon()) == null) {
                    node.left = newNode;
                    return;
                } else {
                    node = n;
                }
            } else if (c >= 0) {
                if ((n = node.getRightSon()) == null) {
                    node.right = newNode;
                    return;
                } else {
                    node = n;
                }
            }
        }
    }

    /**
	 * Create string representation of the Tree.  
	 * 
	 * Really only useful for debugging and testCase validation.
	 */
    public String toString() {
        if (root == null) {
            return "()";
        }
        return formatNode(root);
    }

    /**
	 * Format the node, recursively.
	 * 
	 * @param node    desired node to be expressed as a String.
	 * @return
	 */
    private String formatNode(BinaryNode<T> node) {
        BinaryNode<T> n;
        StringBuilder response = new StringBuilder("(");
        if ((n = node.getLeftSon()) != null) {
            response.append(formatNode(n));
        }
        response.append(node.toString());
        if ((n = node.getRightSon()) != null) {
            response.append(formatNode(n));
        }
        response.append(")");
        return response.toString();
    }

    /**
	 * Use in-order traversal over the tree.
	 */
    public Iterator<T> inorder() {
        if (root == null) {
            return empty();
        }
        return new ValueExtractor<T>(new InorderTraversal(root));
    }

    /**
	 * Use pre-order traversal over the tree.
	 */
    public Iterator<T> preorder() {
        if (root == null) {
            return empty();
        }
        return new ValueExtractor<T>(new PreorderTraversal(root));
    }

    /**
	 * Use post-order traversal over the tree.
	 */
    public Iterator<T> postorder() {
        if (root == null) {
            return empty();
        }
        return new ValueExtractor<T>(new PostorderTraversal(root));
    }

    /**
	 * Provide useful in-order iteration over the values of the Binary Tree.
	 */
    public Iterator<T> iterator() {
        return inorder();
    }
}
