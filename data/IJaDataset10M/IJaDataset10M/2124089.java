package de.plugmail.plugins.gui;

import de.plugmail.plugins.*;

public abstract class MenuPlugin extends GUIBasePlugin {

    public MenuPlugin() {
        super(null, null);
    }

    public abstract Object getMenu();
}
