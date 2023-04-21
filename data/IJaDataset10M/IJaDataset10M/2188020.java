package org.galaxy.gpf.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import org.galaxy.gpf.util.Resource;

/**
 * A factory for creating Cursor objects.
 * 
 * @author Cheng Liang
 * @version 1.0.0
 */
public class CursorFactory {

    /** The tool kit. */
    private static Toolkit toolkit = Toolkit.getDefaultToolkit();

    /** The pencil cursor. */
    private static Cursor pencil;

    /** The radius. */
    private static int pencilRadius;

    /**
	 * Gets the pencil cursor.
	 * 
	 * @param radius the radius of pencil
	 * 
	 * @return the pencil cursor
	 */
    public static Cursor getPencilCursor(int radius) {
        if (pencilRadius != radius) {
            pencilRadius = radius;
            int lower = radius - 5;
            int upper = radius + 5;
            int in = radius << 1;
            int out = in + 2;
            Image img = new BufferedImage(out, out, BufferedImage.TYPE_INT_ARGB);
            Graphics g = img.getGraphics();
            g.setColor(Color.BLACK);
            g.drawOval(0, 0, in, in);
            g.drawLine(lower, radius, upper, radius);
            g.drawLine(radius, lower, radius, upper);
            Resource res = Resource.getInstance();
            pencil = toolkit.createCustomCursor(img, new Point(radius, radius), res.getString("cursor.pencil.text"));
        }
        return pencil;
    }
}
