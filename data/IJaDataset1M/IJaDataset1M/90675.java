package net.sf.amemailchecker.gui.entrypoint.tray;

import net.sf.amemailchecker.gui.entrypoint.BaseEntryPoint;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class EntryPointTrayIcon extends BaseEntryPoint {

    private TrayIcon icon;

    public void addPoint() throws Exception {
        icon = new TrayIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
        icon.setImageAutoSize(true);
        icon.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent e) {
                evaluatePopup(e);
            }

            public void mousePressed(MouseEvent e) {
                evaluatePopup(e);
            }

            private void evaluatePopup(MouseEvent e) {
                if (!e.isPopupTrigger()) return;
                popupMenu.setLocation(e.getXOnScreen(), e.getYOnScreen() - popupMenu.getPreferredSize().height);
                popupMenu.setInvoker(popupMenu);
                popupMenu.setVisible(true);
            }
        });
        SystemTray.getSystemTray().add(icon);
    }

    public String getToolTip() {
        return icon.getToolTip();
    }

    public void setImage(Image image) {
        icon.setImage(image);
    }

    public void setToolTip(String toolTip) {
        icon.setToolTip(toolTip);
    }

    public void removePoint() {
        icon.setPopupMenu(null);
        SystemTray.getSystemTray().remove(icon);
    }

    public void removeListeners() {
        MouseListener[] mouseListeners = icon.getMouseListeners();
        for (MouseListener listener : mouseListeners) icon.removeMouseListener(listener);
    }

    public void showNotificationMessage(String message) {
        icon.displayMessage("", message, TrayIcon.MessageType.INFO);
    }
}
