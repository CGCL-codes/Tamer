package com.iver.andami.plugins;

import com.iver.andami.plugins.status.IExtensionStatus;

/**
 * This class extends the functionality of Extension class to let the programmer
 * set an extension visible or not on-the-fly.
 *
 * @autor Jaume Dominguez Faus - jaume.dominguez@iver.es
 */
public class ExtensionDecorator implements HiddableExtension {

    public static final int INACTIVE = 0;

    public static final int ALWAYS_VISIBLE = 1;

    public static final int ALWAYS_INVISIBLE = 2;

    int alwaysVisible;

    IExtension extension;

    public ExtensionDecorator(IExtension e, int visibilityControl) {
        setExtension(e);
        setVisibility(visibilityControl);
    }

    public void setExtension(IExtension e) {
        this.extension = e;
    }

    public void setVisibility(int state) {
        this.alwaysVisible = state;
    }

    public int getVisibility() {
        return alwaysVisible;
    }

    public IExtension getExtension() {
        return extension;
    }

    public void initialize() {
        extension.initialize();
    }

    public void terminate() {
    }

    public void execute(String actionCommand) {
        extension.execute(actionCommand);
    }

    public boolean isEnabled() {
        return extension.isEnabled();
    }

    public boolean isVisible() {
        if (alwaysVisible == INACTIVE) return extension.isVisible(); else if (alwaysVisible == ALWAYS_VISIBLE) return true; else return false;
    }

    public void postInitialize() {
    }

    public IExtensionStatus getStatus() {
        return extension.getStatus();
    }

    public IExtensionStatus getStatus(IExtension extension) {
        return this.extension.getStatus(extension);
    }
}
