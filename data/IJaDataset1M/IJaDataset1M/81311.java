package com.golden.gamedev.object.background;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import com.golden.gamedev.object.Background;

/**
 * Background that use a single image as the background.
 */
public class ImageBackground extends Background {

    /**
   * 
   */
    private static final long serialVersionUID = -4083512078848542717L;

    private transient BufferedImage image;

    /**
   * Creates new <code>ImageBackground</code> with specified image and
   * background size.
   */
    public ImageBackground(BufferedImage image, int w, int h) {
        super(w, h);
        this.image = image;
    }

    /**
   * Creates new <code>ImageBackground</code> with specified image and the
   * background size is as large as the image.
   */
    public ImageBackground(BufferedImage image) {
        super(image.getWidth(), image.getHeight());
        this.image = image;
    }

    /**
   * Returns this background image.
   */
    public BufferedImage getImage() {
        return this.image;
    }

    /**
   * Sets this background image, and the size of this background is set to the
   * image size.
   */
    public void setImage(BufferedImage image) {
        this.image = image;
        this.setSize(image.getWidth(), image.getHeight());
    }

    /** ************************************************************************* */
    public void render(Graphics2D g, int xbg, int ybg, int x, int y, int w, int h) {
        g.drawImage(this.image, x, y, x + w, y + h, xbg, ybg, xbg + w, ybg + h, null);
    }
}
