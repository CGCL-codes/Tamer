package astcentric.structure.basic;

/**
 * Call-back interface for internal iteration of {@link Node} collections. 
 */
public interface NodeHandler {

    /**
   * Handles the specified node of the collection. When this method is
   * called the {@link AST} to which <code>node</code> belongs 
   * has usually a read lock. As a consequence it isn't 
   * possible to change the AST inside this method without leading to
   * a blocking state.
   * 
   * @param node A non-<code>null</code> Node object.
   * @return <code>true</code> if iteration should be finished.
   */
    public boolean handle(Node node);
}
