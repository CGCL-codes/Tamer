package jp.riken.omicspace.frame;

import jp.riken.omicspace.osml.impl.*;
import jp.riken.omicspace.graphics.*;
import jp.riken.omicspace.graphics.impl.*;
import java.awt.*;
import java.lang.*;
import java.net.*;
import java.io.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;
import javax.swing.event.*;

public class SplashWindow extends JWindow {

    private String sVersion;

    private Image windowImage = null;

    public SplashWindow(String version) {
        super();
        sVersion = version;
        URL url = SplashWindow.class.getResource("images/splash_title6.jpg");
        MediaTracker tracker = new MediaTracker(this);
        windowImage = Toolkit.getDefaultToolkit().getImage(url);
        tracker.addImage(windowImage, 0);
        try {
            tracker.waitForID(0);
        } catch (InterruptedException e) {
        }
        int w = windowImage.getWidth(this);
        int h = windowImage.getHeight(this);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = dim.width / 2 - w / 2;
        int y = dim.height / 2 - h / 2;
        setLocation(new Point(x, y));
        setSize(w, h);
    }

    public void update(Graphics g) {
        if (windowImage != null) {
            g.drawImage(windowImage, 0, 0, this);
        }
        String msg[] = sVersion.split("\n");
        for (int i = 0; i < msg.length; i++) {
            g.setColor(new Color(0x000000));
            g.drawString(msg[i], 100, 320 + i * 10);
        }
    }

    public void paint(Graphics g) {
        update(g);
    }

    public void show() {
        super.show();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
        }
        super.hide();
    }
}
