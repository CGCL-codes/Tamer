package imi.scene.utils.traverser;

import imi.scene.PNode;

/**
 * This is the interface needed to process a node during a tree traversal.
 * @author Ronald E Dahlgren
 */
public interface NodeProcessor {

    /**
     * This method processes a given node using any logic that is needed.
     * The return value is used by the TreeTraverser to determine whether or
     * not a branch should be pruned. This is useful for traversing (for instance)
     * only the joints in animation data and not bothering with any meshes
     * that may be hooked onto it.
     * @param currentNode The node currently being processed
     * @return false = PRUNE BRANCH, true = NORMAL PROCESSING
     */
    public abstract boolean processNode(PNode currentNode);
}
