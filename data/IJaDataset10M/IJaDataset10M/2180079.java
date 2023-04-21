package gui.components;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * This class represents a panel displaying an image.
 * 
 * @author Thomas Pedley
 */
public class Image extends JPanel {

    /** The serialisation UID. */
    private static final long serialVersionUID = -3253275064314124177L;

    /** The image to draw. */
    private BufferedImage image = null;

    /**
	 * Constructor.
	 * 
	 * @param image The image to display.
	 */
    public Image(BufferedImage image) {
        this.image = image;
    }

    /**
	 * Set the image.
	 * 
	 * @param image The image to set.
	 */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
	 * Called to paint the component.
	 * 
	 * @param g The graphics to paint.
	 */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    /**
	 * Get the image (used if just the raw image is required).
	 * 
	 * @return The image contained within this control.
	 */
    public BufferedImage getImage() {
        return image;
    }
}
