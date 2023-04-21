package net.sourceforge.javagg.gsc.defaults;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import net.sourceforge.javagg.gsc.abstractions.AbstractScreen;

/**
 * 
 * This is a very important class
 * 
 * @author Larry Gray
 * @version 1.4
 * 
 * 
 */
public class DefaultScreen extends AbstractScreen {

    public DefaultScreen() {
        super();
    }

    public void update(Graphics g) {
        paintComponent(g);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (screenImage == null) clearScreen();
        createGraphics();
        ((Graphics2D) g).drawImage(screenImage.getScaledInstance(super.getHorizontalResolution() * zoomSize, super.getVerticalResolution() * zoomSize, Image.SCALE_REPLICATE), 0, 0, null);
    }

    public void bigger() {
        zoomSize++;
        repaint();
    }

    public void createGraphics() {
        if (screenImage == null) clearScreen();
        graphics2D = screenImage.createGraphics();
    }

    public void clearScreen() {
        super.screenImage = new BufferedImage(super.getHorizontalResolution(), super.getVerticalResolution(), BufferedImage.TYPE_INT_RGB);
        createGraphics();
        super.repaint();
    }

    public void smaller() {
        zoomSize--;
        repaint();
    }
}
