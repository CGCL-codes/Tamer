package astcentric.structure.validation;

/**
 * Call-back interface for internal iteration over a collection of names.
 */
public interface NameHandler {

    /**
   * Handles the specified name.
   * 
   * @param name A non-<code>null</code> name.
   */
    public void handle(String name);
}
