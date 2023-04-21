package buseylab.fiteyemodel.logic;

import buseylab.fiteyemodel.gauss.GaussKernel1d;
import ij.plugin.filter.Convolver;
import ij.process.Blitter;
import ij.process.ImageProcessor;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.util.Vector;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.swing.ImageIcon;

public class ImageUtils {

    public static Image scaleImage(Image I, int preferredWidth, int preferredHeight) {
        Image scaledI = I;
        int width = I.getWidth(null);
        int height = I.getHeight(null);
        double scaledWidth, scaledHeight;
        double scaleFactor;
        scaleFactor = (double) preferredWidth / (double) width;
        scaledWidth = width * scaleFactor;
        scaledHeight = height * scaleFactor;
        scaleFactor = (double) preferredHeight / (double) scaledHeight;
        scaledWidth = scaledWidth * scaleFactor;
        scaledHeight = scaledHeight * scaleFactor;
        System.out.println("Scaling image to " + (int) scaledWidth + "x" + (int) scaledHeight);
        scaledI = I.getScaledInstance((int) scaledWidth, (int) scaledHeight, Image.SCALE_DEFAULT);
        return scaledI;
    }

    public static Image stitchImages(Image left, Image right) {
        int leftWidth = left.getWidth(null);
        int leftHeight = left.getHeight(null);
        int rightWidth = right.getWidth(null);
        int rightHeight = right.getHeight(null);
        int taller;
        BufferedImage stitched;
        Graphics stitchedGraphics;
        if (leftHeight > rightHeight) {
            taller = leftHeight;
        } else {
            taller = rightHeight;
        }
        stitched = new BufferedImage(leftWidth + rightWidth, taller, BufferedImage.TYPE_INT_RGB);
        stitchedGraphics = stitched.getGraphics();
        stitchedGraphics.drawImage(left, 0, 0, null);
        stitchedGraphics.drawImage(right, leftWidth, 0, null);
        return stitched;
    }

    public static Image stitchImages(Image left, Image right, int padWidth, Color padColor) {
        int leftWidth = left.getWidth(null);
        int leftHeight = left.getHeight(null);
        int rightWidth = right.getWidth(null);
        int rightHeight = right.getHeight(null);
        int taller;
        BufferedImage stitched;
        Graphics stitchedGraphics;
        if (leftHeight > rightHeight) {
            taller = leftHeight;
        } else {
            taller = rightHeight;
        }
        stitched = new BufferedImage(leftWidth + padWidth + rightWidth, taller, BufferedImage.TYPE_INT_RGB);
        stitchedGraphics = stitched.getGraphics();
        stitchedGraphics.drawImage(left, 0, 0, null);
        stitchedGraphics.setColor(padColor);
        stitchedGraphics.fillRect(leftWidth, 0, leftWidth + padWidth, taller);
        stitchedGraphics.drawImage(right, leftWidth + padWidth, 0, null);
        return stitched;
    }

    public static int[] getPixels(Image I) {
        int width = I.getWidth(null);
        int height = I.getHeight(null);
        int pixels[] = new int[width * height];
        PixelGrabber pg = new PixelGrabber(I, 0, 0, width, height, pixels, 0, width);
        try {
            pg.grabPixels();
        } catch (InterruptedException ie) {
            System.out.println("Interrupted");
        }
        return pixels;
    }

    public static int[] getPixels(Image I, int x, int y, int width, int height) {
        int pixels[] = new int[width * height];
        PixelGrabber pg = new PixelGrabber(I, x, y, width, height, pixels, 0, width);
        try {
            pg.grabPixels();
        } catch (InterruptedException ie) {
            System.out.println("Interrupted");
        }
        return pixels;
    }

    public static double[] RGBtoGrayDouble(int[] RGBpixels) {
        double[] grayDoublePixels = new double[RGBpixels.length];
        for (int i = 0; i < RGBpixels.length; i++) {
            int blue = RGBpixels[i] & 0xff;
            grayDoublePixels[i] = (double) blue;
        }
        return grayDoublePixels;
    }

    public static int[] RGBtoGray(int[] RGBpixels) {
        int[] grayPixels = new int[RGBpixels.length];
        for (int i = 0; i < RGBpixels.length; i++) {
            int blue = RGBpixels[i] & 0xff;
            grayPixels[i] = blue;
        }
        return grayPixels;
    }

