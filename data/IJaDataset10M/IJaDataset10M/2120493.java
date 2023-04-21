package com.mekmaker.common;

import com.mekmaker.gui.MainJFrame;

/**
 *
 * @author nate
 */
public class MainWindowManager {

    private static MainJFrame window = null;

    /** Creates a new instance of MainWindowManager */
    private MainWindowManager() {
    }

    public static void setMainFrame(MainJFrame window) {
        MainWindowManager.window = window;
    }

    public static boolean isMainFrameAvailable() {
        return window != null;
    }

    public static MainJFrame getMainFrame() {
        return window;
    }
}
