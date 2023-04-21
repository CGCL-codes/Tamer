package ws.system;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;

public class PngEncoder {

    BufferedImage image = null;

    public PngEncoder(BufferedImage img) {
        image = img;
    }

    public byte[] pngEncode() throws Exception {
        ByteArrayOutputStream imageData = new ByteArrayOutputStream();
        ImageIO.setUseCache(false);
        ImageIO.write(image, "PNG", imageData);
        return imageData.toByteArray();
    }
}
