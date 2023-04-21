package easyplay.reaction;

import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class GameObject extends JButton {

    private JButton b;

    protected GameObject() {
    }

    protected GameObject(JButton button, ActionListener lis) {
        b = button;
        b.setMargin(new Insets(0, 0, 0, 0));
        b.setActionCommand("click");
        b.addActionListener(lis);
        makeIcon(b, "blank.gif");
    }

    protected JButton getButton() {
        return (b);
    }

    protected void makeIcon(JButton b, String src) {
        URL img = GameObject.class.getResource(src);
        b.setIcon(new ImageIcon(img));
    }
}
