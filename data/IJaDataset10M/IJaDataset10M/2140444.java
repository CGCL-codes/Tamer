package com.g2d.display.ui.text.ga;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import com.g2d.Tools;

public class GraphicsAttributeImage extends java.awt.font.GraphicAttribute {

    public static Image ERROR_IMAGE = Tools.createImage(16, 16);

    private Image fImage;

    private Rectangle fImageBounds;

    public GraphicsAttributeImage(String image_path) {
        super(GraphicsAttributeImage.BOTTOM_ALIGNMENT);
        fImage = Tools.readImage(image_path);
        if (fImage == null) {
            fImage = ERROR_IMAGE;
        }
        fImageBounds = new Rectangle(0, 0, fImage.getWidth(null), fImage.getHeight(null));
    }

    public GraphicsAttributeImage(Image image) {
        super(GraphicsAttributeImage.BOTTOM_ALIGNMENT);
        fImage = image;
        fImageBounds = new Rectangle(0, 0, fImage.getWidth(null), fImage.getHeight(null));
    }

    public float getAscent() {
        return 0;
    }

    public float getDescent() {
        return fImageBounds.height;
    }

    public float getAdvance() {
        return fImageBounds.width;
    }

    public Rectangle2D getBounds() {
        return fImageBounds;
    }

    public void draw(Graphics2D graphics, float x, float y) {
        graphics.drawImage(fImage, (int) (x), (int) (y), null);
    }
}
