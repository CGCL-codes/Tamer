package Converter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.imageio.ImageIO;

public class ModifyImage {

    public static void imageParser(Client c, File imageFile) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(imageFile));
        BufferedImage bi = ImageIO.read(is);
        if (bi == null) return;
        Graphics2D g = bi.createGraphics();
        double maxX = (double) bi.getWidth();
        double maxY = (double) bi.getHeight();
        g.setColor(Color.BLACK);
        List<Rectangle2D> listOfRectangles = c.getImageRecs();
        Font f = g.getFont();
        g.setFont(new Font(f.getFontName(), f.getStyle(), 18));
        for (int i = 0; i < listOfRectangles.size(); i++) {
            Rectangle2D rect = listOfRectangles.get(i);
            if (Global.DEBUG) g.setColor(Color.ORANGE); else g.setColor(Color.WHITE);
            if (rect.getMaxX() > maxX || rect.getMaxY() > maxY) {
            } else {
                g.fill(rect);
                g.setColor(Color.BLACK);
                int midY = (int) rect.getHeight() / 2;
                g.drawString(c.getID(), (int) rect.getMinX() + 5, (int) rect.getCenterY());
            }
        }
        is.close();
        ImageIO.write(bi, "JPG", imageFile);
    }

    public static BufferedImage pdfToImageInstantEdit(List<Rectangle2D> rects, BufferedImage bi, Client c) {
        if (rects == null || bi == null) {
            if (Global.DEBUG) {
                System.out.println("BufferedImage or Client is null");
            }
            return bi;
        }
        Graphics2D g = bi.createGraphics();
        double maxX = (double) bi.getWidth();
        double maxY = (double) bi.getHeight();
        List<Rectangle2D> listOfRectangles = rects;
        Font f = g.getFont();
        g.setFont(new Font(f.getFontName(), f.getStyle(), 18));
        for (int i = 0; i < listOfRectangles.size(); i++) {
            System.out.println("Adding rect " + i);
            Rectangle2D rect = listOfRectangles.get(i);
            if (Global.DEBUG) g.setColor(Color.ORANGE); else g.setColor(Color.WHITE);
            System.out.println(rect.getMaxX() + " > " + maxX + " and " + rect.getMaxY() + " > " + maxY);
            if (rect.getMaxX() > maxX || rect.getMaxY() > maxY) {
                System.out.println("Ignoring rect as it is bigger than the image.");
            } else {
                g.fill(rect);
                int midY = (int) rect.getHeight() / 2;
                g.setColor(Color.BLACK);
                g.drawString(c.getID(), (int) rect.getMinX() + 5, (int) rect.getCenterY());
            }
        }
        return bi;
    }

    public static BufferedImage pdfToImageEdit(Client c, BufferedImage bi) {
        if (c == null || bi == null) {
            if (Global.DEBUG) {
                System.out.println("BufferedImage or Client is null");
            }
            return bi;
        }
        Graphics2D g = bi.createGraphics();
        double maxX = (double) bi.getWidth();
        double maxY = (double) bi.getHeight();
        List<Rectangle2D> listOfRectangles = c.getPdfRecs();
        Font f = g.getFont();
        g.setFont(new Font(f.getFontName(), f.getStyle(), 18));
        for (int i = 0; i < listOfRectangles.size(); i++) {
            Rectangle2D rect = listOfRectangles.get(i);
            if (Global.DEBUG) g.setColor(Color.ORANGE); else g.setColor(Color.WHITE);
            if (rect.getMaxX() > maxX || rect.getMaxY() > maxY) {
            } else {
                g.fill(rect);
                int midY = (int) rect.getHeight() / 2;
                g.setColor(Color.BLACK);
                g.drawString(c.getID(), (int) rect.getMinX() + 5, (int) rect.getCenterY());
            }
        }
        return bi;
    }

    public static void main(String args[]) throws IOException {
        ArrayList<Rectangle2D> l = new ArrayList<Rectangle2D>();
        Rectangle rect = new Rectangle(10, 10, 100, 100);
        l.add(rect);
        rect = new Rectangle(500, 500, 300, 300);
        l.add(rect);
        ModifyImage.imageParser(new Client(), new File(Global.desktopPath() + "boshiimage.JPG"));
    }
}
