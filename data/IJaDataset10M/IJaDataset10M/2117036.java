package msgnet;

/**
 * Interface for all node factory implementations.
 *
 * You must implement a subtype of this class for real applications to specify
 * how new nodes are created.
 */
public interface NodeFactory {

    /**
     * Returns a new node.
     *
     * Implement this method by generating a new node and returning it.
     *
     * @param x The horizontal position of the new node.
     * @param y The vertical position of the new node.
     * @return The new node.
     */
    public Node newNode(int x, int y);
}
