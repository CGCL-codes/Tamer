package hailh.utils.swing;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import javax.swing.JPanel;

public class JPanelImage extends JPanel {

    private Image image;

    /**
	 * Create the panel
	 */
    public JPanelImage() {
        super();
    }

    @Override
    public void paint(Graphics gc) {
        super.paint(gc);
        gc.drawImage(image, 0, 0, null);
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }
}
