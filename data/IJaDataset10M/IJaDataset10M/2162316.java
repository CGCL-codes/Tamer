package org.andrewberman.ui.menu;

import java.awt.event.KeyEvent;
import org.andrewberman.ui.tools.Tool;

public class ToolDockItem extends DockItem {

    protected Tool tool;

    private String toolString;

    private String shortcutString;

    public void setMenu(Menu menu) {
        super.setMenu(menu);
    }

    public MenuItem setShortcut(String s) {
        shortcutString = s;
        if (tool != null) tool.setShortcut(s);
        return this;
    }

    public void setTool(String toolClass) {
        tool = nearestMenu.context.getToolManager().createTool(getName(), toolClass);
        if (shortcutString != null) tool.setShortcut(shortcutString);
    }

    @Override
    public void keyEvent(KeyEvent e) {
        super.keyEvent(e);
    }

    public String getLabel() {
        return getName() + " (" + tool.getShortcut().label + ")";
    }

    public void performAction() {
        super.performAction();
        nearestMenu.context.tools().switchTool(getName());
    }
}
