package com.memoire.bu;

import java.awt.Graphics;
import javax.swing.JPasswordField;

/**
 * A simple password field.
 */
public class BuPasswordField extends JPasswordField {

    public BuPasswordField() {
        this(null, -1);
    }

    public BuPasswordField(int _cols) {
        this(null, _cols);
    }

    public BuPasswordField(String _text) {
        this(_text, -1);
    }

    public BuPasswordField(String _text, int _cols) {
        super();
        setEchoChar('*');
        if (_cols > 0) setColumns(_cols);
        setText(_text == null ? "" : _text);
    }

    public void paint(Graphics _g) {
        BuLib.setAntialiasing(this, _g);
        super.paint(_g);
    }
}
