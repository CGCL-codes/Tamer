package net.sourceforge.picdev.ui;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.Rectangle;
import java.awt.AWTEvent;
import java.awt.event.InputMethodEvent;
import java.awt.event.MouseEvent;

/**
 * Temporary fix to avoid automatic scroll on textchanges.
 *
 * @author Klaus Friedel
 *         Date: 31.12.2007
 *         Time: 10:21:03
 */
public class NoEditTextArea extends JTextArea {

    public NoEditTextArea() {
        this("");
    }

    public NoEditTextArea(String text) {
        super(text);
        setEnabled(true);
        setEditable(false);
        setCaret(new DefaultCaret() {

            protected void adjustVisibility(Rectangle nloc) {
            }
        });
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    }
}
