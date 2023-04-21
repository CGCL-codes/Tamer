package com.traxel.lumbermill.desk;

import java.util.prefs.Preferences;
import javax.swing.Action;
import javax.swing.JMenu;
import ucar.util.prefs.PreferencesExt;

public class MillDesktopControl {

    private final MillDesktop DESKTOP;

    private final WindowMenu WINDOW_MENU;

    private final MillMenu MILL_MENU;

    public MillDesktopControl(PreferencesExt preferences) {
        DESKTOP = new MillDesktop((PreferencesExt) preferences.node("desktop"));
        WINDOW_MENU = new WindowMenu(DESKTOP, preferences);
        MILL_MENU = new MillMenu(this, preferences);
    }

    public MillDesktop getDesktop() {
        return DESKTOP;
    }

    public JMenu getWindowMenu() {
        return WINDOW_MENU;
    }

    public void add(MillFrame frame) {
        WINDOW_MENU.addMillFrame(DESKTOP, frame);
        DESKTOP.add(frame);
    }

    public MillMenu getMillMenu() {
        return MILL_MENU;
    }
}
