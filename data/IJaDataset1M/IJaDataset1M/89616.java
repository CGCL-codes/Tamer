package ee.ut.logic.gui;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Key listener for user input area
 * @author Janno Liivak
 * @version 1.3
 */
public class InputKeyListener implements KeyListener {

    /** user input area */
    TextArea inputArea;

    /** button "Puhasta" */
    Button clearButton;

    /**
 * Constructs key listener for user input area
 * @param input user input area
 * @param clear button "Puhasta"
 */
    public InputKeyListener(TextArea input, Button clear) {
        inputArea = input;
        clearButton = clear;
    }

    /**
 * Invoked when a key has been pressed
 * @param e An event which indicates that a keystroke occurred in a component
 */
    public void keyPressed(KeyEvent e) {
        if ((e.getKeyCode() == KeyEvent.VK_TAB) && (e.isShiftDown())) {
            clearButton.requestFocus();
        } else if (e.getKeyCode() == KeyEvent.VK_TAB) {
            inputArea.transferFocus();
        }
    }

    /**
 * Invoked when a key has been typed
 * @param e An event which indicates that a keystroke occurred in a component
 */
    public void keyTyped(KeyEvent e) {
    }

    /**
 * Invoked when a key has been released
 * @param e An event which indicates that a keystroke occurred in a component
 */
    public void keyReleased(KeyEvent e) {
    }
}
