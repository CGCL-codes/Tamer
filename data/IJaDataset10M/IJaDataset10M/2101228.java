package org.jdmp.sigmen.client.carte;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class LocateBufferedImage {

    private BufferedImage img;

    private BufferedImage scaled;

    private int x;

    private int y;

    private int height;

    private int width;

    public LocateBufferedImage(BufferedImage img, int x, int y) {
        this.img = img;
        scaled = img;
        this.x = x;
        this.y = y;
        this.height = img.getHeight();
        this.width = img.getWidth();
    }

    public void draw(Graphics g, int size) {
        draw(g, 0, 0, size, size);
    }

    public void draw(Graphics g, int decalageX, int decalageY, int size) {
        draw(g, decalageX, decalageY, size, size);
    }

    public void draw(Graphics g, int height, int width) {
        draw(g, 0, 0, height, width);
    }

    public void draw(Graphics g, int decalageX, int decalageY, int height, int width) {
        if (height != this.height || width != this.width) {
            resize(height, width);
        }
        g.drawImage(scaled, (x + decalageX) * width / 100, (y + decalageY) * height / 100, null);
    }

    public void resize(int height, int width) {
        this.height = height;
        this.width = width;
        scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, width, height, null);
        g.dispose();
    }

    public void draw(Graphics g) {
        draw(g, 0, 0, img.getHeight() * Carte.getCaseSize() / 100, img.getWidth() * Carte.getCaseSize() / 100);
    }

    public void draw(Graphics g, int x, int y, boolean pos) {
        if (pos) {
            draw(g, x, y, img.getHeight() * Carte.getCaseSize() / 100, img.getWidth() * Carte.getCaseSize() / 100);
        } else {
            draw(g, x, y);
        }
    }

    public BufferedImage getImg() {
        return img;
    }
}
