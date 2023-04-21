package n2hell;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;

public class Tray implements IController {

    final TrayIcon trayIcon;

    private URI uri = null;

    public Tray(String iconPath) throws Exception {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage(iconPath);
            MouseListener mouseListener = new MouseListener() {

                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) openURI();
                }

                public void mouseEntered(MouseEvent e) {
                }

                public void mouseExited(MouseEvent e) {
                }

                public void mousePressed(MouseEvent e) {
                }

                public void mouseReleased(MouseEvent e) {
                }
            };
            ActionListener exitListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
            };
            ActionListener openListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    openURI();
                }
            };
            PopupMenu popup = new PopupMenu();
            MenuItem openItem = new MenuItem("Open");
            openItem.addActionListener(openListener);
            popup.add(openItem);
            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(exitListener);
            popup.add(exitItem);
            trayIcon = new TrayIcon(image, "n2hell", popup);
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(openListener);
            trayIcon.addMouseListener(mouseListener);
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }
        } else {
            trayIcon = null;
            throw new Exception("Tray icon not supported by this OS");
        }
    }

    private void openURI() {
        if (uri != null) if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(uri);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void showMessage(String title, String message) {
        trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
    }

    public void showError(String title, String message) {
        trayIcon.displayMessage(title, message, TrayIcon.MessageType.ERROR);
    }

    public void setURI(URI uri) {
        this.uri = uri;
    }
}
