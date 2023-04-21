package pl.kane.autokomp.applications.dto;

public class MenuData {

    private MenuItemData[] menuItems;

    public MenuData() {
        super();
    }

    public MenuItemData[] getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(MenuItemData[] menuItems) {
        this.menuItems = menuItems;
    }
}
