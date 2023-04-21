package org.zeroexchange.web.navigation.menu.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;

/**
 * 
 * @author black
 *
 */
public class Submenu extends MenuItem {

    private static final long serialVersionUID = 1L;

    private List<MenuItem> menuItems;

    @XmlElements({ @XmlElement(name = "item", type = Leaf.class), @XmlElement(name = "submenu", type = Submenu.class) })
    @XmlElementWrapper(name = "items", required = true)
    public List<MenuItem> getMenuItems() {
        if (menuItems == null) {
            menuItems = new ArrayList<MenuItem>();
        }
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
}
