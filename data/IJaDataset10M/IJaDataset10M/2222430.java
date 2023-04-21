package org.paw.server;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author  LUELLJOC
 * @version 
 */
public class PawMain {

    static PawServer pawServer;

    TrayIcon trayIcon;

    private boolean addSystemTray() {
        try {
            if (SystemTray.isSupported()) {
                SystemTray tray = SystemTray.getSystemTray();
                ActionListener menuListener = new MenuListener();
                PopupMenu popup = new PopupMenu();
                MenuItem exitItem = new MenuItem("Shutdown Server");
                exitItem.addActionListener(menuListener);
                exitItem.setActionCommand("quit");
                popup.add(exitItem);
                Image trayImage = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/org/paw/server/trayicon.png"));
                trayIcon = new TrayIcon(trayImage, "PAW Server" + " " + PawServer.class.getPackage().getImplementationVersion(), popup);
                trayIcon.setImageAutoSize(true);
                tray.add(trayIcon);
                return true;
            }
            return false;
        } catch (NoClassDefFoundError e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        if (args.length == 1) {
            pawServer = new PawServer(args[0]);
        } else {
            pawServer = new PawServer("conf/server.xml");
        }
        PawMain pawMain = new PawMain();
        pawMain.addSystemTray();
        if (pawServer.adminActive) {
            int adminPort = Integer.decode(pawServer.adminPort).intValue();
            new PawAdmin(pawServer, adminPort, pawServer.adminUser, pawServer.adminPass);
        }
    }

    private class MenuListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("quit")) {
                trayIcon.displayMessage("PAW Server" + " " + PawServer.class.getPackage().getImplementationVersion(), "PAW Server shutting down...", TrayIcon.MessageType.INFO);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                System.exit(0);
            }
        }
    }

    /**
     * Returns the server.
     * @return the server
     */
    public static PawServer getServer() {
        return pawServer;
    }
}
