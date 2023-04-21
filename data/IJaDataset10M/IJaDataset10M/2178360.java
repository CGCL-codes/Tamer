package org.gjt.sp.jedit.gui.tray;

import java.awt.Image;

/**
 * The mother class of the tray icon service.
 * If you want to replace the tray icon of jEdit, you must extend it
 * and declare a service "org.gjt.sp.jedit.gui.tray.JEditTrayIcon"
 * @see org.gjt.sp.jedit.ServiceManager
 * @author Matthieu Casanova
 */
public abstract class JEditTrayIcon extends JTrayIcon {

    protected JEditTrayIcon(Image image, String tooltip) {
        super(image, tooltip);
    }

    abstract void setTrayIconArgs(boolean restore, String userDir, String[] args);
}
