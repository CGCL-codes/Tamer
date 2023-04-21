package Peet;

import System.*;
import System.Collections.*;
import System.Collections.Generic.*;
import SXM.*;

/** @attribute Atomic() */
public class RBTreeNested extends IntSetBenchmark {

    public enum Color {

        BLACK, RED
    }

    ;

    private RBNodeNested root;

    private final RBNodeNested sentinelNode;

    public RBTreeNested() {
        sentinelNode = new RBNodeNested();
        sentinelNode.left = null;
        sentinelNode.right = null;
        sentinelNode.parent = null;
        sentinelNode.color = Color.BLACK;
        sentinelNode.value = Int32.MinValue;
        root = sentinelNode;
        Random random = new Random(this.GetHashCode());
        int size = 0;
        while (size < INITIAL_SIZE) if (Insert(random.Next())) size++;
        XAction.commits -= INITIAL_SIZE;
    }

    public boolean Insert(int key) {
        if (key % 2 == 0) return NestedInsert(key);
        new XAction();
        try {
            return NestedInsert(key);
        } finally {
            XAction.TxFinally();
        }
    }

    /** @attribute Atomic(XKind.Starts) */
    public boolean NestedInsert(int key) {
        RBNodeNested node = new RBNodeNested();
        RBNodeNested temp = root;
        while (temp != sentinelNode) {
            node.parent = temp;
            if (key == temp.value) return false; else if (key > temp.value) temp = temp.right; else temp = temp.left;
        }
        node.value = key;
        node.left = sentinelNode;
        node.right = sentinelNode;
        if (node.parent != null) {
            if (node.value > node.parent.value) node.parent.right = node; else node.parent.left = node;
        } else root = node;
        RestoreAfterInsert(node);
        return true;
    }

    /** @attribute Atomic(XKind.Starts) */
    public boolean Contains(int key) {
        RBNodeNested node = root;
        while (node != sentinelNode) {
            if (key == node.value) return true; else if (key < node.value) node = node.left; else node = node.right;
        }
        return false;
    }

    /** @attribute Atomic(XKind.Starts) */
    public boolean Remove(int key) {
        RBNodeNested node = root;
        while (node != sentinelNode) {
            if (key == node.value) break; else if (key < node.value) node = node.left; else node = node.right;
        }
        if (node == sentinelNode) return false;
        Delete(node);
        return true;
    }

    public IEnumerator GetEnumerator() {
        return new Enumerator(root, sentinelNode);
    }

    /** @attribute Atomic(XKind.Uses) */
    public void Validate() {
        super.Validate();
        if (sentinelNode != root && Color.BLACK != root.color) Console.WriteLine("ERROR: Tree root is wrong color!");
        int blackNodes = CountBlackNodes(root);
        RecursiveValidate(root, blackNodes, 0);
    }

    /** @attribute Atomic(XKind.Uses) */
    private void Delete(RBNodeNested z) {
        RBNodeNested x = new RBNodeNested();
        RBNodeNested y;
        if (z.left == sentinelNode || z.right == sentinelNode) y = z; else {
            y = z.right;
            while (y.left != sentinelNode) y = y.left;
        }
        if (y.left != sentinelNode) x = y.left; else x = y.right;
        x.parent = y.parent;
        if (y.parent != null) if (y == y.parent.left) y.parent.left = x; else y.parent.right = x; else root = x;
        if (y != z) z.value = y.value;
        if (y.color == Color.BLACK) RestoreAfterDelete(x);
    }

