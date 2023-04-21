package org.warko.dapp;

import org.warko.app.Application;
import org.warko.app.ApplicationArchitecture;
import org.warko.app.ApplicationNotificationManager;
import org.warko.app.ApplicationOS;
import org.warko.app.ApplicationPreferencesManager;
import org.warko.app.ApplicationSystemManager;
import org.warko.app.ApplicationSystemTrayManager;

public class GenericSystemManager implements ApplicationSystemManager {

    private Application app;

    private ApplicationOS currentOS;

    private ApplicationArchitecture currentArch;

    private ApplicationSystemTrayManager systemTrayManager;

    private ApplicationPreferencesManager userPreferencesManager;

    private ApplicationPreferencesManager systemPreferencesManager;

    private ApplicationNotificationManager notificationManager;

    public GenericSystemManager(Application app) {
        this.app = app;
        this.currentOS = new GenericOS(app, System.getProperty("os.name"), System.getProperty("os.version"));
        this.currentArch = new GenericArchitecture(app, System.getProperty("os.arch"));
        this.systemTrayManager = new GenericSystemTrayManager(app);
        this.systemPreferencesManager = new GenericPreferencesManager(app, ApplicationPreferencesManager.SYSTEM);
        this.userPreferencesManager = new GenericPreferencesManager(app, ApplicationPreferencesManager.USER);
        this.notificationManager = new GenericNotificationManager(app);
    }

    public Application getApplication() {
        return this.app;
    }

    public ApplicationOS getCurrentOS() {
        return this.currentOS;
    }

    public ApplicationArchitecture getCurrentArchitecture() {
        return this.currentArch;
    }

    public ApplicationSystemTrayManager getSystemTrayManager() {
        return this.systemTrayManager;
    }

    public String dump() {
        return "{app=" + this.app + ",currentOS=" + this.currentOS.dump() + ",currentArch=" + this.currentArch.dump() + "}";
    }

    public ApplicationPreferencesManager getSystemPreferencesManager() {
        return this.systemPreferencesManager;
    }

    public ApplicationPreferencesManager getUserPreferencesManager() {
        return this.userPreferencesManager;
    }

    public ApplicationNotificationManager getNotificationManager() {
        return this.notificationManager;
    }
}
