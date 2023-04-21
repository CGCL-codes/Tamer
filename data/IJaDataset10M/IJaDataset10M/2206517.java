package com.yeep.basis.swing.widget.menu;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

public class MenuItemGroup<X extends JMenuItem, Y extends JSeparator> {

    private List<JComponent> items = new ArrayList<JComponent>();

    /**
	 * Add a menu item
	 * @param x
	 */
    public void addItem(X x) {
        this.items.add(x);
    }

    /**
	 * Add a separator item
	 * @param y
	 */
    public void addItem(Y y) {
        this.items.add(y);
    }

    /**
	 * Get the menu items which belong to this group
	 * @return
	 */
    public List<JComponent> getItems() {
        return this.items;
    }
}
