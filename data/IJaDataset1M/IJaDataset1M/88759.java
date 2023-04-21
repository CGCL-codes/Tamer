package hexxa.structs.tree;

import hexxa.handlers.GameHandler;
import hexxa.structs.Player;
import hexxa.structs.list.ListLinked;
import hexxa.structs.misc.Move;

/**
 * This class is the structure of the tree, it acts like a node
 * of a tree and as a root too, it contains list to all the next
 * nodes, and the move it represents.
 * @author David Ach
 */
public class TreeGame {

    /**
	 * The move this node represents
	 */
    private Move _move;

    /**
	 * All the possible next nodes
	 */
    private ListLinked<TreeGame> _treeNodes;

    /**
	 * The priority of this move, according to the owner
	 */
    private int _priority;

    /**
	 * The owner of the tree, it's static because all the tree
	 * nodes has the same.
	 */
    private static Player owner;

    /**
	 * The id of the node, it's here just to count how many nodes
	 * have been created in this tree.
	 */
    private static int id = 0;

    /**
	 * Constructor that initializes the basic parameters
	 * @param move The move of this node
	 */
    public TreeGame(Move move) {
        this._move = move;
        _treeNodes = new ListLinked<TreeGame>();
        this._priority = 0;
        id++;
    }

    /**
	 * Calculate the priority according to the owner
	 */
    public void createPriority() {
        if (owner == GameHandler.getPlayer2()) {
            _priority = GameHandler.getPlayer2().getVirtualCount() - GameHandler.getPlayer1().getVirtualCount();
        } else {
            _priority = GameHandler.getPlayer1().getVirtualCount() - GameHandler.getPlayer2().getVirtualCount();
        }
    }

    public void addTreeNode(TreeGame treeNode) {
        _treeNodes.add(treeNode);
    }

    public ListLinked<TreeGame> getTreeNodes() {
        return _treeNodes;
    }

    public Move getMove() {
        return _move;
    }

    public int getPriority() {
        return _priority;
    }

    public void setPriority(int priority) {
        this._priority = priority;
    }

    public static void setOwner(Player owner) {
        TreeGame.owner = owner;
    }

    public static Player getOwner() {
        return owner;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        TreeGame.id = id;
    }
}
