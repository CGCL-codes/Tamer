package seevolution.tree;

/**
 * A class that contains all the information necessary to identify a position on a tree
 * @author Andres Esteban Marcos
 * @version 1.0
 */
public class TreePosition {

    /**
	 * This node represents the branch on which the position is.	 
	 */
    public TreeNode node;

    /**
	 * A value between 0 and 1 that represents how far down the branch that joins node and its parent the position is, where 0 is at the parent and 1 is at node.
	 */
    public float position;

    /**
	 * Marks the direction on a node path. If true, the node is found on the ascending part of the path.<br>
	 * This doesn't belong in here, but was included to avoid creating a new class with similar functionality
	 */
    public boolean up;

    /**
	 * Creates a new position
	 * @param node The position is in the branch that joins this node with its parent
	 * @param position A value between 0 and 1 that represents how far down the branch the position is.
	 */
    public TreePosition(TreeNode node, float position) {
        this.node = node;
        this.position = position;
        up = false;
    }

    /**
	 * Creates a new position object with information used to describe a node path within the tree
	 * @param node The node
	 * @param up Whether the node is in the ascending or descending part of the path.
	 */
    public TreePosition(TreeNode node, boolean up) {
        this.node = node;
        this.up = up;
        this.position = 0;
    }
}
