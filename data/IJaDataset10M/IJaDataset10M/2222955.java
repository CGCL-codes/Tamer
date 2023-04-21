package csa.jmom.grafics;

import csa.gui.*;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.*;
import java.util.*;

/**
 *
 * @author Malban
 */
public class ImageComponent extends JComponent implements Runnable {

    private static int counter = 0;

    final int UID = (counter++);

    Vector<Image> imagesUse = new Vector<Image>();

    Vector<Image> imagesOrg = new Vector<Image>();

    Thread animator = null;

    boolean doRun = false;

    int index = -1;

    int animDelay = 25;

    boolean isOpaque = false;

    public ImageComponent() {
        super();
    }

    public ImageComponent(Vector<Image> i) {
        super();
        imagesOrg = i;
        for (int j = 0; j < i.size(); j++) imagesUse.addElement(i.elementAt(j));
    }

    public void setImages(Vector<Image> i) {
        boolean v = isVisible();
        if (v) setVisible(false);
        imagesOrg = i;
        imagesUse = new Vector<Image>();
        for (int j = 0; j < i.size(); j++) imagesUse.addElement(i.elementAt(j));
        if (v) setVisible(true);
    }

    public void setSequence(ImageSequence is) {
        setDelay(is.getDelay());
        setImages(is.getImageVector());
    }

    public void setDelay(int d) {
        animDelay = d;
    }

    public void setScaled(boolean scaled, int h, int w) {
        boolean v = isVisible();
        if (v) setVisible(false);
        imagesUse = new Vector<Image>();
        for (int i = 0; i < imagesOrg.size(); i++) {
            Image image = imagesOrg.elementAt(i);
            if (scaled) {
                Image scaledImage = csa.util.UtilityImage.toBufferedImage(csa.util.UtilityImage.imageScale(image, w, h));
                imagesUse.addElement(scaledImage);
            } else {
                imagesUse.addElement(image);
                h = image.getHeight(null);
                w = image.getWidth(null);
            }
        }
        setSize(new Dimension(w, h));
        setPreferredSize(new Dimension(w, h));
        if (v) setVisible(true);
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (imagesUse.size() == 0) {
            index = -1;
            return;
        }
        if (imagesUse.size() == 1) {
            index = 0;
            return;
        }
        if (b) {
            if (animator == null) {
                animator = new Thread(this);
                doRun = true;
                animator.start();
            }
        } else {
            if (animator != null) {
                synchronized (this) {
                    doRun = false;
                    animator.interrupt();
                    animator = null;
                }
            }
        }
    }

    public void run() {
        while (doRun) {
            try {
                Thread.sleep(animDelay);
            } catch (InterruptedException e) {
                break;
            }
            synchronized (this) {
                if (imagesUse.size() != 0) index = (index + 1) % imagesUse.size(); else index = -1;
            }
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        int clearWidth = getWidth();
        int clearHeight = getHeight();
        if (!isOpaque) g.clearRect(0, 0, clearWidth, clearHeight);
        if (index == -1) return;
        if ((imagesUse == null) || (imagesUse.size() == 0)) return;
        if (index > imagesUse.size() - 1) return;
        g.drawImage(imagesUse.elementAt(index), 0, 0, null);
    }
}
