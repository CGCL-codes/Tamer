package de.esoco.ewt.component;

import de.esoco.ewt.UserInterfaceContext;
import de.esoco.ewt.component.menu.MenuBar;

/********************************************************************
 * This is the base interface for views. Views are top-level windows that
 * contain other microEWT components. Instances of view implementations are
 * created through the corresponding methods in the {@link UserInterfaceContext}
 * class.
 */
public interface View extends Container {

    /***************************************
	 * Returns the element's title.
	 *
	 * @return The title string
	 */
    public String getTitle();

    /***************************************
	 * Returns the maximized state of this view.
	 *
	 * @return The maximized state
	 */
    public boolean isMaximized();

    /***************************************
	 * Validates and resizes this view to it's preferred size.
	 */
    public void pack();

    /***************************************
	 * Sets the maximized state of this view. A maximized view fills the
	 * available view area of the underlying toolkit completely. This will also
	 * cause a repaint of this view.
	 *
	 * @param bMaximized The new maximized state
	 */
    public void setMaximized(boolean bMaximized);

    /***************************************
	 * Sets this view's menu bar.
	 *
	 * @param rMenuBar The new menu bar
	 */
    public void setMenuBar(MenuBar rMenuBar);

    /***************************************
	 * Sets the element's title.
	 *
	 * @param sTitle The new title
	 */
    public void setTitle(String sTitle);
}
