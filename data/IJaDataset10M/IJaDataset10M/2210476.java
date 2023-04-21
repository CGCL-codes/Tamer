package edu.kds.gui.editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A popupmenu superclass. Simply listens to mouseclicks, 
 * decides if the click was a popup-trigger and possibly shows
 * the popupmenu.
 * @author Rasmus Hansen
 */
public abstract class SJPopupMenu extends JPopupMenu implements MouseListener {

    protected Point clickPos;

    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            clickPos = e.getPoint();
            show(e.getComponent(), e.getX(), e.getY());
        }
    }
}
