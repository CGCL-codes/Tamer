package com.scythebill.birdlist.ui.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Base class for listeners looking for double-clicks. 
 */
public abstract class DoubleClickListener implements MouseListener {

    protected abstract void doubleClicked(MouseEvent event);

    public void mouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) doubleClicked(event);
    }

    public void mousePressed(MouseEvent event) {
    }

    public void mouseReleased(MouseEvent event) {
    }

    public void mouseEntered(MouseEvent event) {
    }

    public void mouseExited(MouseEvent event) {
    }
}
