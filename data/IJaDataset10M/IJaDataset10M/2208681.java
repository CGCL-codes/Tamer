package sk.bielyvlk.vlkui;

public class MenuEvent {

    private VlkMenu vlkMenu;

    private VlkItem menuItem;

    public MenuEvent(VlkMenu vlkMenu, VlkItem menuItem) {
        this.vlkMenu = vlkMenu;
        this.menuItem = menuItem;
    }

    public VlkMenu getMenu() {
        return vlkMenu;
    }

    public VlkItem getMenuItem() {
        return menuItem;
    }
}
