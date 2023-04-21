package com.itbs.gui;

import org.jdesktop.jdic.misc.Alerter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides a simple implementation of Tray Icon services for an application.
 * <p>
 * Minimizing frame will cause it to hide.<br>
 * Clicking the tray icon toggles the window.<br>
 * Icon services can be disabled<br>
 *
 * @author Alex Rass
 * @since Mar 25, 2005
 */
public class TrayAdapter {

    private static final Logger log = Logger.getLogger(TrayAdapter.class.getName());

    private static TrayIcon trayIcon;

    private static boolean lastState;

    private static Alerter alerter;

    static {
        try {
            alerter = Alerter.newInstance();
            log.fine("Alerter support: " + alerter.isAlertSupported());
        } catch (Throwable e) {
            log.log(Level.SEVERE, "Failed to load alerter. See if jawt.dll is in library path", e);
        }
    }

    /**
     * Utility class.  No constructor.
     */
    private TrayAdapter() {
    }

    /**
     * Creates the tray icon and attaches minimization actions to the frame.
     * Call this once you create your frame.
     * It is up to the caller not to call this more than once per VM.
     * @param useTray if the icon is to be displayed and used
     * @param frame reference to window
     * @param icon to use
     * @param caption to display in tray.
     */
    public static void create(final boolean useTray, final Frame frame, ImageIcon icon, String caption) {
        try {
            trayIcon = new TrayIcon(icon.getImage(), caption);
            trayIcon.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    frame.setVisible(!frame.isVisible());
                    frame.setState(Frame.NORMAL);
                    frame.toFront();
                }
            });
            updateTrayIcon(useTray);
            frame.addWindowListener(new WindowAdapter() {

                /**
                 * Invoked when a window is iconified.
                 */
                public void windowIconified(WindowEvent e) {
                    if (lastState && trayIcon != null) {
                        frame.setVisible(false);
                    }
                }
            });
        } catch (NoClassDefFoundError e) {
            trayIcon = null;
            log.log(Level.SEVERE, "Failed to load Tray Adapter", e);
        }
    }

    /**
     * Convenience method.
     * @return true if mechanism is loaded and working
     */
    public static boolean isAvailable() {
        return trayIcon != null;
    }

    /**
     * Call to add or remove the tray icon based on usage boolean
     * @param useTray to use or not to use!
     */
    public static void updateTrayIcon(boolean useTray) {
        if (isAvailable() && lastState != useTray) {
            try {
                SystemTray tray = SystemTray.getSystemTray();
                if (useTray) {
                    tray.add(trayIcon);
                } else {
                    tray.remove(trayIcon);
                }
                lastState = useTray;
            } catch (UnsatisfiedLinkError e) {
                log.log(Level.SEVERE, "", e);
                trayIcon = null;
            } catch (AWTException e) {
                log.log(Level.SEVERE, "", e);
                trayIcon = null;
            }
        }
    }

    /**
     * Use to get to TrayIcon to change caption, change icons etc.
     * @return reference to TrayIcon
     */
    public static TrayIcon getIcon() {
        return trayIcon;
    }

    public static void showBubble(String caption, String text) {
        if (isAvailable() && lastState) {
            try {
                trayIcon.displayMessage(caption, text, TrayIcon.MessageType.INFO);
            } catch (NullPointerException e) {
            } catch (UnsatisfiedLinkError e) {
                log.log(Level.SEVERE, "", e);
                trayIcon = null;
            }
        }
    }

    public static void alert(JFrame frame) {
        if (alerter != null) {
            try {
                if (alerter.isAlertSupported()) {
                    alerter.alert(frame);
                }
            } catch (Throwable e) {
                log.log(Level.SEVERE, "Failed to Alert", e);
            }
        }
    }
}
