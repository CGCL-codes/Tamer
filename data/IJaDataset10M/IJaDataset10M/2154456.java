package astcentric.structure.basic;

/**
 * An ordered collection of {@link Node Nodes}.
 */
public interface NodeCollection {

    /**
   * Traverses over all nodes of the collection. 
   * Iteration is stopped if <code>handler.handle()</code>
   * returns <code>true</code>.
   * 
   * @param handler A non-<code>null</code> argument.
   */
    public void traverseNodes(NodeHandler handler);

    /**
   * Returns <code>true</code> if this collection contains the original node 
   * of the specified node. That is, <code>true</code> is returned if the
   * {@link NodeHandler} of {@link #traverseNodes(NodeHandler)} is invoked
   * with a node which has the same original node as <code>node</code>. 
   */
    public boolean contains(Node node);
}
