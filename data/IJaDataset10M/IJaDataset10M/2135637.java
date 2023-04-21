package org.thechiselgroup.choosel.dnd.client.windows;

import org.thechiselgroup.choosel.core.client.command.CommandManager;

public class DesktopWindowManager extends AbstractWindowManager {

    public DesktopWindowManager(DefaultDesktop desktopPanel, CommandManager commandManager) {
        super(desktopPanel, commandManager);
    }

    @Override
    public void bringToFront(WindowPanel window) {
        getDesktopPanel().bringToFront(window);
    }

    @Override
    public void close(WindowPanel window) {
        getDesktopPanel().removeWindow(window);
    }

    protected DefaultDesktop getDesktopPanel() {
        return (DefaultDesktop) getBoundaryPanel();
    }
}
