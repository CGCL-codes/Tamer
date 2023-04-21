package com.monkygames.sc2bob.gui.utils;

import java.applet.Applet;
import java.awt.*;

public class IconTest extends Applet {

    Label message;

    Icon icon1a, icon1b, icon2, icon3;

    public void init() {
        Font labelFont = new Font("Helvetica", Font.BOLD, 28);
        message = new Label("Click and drag any of the images");
        message.setFont(labelFont);
        message.setBackground(Color.red);
        message.setForeground(Color.yellow);
        add(message);
        icon1a = new Icon("http://www.apl.jhu.edu/~hall/images/duke.gif");
        icon1b = new Icon("http://www.apl.jhu.edu/~hall/images/duke.gif");
        icon2 = new Icon("http://www.apl.jhu.edu/~hall/images/methods.gif");
        icon3 = new Icon("http://www.apl.jhu.edu/~hall/images/yellow-ball.gif");
        icon1b.setBorder(5);
        icon1b.setBorderColor(Color.yellow);
        add(icon1a);
        add(icon1b);
        add(icon2);
        add(icon3);
    }

    public boolean handleEvent(Event event) {
        if (Icon.handleIconEvent(event, this)) return (true); else return (super.handleEvent(event));
    }

    public boolean mouseDown(Event e, int x, int y) {
        System.out.println("Applet: mouseDown at (" + x + "," + y + ").");
        return (true);
    }

    public static void main(String[] args) {
        Frame mainFrame = new Frame("IconTest");
        IconTest app = new IconTest();
        app.init();
        mainFrame.resize(500, 400);
        mainFrame.add("Center", app);
        mainFrame.show();
        mainFrame.repaint();
    }
}
