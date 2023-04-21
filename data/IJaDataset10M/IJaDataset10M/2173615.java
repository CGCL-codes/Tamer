package net.sf.jftp.gui.tasks;

import net.sf.jftp.config.Settings;
import net.sf.jftp.gui.*;
import net.sf.jftp.gui.framework.*;
import net.sf.jftp.util.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class ImageViewer extends JInternalFrame {

    public ImageViewer(String img) {
        super(img, true, true, true, true);
        setLocation(150, 50);
        setSize(400, 300);
        setLayout(new BorderLayout(2, 2));
        ImagePanel p = new ImagePanel(img);
        JScrollPane scroll = new JScrollPane(p);
        getContentPane().add("Center", scroll);
        p.setMinimumSize(new Dimension(1500, 1500));
        p.setPreferredSize(new Dimension(1500, 1500));
        p.setMaximumSize(new Dimension(1500, 1500));
        setVisible(true);
    }
}

class ImagePanel extends JPanel {

    private Image img;

    public ImagePanel(String url) {
        try {
            setBackground(Color.white);
            img = Toolkit.getDefaultToolkit().getImage(new URL(url));
            MediaTracker mt = new MediaTracker(this);
            mt.addImage(img, 1);
            mt.waitForAll();
            repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, 1500, 1500);
        g.drawImage(img, 0, 0, null);
    }

    public void update(Graphics g) {
        paintComponent(g);
    }
}
