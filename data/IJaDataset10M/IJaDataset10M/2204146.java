package org.larz.dom3.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

public class ImageConverter {

    public static ImageData convertToSWT(BufferedImage bufferedImage) {
        if (bufferedImage.getColorModel() instanceof DirectColorModel) {
            DirectColorModel colorModel = (DirectColorModel) bufferedImage.getColorModel();
            PaletteData palette = new PaletteData(colorModel.getRedMask(), colorModel.getGreenMask(), colorModel.getBlueMask());
            ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
            for (int y = 0; y < data.height; y++) {
                for (int x = 0; x < data.width; x++) {
                    int rgb = bufferedImage.getRGB(x, y);
                    int pixel = palette.getPixel(new RGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF));
                    data.setPixel(x, y, pixel);
                    if (colorModel.hasAlpha()) {
                        data.setAlpha(x, y, (rgb >> 24) & 0xFF);
                    }
                }
            }
            return data;
        } else if (bufferedImage.getColorModel() instanceof IndexColorModel) {
            IndexColorModel colorModel = (IndexColorModel) bufferedImage.getColorModel();
            int size = colorModel.getMapSize();
            byte[] reds = new byte[size];
            byte[] greens = new byte[size];
            byte[] blues = new byte[size];
            colorModel.getReds(reds);
            colorModel.getGreens(greens);
            colorModel.getBlues(blues);
            RGB[] rgbs = new RGB[size];
            for (int i = 0; i < rgbs.length; i++) {
                rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF, blues[i] & 0xFF);
            }
            PaletteData palette = new PaletteData(rgbs);
            ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
            data.transparentPixel = colorModel.getTransparentPixel();
            WritableRaster raster = bufferedImage.getRaster();
            int[] pixelArray = new int[1];
            for (int y = 0; y < data.height; y++) {
                for (int x = 0; x < data.width; x++) {
                    raster.getPixel(x, y, pixelArray);
                    data.setPixel(x, y, pixelArray[0]);
                }
            }
            return data;
        }
        return null;
    }

    static final int BLACK = Color.BLACK.getRGB();

    static final int WHITE = Color.WHITE.getRGB();

    static final int MAGENTA = new Color(248, 0, 248).getRGB();

    public static BufferedImage cropImage(BufferedImage buffer) {
        int x = getX(buffer);
        int y = getY(buffer);
        int z = getZ(buffer);
        BufferedImage image = buffer.getSubimage(x, y, z - x, buffer.getHeight() - y);
        for (int j = 0; j < image.getWidth(); j++) {
            for (int k = 0; k < image.getHeight(); k++) {
                int rgb = image.getRGB(j, k);
                if (rgb == BLACK) {
                    image.setRGB(j, k, WHITE);
                }
                if (rgb == MAGENTA) {
                    image.setRGB(j, k, BLACK);
                }
            }
        }
        return image;
    }

    private static int getX(BufferedImage buffer) {
        for (int x = 0; x < buffer.getWidth(); x++) {
            for (int y = 0; y < buffer.getHeight(); y++) {
                int rgb = buffer.getRGB(x, y);
                if (rgb != BLACK) {
                    return x;
                }
            }
        }
        return 0;
    }

    private static int getY(BufferedImage buffer) {
        for (int y = 0; y < buffer.getHeight(); y++) {
            for (int x = 0; x < buffer.getWidth(); x++) {
                int rgb = buffer.getRGB(x, y);
                if (rgb != BLACK) {
                    return y;
                }
            }
        }
        return 0;
    }

    private static int getZ(BufferedImage buffer) {
        for (int z = buffer.getWidth() - 1; z >= 0; z--) {
            for (int y = 0; y < buffer.getHeight(); y++) {
                int rgb = buffer.getRGB(z, y);
                if (rgb != BLACK) {
                    return z;
                }
            }
        }
        return 0;
    }
}
