package register.ucm;

import java.awt.image.*;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.*;
import javax.imageio.*;
import com.sun.image.codec.jpeg.*;

/**
 *
 * @author Vitamin
 */
public class PhotoPruner {

    private static Image baseImage;

    private static Image croppedImage;

    private static BufferedImage tag;

    private static final int WIDTH = 180;

    private static final int HEIGHT = 240;

    public PhotoPruner() {
    }

    public void startPrune(String temp) throws Exception {
        File file = new File("D:\\TP_ORIGINAL\\" + temp + ".jpg");
        baseImage = ImageIO.read(file);
        croppedImage = cropImage(70, 0, WIDTH, HEIGHT);
        tag = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        tag.getGraphics().drawImage(croppedImage, 0, 0, WIDTH, HEIGHT, null);
        FileOutputStream fos = new FileOutputStream("D:\\TP_PRUNED\\" + temp + ".jpg");
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(bos);
        encoder.encode(tag);
        bos.close();
    }

    private static Image cropImage(int sx, int sy, int width, int height) {
        ImageFilter filter = new CropImageFilter(sx, sy, width, height);
        ImageProducer producer = new FilteredImageSource(baseImage.getSource(), filter);
        Image img = Toolkit.getDefaultToolkit().createImage(producer);
        return img;
    }
}
