package org.opennms.map.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

public class MapMenu extends Composite {

    private boolean m_adminMenuEnabled = false;

    private boolean m_adminMenuAllowed = false;

    private boolean m_fullScreenDisplayEnabled = false;

    MapMenuConstants constants;

    DeckPanel m_menuBarPanel = new DeckPanel();

    MenuBar m_userMenuBar;

    MenuBar m_adminMenuBar;

    private MenuItem m_nodeMenuItem;

    private MenuItem m_enableEditMenu;

    public MapMenu() {
        constants = (MapMenuConstants) GWT.create(MapMenuConstants.class);
        m_userMenuBar = createMenuBar(false);
        m_adminMenuBar = createMenuBar(true);
        m_menuBarPanel.add(m_userMenuBar);
        m_menuBarPanel.add(m_adminMenuBar);
        initWidget(m_menuBarPanel);
        getIsRoleAdminFromServer();
    }

    public boolean isAdminMenuAllowed() {
        return m_adminMenuAllowed;
    }

    public void setAdminMenuAllowed(boolean adminMenuAllowed) {
        m_adminMenuAllowed = adminMenuAllowed;
        updateMenu();
    }

    public boolean isAdminMenuEnabled() {
        return m_adminMenuEnabled;
    }

    public void setAdminMenuEnabled(boolean adminMenuEnabled) {
        m_adminMenuEnabled = adminMenuEnabled;
        updateMenu();
    }

    public boolean isFullScreenDisplayEnabled() {
        return m_fullScreenDisplayEnabled;
    }

    public void setFillScreenDisplayEnabled(boolean fullScreenDisplayEnabled) {
        m_fullScreenDisplayEnabled = fullScreenDisplayEnabled;
        updateMenu();
    }

    protected void onLoad() {
        updateMenu();
    }

    public void updateMenu() {
        m_nodeMenuItem.setVisible(isAdminMenuEnabled());
        m_enableEditMenu.setText(getEnableMenuItemText());
        if (isAdminMenuAllowed()) {
            showMenu(m_adminMenuBar);
        } else {
            showMenu(m_userMenuBar);
        }
    }

    private void showMenu(MenuBar menuBar) {
        m_menuBarPanel.showWidget(m_menuBarPanel.getWidgetIndex(menuBar));
    }

    private MenuBar createMenuBar(boolean isAdminUser) {
        MenuBar menu = new MenuBar();
        MenuBar menuMap = createMapsSubMenu(isAdminUser);
        menu.addItem(constants.MapMenuItemName(), menuMap);
        if (isAdminUser) {
            MenuBar nodeMenu = createNodesSubMenu();
            m_nodeMenuItem = new MenuItem(constants.NodeMenuItemName(), nodeMenu);
            menu.addItem(m_nodeMenuItem);
        }
        MenuBar menuView = createViewSubMenu();
        menu.addItem(constants.ViewMenuItemName(), menuView);
        MenuBar menuHelp = createHelpSubMenu();
        menu.addItem(constants.HelpMenuItemName(), menuHelp);
        return menu;
    }

    public class SwitchFullScreenCommand implements Command {

        MenuItem m_menuItem;

        public SwitchFullScreenCommand(MenuItem fullScreenMenuItem) {
            m_menuItem = fullScreenMenuItem;
        }

        public void execute() {
            Window.alert("SwitchFullScreen:Old FullScreen? " + isFullScreenDisplayEnabled());
            setFillScreenDisplayEnabled(!isFullScreenDisplayEnabled());
            m_menuItem.setText(getFullScreenMenuItemText());
            Window.alert("SwitchFullScreen:new FullScreen? " + isFullScreenDisplayEnabled());
        }
    }

    public class DummyCommand implements Command {

        public void execute() {
            Window.alert("Menu Item clicked.");
        }
    }

