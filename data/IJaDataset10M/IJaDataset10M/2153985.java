package net.sf.doolin.gui.action.swing;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import net.sf.doolin.bus.support.SubscriberValidator;
import org.apache.commons.lang.StringUtils;

/**
 * {@link MenuBuilder} for a {@link JMenu}.
 * 
 * @author Damien Coraboeuf
 * 
 */
public class JMenuBuilder extends AbstractJMenuBuilder {

    private final JMenu menu;

    /**
	 * Constructor.
	 * 
	 * @param menu
	 *            Menu to build
	 */
    public JMenuBuilder(JMenu menu) {
        this.menu = menu;
    }

    @Override
    public MenuBuilder createSubMenu(String name, String label) {
        JMenu subMenu = getMenuByName(name);
        if (subMenu != null) {
            return (JMenuBuilder) subMenu.getClientProperty(JMenuBuilder.class);
        } else {
            subMenu = createMenu(name, label);
            this.menu.add(subMenu);
            noSeparator();
            JMenuBuilder menuContainer = new JMenuBuilder(subMenu);
            subMenu.putClientProperty(JMenuBuilder.class, menuContainer);
            return menuContainer;
        }
    }

    /**
	 * Utility method that looks for a sub-menu using the
	 * {@link JMenu#getName() name} of the menu as an identifier.
	 * 
	 * @param name
	 *            Name to find
	 * @return Found {@link JMenu} or <code>null</code>
	 */
    protected JMenu getMenuByName(String name) {
        int count = this.menu.getItemCount();
        for (int i = 0; i < count; i++) {
            JMenuItem menuItem = this.menu.getItem(i);
            if (menuItem instanceof JMenu && StringUtils.equals(name, menuItem.getName())) {
                return (JMenu) menuItem;
            }
        }
        return null;
    }

    /**
	 * Utility method that looks for a menu item using the
	 * {@link JMenuItem#getName() name} of the menu item as an identifier.
	 * 
	 * @param name
	 *            Name to find
	 * @return Found {@link JMenuItem} or <code>null</code>
	 */
    protected JMenuItem getMenuItemByName(String name) {
        int count = this.menu.getItemCount();
        for (int i = 0; i < count; i++) {
            JMenuItem menuItem = this.menu.getItem(i);
            if (menuItem != null && StringUtils.equals(name, menuItem.getName())) {
                return menuItem;
            }
        }
        return null;
    }

    @Override
    public void clear() {
        this.menu.removeAll();
    }

    @Override
    protected void createSeparator() {
        this.menu.addSeparator();
    }

    @Override
    public void add(SubscriberValidator subscriberValidator, Action swingAction) {
        noSeparator();
        JMenuItem menuItem = createMenuItem(subscriberValidator, swingAction);
        this.menu.add(menuItem);
    }
}
