package net.sourceforge.sdm.model;

import java.util.Observable;

/**
 * Tracks if User wont to use BrowserLaunche or just copy URL on clipboard
 * @author Mirko
 *
 */
public class UseBrowserLauncherObservable extends Observable {

    boolean useBrowserLauncher = false;

    public void setUseBrowserLauncher(boolean useBrowserLauncher) {
        this.useBrowserLauncher = useBrowserLauncher;
        super.setChanged();
        notifyObservers();
    }

    public boolean isUseBrowserLauncher() {
        return useBrowserLauncher;
    }
}
