package ezsudoku.view;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 * Extends JPanel features.
 *
 * @author Cedric Chantepie (cchantepie@corsaire.fr
 */
public abstract class AbstractPanel extends JPanel {

    /**
     */
    private Image backgroundImage = null;

    /**
     * Returns the background image
     * @return Background image
     */
    public Image getBackgroundImage() {
        return backgroundImage;
    }

    /**
     * Sets the background image
     * @param backgroundImage Background image
     */
    public void setBackgroundImage(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    /**
     * Overrides the painting to display a background image
     */
    protected void paintComponent(Graphics g) {
        if (isOpaque()) {
            paintBackground(g);
        }
        if (backgroundImage != null) {
            g.drawImage(this.backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        super.paintComponent(g);
    }

    /**
     */
    protected void paintBackground(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Sole constructor.
     */
    public AbstractPanel() {
        super();
    }
}
