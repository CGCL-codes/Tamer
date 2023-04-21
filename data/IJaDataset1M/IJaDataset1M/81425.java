package dw2;

import java.awt.*;
import java.awt.image.*;

public class ImageFunctions {

    public static Image toImage(BufferedImage bufferedImage) {
        return Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource());
    }

    public static BufferedImage toBufferedImage(Image image) {
        int width = image.getWidth(null), height = image.getHeight(null);
        if (width <= 0) width = 1;
        if (height <= 0) height = 1;
        BufferedImage ret = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        ret.getGraphics().drawImage(image, 0, 0, null);
        return ret;
    }

    public static java.awt.Image ApplyAlphaMask(java.awt.Image original, java.awt.Image alphamask) {
        int width = original.getWidth(null), height = original.getHeight(null);
        if (width <= 0) width = 1;
        if (height <= 0) height = 1;
        BufferedImage resultImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        BufferedImage image = toBufferedImage(original);
        BufferedImage mask = toBufferedImage(alphamask);
        for (int y = 0; y < image.getHeight(null); y++) {
            for (int x = 0; x < image.getWidth(null); x++) {
                try {
                    Color c = new Color(image.getRGB(x, y));
                    Color maskC = new Color(mask.getRGB(x, y));
                    Color maskedColor = new Color(c.getRed(), c.getGreen(), c.getBlue(), maskC.getRed());
                    resultImg.setRGB(x, y, maskedColor.getRGB());
                } catch (Exception e) {
                }
            }
        }
        return toImage(resultImg);
    }
}
