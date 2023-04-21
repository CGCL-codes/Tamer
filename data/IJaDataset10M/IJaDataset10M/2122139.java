package net.sourceforge.buddi.api.plugin;

import net.roydesign.ui.JScreenMenuItem;

public abstract class BuddiMenuPlugin extends JScreenMenuItem implements BuddiFilePlugin, AutoRefreshPlugin {

    public int getRefreshInterval() {
        return 2;
    }

    @Override
    public String toString() {
        return "Buddi Menu Plugin: " + getDescription();
    }
}
