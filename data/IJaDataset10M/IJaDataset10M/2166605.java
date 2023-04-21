package gui;

import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;

public class JHumPaletteWindow extends JWindow {

    public BufferedImage background;

    JHumPaletteWindow(int x, int y, int w, int h, JHumPalette npalette) {
        super();
        try {
            Robot rbt = new Robot();
            background = rbt.createScreenCapture(new Rectangle(x, y, w, h));
        } catch (Exception ex) {
            System.out.println(ex);
            System.exit(1);
        }
        add(npalette);
        pack();
        setSize(w, h);
        setLocation(x, y);
        setVisible(true);
    }

    public void unpop() {
        setVisible(false);
        dispose();
    }

    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, null);
        super.paint(g);
    }
}
