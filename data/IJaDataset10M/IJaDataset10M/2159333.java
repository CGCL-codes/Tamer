package fr.itris.glips.svgeditor.actions.popup;

import java.util.*;
import fr.itris.glips.svgeditor.*;
import javax.swing.*;
import org.w3c.dom.*;

/**
 * the class of the pop up sub menus
 * @author ITRIS, Jordi SUC
 */
public class PopupSubMenu extends PopupItem {

    /**
	 * the collection of the popup items that are contained in this popup submenu
	 */
    protected LinkedList<PopupItem> subPopupItem = new LinkedList<PopupItem>();

    /**
	 * whether the popup items are enabled or not
	 */
    protected boolean isEnabled = false;

    /**
	 * the constructor of the class
	 * @param editor the editor
	 * @param id the id of this popup item
	 * @param label the label for the popup item
	 * @param iconName the name of an icon
	 */
    public PopupSubMenu(Editor editor, String id, String label, String iconName) {
        super(editor, id, label, iconName);
        menuItem = new JMenu(label);
        if (icon != null) {
            menuItem.setIcon(icon);
        }
        if (disabledIcon != null) {
            menuItem.setDisabledIcon(disabledIcon);
        }
    }

    @Override
    public JMenuItem getPopupItem(LinkedList<Element> nodes) {
        ((JMenu) menuItem).removeAll();
        JMenuItem popupItem = null;
        isEnabled = false;
        for (PopupItem item : subPopupItem) {
            if (item != null) {
                popupItem = item.getPopupItem(nodes);
                if (popupItem != null) {
                    ((JMenu) menuItem).add(popupItem);
                    isEnabled = isEnabled || popupItem.isEnabled();
                }
            }
        }
        return menuItem;
    }

    /**
	 * adds a popup item to this popup sub menu item
	 * @param item a popup item
	 */
    public void addPopupItem(PopupItem item) {
        if (item != null) {
            subPopupItem.add(item);
        }
    }

    @Override
    public void setToInitialState() {
        for (PopupItem item : subPopupItem) {
            if (item != null) {
                item.setToInitialState();
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
