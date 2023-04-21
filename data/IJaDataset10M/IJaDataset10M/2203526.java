package de.cweiske.birthday.frontend.tray;

import org.jdesktop.jdic.tray.SystemTray;
import de.cweiske.birthday.frontend.GUI;
import de.cweiske.birthday.images.DummyIconLoader;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Tray Icon for birthday reminder
 *
 */
public class TrayIcon implements ActionListener {

    VisibleInterface dlg = null;

    SystemTray tray = null;

    org.jdesktop.jdic.tray.TrayIcon tIcon = null;

    boolean bInTray = false;

    JMenuItem mnuiShow = null;

    JMenuItem mnuiExit = null;

    JPopupMenu popupMenu = null;

    boolean bLibraryAvailable = true;

    /**
	 * small constructor
	 */
    public TrayIcon(VisibleInterface dlg) {
        this.dlg = dlg;
    }

    /**
	 * create the tray icon and connect it
	 */
    private void setUp() {
        try {
            this.tray = SystemTray.getDefaultSystemTray();
        } catch (Error e) {
            System.out.println("ERROR: tray library is not available/not in library path");
            this.bLibraryAvailable = false;
            return;
        }
        this.popupMenu = new JPopupMenu(GUI.resbundle.getString("bdr.ProgramTitle"));
        this.mnuiShow = new JMenuItem(GUI.resbundle.getString("bdr.tray.show"));
        this.mnuiExit = new JMenuItem(GUI.resbundle.getString("bdr.tray.exit"));
        popupMenu.add(this.mnuiShow);
        popupMenu.add(this.mnuiExit);
        this.mnuiShow.addActionListener(this);
        this.mnuiExit.addActionListener(this);
        this.tIcon = new org.jdesktop.jdic.tray.TrayIcon(new ImageIcon(DummyIconLoader.class.getResource("birthday.16x16.png")), GUI.resbundle.getString("bdr.ProgramTitle"), popupMenu);
        this.tIcon.setIconAutoSize(true);
        this.tIcon.addActionListener(this);
        this.bInTray = false;
    }

    /**
	 * adds the birthday tray icon to the tray and hides the dialog
	 */
    public void toTray() {
        if (this.tray == null && this.bLibraryAvailable) {
            this.setUp();
        }
        if (!this.bInTray && this.bLibraryAvailable) {
            this.bInTray = true;
            this.tray.addTrayIcon(this.tIcon);
            this.dlg.setVisible(false);
        }
    }

    /**
	 * removes the birthday icon from the tray and shows the dialog
	 */
    public void fromTray() {
        if (this.bInTray && this.tray != null && this.tIcon != null && this.bLibraryAvailable) {
            this.bInTray = false;
            this.tray.removeTrayIcon(this.tIcon);
            this.dlg.setVisible(true);
        }
    }

    /**
	 * checks if birthday reminder is in the tray
	 * (invisible & tray icon available)
	 */
    public boolean isInTray() {
        return this.bInTray;
    }

    /**
	 * the icon has been clicked
	 */
    public void actionPerformed(ActionEvent event) {
        if (event.getSource().equals(this.mnuiExit)) {
            this.dlg.exit();
        } else {
            this.fromTray();
        }
    }
}
