package org.xebra.client.gui.cine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;

/**
 * The Label covering the viewer that displays the initializing messages.
 * 
 * @author Rafael Chargel
 * @version $Revision: 1.1 $
 */
class MovieLabel extends JLabel {

    private static final long serialVersionUID = 2914665842246420314L;

    private BufferedImage offscreen;

    private CineModalDialog dialog;

    public MovieLabel(CineModalDialog dialog) {
        super();
        this.dialog = dialog;
    }

    protected void paintBuffer() {
        if (this.offscreen == null) {
            this.offscreen = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        }
        Graphics2D g = this.offscreen.createGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        BufferedImage image = this.dialog.getImage();
        int x = (getWidth() / 2) - (image.getWidth() / 2);
        int y = (getHeight() / 2) - (image.getHeight() / 2);
        g.drawImage(image, x, y, this);
    }

    public void paint(Graphics g) {
        paintBuffer();
        g.drawImage(this.offscreen, 0, 0, this);
    }

    public void update(Graphics g) {
        paint(g);
    }
}
