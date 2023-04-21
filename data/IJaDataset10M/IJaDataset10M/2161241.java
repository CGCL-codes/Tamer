package com.kenstevens.stratdom.ui.image;

import java.io.FileNotFoundException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import com.kenstevens.stratdom.main.Constants;

public class ImageUtils {

    private static final int CONTRAST_REDUCTION = 3;

    public static Image colourifyImage(Image image, RGB colour, PixelFilter pixelFilter) {
        Image imageCopy = new Image(Display.getCurrent(), image, SWT.IMAGE_COPY);
        ImageData imageData = imageCopy.getImageData();
        colourifyImage(imageData, colour, pixelFilter);
        return new Image(Display.getDefault(), imageData);
    }

    private static void colourifyImage(ImageData imageData, RGB colour, PixelFilter pixelFilter) {
        int[] lineData = new int[imageData.width];
        RGBRange rgbRange = new RGBRange(lineData, imageData);
        RGB avg = rgbRange.average();
        for (int y = 0; y < imageData.height; y++) {
            imageData.getPixels(0, y, Constants.IMG_PIXELS, lineData, 0);
            for (int x = 0; x < lineData.length; x++) {
                int pixelValue = lineData[x];
                if (pixelFilter.match(imageData.palette, pixelValue)) {
                    continue;
                }
                RGB rgb = getRGB(imageData.palette, pixelValue);
                if (rgb.red > 20 || rgb.green > 20 || rgb.blue > 20) {
                    int newR = Math.min(255, Math.max(0, colour.red + (rgb.red - avg.red) / CONTRAST_REDUCTION));
                    int newG = Math.min(255, Math.max(0, colour.green + (rgb.green - avg.green) / CONTRAST_REDUCTION));
                    int newB = Math.min(255, Math.max(0, colour.blue + (rgb.blue - avg.blue) / CONTRAST_REDUCTION));
                    int newPixelValue = newR + (newG << 8) + (newB << 16);
                    imageData.setPixel(x, y, newPixelValue);
                }
            }
        }
        ;
    }

    static RGB getRGB(PaletteData palette, int pixelValue) {
        int redMask = palette.redMask;
        int greenMask = palette.greenMask;
        int blueMask = palette.blueMask;
        int r = pixelValue & redMask;
        int g = (pixelValue & greenMask) >> 8;
        int b = (pixelValue & blueMask) >> 16;
        return new RGB(r, g, b);
    }

    public static Image getTransparentImage(Image image) throws FileNotFoundException, Exception {
        ImageData imageData = image.getImageData();
        int whitePixel = imageData.palette.getPixel(new RGB(255, 255, 255));
        convertBlueOrGreenToWhite(imageData);
        imageData.transparentPixel = whitePixel;
        return new Image(Display.getDefault(), imageData);
    }

    public static Image greyifyImage(Image transparentImage) {
        return new Image(Display.getDefault(), transparentImage, SWT.IMAGE_GRAY);
    }

    public static void convertBlueOrGreenToWhite(ImageData imageData) {
        int[] lineData = new int[imageData.width];
        for (int y = 0; y < imageData.height; y++) {
            imageData.getPixels(0, y, Constants.IMG_PIXELS, lineData, 0);
            for (int x = 0; x < lineData.length; x++) {
                int pixelValue = lineData[x];
                RGB rgb = getRGB(imageData.palette, pixelValue);
                if (isBlueish(rgb)) {
                    imageData.setPixel(x, y, 0xFFFFFF);
                } else if (isGreenish(rgb)) {
                    imageData.setPixel(x, y, 0xFFFFFF);
                }
            }
        }
    }

    public static boolean isGreenish(RGB rgb) {
        return rgb.green >= 67 && (rgb.red == 0 || rgb.green / rgb.red >= 3) && (rgb.blue == 0 || rgb.green / rgb.blue >= 4);
    }

    public static boolean isBlueish(RGB rgb) {
        return rgb.blue >= 124 && (rgb.red == 0 || (float) rgb.blue / rgb.red >= 2.8) && (rgb.green == 0 || (float) rgb.blue / rgb.green >= 1.5);
    }

    public static boolean isBlackish(RGB rgb) {
        return rgb.red <= 4 && rgb.green <= 4 && rgb.blue <= 4 || Math.abs((float) rgb.red / rgb.green - 1.0) < 0.15 && Math.abs((float) rgb.red / rgb.blue - 1.0) < 0.15;
    }

    public static PixelFilter getBlueGreenFilter() {
        return new PixelFilter() {

            @Override
            public boolean match(PaletteData palette, int pixelValue) {
                RGB rgb = ImageUtils.getRGB(palette, pixelValue);
                return ImageUtils.isBlueish(rgb) || ImageUtils.isGreenish(rgb);
            }
        };
    }

    public static PixelFilter getWhiteFilter() {
        return new PixelFilter() {

            @Override
            public boolean match(PaletteData palette, int pixelValue) {
                return pixelValue == 0xFFFFFF;
            }
        };
    }
}
