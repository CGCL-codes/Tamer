package linker.list;

import javax.swing.JPopupMenu;

/**
 * The interface of list items.
 * 
 * @version 2008-05-28
 * @author AwakenRz awakenrz@gmail.com
 * @author Tan tan1986@gmail.com
 * 
 */
public interface ListState {

    /**
	 * Get popupMenu by select item.
	 * 
	 * @param listItem
	 *            The selected item.
	 * @return The created popupMenu.
	 */
    JPopupMenu getPopupMenuByItem(ListItem listItem);

    /**
	 * 
	 * @param item
	 *            The selected item.
	 */
    void doubleClickByItem(ListItem item);
}
