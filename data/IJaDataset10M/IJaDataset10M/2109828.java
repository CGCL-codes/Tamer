package org.rt.engine;

import java.util.*;

/**
 * Maintains two queues of nodes that have not been visited, 
 * one for forward, and one for backward.
 */
public class ProofQueue {

    /** Queue containing all unexplored backwardNodes. */
    LinkedList<RoleNode> backwardQueue = new LinkedList<RoleNode>();

    /** Queue containing all unexplored forwardNodes. */
    LinkedList<Node> forwardQueue = new LinkedList<Node>();

    /**
     * Returns a boolean expressing whether there are any unexplored
     * backward nodes left in the queue.
     * @return a boolean, which is true when there are unexplored nodes in the queue
     */
    public boolean hasBackwardNodes() {
        return !backwardQueue.isEmpty();
    }

    /**
     * Returns a boolean expressing whether there are any unexplored
     * forward nodes left in the queue.
     * @return a boolean, which is true when there are unexplored nodes in the queue
     */
    public boolean hasForwardNodes() {
        return !forwardQueue.isEmpty();
    }

    /**
     * Retrieves and removes the next unexplored backward node.
     * @return a RoleNode that is the next unexplored backward node
     */
    public RoleNode nextBackwardNode() {
        return backwardQueue.removeFirst();
    }

    /**
     * Retrieves and removes the next unexplored forward node.
     * @return a PrincipalNode that is the next unexplored forward node
     */
    public Node nextForwardNode() {
        return forwardQueue.removeFirst();
    }

    /**
     * Inserts a backward node into the back of the queue.
     * @param node the RoleNode to be inserted
     */
    public void addBackwardNode(RoleNode node) {
        backwardQueue.add(node);
    }

    /**
     * Inserts a forward node into the back of the queue.
     * @param node the PrincipalNode to be inserted
     */
    public void addForwardNode(Node node) {
        forwardQueue.add(node);
    }
}