    /** @attribute Atomic(XKind.Uses) */
    private void RestoreAfterDelete(RBNodeNested x) {
        RBNodeNested y;
        while (x != root && x.color == Color.BLACK) {
            if (x == x.parent.left) {
                y = x.parent.right;
                if (y.color == Color.RED) {
                    y.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    RotateLeft(x.parent);
                    y = x.parent.right;
                }
                if (y.left.color == Color.BLACK && y.right.color == Color.BLACK) {
                    y.color = Color.RED;
                    x = x.parent;
                } else {
                    if (y.right.color == Color.BLACK) {
                        y.left.color = Color.BLACK;
                        y.color = Color.RED;
                        RotateRight(y);
                        y = x.parent.right;
                    }
                    y.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    y.right.color = Color.BLACK;
                    RotateLeft(x.parent);
                    x = root;
                }
            } else {
                y = x.parent.left;
                if (y.color == Color.RED) {
                    y.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    RotateRight(x.parent);
                    y = x.parent.left;
                }
                if (y.right.color == Color.BLACK && y.left.color == Color.BLACK) {
                    y.color = Color.RED;
                    x = x.parent;
                } else {
                    if (y.left.color == Color.BLACK) {
                        y.right.color = Color.BLACK;
                        y.color = Color.RED;
                        RotateLeft(y);
                        y = x.parent.left;
                    }
                    y.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    y.left.color = Color.BLACK;
                    RotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.color = Color.BLACK;
    }

    /** @attribute Atomic(XKind.Uses) */
    private void RestoreAfterInsert(RBNodeNested x) {
        RBNodeNested y;
        while (x != root && x.parent.color == Color.RED) {
            if (x.parent == x.parent.parent.left) {
                y = x.parent.parent.right;
                if (y != null && y.color == Color.RED) {
                    x.parent.color = Color.BLACK;
                    y.color = Color.BLACK;
                    x.parent.parent.color = Color.RED;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.right) {
                        x = x.parent;
                        RotateLeft(x);
                    }
                    x.parent.color = Color.BLACK;
                    x.parent.parent.color = Color.RED;
                    RotateRight(x.parent.parent);
                }
            } else {
                y = x.parent.parent.left;
                if (y != null && y.color == Color.RED) {
                    x.parent.color = Color.BLACK;
                    y.color = Color.BLACK;
                    x.parent.parent.color = Color.RED;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.left) {
                        x = x.parent;
                        RotateRight(x);
                    }
                    x.parent.color = Color.BLACK;
                    x.parent.parent.color = Color.RED;
                    RotateLeft(x.parent.parent);
                }
            }
        }
        root.color = Color.BLACK;
    }

    /** @attribute Atomic(XKind.Uses) */
    public void RotateLeft(RBNodeNested x) {
        RBNodeNested y = x.right;
        x.right = y.left;
        if (y.left != sentinelNode) y.left.parent = x;
        if (y != sentinelNode) y.parent = x.parent;
        if (x.parent != null) {
            if (x == x.parent.left) x.parent.left = y; else x.parent.right = y;
        } else root = y;
        y.left = x;
        if (x != sentinelNode) x.parent = y;
    }

    /** @attribute Atomic(XKind.Uses) */
    public void RotateRight(RBNodeNested x) {
        RBNodeNested y = x.left;
        x.left = y.right;
        if (y.right != sentinelNode) y.right.parent = x;
        if (y != sentinelNode) y.parent = x.parent;
        if (x.parent != null) {
            if (x == x.parent.right) x.parent.right = y; else x.parent.left = y;
        } else root = y;
        y.right = x;
        if (x != sentinelNode) x.parent = y;
    }

    /** @attribute Atomic(XKind.Uses) */
    private int CountBlackNodes(RBNodeNested root) {
        if (sentinelNode == root) return 0;
        int me = (root.color == Color.BLACK) ? 1 : 0;
        RBNodeNested left = (sentinelNode == root.left) ? sentinelNode : root.left;
        return me + CountBlackNodes(left);
    }

    /** @attribute Atomic(XKind.Uses) */
    private int Count(RBNodeNested root) {
        if (root == sentinelNode) return 0;
        return 1 + Count(root.left) + Count(root.right);
    }

    /** @attribute Atomic(XKind.Uses) */
    private void RecursiveValidate(RBNodeNested root, int blackNodes, int soFar) {
        if (sentinelNode == root) return;
        Color rootcolor = root.color;
        soFar += ((Color.BLACK == rootcolor) ? 1 : 0);
        root.marked = true;
        RBNodeNested left = root.left;
        if (sentinelNode != left) {
            if (left.color == Color.RED && rootcolor == Color.RED) {
                Console.WriteLine("ERROR: Two consecutive red nodes!");
                return;
            }
            if (left.value >= root.value) {
                Console.WriteLine("ERROR: Tree values out of order!");
                return;
            }
            if (left.marked) {
                Console.WriteLine("ERROR: Cycle in tree structure!");
                return;
            }
            RecursiveValidate(left, blackNodes, soFar);
        }
        RBNodeNested right = root.right;
        if (sentinelNode != right) {
            if (right.color == Color.RED && rootcolor == Color.RED) {
                Console.WriteLine("ERROR: Two consecutive red nodes!");
                return;
            }
            if (right.value <= root.value) {
                Console.WriteLine("ERROR; Tree values out of order!");
                return;
            }
            if (right.marked) {
                Console.WriteLine("ERROR: Cycle in tree structure!");
                return;
            }
            RecursiveValidate(right, blackNodes, soFar);
        }
        if (sentinelNode == root.left || sentinelNode == root.right) if (soFar != blackNodes) {
            Console.WriteLine("Variable number of black nodes to leaves!");
            return;
        }
    }

    public class Enumerator implements IEnumerator {

        private final Stack<RBNodeNested> stack;

        private final RBNodeNested root, sentinelNode;

        private boolean first;

        public Enumerator(RBNodeNested root, RBNodeNested sentinelNode) {
            this.sentinelNode = sentinelNode;
            this.root = root;
            this.stack = new Stack<RBNodeNested>();
            RBNodeNested node = root;
            while (node != sentinelNode) {
                stack.Push(node);
                node = node.left;
            }
            first = true;
        }

        public void Reset() {
            stack.Clear();
            stack.Push(root);
        }

        /** @attribute Atomic(XKind.Uses) */
        public Object get_Current() {
            return (Int32) stack.Peek().value;
        }

        /** @attribute Atomic(XKind.Uses) */
        public boolean MoveNext() {
            if (stack.get_Count() == 0) return false;
            if (first) {
                first = false;
                return true;
            }
            RBNodeNested node = stack.Peek();
            if (node.right != sentinelNode) {
                node = node.right;
                while (node != sentinelNode) {
                    stack.Push(node);
                    node = node.left;
                }
            } else {
                RBNodeNested seen = sentinelNode;
                while (node.right == seen) {
                    seen = stack.Pop();
                    if (stack.get_Count() == 0) return false; else node = stack.Peek();
                }
            }
            return true;
        }
    }

    /** @attribute Atomic() */
    public class RBNodeNested {

        public int value;

        public Color color;

        public boolean marked;

        public RBNodeNested parent, left, right;

        public RBNodeNested() {
            value = 0;
            color = Color.RED;
            parent = null;
            left = null;
            right = null;
        }
    }
}
