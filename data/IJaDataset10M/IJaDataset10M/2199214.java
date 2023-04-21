package com.tapper.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import com.tapper.util.*;

public class TapperAnimatedSplash extends javax.swing.JPanel {

    private int CLIP_X = 10;

    private int CLIP_Y = 80;

    private int CLIP_WIDTH = 350;

    private int CLIP_HEIGHT = 250;

    private int SLEEP_TIME = 50;

    private int IMAGE_WIDTH = 350;

    private int IMAGE_HEIGHT = 256;

    private Image sp;

    private boolean done = false;

    private Image newImage = null;

    private Properties props = Tapper.getProperties();

    Vector lineVector = new Vector();

    Vector linePositions = new Vector();

    private Runnable splasher = new Runnable() {

        public void run() {
            while ((!done) && isShowing()) {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                }
                repaint();
            }
        }
    };

    private static Thread splashRunner;

    public TapperAnimatedSplash(String line1, String line2) {
        super();
        lineVector.addElement(line1);
        lineVector.addElement(line2);
        linePositions.addElement(new Integer(200));
        linePositions.addElement(new Integer(218));
        initImages();
        GridLayout gl = new GridLayout(1, 1);
        setLayout(gl);
        setSize(getImageSize());
        String fileText = "testfile.txt";
        BufferedReader in = null;
        JScrollPane sp1 = null;
        if (fileText != null) {
            try {
                lineVector.removeAllElements();
                linePositions.removeAllElements();
                in = new BufferedReader(new FileReader(fileText));
                String stuff = new String();
                do {
                    stuff = in.readLine();
                    if (stuff != null) {
                        lineVector.addElement(stuff);
                    }
                } while (stuff != null);
                for (int i = 0; i < lineVector.size(); i++) {
                    linePositions.addElement(new Integer(50 + IMAGE_HEIGHT + (18 * i)));
                }
            } catch (Exception e) {
                lineVector.removeAllElements();
                lineVector.addElement(line1);
                lineVector.addElement(line2);
                linePositions.removeAllElements();
                linePositions.addElement(new Integer(200));
                linePositions.addElement(new Integer(218));
            }
        } else {
            System.out.println("file was null");
        }
    }

    public void startAnimation() {
        done = false;
        splashRunner = new Thread(splasher);
        System.out.println("thread was Started");
        splashRunner.start();
    }

    public void stopAnimation() {
        done = true;
        System.out.println("thread was Stopped");
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        FontMetrics fm = g.getFontMetrics();
        Dimension d = this.getSize();
        g2.setBackground(getBackground());
        g2.clearRect(0, 0, d.width, d.height);
        int xLoc = (int) (d.width / 2) - (int) (IMAGE_WIDTH / 2);
        int yLoc = (int) (d.height / 2) - (int) (IMAGE_HEIGHT / 2);
        if (xLoc < 0) {
            xLoc = 0;
        }
        if (yLoc < 0) {
            yLoc = 0;
        }
        g.drawImage(sp, xLoc, yLoc, this);
        g.setColor(Color.white);
        g.drawRect(xLoc, yLoc, IMAGE_WIDTH, IMAGE_HEIGHT);
        g.setClip(xLoc, yLoc, IMAGE_WIDTH, IMAGE_HEIGHT);
        int lineNum = lineVector.size();
        for (int i = 0; i < lineNum; i++) {
            String line = (String) lineVector.elementAt(i);
            Integer yPos = (Integer) linePositions.elementAt(i);
            int y = yPos.intValue();
            int sx = (int) (CLIP_WIDTH / 2) - (int) (fm.stringWidth(line) / 2);
            g.drawString(line, xLoc + sx, y);
            y = y - 1;
            linePositions.setElementAt(new Integer(y), i);
            if (y < yLoc) {
                linePositions.setElementAt(new Integer(50 + IMAGE_HEIGHT + (18 * lineNum)), i);
            }
        }
    }

    private void initImages() {
        MediaTracker tracker = new MediaTracker(this);
        ImageIconLoader loader = new ImageIconLoader();
        ImageIcon icon = loader.loadImageIcon("com/tapper/images/hands1.jpg");
        IMAGE_WIDTH = icon.getIconWidth();
        IMAGE_HEIGHT = icon.getIconHeight();
        CLIP_WIDTH = IMAGE_WIDTH;
        sp = icon.getImage();
        tracker.addImage(sp, 1);
        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
        }
    }

    public Dimension getImageSize() {
        ImageIcon icon = new ImageIcon(sp);
        return (new Dimension(icon.getIconWidth(), icon.getIconHeight()));
    }
}
