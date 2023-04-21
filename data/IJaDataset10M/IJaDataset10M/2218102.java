package com.apple.eawt;

public abstract class Application {

    public abstract void addAboutMenuItem();

    public abstract void addApplicationListener(ApplicationListener listener);

    public abstract void addPreferencesMenuItem();

    public static Application getApplication() {
        return null;
    }

    public abstract boolean getEnabledAboutMenu();

    public abstract boolean getEnabledPreferencesMenu();

    public static java.awt.Point getMouseLocationOnScreen() {
        return null;
    }

    public abstract boolean isAboutMenuItemPresent();

    public abstract boolean isPreferencesMenuItemPresent();

    public abstract void removeAboutMenuItem();

    public abstract void removeApplicationListener(ApplicationListener listener);

    public abstract void removePreferencesMenuItem();

    public abstract void setEnabledAboutMenu(boolean enable);

    public abstract void setEnabledPreferencesMenu(boolean enable);
}
