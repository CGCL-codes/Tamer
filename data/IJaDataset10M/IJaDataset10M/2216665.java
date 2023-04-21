package org.jumpmind.pos.ui;

import org.springframework.beans.factory.BeanNameAware;

public class MenuItem implements Comparable<MenuItem>, BeanNameAware {

    private String id = "root";

    private String locationId;

    private String nameId;

    private MenuItemType menuItemType = MenuItemType.MENU;

    private String iconId;

    private String keyStroke = null;

    /**
     * Indicates whether a menu item should be hidden when it is unavailable.
     */
    private boolean hiddenWhenDisabled = true;

    private String activityToExecute = "idleActivity";

    private String screenToShow = null;

    private int displayOrder = 100;

    private int accessLevel = 0;

    private int[] displayStates = null;

    boolean escape = false;

    public MenuItem() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public void setBeanName(String name) {
        if (id == null) {
            id = name;
        }
    }

    public MenuItem(String uniqueId) {
        this.locationId = uniqueId;
    }

    public MenuItem(String uniqueId, int displayOrder) {
        this(uniqueId);
        this.displayOrder = displayOrder;
    }

    public MenuItem(String uniqueId, int displayOrder, MenuItemType type) {
        this(uniqueId, displayOrder);
        this.menuItemType = type;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String uniqueId) {
        this.locationId = uniqueId;
    }

    public void setDisplayState(int storeLevel) {
        this.displayStates = new int[] { storeLevel };
    }

    public void setDisplayStates(int[] displayStates) {
        this.displayStates = displayStates;
    }

    public int[] getDisplayStates() {
        return displayStates;
    }

    public String getNameId() {
        return nameId;
    }

    public void setNameId(String nameId) {
        this.nameId = nameId;
    }

    public MenuItemType getMenuItemType() {
        return menuItemType;
    }

    public void setMenuItemType(MenuItemType type) {
        this.menuItemType = type;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public String getKeyStroke() {
        return keyStroke;
    }

    public void setKeyStroke(String mnemonicKey) {
        this.keyStroke = mnemonicKey;
    }

    public boolean isHiddenWhenDisabled() {
        return hiddenWhenDisabled;
    }

    public void setHiddenWhenDisabled(boolean hiddenWhenDisabled) {
        this.hiddenWhenDisabled = hiddenWhenDisabled;
    }

    public String getActivityToExecute() {
        return activityToExecute;
    }

    public void setActivityToExecute(String activityToExecute) {
        this.activityToExecute = activityToExecute;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public void setEscape(boolean escape) {
        this.escape = escape;
    }

    public boolean isEscape() {
        return escape;
    }

    public void setScreenToShow(String screenToShow) {
        this.screenToShow = screenToShow;
    }

    public String getScreenToShow() {
        return screenToShow;
    }

    @Override
    public int compareTo(MenuItem o) {
        if (o != null) {
            return (this.displayOrder == o.getDisplayOrder()) ? 0 : (this.displayOrder > o.displayOrder ? 1 : -1);
        } else {
            return -1;
        }
    }
}
