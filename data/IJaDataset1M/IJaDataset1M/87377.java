package com.rapidminer.gui.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.JPanel;

/**
 * This panel can be used to display an image.
 *
 * @author Ingo Mierswa
 */
public class ImagePanel extends JPanel {

    private static final long serialVersionUID = 3903395116300542548L;

    public static final int CHILDRENS_PREFERRED_SIZE = 0;

    public static final int IMAGE_PREFERRED_SIZE = 1;

    public static final int IMAGE_PREFERRED_HEIGHT = 2;

    private transient Image image = null;

    private int preferredSizeType = CHILDRENS_PREFERRED_SIZE;

    public ImagePanel(Image image, int preferredSizeType) {
        this.image = image;
        this.preferredSizeType = preferredSizeType;
        setOpaque(true);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension dimension = super.getPreferredSize();
        switch(this.preferredSizeType) {
            case CHILDRENS_PREFERRED_SIZE:
                break;
            case IMAGE_PREFERRED_HEIGHT:
                dimension.height = image.getHeight(null);
                break;
            case IMAGE_PREFERRED_SIZE:
                dimension.height = image.getHeight(null);
                dimension.width = image.getWidth(null);
                break;
        }
        return dimension;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
        paintChildren(graphics);
    }
}
