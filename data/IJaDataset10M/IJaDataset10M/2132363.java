package com.googlecode.simpleret.viewer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import com.googlecode.simpleret.Constants;

class ViewerKeyListener implements KeyListener {

    Data data = null;

    Viewer viewer = null;

    ViewerKeyListener(Data data, Viewer viewer) {
        this.data = data;
        this.viewer = viewer;
    }

    public void keyTyped(KeyEvent event) {
        char key = event.getKeyChar();
        key = Character.toLowerCase(key);
        int difference = 0;
        if (key == 'a') {
            difference = 1;
        }
        if (key == 's') {
            difference = Constants.PAGE_LENGTH;
        }
        if (key == 'd') {
            difference = Constants.PAGE_LENGTH * 3;
        }
        if (key == 'f') {
            difference = Constants.PAGE_LENGTH * 100;
        }
        if (key == 'q') {
            difference = -1;
        }
        if (key == 'w') {
            difference = -Constants.PAGE_LENGTH;
        }
        if (key == 'e') {
            difference = -Constants.PAGE_LENGTH * 3;
        }
        if (key == 'r') {
            difference = -Constants.PAGE_LENGTH * 100;
        }
        if (difference != 0) {
            viewer.firePropertyChange(FrameConstants.EVENT_DIFFERENCE, 0, difference);
            return;
        }
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}
