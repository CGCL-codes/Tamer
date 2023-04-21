package astcentric.editor.common.control.menu;

/**
 * Interface for inner iteration over all {@link MenuElement MenuElements}
 * of a {@link Menu}.
 */
public interface MenuElementHandler {

    /**
   * Handles the specified menu element.
   * 
   * @return <code>true</code> if iterating should be stopped.
   */
    public boolean handle(MenuElement element);
}
