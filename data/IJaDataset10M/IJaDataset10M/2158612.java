package client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageList {

    private int finger;

    private Vector<String> images;

    private final String imageUrl1 = "WebContent/design/upload1.png";

    private final String imageUrl2 = "WebContent/design/upload2.png";

    private final String imageUrl3 = "WebContent/design/upload3.png";

    private final String imageUrl4 = "WebContent/design/upload4.png";

    /**
	 * Creates an imagelist with 4 images
	 */
    public ImageList() {
        images = new Vector<String>(4);
        images.add(imageUrl1);
        images.add(imageUrl2);
        images.add(imageUrl3);
        images.add(imageUrl4);
        finger = 0;
    }

    /**
	 * Rotate the image, in other words: return the next image from the vector images
	 * @return ImageIcon
	 */
    public ImageIcon rotate() {
        finger++;
        BufferedImage img = null;
        if (finger == images.size()) {
            finger = 0;
        }
        try {
            img = ImageIO.read(new File(images.get(finger)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ImageIcon(img);
    }

    /**
	 * @return ImageIcon the image currently select by this imagelist
	 */
    public ImageIcon getCurrentImage() {
        BufferedImage img = null;
        try {
            File file = new File(images.get(finger));
            img = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ImageIcon(img);
    }
}
