package com.panomedic.gui;

import com.panomedic.core.Photo;
import com.panomedic.utils.Utils;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Yare
 */
public class PhotoPanel extends JPanel {

    protected Photo photo;

    protected BufferedImage image;

    public PhotoPanel() {
        image = null;
        setBackground(Color.WHITE);
        repaint(0, 0, 0, getWidth(), getHeight());
    }

    public PhotoPanel(BufferedImage image) {
        this();
        this.image = image;
    }

    public void paint(Graphics g) {
        int cellWidth = getWidth();
        int cellHeight = getHeight();
        Graphics2D g2 = (Graphics2D) g;
        g2.setBackground(getBackground());
        g.setColor(getBackground());
        g.fillRect(0, 0, cellWidth, cellHeight);
        if (image != null) {
            Dimension limits = new Dimension(cellWidth, cellHeight);
            int w = image.getWidth();
            int h = image.getHeight();
            Dimension dim = Utils.getScaledSize(limits, w, h);
            int x = (cellWidth - dim.width) / 2;
            int y = (cellHeight - dim.height) / 2;
            g.drawImage(image, x, y, dim.width, dim.height, this);
        }
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
        if (photo != null) image = photo.getImage();
        repaint(0, 0, 0, getWidth(), getHeight());
    }

    public void setImage(BufferedImage image) {
        photo = null;
        this.image = image;
        repaint(0, 0, 0, getWidth(), getHeight());
    }

    public BufferedImage getImage() {
        return image;
    }
}
