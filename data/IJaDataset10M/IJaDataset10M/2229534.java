package filter;

import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import com.sun.image.codec.jpeg.*;

public class ImageResizer {

    public ImageResizer() {
    }

    public void getThumbnails(String path, String image) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(path + File.separatorChar + image));
            double smallScaleX = 75 / (double) bufferedImage.getWidth();
            double smallScaleY = 75 / (double) bufferedImage.getHeight();
            double middleScaleX = 480 / (double) bufferedImage.getWidth();
            double middleScaleY = 480 / (double) bufferedImage.getHeight();
            double smallScaler;
            double middleScaler;
            if (smallScaleX > smallScaleY) {
                smallScaler = smallScaleY;
            } else {
                smallScaler = smallScaleX;
            }
            if (middleScaleX > middleScaleY) {
                middleScaler = middleScaleY;
            } else {
                middleScaler = middleScaleX;
            }
            AffineTransform tx = new AffineTransform();
            AffineTransformOp op = null;
            if (smallScaler > 1) {
                smallScaler = 1;
            }
            tx.scale(smallScaler, smallScaler);
            op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            BufferedImage smallImage = op.filter(bufferedImage, null);
            tx = new AffineTransform();
            if (middleScaler > 1) {
                middleScaler = 1;
            }
            tx.scale(middleScaler, middleScaler);
            op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            BufferedImage middleImage = op.filter(bufferedImage, null);
            FileOutputStream out = new FileOutputStream(new File(path + File.separatorChar + "s_" + image));
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            JPEGEncodeParam jpegParams = encoder.getDefaultJPEGEncodeParam(smallImage);
            jpegParams.setQuality(0.8f, false);
            encoder.setJPEGEncodeParam(jpegParams);
            encoder.encode(smallImage);
            out.close();
            out = new FileOutputStream(new File(path + File.separatorChar + "m_" + image));
            encoder = JPEGCodec.createJPEGEncoder(out);
            jpegParams = encoder.getDefaultJPEGEncodeParam(middleImage);
            jpegParams.setQuality(0.8f, false);
            encoder.setJPEGEncodeParam(jpegParams);
            encoder.encode(middleImage);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
