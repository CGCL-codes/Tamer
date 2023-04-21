package be.lassi.ui.util;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

public class PopupMenuBuilder {

    private JPopupMenu menu = new JPopupMenu();

    public void add(final LassiAction action) {
        JMenuItem item = menu.add(action);
        Icon icon = action.getDisabledIcon();
        if (icon != null) {
            item.setDisabledIcon(icon);
        }
    }

    public void addSeparator() {
        menu.add(new JSeparator());
    }

    public JPopupMenu getMenu() {
        return menu;
    }
}
