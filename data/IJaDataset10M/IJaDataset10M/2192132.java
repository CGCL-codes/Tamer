package org.nixinet.netgrapher.ui;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.io.File;
import javax.swing.JFrame;

public class Utils {

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static void center(JFrame frame, Dimension d) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point center = ge.getCenterPoint();
        int x = center.x - (int) d.getWidth() / 2, y = center.y - (int) d.getHeight() / 2;
        frame.setBounds(x, y, (int) d.getWidth(), (int) d.getHeight());
        frame.validate();
    }
}