    public static int[] grayDoubleToRGB(double[] grayDoublePixels) {
        double pixel;
        int numClipneg = 0;
        int numClip = 0;
        int[] RGBpixels = new int[grayDoublePixels.length];
        int red, green, blue;
        int alpha = 255;
        for (int i = 0; i < grayDoublePixels.length; i++) {
            pixel = grayDoublePixels[i];
            if (pixel < 0) {
                pixel = 0;
                numClipneg++;
            }
            if (pixel > 255) {
                pixel = 255;
                numClip++;
            }
            red = (int) pixel;
            green = (int) pixel;
            blue = (int) pixel;
            RGBpixels[i] = (alpha << 24) | (red << 16) | (green << 8) | blue;
        }
        return RGBpixels;
    }

    public static int[] grayToRGB(int[] grayPixels) {
        double pixel;
        int numClipneg = 0;
        int numClip = 0;
        int[] RGBpixels = new int[grayPixels.length];
        int red, green, blue;
        int alpha = 255;
        for (int i = 0; i < grayPixels.length; i++) {
            pixel = grayPixels[i];
            if (pixel < 0) {
                pixel = 0;
                numClipneg++;
            }
            if (pixel > 255) {
                pixel = 255;
                numClip++;
            }
            red = (int) pixel;
            green = (int) pixel;
            blue = (int) pixel;
            RGBpixels[i] = (alpha << 24) | (red << 16) | (green << 8) | blue;
        }
        return RGBpixels;
    }

