package net.sourceforge.pinemup.ui.swing;

import java.awt.TrayIcon;
import net.sourceforge.pinemup.io.ResourceLoader;
import net.sourceforge.pinemup.ui.swing.menus.TrayMenu;

public final class PinEmUpTrayIcon extends TrayIcon {

    private static PinEmUpTrayIcon instance = new PinEmUpTrayIcon();

    private PinEmUpTrayIcon() {
        super(ResourceLoader.getInstance().getTrayIcon(), "pin 'em up", new TrayMenu());
        setImageAutoSize(false);
        IconClickLogic myIconListener = new IconClickLogic();
        addActionListener(myIconListener);
        addMouseListener(myIconListener);
    }

    public static PinEmUpTrayIcon getInstance() {
        return PinEmUpTrayIcon.instance;
    }
}
