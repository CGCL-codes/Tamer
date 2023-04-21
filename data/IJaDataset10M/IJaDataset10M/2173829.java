package com.guanda.swidgex.widgets.mp3player;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JMenuItem;

public class ColorMenuItem extends JMenuItem {

    private static final long serialVersionUID = 1L;

    private static final int PAD = 10;

    private static final int LEFT_EDGE_PAD = 5;

    private Color color;

    public ColorMenuItem(String string, Color c) {
        super(string);
        color = c;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int h = getHeight();
        int x = LEFT_EDGE_PAD;
        int y = (h - PAD) / 2;
        g.setColor(color);
        g.fillRect(x, y, PAD, PAD);
        g.setColor(isArmed() ? Color.white : Color.black);
        g.drawRect(x, y, PAD, PAD);
    }
}
