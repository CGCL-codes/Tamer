package fr.itris.glips.extension.jwidget.base.runtime;

import javax.swing.*;
import java.util.*;
import org.w3c.dom.*;
import fr.itris.glips.rtda.animaction.Action;
import fr.itris.glips.rtda.jwidget.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * the class of the widget containing the menu items
 * @author ITRIS, Jordi SUC
 */
public class MenuItemsContainerWidget extends JComponent {

    /**
	 * the menuitem widget id
	 */
    protected static final String menuItemWidgetId = "MenuItemWidget";

    /**
	 * the menu widget id
	 */
    protected static final String menuWidgetId = "MenuWidget";

    /**
	 * the separator
	 */
    protected static final String separator = "/";

    /**
	 * the jwidget runtime object to which this component is linked
	 */
    protected JWidgetRuntime jwidgetRuntime;

    /**
	 * whether this version of the menu items container is the menu bar version
	 */
    private boolean isMenuBar = true;

    /**
	 * the component
	 */
    private JComponent component = null;

    /**
	 * the map associating the source id of a menu item to this menu item
	 */
    private Map<String, JMenuItem> menuItems = new HashMap<String, JMenuItem>();

    /**
	 * the map associating a menu item to its listener
	 */
    private Map<JMenuItem, ActionListener> listeners = new HashMap<JMenuItem, ActionListener>();

    /**
	 * the constructor of the class
	 * @param jwidgetRuntime the jwidget runtime object to which this component is linked
	 * @param jwidgetElement the jwidget element
	 */
    public MenuItemsContainerWidget(JWidgetRuntime jwidgetRuntime, Element jwidgetElement) {
        this.jwidgetRuntime = jwidgetRuntime;
        this.isMenuBar = jwidgetElement.getAttribute(JWidgetRuntime.jwidgetNameAttributeName).equals("MenuBarWidget");
        buildMenuItems(jwidgetElement);
    }

    /**
	 * builds the menu items
	 * @param jwidgetElement the jwidget element
	 */
    protected void buildMenuItems(Element jwidgetElement) {
        if (jwidgetElement != null) {
            String sourceId = separator + jwidgetElement.getAttribute(JWidgetRuntime.jwidgetIdAttributeName);
            if (isMenuBar) {
                JMenuBar menuBar = new JMenuBar();
                buildMenu(jwidgetElement, sourceId, menuBar);
                component = menuBar;
            } else {
                JPopupMenu popupMenu = new JPopupMenu();
                buildMenu(jwidgetElement, sourceId, popupMenu);
                popupMenu.pack();
                component = popupMenu;
            }
            JWidgetToolkit.handleLook(jwidgetElement, new HashSet<Component>(menuItems.values()));
        }
    }

    /**
	 * handle the bounds of the menu items container
	 * @param bounds the bounds for the component
	 */
    public void handleBounds(Rectangle2D bounds) {
        if (isMenuBar) {
            Dimension size = new Dimension((int) bounds.getWidth(), (int) bounds.getHeight());
            ((JMenuBar) component).setSize(size);
            ((JMenuBar) component).setPreferredSize(size);
            ((JMenuBar) component).setLocation((int) bounds.getX(), (int) bounds.getY());
        }
    }

    /**
	 * builds the menu for the given jwidget element and menu
	 * @param jwidgetElement a jwidget element
	 * @param parentSourceId the parent source id 
	 * @param parentMenu the menu into which the menu items corresponding to the 
	 * 							given jwidget element should be added
	 */
    protected void buildMenu(Element jwidgetElement, String parentSourceId, Object parentMenu) {
        JMenuItem menuItem = null;
        Element el = null;
        String source = "";
        ActionListener listener = null;
        for (Node node = jwidgetElement.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (node instanceof Element) {
                el = (Element) node;
                source = parentSourceId + separator + el.getAttribute(JWidgetRuntime.jwidgetIdAttributeName);
                if (el.getAttribute(JWidgetRuntime.jwidgetNameAttributeName).equals(menuItemWidgetId)) {
                    menuItem = new JMenuItem(el.getAttribute(JWidgetRuntime.jwidgetLabelAttributeName));
                } else if (el.getAttribute(JWidgetRuntime.jwidgetNameAttributeName).equals(menuWidgetId)) {
                    menuItem = new JMenu(el.getAttribute(JWidgetRuntime.jwidgetLabelAttributeName));
                    buildMenu(el, source, menuItem);
                }
                if (menuItem != null) {
                    menuItems.put(source, menuItem);
                    final Component cmp = menuItem;
                    listener = new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            for (Action action : jwidgetRuntime.getActions()) {
                                if (action.getActionComponent() != null && action.getActionComponent().equals(cmp)) {
                                    action.performAction(e);
                                }
                            }
                        }
                    };
                    listeners.put(menuItem, listener);
                    menuItem.addActionListener(listener);
                    if (parentMenu instanceof JMenu) {
                        ((JMenu) parentMenu).add(menuItem);
                    } else if (parentMenu instanceof JMenuBar) {
                        ((JMenuBar) parentMenu).add(menuItem);
                    } else if (parentMenu instanceof JPopupMenu) {
                        ((JPopupMenu) parentMenu).add(menuItem);
                    }
                }
            }
        }
    }

    /**
	 * @return the componentgenerated by this class
	 */
    public JComponent getComponent() {
        return component;
    }

    /**
	 * returns whether the given source id represents a valid menu item
	 * @param sourceId a source id 
	 * @return whether the given source id represents a valid menu item
	 */
    public boolean containsSourceId(String sourceId) {
        return menuItems.containsKey(sourceId);
    }

    /**
	 * sets whether the menu item denoted by the given id should be enabled or not
	 * @param sourceId the id of a menu item
	 * @param enable whether the menu item denoted by the given id should be enabled or not
	 */
    public void setEnabled(String sourceId, boolean enable) {
        JMenuItem item = menuItems.get(sourceId);
        if (item != null) {
            item.setEnabled(enable);
        }
    }

    /**
	 * returns the menu item corresponding to the given source id
	 * @param sourceId the id of a source
	 * @return the menu item corresponding to the given source id
	 */
    public JMenuItem getItem(String sourceId) {
        return menuItems.get(sourceId);
    }

    /**
	 * @return the map associating the source 
	 * id of a menu item to this menu item
	 */
    public Map<String, JMenuItem> getMenuItems() {
        return menuItems;
    }

    /**
     * registers all the listeners
     */
    public void registerListeners() {
        ActionListener listener;
        for (JMenuItem item : listeners.keySet()) {
            listener = listeners.get(item);
            item.addActionListener(listener);
        }
    }

    /**
     * unregisters all the listeners
     */
    public void unregisterListeners() {
        ActionListener listener;
        for (JMenuItem item : listeners.keySet()) {
            listener = listeners.get(item);
            item.removeActionListener(listener);
        }
    }

    /**
	 * disposes the component
	 */
    public void dispose() {
        ActionListener listener;
        for (JMenuItem item : listeners.keySet()) {
            listener = listeners.get(item);
            item.removeActionListener(listener);
        }
    }
}
