package com.gite.core;

import java.awt.AWTException;
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
import javax.swing.JFrame;

public class MySystemTray {

    private TrayIcon trayIcon;

    private JFrame parent;

    public MySystemTray(JFrame parent) {
        this.parent = parent;
        addSupport();
    }

    public void setImage(Image image) {
        trayIcon.setImage(image);
    }

    public void setImage(String image_with_path) {
        setImage(Toolkit.getDefaultToolkit().getImage(image_with_path));
    }

    public void setToolTip(String tip) {
        trayIcon.setToolTip(tip);
    }

    public void displayInfoMessage(String caption, String text) {
        displayMessage(caption, text, TrayIcon.MessageType.INFO);
    }

    public void displayErrorMessage(String caption, String text) {
        displayMessage(caption, text, TrayIcon.MessageType.ERROR);
    }

    public void displayWarningMessage(String caption, String text) {
        displayMessage(caption, text, TrayIcon.MessageType.WARNING);
    }

    public void displayMessage(String caption, String text) {
        displayMessage(caption, text, TrayIcon.MessageType.NONE);
    }

    public void displayMessage(String caption, String text, TrayIcon.MessageType mt) {
        trayIcon.displayMessage(caption, text, mt);
    }

    private void addSupport() {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage(Utils.getDataFolder() + "\\resources\\core\\app.gif");
            PopupMenu popup = new PopupMenu();
            MenuItem restoreItem = new MenuItem("Show");
            restoreItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (!parent.isVisible()) {
                        parent.setState(JFrame.NORMAL);
                        parent.setVisible(true);
                    }
                }
            });
            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
            });
            popup.add(restoreItem);
            popup.add(exitItem);
            trayIcon = new TrayIcon(image, "GITe", popup);
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    trayIcon.displayMessage("Action Event", "An Action Event Has Been Peformed!", TrayIcon.MessageType.INFO);
                }
            });
            trayIcon.addMouseListener(new MouseListener() {

                public void mouseClicked(MouseEvent e) {
                    System.out.println("Tray Icon - Mouse clicked!");
                }

                public void mouseEntered(MouseEvent e) {
                    System.out.println("Tray Icon - Mouse entered!");
                }

                public void mouseExited(MouseEvent e) {
                    System.out.println("Tray Icon - Mouse exited!");
                }

                public void mousePressed(MouseEvent e) {
                    System.out.println("Tray Icon - Mouse pressed!");
                }

                public void mouseReleased(MouseEvent e) {
                    System.out.println("Tray Icon - Mouse released!");
                }
            });
            try {
                tray.add(trayIcon);
                displayInfoMessage("working", "c'est la vie");
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }
        } else {
            throw new RuntimeException("Trayicon not supported");
        }
    }
}
