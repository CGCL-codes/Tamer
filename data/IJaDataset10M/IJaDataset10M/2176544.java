package astcentric.structure.basic;

/**
 * Call-back interface for internal iteration over an {@link ASTCollection}. 
 */
public interface ASTHandler {

    /**
   * Handles the specified AST of the collection.
   * 
   * @param ast A non-<code>null</code> AST object.
   * @return <code>true</code> if iteration should be finished.
   */
    public boolean handle(AST ast);
}
