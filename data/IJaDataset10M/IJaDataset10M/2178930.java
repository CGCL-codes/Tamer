package cmjTracer.gui;

import javax.swing.*;
import cmjTracer.color.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.*;
import java.awt.geom.*;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel {

    protected BufferedImage bitmap;

    protected Graphics bitmapGraphics;

    public ImagePanel(int w, int h) {
        bitmap = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        bitmapGraphics = bitmap.getGraphics();
        setPreferredSize(new Dimension(w, h));
    }

    public void setRGB(int x, int y, int rgb) {
        bitmap.setRGB(x, y, rgb);
    }

    public int getRGB(int x, int y) {
        return bitmap.getRGB(x, y);
    }

    public void square(int x, int y, Color3d color, int spread) {
        bitmapGraphics.setColor(color.toAWTColor());
        bitmapGraphics.fillRect(x, y, spread, spread);
    }

    public void paint(Graphics g) {
        ((Graphics2D) g).drawImage(bitmap, (AffineTransform) null, (ImageObserver) null);
    }
}
