package sushmu.sted.ui;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import sushmu.sted.Main;
import sushmu.sted.util.Resources;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Logger;

public class MenuHandler extends DefaultHandler {

    private static final Map actions = new HashMap();

    private static final Map imageIcons = new HashMap();

    private static final Map toolTips = new HashMap();

    private static final Map toolButtons = new HashMap();

    private static final Map menuItems = new HashMap();

    private static final Map menus = new HashMap();

    private static final Map popupMenus = new HashMap();

    private static MenuHandler loadMenuBar = null;

    private static final Logger logger = Logger.getLogger("sushmu.sted.ui.MenuHandler");

    private static final JMenuBar jMenuBar = new JMenuBar();

    private static final JToolBar jToolBar = new JToolBar(JToolBar.HORIZONTAL);

    private JPopupMenu popupMenu = null;

    private final Stack stack = new Stack();

    private MenuHandler() {
        super();
    }

    public static synchronized MenuHandler getInstance(final String xml) throws ParserConfigurationException, SAXException, IOException {
        if (loadMenuBar == null) {
            loadMenuBar = new MenuHandler();
            MenuHandler.loadMenu(xml);
        }
        return loadMenuBar;
    }

    private static void loadMenu(final String xml) throws SAXException, ParserConfigurationException, IOException {
        final SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setValidating(true);
        final SAXParser saxParser = saxParserFactory.newSAXParser();
        saxParser.parse(ClassLoader.getSystemResourceAsStream(xml), loadMenuBar);
    }

    public static JMenuBar getMenuBar() {
        return jMenuBar;
    }

    public static JToolBar getToolBar() {
        jToolBar.setOrientation(JToolBar.HORIZONTAL);
        jToolBar.setFloatable(false);
        jToolBar.setRollover(true);
        jToolBar.add(Box.createVerticalGlue());
        return jToolBar;
    }

    public static Map getTooltips() {
        return toolTips;
    }

    public static Map getActions() {
        return actions;
    }

    public static Map getImageIcons() {
        return imageIcons;
    }

    public static Action getAction(final String name) {
        return (Action) actions.get(name);
    }

    public static AbstractButton getToolButton(final String name) {
        return (AbstractButton) toolButtons.get(name);
    }

    public static JMenu getMenu(final String name) {
        return (JMenu) menus.get(name);
    }

    public static JMenuItem getMenuItem(final String name) {
        return (JMenuItem) menuItems.get(name);
    }

    public static void addMenuItem(final JMenuItem menuItem) {
        if (!menuItems.containsKey(menuItem.getName())) {
            menuItems.put(menuItem.getName(), menuItem);
        }
    }

