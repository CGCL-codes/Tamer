package tufts.vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * VueToolPopupAdapter
 *
 * This calss handles support for activating a popuup menu for the radio
 * button tool menu of the VUE Toolbar.
 *
 * T@author scottb
 * @deprecated -- no longer used -- smf 2004-01-29 19:42.02 
 **/
public class VueToolPopupAdapter extends MouseAdapter {

    /** The popup menu to be displayed **/
    private JPopupMenu mPopup;

    public VueToolPopupAdapter(JPopupMenu pPopup) {
        super();
        setPopup(pPopup);
    }

    /**
     * setPopupMenu
     *
     * This method sets the popup menu to display
     *
     * @param pPopup the popup menu to display
     **/
    public void setPopup(JPopupMenu pPopup) {
        mPopup = pPopup;
    }

    /**
    * getPopupMenu
    * this method returns the current popup menu used by the adapter
    * @return the popup menu
    **/
    public JPopupMenu getPopupMenu() {
        return mPopup;
    }

    /**
     * mousePressed
     * Thimethod will handle the mouse press event and cause the
     * popup to display at the proper location of th
     **/
    private boolean mMenuWasShowing = false;

    public void mousePressed(MouseEvent e) {
        debug(e.paramString() + " on " + e.getSource());
        if (!mPopup.isVisible()) {
            mMenuWasShowing = false;
            showPopup(e);
        } else {
            mMenuWasShowing = true;
        }
    }

    /**
     * mouse Released
     * This handles the mouse release events
     *
     * @param MouseEvent e the event
     **/
    public void mouseReleased(MouseEvent e) {
        debug(e.paramString() + " on " + e.getSource());
        if (mPopup.isVisible()) {
        } else {
            if (!mMenuWasShowing) showPopup(e);
        }
    }

    private void showPopup(MouseEvent e) {
        if (sDebug) System.out.println("\tshowing " + mPopup);
        Component c = e.getComponent();
        mPopup.show(c, 0, c.getBounds().height);
    }

    /**
     * mouseClicked
     * This handles the mouse clicked events
     *
     * @param MouseEvent e the event
     **/
    public void mouseClicked(MouseEvent e) {
        debug(e.paramString() + " on " + e.getSource());
    }

    private static boolean sDebug = false;

    private void debug(String pStr) {
        if (sDebug) {
            System.out.println("VueToolPopupAdapter " + Integer.toHexString(hashCode()) + ": " + pStr);
        }
    }
}
