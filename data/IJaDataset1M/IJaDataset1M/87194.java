package EnigmaUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Snowangelic
 */
public class ImageLoader {

    public static BufferedImage loadImage(String filename) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(filename));
            return bufferedImage;
        } catch (IOException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static BufferedImage getGreenSquareImage() {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File("./images/greensquare.png"));
            return bufferedImage;
        } catch (IOException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static BufferedImage getOrangeSquareImage() {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File("./images/orangesquare.png"));
            return bufferedImage;
        } catch (IOException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static BufferedImage getRedSquareImage() {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File("./images/redsquare.png"));
            return bufferedImage;
        } catch (IOException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
