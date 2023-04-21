package freemind.controller;

import javax.swing.Action;
import javax.swing.JMenuItem;

public class StructuredMenuItemHolder {

    private JMenuItem menuItem;

    private Action action;

    private MenuItemEnabledListener enabledListener;

    private MenuItemSelectedListener selectedListener;

    public StructuredMenuItemHolder() {
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
        if (action instanceof MenuItemEnabledListener) {
            MenuItemEnabledListener listener = (MenuItemEnabledListener) action;
            setEnabledListener(listener);
        }
        if (action instanceof MenuItemSelectedListener) {
            MenuItemSelectedListener listener = (MenuItemSelectedListener) action;
            setSelectedListener(listener);
        }
    }

    public MenuItemEnabledListener getEnabledListener() {
        return enabledListener;
    }

    public void setEnabledListener(MenuItemEnabledListener enabledListener) {
        this.enabledListener = enabledListener;
    }

    public JMenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(JMenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public MenuItemSelectedListener getSelectedListener() {
        return selectedListener;
    }

    public void setSelectedListener(MenuItemSelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }
}
