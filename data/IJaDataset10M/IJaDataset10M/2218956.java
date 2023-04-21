package net.maizegenetics.util;

import java.awt.*;
import javax.swing.*;

public class ErrorDialog extends JDialog {

    Frame parent;

    String message;

    public ErrorDialog(Frame parent, String message) {
        super(parent, "Error", true);
        setBackground(Color.yellow);
        this.parent = parent;
        this.message = message;
        Panel p;
        p = new Panel();
        p.add(new Label(message));
        p.setFont(new Font("System", Font.BOLD, 12));
        this.getContentPane().add("Center", p);
        Dimension d;
        d = parent.getSize();
        setBounds(200, 50, 420, 100);
        setResizable(false);
    }

    public boolean handleEvent(Event event) {
        switch(event.id) {
            case Event.KEY_PRESS:
            case Event.WINDOW_MOVED:
            case Event.MOUSE_UP:
                dispose();
                parent.requestFocus();
                return (true);
        }
        return (false);
    }
}
