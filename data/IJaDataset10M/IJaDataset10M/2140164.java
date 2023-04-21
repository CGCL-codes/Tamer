package com.ourrosary.swing.base;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * @author Bhavani P Polimetla
 * @since Aug-15-2009
 */
public class ImagePanel extends JPanel {

    private static final long serialVersionUID = -1926563015297556721L;

    private Image img;

    private BufferedImage bufferedImage = null;

    public ImagePanel(Image img) {
        super();
        try {
            this.img = img;
            Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);
            setSize(size);
            setLayout(null);
            setBackground(Color.WHITE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ImagePanel(String img) {
        this(new ImageIcon(img).getImage());
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (null == bufferedImage) g.drawImage(img, 0, 0, null); else g.drawImage(bufferedImage, 0, 0, null);
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }
}
