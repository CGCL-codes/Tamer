package com.nullfish.lib.ui.xml_menu;

import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.jdom.Element;
import com.nullfish.lib.command.CommandCallable;
import com.nullfish.lib.resource.ResourceManager;

/**
 * @author shunji
 *
 */
public class XMLPopupMenu extends JPopupMenu implements XMLMenuElement {

    private ResourceManager resource;

    private CommandCallable callable;

    public XMLPopupMenu() {
    }

    public XMLPopupMenu(ResourceManager resource, CommandCallable callable) {
        this.resource = resource;
        this.callable = callable;
    }

    public void convertFromNode(Element node) {
        removeAll();
        List children = node.getChildren();
        for (int i = 0; i < children.size(); i++) {
            Element child = (Element) children.get(i);
            if (XMLMenu.NODE_SEPARATOR.equals(child.getName())) {
                addSeparator();
            } else {
                XMLMenuElement item = null;
                if (XMLMenu.NODE_NAME.equals(child.getName())) {
                    item = new XMLMenu();
                } else if (XMLMenuItem.NODE_NAME.equals(child.getName())) {
                    item = new XMLMenuItem();
                }
                if (item != null) {
                    item.setCallable(callable);
                    item.setResource(resource);
                    item.convertFromNode(child);
                    add((JMenuItem) item);
                }
            }
        }
    }

    public void setResource(ResourceManager resource) {
        this.resource = resource;
    }

    public void setCallable(CommandCallable callable) {
        this.callable = callable;
    }
}
