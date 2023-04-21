package com.bluesky.javawebbrowser.ui.test;

import java.awt.*;
import javax.swing.*;

public class GuiScreens {

    public static void main(String[] args) {
        Rectangle virtualBounds = new Rectangle();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        JFrame frame[][] = new JFrame[gs.length][];
        for (int j = 0; j < gs.length; j++) {
            GraphicsDevice gd = gs[j];
            System.out.println("Device " + j + ": " + gd);
            GraphicsConfiguration[] gc = gd.getConfigurations();
            frame[j] = new JFrame[gc.length];
            for (int i = 0; i < gc.length; i++) {
                System.out.println("  Configuration " + i + ": " + gc[i]);
                System.out.println("    Bounds: " + gc[i].getBounds());
                virtualBounds = virtualBounds.union(gc[i].getBounds());
                frame[j][i] = new JFrame("Config: " + i, gc[i]);
                frame[j][i].setBounds(50, 50, 400, 100);
                frame[j][i].setLocation((int) gc[i].getBounds().getX() + 50, (int) gc[i].getBounds().getY() + 50);
                frame[j][i].getContentPane().add(new JTextArea("Config:\n" + gc[i]));
                frame[j][i].setVisible(true);
            }
            System.out.println("Overall bounds: " + virtualBounds);
        }
    }
}
