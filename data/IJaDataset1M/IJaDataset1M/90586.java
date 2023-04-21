package sequences;

import gov.nasa.jpf.symbc.Debug;
import gov.nasa.jpf.symbc.Preconditions;
import gov.nasa.jpf.symbc.Symbolic;

/**
 * BST node
 */
class Node {

    public int value;

    public Node left, right;

    public Node(int x) {
        value = x;
        left = null;
        right = null;
    }
}

/**
 *
 * @author Mithun Acharya
 * Taken from Tao Xie's CSC 591t class
 * In fact, this is same as issta2006.BinTree.BinTree.java
 */
public class BST {

    private Node root;

    public BST() {
        root = null;
    }

    public void add(int x) {
        Node current = root;
        if (root == null) {
            root = new Node(x);
            return;
        }
        while (current.value != x) {
            if (x < current.value) {
                if (current.left == null) {
                    current.left = new Node(x);
                } else {
                    current = current.left;
                }
            } else {
                if (current.right == null) {
                    current.right = new Node(x);
                } else {
                    current = current.right;
                }
            }
        }
    }

    public boolean find(int x) {
        Node current = root;
        while (current != null) {
            if (current.value == x) {
                return true;
            }
            if (x < current.value) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return false;
    }

    public boolean remove(int x) {
        Node current = root;
        Node parent = null;
        boolean branch = true;
        while (current != null) {
            if (current.value == x) {
                Node bigson = current;
                while (bigson.left != null || bigson.right != null) {
                    parent = bigson;
                    if (bigson.right != null) {
                        bigson = bigson.right;
                        branch = false;
                    } else {
                        bigson = bigson.left;
                        branch = true;
                    }
                }
                if (parent != null) {
                    if (branch) {
                        parent.left = null;
                    } else {
                        parent.right = null;
                    }
                }
                if (bigson != current) {
                    current.value = bigson.value;
                }
                return true;
            }
            parent = current;
            if (current.value > x) {
                current = current.left;
                branch = true;
            } else {
                current = current.right;
                branch = false;
            }
        }
        return false;
    }
}
