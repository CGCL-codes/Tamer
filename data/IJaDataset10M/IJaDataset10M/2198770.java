package org.middleheaven.image;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Map;

public abstract class CanvasImageSource implements ImageSource {

    @Override
    public final Image getImage(Map<String, Object> params) {
        Dimension dim = getDimension(params);
        BufferedImage image = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        paint(g2d, params);
        return image;
    }

    protected abstract Dimension getDimension(Map<String, Object> params);

    protected abstract void paint(Graphics g, Map<String, Object> params);
}
