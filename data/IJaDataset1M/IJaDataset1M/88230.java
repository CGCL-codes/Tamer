package com.orientechnologies.tools.oexplorer.menu;

import java.awt.event.ActionEvent;
import com.orientechnologies.tools.oexplorer.Application;

public class HelpMenu extends ContextMenu {

    protected HelpMenu() {
        super("Help");
        addSubMenuItem("About...", this, "About");
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("About...")) Application.getInstance().getMainFrame().openAboutWindow(); else return;
        handleActivation(null);
    }

    /**
     * Handle the activation of menus
     */
    public void handleActivation(Object iItemValue) {
    }

    /**
     * SINGLETON PATTERN
     */
    public static HelpMenu getInstance() {
        if (_instance == null) {
            synchronized (HelpMenu.class) {
                if (_instance == null) _instance = new HelpMenu();
            }
        }
        return _instance;
    }

    private static HelpMenu _instance = null;
}