    public static JPopupMenu getPopupMenu(final String name) {
        return (JPopupMenu) popupMenus.get(name);
    }

    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
        if ("menu".equals(qName)) {
            try {
                stack.push(createMenu(attributes));
            } catch (Exception e) {
                logger.severe("Unable to create Menu Item: " + e.getMessage());
                Main.showMessage("Unable to create Menu Item: " + e.getMessage());
                e.printStackTrace();
            }
        } else if ("popup_menu".equals(qName)) {
            popupMenu = createPopupMenu(attributes);
        } else if ("menuitem".equals(qName)) {
            if (popupMenu == null) {
                final JMenu menu = (JMenu) stack.peek();
                menu.add(createMenuItem(attributes));
            } else {
                popupMenu.add(createMenuItem(attributes));
            }
        } else if ("menuitemref".equals(qName)) {
            if (popupMenu == null) {
                final JMenu menu = (JMenu) stack.peek();
                menu.add(createMenuItemRef(attributes));
            } else {
                popupMenu.add(createMenuItemRef(attributes));
            }
        } else if ("seperator".equals(qName)) {
            if (popupMenu == null) {
                final JMenu menu = (JMenu) stack.peek();
                menu.addSeparator();
            } else {
                popupMenu.addSeparator();
            }
        }
    }

    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        if ("menu".equals(qName)) {
            final JMenu menu = (JMenu) stack.pop();
            if (stack.isEmpty()) {
                jToolBar.add(Box.createHorizontalStrut(5));
                jMenuBar.add(menu);
            } else {
                final JMenu parent = (JMenu) stack.peek();
                parent.add(menu);
            }
            menus.put(menu.getName(), menu);
        } else if ("popup_menu".equals(qName)) {
            popupMenus.put(popupMenu.getName(), popupMenu);
        }
    }

    private static JMenu createMenu(final Attributes attributes) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        final JMenu menu = new JMenu();
        final String name = attributes.getValue("name");
        menu.setName(name);
        menu.setText(name);
        final String mnemonic = attributes.getValue("mnemonic");
        menu.setMnemonic(mnemonic.charAt(0));
        final String actionName = attributes.getValue("action");
        if (null != actionName) {
            final Action action = (Action) Class.forName(actionName).newInstance();
            action.putValue(Action.NAME, name);
            action.putValue(Action.MNEMONIC_KEY, new Integer((int) mnemonic.charAt(0)));
            menu.setAction(action);
            actions.put(name, action);
        }
        menu.setEnabled(Boolean.valueOf(attributes.getValue("actionEnabled")).booleanValue());
        return menu;
    }

    private static JPopupMenu createPopupMenu(final Attributes attributes) {
        final JPopupMenu menu = new JPopupMenu(attributes.getValue("name"));
        menu.setName(attributes.getValue("name"));
        return menu;
    }

    private static JMenuItem createMenuItemRef(final Attributes attributes) {
        final JMenuItem menuItem = getMenuItem(attributes.getValue("name"));
        final JMenuItem cloned = new JMenuItem(menuItem.getAction());
        cloned.setName(menuItem.getName());
        cloned.setText(menuItem.getText());
        cloned.setSelected(menuItem.isSelected());
        cloned.setHorizontalTextPosition(menuItem.getHorizontalTextPosition());
        final ItemListener[] itemListeners = menuItem.getItemListeners();
        if (itemListeners != null) {
            for (int i = 0; i < itemListeners.length; i++) {
                final ItemListener itemListener = itemListeners[i];
                cloned.addItemListener(itemListener);
            }
        }
        return cloned;
    }

    private static JMenuItem createMenuItem(final Attributes attributes) {
        JMenuItem menuItem = null;
        try {
            final String name = attributes.getValue("name");
            final String type = attributes.getValue("type");
            final String ic = attributes.getValue("icon");
            final String tooltip = attributes.getValue("tooltip");
            toolTips.put(name, tooltip);
            final char mnemonic = attributes.getValue("mnemonic").charAt(0);
            final Action action = (Action) Class.forName(attributes.getValue("action")).newInstance();
            action.putValue(Action.NAME, name);
            if (ic != null) {
                final Icon icon = Resources.getSystemResourceIcon(ic);
                imageIcons.put(name, icon);
                action.putValue(Action.SMALL_ICON, icon);
            }
            action.putValue(Action.SHORT_DESCRIPTION, tooltip);
            action.putValue(Action.MNEMONIC_KEY, new Integer((int) mnemonic));
            action.putValue(Action.ACCELERATOR_KEY, getAccelerator(attributes.getValue("accelerator")));
            final String cmd = attributes.getValue("actionCommand");
            action.putValue(Action.ACTION_COMMAND_KEY, cmd);
            final String listener = attributes.getValue("listener");
            if (listener != null) {
            }
            final String enabled = attributes.getValue("actionEnabled");
            if (enabled != null) {
                action.setEnabled(Boolean.valueOf(enabled).booleanValue());
            }
            menuItem = (JMenuItem) Class.forName(type).newInstance();
            menuItem.setHorizontalTextPosition(JMenuItem.RIGHT);
            menuItem.setAction(action);
            menuItem.setSelected("on".equalsIgnoreCase(attributes.getValue("actionMode")));
            actions.put(name, action);
            menuItems.put(name, menuItem);
            final String button = attributes.getValue("toolButton");
            final String buttonVisible = attributes.getValue("toolButtonVisible");
            if ("true".equalsIgnoreCase(buttonVisible)) {
                final JComponent component = (JComponent) Class.forName(button).newInstance();
                component.setToolTipText(tooltip);
                if (AbstractButton.class.isInstance(component)) {
                    final AbstractButton abstractButton = (AbstractButton) component;
                    abstractButton.setAction(action);
                    if (ItemListener.class.isInstance(action)) {
                        abstractButton.addItemListener((ItemListener) action);
                    }
                }
                jToolBar.add(component);
                toolButtons.put(name, component);
            }
            if (ItemListener.class.isInstance(action)) {
                menuItem.addItemListener((ItemListener) action);
            }
        } catch (InstantiationException e) {
            logger.severe("Unable to create Menu Item: " + e.getMessage());
            Main.showMessage("Unable to create Menu Item: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            logger.severe("Unable to create Menu Item: " + e.getMessage());
            Main.showMessage("Unable to create Menu Item: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            logger.severe("Unable to create Menu Item: " + e.getMessage());
            Main.showMessage("Unable to create Menu Item: " + e.getMessage());
            e.printStackTrace();
        }
        return menuItem;
    }

    private static KeyStroke getAccelerator(final String key) {
        if (key != null && key.length() > 0) {
            return KeyStroke.getKeyStroke(key);
        }
        return null;
    }
}
