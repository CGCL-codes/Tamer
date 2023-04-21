package arcane.object;

import java.awt.image.BufferedImage;

public class ImagePath implements java.io.Serializable {

    transient BufferedImage img;

    String path;

    public ImagePath() {
        this(null, "");
    }

    public ImagePath(BufferedImage img, String path) {
        this.img = img;
        this.path = path;
    }

    public void setImage(BufferedImage img, String path) {
        this.img = img;
        this.path = path;
    }

    public BufferedImage getImage() {
        return img;
    }

    public String getPath() {
        return path;
    }
}
