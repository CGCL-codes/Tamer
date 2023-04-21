package se.antimon.colourcontrols;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class MouseCursors {

    public static BufferedImage lineSetImage;

    public static Cursor lineSetCursor;

    public static BufferedImage colourSampleImage;

    public static Cursor colourSampleCursor;

    static {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Color transparent = new Color(0, true);
        lineSetImage = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) lineSetImage.getGraphics();
        g2d.setColor(transparent);
        g2d.fillRect(0, 0, 20, 20);
        g2d.setColor(Color.white);
        g2d.fillRect(0, 9, 20, 3);
        int[] polygonX = new int[] { 6, 14, 10 };
        int[] polygonY = new int[] { 6, 6, 10 };
        Polygon arrow = new Polygon(polygonX, polygonY, 3);
        g2d.fillPolygon(arrow);
        polygonX = new int[] { 6, 14, 10 };
        polygonY = new int[] { 14, 14, 10 };
        arrow = new Polygon(polygonX, polygonY, 3);
        g2d.fillPolygon(arrow);
        g2d.setColor(Color.black);
        polygonX = new int[] { 12, 8, 10 };
        polygonY = new int[] { 7, 7, 9 };
        arrow = new Polygon(polygonX, polygonY, 3);
        g2d.fillPolygon(arrow);
        polygonX = new int[] { 8, 12, 10 };
        polygonY = new int[] { 13, 13, 11 };
        arrow = new Polygon(polygonX, polygonY, 3);
        g2d.drawLine(8, 13, 12, 13);
        g2d.fillPolygon(arrow);
        g2d.drawLine(1, 10, 18, 10);
        Point hotspot = new Point(10, 10);
        lineSetCursor = toolkit.createCustomCursor(lineSetImage, hotspot, "lineSetCursor");
        colourSampleImage = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D) colourSampleImage.getGraphics();
        g2d.setColor(transparent);
        g2d.fillRect(0, 0, 20, 20);
        g2d.setColor(Color.white);
        g2d.fillOval(12, 0, 7, 7);
        g2d.setColor(Color.black);
        g2d.fillOval(13, 1, 5, 5);
        g2d.drawLine(6, 12, 13, 5);
        g2d.drawLine(7, 13, 14, 6);
        g2d.setColor(Color.white);
        g2d.drawLine(8, 11, 14, 5);
        hotspot = new Point(7, 12);
        colourSampleCursor = toolkit.createCustomCursor(colourSampleImage, hotspot, "colourSampleCursor");
    }
}
