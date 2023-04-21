package astcentric.structure.basic;

/**
 * Empty node collection. Can be subclassed. Only 
 * {@link #traverseNodes(NodeHandler)} has to be overridden.
 *
 */
public class EmptyNodeCollection implements NodeCollection {

    public static final NodeCollection EMPTY_COLLECTION = new EmptyNodeCollection();

    public boolean contains(Node node) {
        return NodeTool.contains(this, node);
    }

    public void traverseNodes(NodeHandler handler) {
    }
}
