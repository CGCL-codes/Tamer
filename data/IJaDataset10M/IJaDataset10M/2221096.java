package com.rapidminer.gui.tools;

import com.rapidminer.gui.MainFrame;

/**
 * This is the update manager used for the community edition. It simply
 * starts a {@link CheckForUpdatesThread}.
 * 
 * @author Sebastian Land, Ingo Mierswa
 */
public class CommunityUpdateManager implements UpdateManager {

    public void checkForUpdates(MainFrame mainframe, boolean showDialog) {
        Thread updateCheckThread = new CheckForUpdatesThread(mainframe, showDialog);
        updateCheckThread.start();
    }
}
