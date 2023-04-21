package computational.geometry;

import java.util.*;

public class BSPTree {

    private Node root;

    public BSPTree(PartitionableList l) {
        if (l.size() > 0) root = new Node(l.get(0), l);
    }

    /** return the root of the tree*/
    public Node getRoot() {
        return root;
    }

    public class Node {

        private Node left, right;

        private Object o;

        public Node(Object o, PartitionableList l) {
            this.o = o;
            if (l != null) {
                if (l.size() <= 1) {
                    if (l.size() > 0) {
                        this.left = new Node(null, null);
                        this.right = new Node(null, null);
                    }
                } else {
                    l.divide(0);
                    PartitionableList testl = l.leftPartition();
                    PartitionableList testr = l.rightPartition();
                    if (testr.size() == 0) {
                        this.left = new Node(testl.get(0), testl);
                        this.right = new Node(null, null);
                    } else if (testl.size() == 0) {
                        this.left = new Node(null, null);
                        this.right = new Node(testr.get(0), testr);
                    } else {
                        this.left = new Node(testl.get(0), testl);
                        this.right = new Node(testr.get(0), testr);
                    }
                }
            }
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }

        public Object getNode() {
            return o;
        }
    }
}