    public MenuBar createMapsSubMenu(boolean isAdminUser) {
        MenuBar menu = new MenuBar(true);
        menu.addItem(constants.OpenMapMenuItemName(), new DummyCommand());
        menu.addItem(constants.CloseMapMenuItemName(), new DummyCommand());
        if (isAdminUser) {
            menu.addItem(constants.NewMapMenuItemName(), new DummyCommand());
            menu.addItem(constants.RenameMapMenuItemName(), new DummyCommand());
            menu.addItem(constants.DeleteMapMenuItemName(), new DummyCommand());
            menu.addItem(constants.SaveMapMenuItemName(), new DummyCommand());
            menu.addItem(constants.ClearMapMenuItemName(), new DummyCommand());
            menu.addItem(constants.SetBgMapMenuItemName(), createSetBGSubMenu());
            m_enableEditMenu = new MenuItem(getEnableMenuItemText(), new Command() {

                public void execute() {
                    boolean before = isAdminMenuEnabled();
                    setAdminMenuEnabled(!isAdminMenuEnabled());
                    Window.alert("before = " + before + " after = " + isAdminMenuEnabled());
                }
            });
            menu.addItem(m_enableEditMenu);
        }
        return menu;
    }

    private String getEnableMenuItemText() {
        return isAdminMenuEnabled() ? constants.RefreshModeMapMenuItemName() : constants.AdminModeMapMenuItemName();
    }

    private MenuBar createNodesSubMenu() {
        MenuBar menu = new MenuBar(true);
        menu.addItem(constants.AddNodeMenuItem(), new DummyCommand());
        menu.addItem(constants.AddByCategoryNodeMenuItem(), new DummyCommand());
        menu.addItem(constants.AddByLabelNodeMenuItem(), new DummyCommand());
        menu.addItem(constants.AddRangeNodeMenuItem(), new DummyCommand());
        menu.addItem(constants.AddNeighNodeMenuItem(), new DummyCommand());
        menu.addItem(constants.AddNodeWithNeighNodeMenuItem(), new DummyCommand());
        menu.addItem(constants.AddMapNodeMenuItem(), new DummyCommand());
        menu.addItem(constants.SetIconNodeMenuItem(), new DummyCommand());
        menu.addItem(constants.DeleteNodeMenuItem(), new DummyCommand());
        return menu;
    }

    private MenuBar createViewSubMenu() {
        MenuBar menu = new MenuBar(true);
        menu.addItem(constants.SetDimensionViewMenuItemName(), new DummyCommand());
        menu.addItem(constants.ViewByMenuItemName(), createViewBySubMenu());
        MenuItem fullScreenMenuItem = new MenuItem(getFullScreenMenuItemText(), (Command) null);
        fullScreenMenuItem.setCommand(new SwitchFullScreenCommand(fullScreenMenuItem));
        menu.addItem(fullScreenMenuItem);
        return menu;
    }

    private String getFullScreenMenuItemText() {
        return isFullScreenDisplayEnabled() ? constants.SetNormalScreenMenuItemName() : constants.SetFullScreenMenuItemName();
    }

    private MenuBar createHelpSubMenu() {
        return new MenuBar(true);
    }

    private MenuBar createSetBGSubMenu() {
        MenuBar menu = new MenuBar(true);
        menu.addItem(constants.SetBgColorMapMenuItemName(), new DummyCommand());
        menu.addItem(constants.SetBgImageMapMenuItemName(), new DummyCommand());
        return menu;
    }

    private MenuBar createViewBySubMenu() {
        MenuBar menu = new MenuBar(true);
        menu.addItem(constants.SeverityViewByMenuItemName(), new DummyCommand());
        menu.addItem(constants.AvailViewByMenuItemName(), new DummyCommand());
        menu.addItem(constants.StatusViewByMenuItemName(), new DummyCommand());
        return menu;
    }

    private void getIsRoleAdminFromServer() {
        MapService.getInstance().isRoleAdmin(new ServerIsRoleAdminUpdater());
    }

    class ServerIsRoleAdminUpdater implements AsyncCallback {

        public void onFailure(Throwable arg0) {
            Window.alert("Failure on download");
        }

        public void onSuccess(Object result) {
            setAdminMenuAllowed(((Boolean) result).booleanValue());
        }
    }
}
