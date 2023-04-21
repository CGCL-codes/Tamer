package com.sts.webmeet.client;

import java.awt.*;
import java.awt.image.*;

public class ImagePanel extends Panel implements ImageObserver {

    public ImagePanel(Image image) {
        this.image = image;
    }

    public void setImage(Image image) {
        this.image = image;
        Graphics g = getGraphics();
        if (null != g) {
            paint(g);
        } else {
        }
        repaint();
    }

    public void paint(Graphics g) {
        if (null != image) {
            g.drawImage(image, 0, 0, this);
        } else {
            System.out.println("MyImagePanel WARNING image is null");
        }
    }

    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    public Dimension getPreferredSize() {
        if (null != image) {
            return new Dimension(image.getWidth(this), image.getHeight(this));
        } else {
            return new Dimension();
        }
    }

    private Image image;
}
