package net.sourceforge.omov.app.gui.comp.generic;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JComponent;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class ImagePanel extends JComponent {

    private static final long serialVersionUID = -9004123909937374280L;

    private Image image;

    public ImagePanel(int width, int height) {
        this(new Dimension(width, height));
    }

    public ImagePanel(Dimension dimension) {
        this.setPreferredSize(dimension);
        this.setMaximumSize(dimension);
        this.setMinimumSize(dimension);
    }

    public ImagePanel(Image image) {
        this.setImage(image);
        this.setLayout(null);
    }

    public void setImage(Image image) {
        this.image = image;
        if (image != null) {
            final Dimension size = new Dimension(image.getWidth(null), image.getHeight(null));
            this.setPreferredSize(size);
            this.setMinimumSize(size);
            this.setMaximumSize(size);
            this.setSize(size);
        }
        this.repaint();
    }

    public void paintComponent(Graphics g) {
        if (this.image != null) {
            g.drawImage(this.image, 0, 0, null);
        }
    }
}
