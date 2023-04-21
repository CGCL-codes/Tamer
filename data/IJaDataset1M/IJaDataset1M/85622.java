package gjset.gui.framework;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 * This class works like any ordinary JPanel, but draws a background image underneath all of its children.
 */
@SuppressWarnings("serial")
public class SimpleImagePanel extends JPanel {

    protected Image image;

    /**
	 * 
	 * Create this SimpleImagePanel with the indicated background image.
	 *
	 * @param image
	 */
    public SimpleImagePanel(Image image) {
        this.image = image;
        setSize(image.getWidth(this), image.getHeight(this));
    }

    /**
	 * 
	 * When painting this component, just draw the image.  Do nothing else.
	 *
	 * @param g
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
    public void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }
}
