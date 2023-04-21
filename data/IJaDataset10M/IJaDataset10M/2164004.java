package be.lassi.ui.util;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import be.lassi.base.Holder;

/**
 * Helps building menus for the Mac platform.  Mac menus do not have
 * icons, so any icons provided by the actions are nulled out.
 */
public class MacMenuBuilder {

    private final JMenu menu;

    /**
     * Constructs a new instance.
     *
     * @param text the menu name
     */
    public MacMenuBuilder(final String text) {
        menu = new JMenu(text);
    }

    /**
     * Adds a menu item, nulls out any icon provided by the action.
     *
     * @param action the action for the menu item to be added
     */
    public void add(final LassiAction action) {
        JMenuItem item = menu.add(action);
        item.setIcon(null);
    }

    /**
     * Adds a menu item with a checkbox that indicates whether the item
     * is selected or not selected, nulls out any icon provided by the action.
     *
     * @param action the action for the menu item to be added
     * @param selected indicates whether the item is selected
     */
    public void add(final LassiAction action, final Holder<Boolean> selected) {
        JCheckBoxMenuItem item = ComponentUtil.buildCheckBoxMenuItem(action, selected);
        menu.add(item);
        item.setIcon(null);
    }

    /**
     * Adds a menu item, nulls out any icon provided in the menu item.
     *
     * @param item the menu item to be added
     */
    public void add(final JMenuItem item) {
        menu.add(item);
        item.setIcon(null);
    }

    /**
     * Adds a separator between menu items.
     */
    public void addSeparator() {
        menu.addSeparator();
    }

    /**
     * Gets the menu that was build so far.
     *
     * @return the menu
     */
    public JMenu getMenu() {
        return menu;
    }
}
