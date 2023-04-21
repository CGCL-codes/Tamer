package net.sf.groofy.match.ui.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolItem;

public class DropdownSelectionListener extends SelectionAdapter {

    private Menu menu;

    /**
	 * Constructs a DropdownSelectionListener
	 * 
	 * @param dropdown
	 *            the dropdown this listener belongs to
	 */
    public DropdownSelectionListener(ToolItem dropdown) {
        menu = new Menu(dropdown.getParent().getShell());
    }

    /**
	 * Adds an item to the dropdown list
	 * 
	 * @param item
	 *            the item to add
	 * @return the created menu item
	 */
    public MenuItem createItem(String item) {
        MenuItem menuItem = new MenuItem(menu, SWT.NONE);
        menuItem.setText(item);
        return menuItem;
    }

    /**
	 * Called when either the button itself or the dropdown arrow is clicked
	 * 
	 * @param event
	 *            the event that trigged this call
	 */
    public void widgetSelected(SelectionEvent event) {
        ToolItem item = (ToolItem) event.widget;
        Rectangle rect = item.getBounds();
        Point pt = item.getParent().toDisplay(new Point(rect.x, rect.y));
        menu.setLocation(pt.x, pt.y + rect.height);
        menu.setVisible(true);
    }
}
