package net.flysource.client.gui.source;

import javax.swing.*;

public class BaseSource {

    private int type;

    private String displayName;

    private ImageIcon selectedIcon;

    private ImageIcon unselectedIcon;

    public BaseSource() {
        displayName = "";
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ImageIcon getSelectedIcon() {
        return selectedIcon;
    }

    public void setSelectedIcon(ImageIcon selectedIcon) {
        this.selectedIcon = selectedIcon;
    }

    public ImageIcon getUnselectedIcon() {
        return unselectedIcon;
    }

    public void setUnselectedIcon(ImageIcon unselectedIcon) {
        this.unselectedIcon = unselectedIcon;
    }
}
