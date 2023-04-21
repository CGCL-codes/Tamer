package architecture.ee.web.model.impl;

import java.io.Serializable;
import java.util.List;
import architecture.ee.model.impl.BaseModelObject;
import architecture.ee.web.model.MenuModel;
import architecture.ee.web.ui.menu.Menu;
import architecture.ee.web.ui.menu.MenuItem;

public class MenuModelImpl extends BaseModelObject<Menu> implements Menu, MenuModel {

    private long menuId;

    private String name;

    private String title;

    private String location;

    private boolean enabled;

    private List<MenuItem> menuItems;

    public long getMenuId() {
        return menuId;
    }

    public void setMenuId(long menuId) {
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String localtion) {
        this.location = localtion;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    @Override
    public Serializable getPrimaryKeyObject() {
        return getMenuId();
    }

    @Override
    public String toString() {
        return String.format("{ mentId:%s, name:%s, title=%s, location=%s, enabled=%s, items=%s }", this.getMenuId(), getName(), getTitle(), getLocation(), isEnabled(), getMenuItems());
    }

    public int getObjectType() {
        return 0;
    }

    public int compareTo(Menu o) {
        return 0;
    }

    @Override
    public Object clone() {
        return null;
    }
}
