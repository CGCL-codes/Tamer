package de.miethxml.toolkit.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;

/**
*
* SplashScreen.java
*
*
*
*
*
* Created: Fri Sep 27 22:31:36 2002
*
* @author <a href="mailto:">Simon Mieth </a>
*/
public class SplashScreen extends Window implements Runnable {

    public static final int VMARGIN = 40;

    public static final int HMARGIN = 10;

    public static final int WIDTH = 320;

    public static final int HEIGHT = 200;

    private BufferedImage buffer;

    private Image img;

    private int height = 200;

    private int width = 320;

    private int margin = 5;

    private String imgSrc;

    private int process = 0;

    private int length;

    private int current = 0;

    private boolean paint = false;

    private Color pcolor = new Color(150, 220, 255);

    private String msg;

    private int elementLength = 10;

    private int elementHeight = 4;

    private int space;

    private int fontHeight;

    private int subSteps = 0;

    private int currentSubStep = 0;

    private boolean hasSubStebs = false;

    private int arcAngle = 0;

    private AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.30f);

    public SplashScreen(int width, int height, String imgSrc, String msg) {
        super(new Frame());
        this.imgSrc = imgSrc;
        this.height = height;
        this.width = width;
        this.msg = msg;
        setSize(width + HMARGIN, height + VMARGIN);
        center();
        createBuffer();
        getImage();
    }

    public SplashScreen(String imgSrc, String msg) {
        this(WIDTH, HEIGHT, imgSrc, msg);
    }

    public SplashScreen(int width, int height, String imgSrc) {
        this(width, height, imgSrc, "");
    }

    private void getImage() {
        img = Toolkit.getDefaultToolkit().getImage(imgSrc);
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(img, 1);
        try {
            mt.waitForID(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Graphics2D g2 = (Graphics2D) buffer.getGraphics();
        g2.drawImage(img, margin, margin, width, height, this);
        repaint();
    }

    private void center() {
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle frameDim = getBounds();
        setLocation((screenDim.width - frameDim.width) / 2, (screenDim.height - frameDim.height) / 2);
    }

    public void setMaximum(int max) {
        process = max;
        elementLength = width / (2 * max);
        space = (width - (process * elementLength)) / (process - 1);
        Graphics g = buffer.createGraphics();
        g.setColor(Color.gray);
        for (int i = 0; i < process; i++) {
            g.drawRect(5 + (i * (elementLength + space)), height + 10, elementLength, elementHeight);
        }
    }

    public void next() {
        if (current < process) {
            paintNextProgress(current, "");
            current++;
            repaint();
        }
    }

    public void next(String msg) {
        if (current < process) {
            paintNextProgress(current, msg);
            current++;
            repaint();
        }
        toFront();
    }

    private void paintNextProgress(int next, String msg) {
        Graphics2D g2 = (Graphics2D) buffer.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(pcolor);
        g2.fillRect(5 + (next * (elementLength + space)) + 1, height + 11, elementLength - 1, elementHeight - 1);
        g2.setColor(Color.WHITE);
        g2.fillRect(1, height + 15, width - 8, 24);
        if (msg.length() > 0) {
            g2.setColor(Color.black);
            g2.drawString(msg, 5, height + 16 + fontHeight);
        }
        g2.dispose();
    }

    public void run() {
        getImage();
    }

    public void update(Graphics g) {
        paint(g);
    }

    private void createBuffer() {
        buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = buffer.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.gray);
        g.drawRect(0, 0, (width + HMARGIN) - 1, (height + VMARGIN) - 1);
        g.drawRect(4, 4, width + 1, height + 1);
        fontHeight = g.getFontMetrics().getHeight();
        for (int i = 0; i < process; i++) {
            g.drawRect(5 + (i * (elementLength + space)), height + 10, elementLength, elementHeight);
        }
    }

    public void paint(Graphics g) {
        g.drawImage(buffer, 0, 0, getWidth(), getHeight(), this);
    }

    public void setVisible(boolean b) {
        super.setVisible(b);
        toFront();
    }

    public void startSubSteps(int count) {
        this.subSteps = count;
        this.hasSubStebs = true;
        this.currentSubStep = 0;
        this.arcAngle = 360 / count * (-1);
        paintNextSubStep(currentSubStep);
        repaint();
        toFront();
    }

    public void nextSubStep() {
        this.currentSubStep++;
        paintNextSubStep(currentSubStep);
        repaint();
        toFront();
    }

    public void endSubSteps() {
        this.hasSubStebs = false;
        paintNextSubStep(currentSubStep);
        repaint();
        toFront();
    }

    private void paintNextSubStep(int current) {
        Graphics2D g2 = (Graphics2D) buffer.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (hasSubStebs) {
            int x;
            int y;
            int w;
            int center_x;
            int center_y;
            if (width > height) {
                x = (2 * margin) + ((width - height) / 2);
                y = 2 * margin;
                w = height - (2 * margin);
            } else {
                x = margin * 2;
                y = (2 * margin) + ((height - width) / 2);
                w = width - (2 * margin);
            }
            int startAngle = ((current - 1) * arcAngle) + 90;
            g2.setComposite(ac);
            if (currentSubStep == 0) {
                g2.setColor(Color.BLACK);
                g2.drawArc(x, y, w, w, 0, 360);
            } else if (currentSubStep == subSteps) {
                g2.setColor(Color.GRAY);
                g2.fillArc(x, y, w, w, startAngle, (-270 - startAngle));
            } else {
                g2.setColor(Color.GRAY);
                g2.fillArc(x, y, w, w, startAngle, arcAngle);
            }
        } else {
            g2.drawImage(img, margin, margin, width, height, this);
        }
    }
}
