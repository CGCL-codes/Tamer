package org.ebayopensource.twin.element;

import org.ebayopensource.twin.*;

/** 
 * An open menu, exposing a list of items.
 * <p>
 * Note that the element that says e.g. "File" is not a menu, it's a <em>menu item</em> that when clicked 
 * displays the file <em>menu</em>, typically as a child of itself. 
 * <p>
 * In this case, the File MenuItem contains a Menu, and the Menu contains MenuItems.
 * <p>
 * Note that multiple menus can be open at once, in the case of submenus.
 */
public interface Menu extends ControlType {

    /** Get the menu item with the given name */
    public MenuItem item(String name) throws TwinException;

    /** Get the menu item at the given index */
    public MenuItem item(int index) throws TwinException;
}
