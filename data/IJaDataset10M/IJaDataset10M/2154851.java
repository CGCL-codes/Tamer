package com.ebixio.jai;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.media.jai.*;
import javax.media.jai.operator.*;
import javax.swing.*;

/**
 * A class to create icons from Planar Images
 */
public class Iconizer implements Icon {

    protected int width = 64;

    protected int height = 64;

    protected BufferedImage icon = null;

    /**
    * Default constructor
    */
    public Iconizer() {
    }

    /**
     * @param source a PlanarImage to be displayed.
     * @param width is the icon width
     * @param height is the icon height
     */
    public Iconizer(PlanarImage image, int width, int height) {
        this.width = width;
        this.height = height;
        icon = iconify(image);
    }

    public int getIconWidth() {
        return width;
    }

    public int getIconHeight() {
        return height;
    }

    /**
     * Paint the icon
     */
    public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2D = null;
        if (g instanceof Graphics2D) {
            g2D = (Graphics2D) g;
        } else {
            return;
        }
        AffineTransform transform = AffineTransform.getTranslateInstance(0, 0);
        g2D.drawRenderedImage(icon, transform);
    }

    private BufferedImage iconify(PlanarImage image) {
        float scale = 1.0F;
        float s1 = (float) width / (float) image.getWidth();
        float s2 = (float) height / (float) image.getHeight();
        if (s1 > s2) {
            scale = s1;
        } else {
            scale = s2;
        }
        InterpolationBilinear interp = new InterpolationBilinear();
        Float scalef = new Float(scale);
        Float zerof = new Float(0.0F);
        PlanarImage temp = (PlanarImage) ScaleDescriptor.create(image, scalef, scalef, zerof, zerof, interp, null);
        return temp.getAsBufferedImage();
    }

    public void save(String filename, String format) {
        JAI.create("filestore", icon, filename, format, null);
    }
}
