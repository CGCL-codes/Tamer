package de.buelowssiege.jayaddress.gui;

import java.awt.Frame;
import java.awt.event.WindowListener;
import de.buelowssiege.jayaddress.bin.jayAddress;
import de.buelowssiege.jayaddress.gui.frames.AddressEditFrame;
import de.buelowssiege.jayaddress.gui.frames.AddressViewFrame;
import de.buelowssiege.jayaddress.gui.frames.MailingListEditFrame;
import de.buelowssiege.jayaddress.misc.AddressEntry;
import de.buelowssiege.jayaddress.misc.MailingList;
import de.buelowssiege.utils.gui.AboutDialog;
import de.buelowssiege.utils.gui.ShowExceptionDialog;

/**
 * Description of the Class
 * 
 * @author Maximilian Schwerin
 * @created 1. Juni 2002
 */
public class InterfaceManager {

    /**
     * Its selfexplaining isnt it! :-)
     */
    public static void editAddress(WindowListener wl, AddressEntry ae) {
        AddressEditFrame aef = new AddressEditFrame(ae);
        aef.addWindowListener(wl);
    }

    /**
     * Its selfexplaining isnt it! :-)
     */
    public static void editMailingList(WindowListener wl, MailingList ml) {
        MailingListEditFrame mef = new MailingListEditFrame(ml);
        mef.addWindowListener(wl);
    }

    /**
     * Adds a feature to the Address attribute of the InterfaceManager class
     */
    public static void addAddress(WindowListener wl) {
        AddressEditFrame aef = new AddressEditFrame(null);
        aef.addWindowListener(wl);
    }

    /**
     * Adds a feature to the MailingList attribute of the InterfaceManager class
     */
    public static void addMailingList(WindowListener wl) {
        MailingListEditFrame mef = new MailingListEditFrame(null);
        mef.addWindowListener(wl);
    }

    /**
     * Its selfexplaining isnt it! :-)
     */
    public static void showMainWindow() {
        javax.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme(new de.buelowssiege.utils.gui.themes.ThinBlackMetalTheme());
        AddressViewFrame avf = new AddressViewFrame(true);
    }

    /**
     * Its selfexplaining isnt it! :-)
     */
    public static void showAddressBook() {
        AddressViewFrame avf = new AddressViewFrame(false);
    }

    /**
     * Its selfexplaining isnt it! :-)
     */
    public static void showError(Object owner, Exception ex) {
        ShowExceptionDialog.showError(owner, ex);
    }

    /**
     * Its selfexplaining isnt it! :-)
     */
    public static void showAbout(Frame parent) {
        String[] messages = new String[9];
        messages[0] = jayAddress.TITLE + " " + jayAddress.getVersion();
        messages[1] = "Copyright � " + jayAddress.COPYRIGHT + " " + jayAddress.AUTHOR;
        messages[2] = jayAddress.WEB;
        messages[3] = " ";
        messages[4] = jayAddress.TITLE + " comes with ABSOLUTELY NO WARRANTY.";
        messages[5] = "This is free software, and you are welcome to redistribute it";
        messages[6] = "under the conditions described in the GNU GENERAL PUBLIC LICENSE.\n";
        messages[7] = " ";
        messages[8] = "Compiled with Jikes Version 1.15.";
        AboutDialog.showAbout(parent, (Object.class).getResource(jayAddress.getResource("jayaddress.icon.splash")), messages, jayAddress.getString("header.frame.about"));
    }
}
