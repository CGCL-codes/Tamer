package net.dadajax.xml;

import net.dadajax.languagemanager.LanguageManager;
import net.dadajax.languagemanager.LanguageManagerImpl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author dadajax
 *
 */
public class XmlSwtMenuBuilder extends XmlBase {

    private static final String ROOT_ELEMENT = "menubar";

    private static final String COMMAND_DATA = "command";

    private Document document;

    private SelectionListener listener;

    private Decorations parent;

    public XmlSwtMenuBuilder(String fileName, SelectionListener listener, Decorations parent) {
        super(fileName, ROOT_ELEMENT);
        document = getDocument();
        this.listener = listener;
        this.parent = parent;
    }

    public Menu getMenu() {
        Menu menuBar = new Menu(parent, SWT.BAR);
        NodeList menus = document.getElementsByTagName("menu");
        for (int i = 0; i < menus.getLength(); i++) {
            MenuItem menuHeader = new MenuItem(menuBar, SWT.CASCADE);
            menuHeader.setText(getName(menus.item(i)));
            Menu menu = new Menu(parent, SWT.DROP_DOWN);
            menuHeader.setMenu(menu);
            NodeList menuItems = menus.item(i).getChildNodes();
            for (int ii = 0; ii < menuItems.getLength(); ii++) {
                if (menuItems.item(ii).getNodeName().equals("item")) {
                    MenuItem item = new MenuItem(menu, SWT.PUSH);
                    item.setText(getName(menuItems.item(ii)));
                    item.setData(COMMAND_DATA, getMenuItemCommand(menuItems.item(ii)));
                    item.addSelectionListener(listener);
                }
            }
        }
        return menuBar;
    }

    /**
	 * Return menu with given name, or null if no menu is found.
	 * @param name Name of menu.
	 * @return Menu with given name, or null if no menu is found.
	 */
    public Menu getPopupMenu(String name, Decorations parent) {
        Menu menu = new Menu(parent, SWT.POP_UP);
        NodeList menus = document.getElementsByTagName("menu");
        for (int i = 0; i < menus.getLength(); i++) {
            if (getRawName(menus.item(i)).equals(name)) {
                NodeList menuItems = menus.item(i).getChildNodes();
                for (int ii = 0; ii < menuItems.getLength(); ii++) {
                    if (menuItems.item(ii).getNodeName().equals("item")) {
                        MenuItem item = new MenuItem(menu, SWT.PUSH);
                        item.setText(getName(menuItems.item(ii)));
                        item.setData(COMMAND_DATA, getMenuItemCommand(menuItems.item(ii)));
                        item.addSelectionListener(listener);
                    }
                }
            }
        }
        return menu;
    }

    /**
	 * Get <b>translated</b> name of given node.
	 * @param node 
	 * @return translated name of node.
	 */
    private String getName(Node node) {
        String name = node.getAttributes().getNamedItem("name").getNodeValue();
        LanguageManager lm = LanguageManagerImpl.getInstance();
        String locName = lm.loc(name);
        return locName;
    }

    /**
	 * Get <b>raw</b> name of given node.
	 * @param node
	 * @return raw name of node.
	 */
    private String getRawName(Node node) {
        String name = node.getAttributes().getNamedItem("name").getNodeValue();
        return name;
    }

    private String getMenuItemCommand(Node node) {
        NodeList nl = node.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeName().equals("command")) {
                String command = nl.item(i).getAttributes().getNamedItem("name").getNodeValue();
                if (!command.equals("")) return command;
            }
        }
        return "";
    }
}
