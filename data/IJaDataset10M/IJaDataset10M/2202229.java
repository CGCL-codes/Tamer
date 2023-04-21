package com.amazonaws.eclipse.ec2.utils;

/**
 * Interface for defining Menus
 */
public interface IMenu {

    public class MenuItem {

        public static final MenuItem SEPARATOR = new MenuItem("SEPARATOR", "Separator");

        private String menuId;

        private String menuText;

        private boolean checked;

        public MenuItem(String menuId, String menuText) {
            this.menuId = menuId;
            this.menuText = menuText;
        }

        public MenuItem(String menuText, boolean checked) {
            this.menuText = menuText;
            this.checked = checked;
        }

        public String getMenuId() {
            return menuId;
        }

        public void setMenuId(String menuId) {
            this.menuId = menuId;
        }

        public String getMenuText() {
            return menuText;
        }

        public void setMenuText(String menuText) {
            this.menuText = menuText;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }
    }

    /**
	 * Callback function for Caller when the MenuItem is clicked
	 * 
	 * @param menuItemSelected The selected MenuItem
	 */
    public void menuClicked(MenuItem menuItemSelected);
}
