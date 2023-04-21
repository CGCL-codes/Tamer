package edu.stanford.math.plex4.graph.random;

/**
 * Implements a binary tree of nodes labeled 1,2,...,n.
 * 
 * @author Tim Harrington
 * @date Feb 8, 2009
 */
public class BinaryTree {

    protected int n;

    public BinaryTree(int n) {
        this.n = n;
    }

    /**
	 * @return the depth of the deepest node in the tree
	 */
    public int getMaxDepth() {
        int level = 0;
        while (n >> level != 0) {
            level++;
        }
        return level;
    }

    /**
	 * Returns the depth of a node in a breadth-first labeled binary tree rooted
	 * at 1. The depth of node 1 is 0.
	 * 
	 * @param i
	 *            node label
	 * @return the depth of node i in the tree
	 */
    public int getDepth(int i) {
        int level = 0;
        while (i >> level != 0) {
            level++;
        }
        return level - 1;
    }

    /**
	 * @param i
	 *            node label
	 * @param j
	 *            node label
	 * @return the label of the first common ancestor in the tree
	 */
    public int getCommonAncestor(int i, int j) {
        if (i == j) return i;
        int x = Math.min(i, j);
        int y = Math.max(i, j);
        int xD = this.getDepth(x);
        int yD = this.getDepth(y);
        if (xD == yD) {
            int level = 0;
            while (i >> level != j >> level) {
                level++;
            }
            return i >> level;
        }
        int temp = y >> yD - xD;
        if (x == temp) return x;
        int level = 0;
        while (x >> level != y >> (yD - xD + level)) {
            level++;
        }
        return x >> level;
    }

    /**
	 * @param i
	 *            node label
	 * @param j
	 *            node label
	 * @return tree depth of the first common ancestor of i and j
	 */
    public int commonAncestorDepth(int i, int j) {
        int k = this.getCommonAncestor(i, j);
        return this.getDepth(k);
    }
}
