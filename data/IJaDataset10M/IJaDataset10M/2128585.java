package Jplay;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * Class responsible for modeling an image.
 * @version 1.0
 * @author Gefersom Cardoso Lima
 * Federal Fluminense University
 * Computer Science
 */
public class GameImage extends GameObject {

    /**
     * Reference for an Image.
     * @see java.awt.Image
     */
    Image image;

    /**
    * Class constructor, it loads an image.
    * @param fileName path of the image and its name.
    * @see #loadImage(java.lang.String)
    */
    public GameImage(String fileName) {
        loadImage(fileName);
    }

    /**
    * This method loads an image.
    * @param fileName path of the image and its name.
     */
    public void loadImage(String fileName) {
        ImageIcon icon = new ImageIcon(fileName);
        this.image = icon.getImage();
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
    }

    /** Draw an image on the screen.*/
    public void draw() {
        Window.getInstance().getGameGraphics().drawImage(image, (int) x, (int) y, width, height, null);
    }
}