    public static Image makeImageFromPixels(int[] pixels, int width, int height) {
        return Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(width, height, pixels, 0, width));
    }

    public static BufferedImage plotXtoImage(BufferedImage I, int x, int y, Color c) {
        int alpha = 255;
        int red = c.getRed();
        int green = c.getGreen();
        int blue = c.getBlue();
        int pixel = (alpha << 24) | (red << 16) | (green << 8) | blue;
        if (x > 0 && y > 0 && x < I.getWidth() && y < I.getHeight()) {
            I.setRGB(x - 1, y, pixel);
            I.setRGB(x, y, pixel);
            I.setRGB(x + 1, y, pixel);
            I.setRGB(x, y - 1, pixel);
            I.setRGB(x, y + 1, pixel);
        }
        return I;
    }

    public static int[] invertPixels(int[] pixels) {
        int[] inverted = new int[pixels.length];
        for (int i = 0; i < pixels.length; i++) {
            inverted[i] = 255 - pixels[i];
        }
        return inverted;
    }

    public static int[] threshold(int[] pixels, int t) {
        int[] threshPixels = new int[pixels.length];
        for (int i = 0; i < pixels.length; i++) {
            if (pixels[i] < t) {
                threshPixels[i] = 0;
            } else {
                threshPixels[i] = 255;
            }
        }
        return threshPixels;
    }

    public static int[] threshold(int[] pixels, int from, int to, int color) {
        int[] threshPixels = new int[pixels.length];
        for (int i = 0; i < pixels.length; i++) {
            if (pixels[i] <= to && pixels[i] >= from) {
                threshPixels[i] = color;
            } else {
                threshPixels[i] = (255 << 32);
            }
        }
        return threshPixels;
    }

    public static double[] rotate(double[] pixels, int width, int height, double angle) {
        double newPixels[] = null;
        if ((width * height) == pixels.length) {
            angle = angle / 180 * Math.PI;
            double midWidth = (double) width / 2.0;
            double midHeight = (double) height / 2.0;
            double CosAngle = Math.cos(angle);
            double SinAngle = Math.sin(angle);
            double mSinAngle = -Math.sin(angle);
            double transX, transY;
            int newX, newY;
            double newXD, newYD;
            double a, b;
            double newPixel;
            newPixels = new double[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    transX = x - midWidth;
                    transY = y - midHeight;
                    newXD = (transX * CosAngle + transY * SinAngle) + midWidth;
                    newYD = (transX * mSinAngle + transY * CosAngle) + midHeight;
                    newX = (int) Math.floor(newXD);
                    newY = (int) Math.floor(newYD);
                    if ((newX < width - 1) & (newX >= 0) & (newY < height - 1) & newY >= 0) {
                        a = newXD - newX;
                        b = newYD - newY;
                        newPixel = (1 - a) * (1 - b) * pixels[newY * width + newX] + a * (1 - b) * pixels[newY * width + (newX + 1)] + (1 - a) * b * pixels[(newY + 1) * width + newX] + a * b * pixels[(newY + 1) * width + (newX + 1)];
                        newPixels[y * width + x] = newPixel;
                    } else {
                        newPixels[y * width + x] = pixels[0];
                    }
                }
            }
        }
        return newPixels;
    }

    public static double[] adjustContrast(double pixels[], int width, int height, double contrastChange, double brightnessChange) {
        double newPixels[] = null;
        int x, y;
        double pixel;
        if ((width * height) == pixels.length) {
            newPixels = new double[width * height];
            int newIndex = 0;
            for (y = 0; y < height; y++) {
                for (x = 0; x < width; x++) {
                    pixel = pixels[y * width + x];
                    pixel = ((pixel - 128.0) * contrastChange) + (128.0 + brightnessChange);
                    newPixels[newIndex++] = pixel;
                }
            }
        }
        return newPixels;
    }

    public static double[] combinePixels(double[] pix1, double pix1weight, double[] pix2, double pix2weight, int width, int height) {
        double newPixels[] = null;
        int newIndex;
        double p1, p2;
        int x, y;
        if ((width * height) == pix1.length && (width * height) == pix2.length) {
            newIndex = 0;
            newPixels = new double[width * height];
            for (y = 0; y < height; y++) {
                for (x = 0; x < width; x++) {
                    p1 = pix1[y * width + x];
                    p2 = pix2[y * width + x];
                    newPixels[newIndex++] = (p1 * pix1weight) + (p2 * pix2weight);
                }
            }
        }
        return newPixels;
    }

    public static double[] linearizePixels(double pixels[], int width, int height, Vector abVector) {
        double newPixels[] = null;
        int x, y;
        double pixel, a, b, newLinear, linearVoltage;
        a = Double.parseDouble((String) abVector.elementAt(0));
        b = Double.parseDouble((String) abVector.elementAt(1));
        double maxLinear = Math.pow((1.0 / a) * 255.0, (1.0 / b));
        if ((width * height) == pixels.length) {
            newPixels = new double[width * height];
            int newIndex = 0;
            for (y = 0; y < height; y++) {
                for (x = 0; x < width; x++) {
                    pixel = pixels[y * width + x];
                    newLinear = (pixel / 255.0) * (maxLinear - 1.0) + 1.0;
                    linearVoltage = a * Math.pow(newLinear, b);
                    pixel = linearVoltage;
                    newPixels[newIndex++] = pixel;
                }
            }
        }
        return newPixels;
    }

    public static int createARGB(int alpha, int r, int g, int b) {
        return (alpha << 24) + (r << 16) + (g << 8) + b;
    }

    /** 
     * This method returns true if the specified image has transparent pixels 
     * From http://www.exampledepot.com/egs/java.awt.image/HasAlpha.html
     */
    public static boolean hasAlpha(Image image) {
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage) image;
            return bimage.getColorModel().hasAlpha();
        }
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }
        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }

    /**
     * This method returns a buffered image with the contents of an image
     * From http://www.exampledepot.com/egs/java.awt.image/Image2Buf.html
     */
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        image = new ImageIcon(image).getImage();
        boolean hasAlpha = hasAlpha(image);
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
        }
        if (bimage == null) {
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
        Graphics g = bimage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }

    public static void unsharpMask(ImageProcessor ip, double sigma, double a) {
        ImageProcessor I = ip.convertToFloat();
        ImageProcessor J = I.duplicate();
        float[] H = GaussKernel1d.create(sigma);
        Convolver cv = new Convolver();
        cv.setNormalize(true);
        cv.convolve(J, H, 1, H.length);
        cv.convolve(J, H, H.length, 1);
        I.multiply(1 + a);
        J.multiply(a);
        I.copyBits(J, 0, 0, Blitter.SUBTRACT);
        ip.insert(I.convertToByte(false), 0, 0);
    }

    public static BufferedImage loadRGBImage(File inputFile) {
        RenderedOp op = null;
        try {
            op = JAI.create("fileload", inputFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BufferedImage buffer = null;
        try {
            buffer = op.getAsBufferedImage();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BufferedImage image = new BufferedImage(buffer.getWidth(), buffer.getHeight(), BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(buffer, 0, 0, null);
        return image;
    }

    /** 
     * Return a part of the regtangle that is within in image. The result is
     * unknown when the rectangle is completely outside of the image
     */
    public static Rectangle clipRectangle(Image image, Rectangle rec) {
        Rectangle imRec = new Rectangle(0, 0, image.getWidth(null), image.getHeight(null));
        return imRec.intersection(rec);
    }
}
