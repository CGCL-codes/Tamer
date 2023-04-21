package net.yura.mobile.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import net.yura.mobile.gui.Graphics2D;
import net.yura.mobile.io.FileUtil;
import net.yura.mobile.logging.Logger;

/**
 * @author Yura Mamyrin
 */
public class ImageUtil {

    /**
     * @see com.nokia.mid.ui.DirectUtils#createImage(int, int, int)
     */
    public static Image makeImage(int w, int h, int color) {
        int[] rgbBuff = new int[w * h];
        for (int i = 0; i < rgbBuff.length; i++) {
            rgbBuff[i] = color;
        }
        return Image.createRGBImage(rgbBuff, w, h, true);
    }

    public static void imageColor(int pixels[], int color) {
        int r = (color & 0xff0000) >> 16;
        int g = (color & 0xff00) >> 8;
        int b = (color & 0xff) >> 0;
        for (int i = 0; i < pixels.length; i++) {
            int alpha = (pixels[i] & 0xff000000) >> 24;
            int blue = (pixels[i] & 0xff) >> 0;
            pixels[i] = alpha << 24 | (blue * r) / 255 << 16 | (blue * g) / 255 << 8 | (blue * b) / 255;
        }
    }

    /**
     * replaces all values of the blue channel with a color
     */
    public static Image imageColor(Image image, int i) {
        int ai[] = new int[image.getWidth() * image.getHeight()];
        image.getRGB(ai, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
        imageColor(ai, i);
        return Image.createRGBImage(ai, image.getWidth(), image.getHeight(), true);
    }

    public static Image colorize(Image original, int newColor) {
        int[] rgba = new int[original.getWidth() * original.getHeight()];
        original.getRGB(rgba, 0, original.getWidth(), 0, 0, original.getWidth(), original.getHeight());
        for (int i = 0; i < rgba.length; i++) {
            int alpha = ((rgba[i] >> 24) & 0xFF);
            rgba[i] = ((newColor & 0xFFFFFF) | (alpha << 24));
        }
        return Image.createRGBImage(rgba, original.getWidth(), original.getHeight(), true);
    }

    public static Image scaleImage(Image img, int newW, int newH) {
        try {
            Class.forName("javax.microedition.m3g.Background");
            Image resImg = Image.createImage(newW, newH);
            new Graphics2D(resImg.getGraphics()).drawScaledImage(img, 0, 0, newW, newH);
            return resImg;
        } catch (Throwable e) {
        }
        return getScaledImage(img, newW, newH);
    }

    /**
     * return null if not enough mem to load image
     */
    public static Image getImageFromFile(String filename) {
        InputStream is = null;
        try {
            is = FileUtil.getInputStreamFromFileConnector(filename);
            return Image.createImage(is);
        } catch (Throwable err) {
            Logger.warn("failed to load image for: " + filename + " " + err.toString());
            Logger.warn(err);
            return null;
        } finally {
            FileUtil.close(is);
        }
    }

    public interface ThumbnailLoader {

        Image getThumbnailFromFile(String fileName);
    }

    public static ThumbnailLoader thumbLoader;

    /**
     * will return null if no thumb found
     */
    public static Image getThumbnailFromFile(final String fileName) {
        if (thumbLoader != null) {
            Image img = thumbLoader.getThumbnailFromFile(fileName);
            if (img != null) {
                return img;
            }
        }
        InputStream dis = null;
        try {
            dis = FileUtil.getInputStreamFromFileConnector(fileName);
            return getThumbFromFile(dis);
        } catch (Throwable err) {
            Logger.warn("failed to load thumb for: " + fileName + " " + err.toString());
            Logger.warn(err);
            return null;
        } finally {
            FileUtil.close(dis);
        }
    }

    public static int LARGESTBYTESTREAM = 170000;

    public static int THUMB_MAX_SIZE = 30000;

    /**
   * Scale and resize image to fit our screen
   * @param dispImage, the image
   * @param width , max width
   * @param heigth, max height
   * @return Image - a scaled(resized) image
   */
    public static Image getScaledImage(Image dispImage, int width, int height) {
        if (dispImage != null) {
            int imWidth = dispImage.getWidth();
            int imHeight = dispImage.getHeight();
            int widthScale = (imWidth << 9) * 4 / (width * 4);
            int heightScale = (imHeight << 9) * 1 / height;
            int chosenScale = Math.max(widthScale, heightScale);
            dispImage = resize(dispImage, 0, chosenScale, 9);
        }
        return dispImage;
    }

    /**
   * Get a thumbnail image from a byte array
   * @param fileBytes
   * @return Image
   */
    public static Image getJPEGthumb(byte[] fileBytes) {
        Image image = null;
        try {
            if (isJPEG(fileBytes[0], fileBytes[1])) {
                int startIndex = 2;
                do {
                    while (fileBytes[startIndex] != -1) {
                        startIndex++;
                    }
                    startIndex++;
                } while ((fileBytes[startIndex] & 0xFF) != 0xD8);
                int endIndex = startIndex + 1;
                do {
                    while (fileBytes[endIndex] != -1) {
                        endIndex++;
                    }
                    endIndex++;
                } while ((fileBytes[endIndex] & 0xFF) != 0xD9);
                image = Image.createImage(fileBytes, startIndex - 1, endIndex - startIndex + 2);
            }
        } catch (Throwable e) {
            Logger.error(e);
        }
        return image;
    }

    /**
   * Reads an InputStream into a array of bytes
   * @param inStr image stream
   * @param no_of_filebytes the size of the stream
   * @return byte[] array of bytes containing the image
   */
    private static byte[] readStream2(InputStream inStr, int no_of_filebytes) throws IOException {
        DataInputStream newStr = new DataInputStream(inStr);
        byte[] byteArray = new byte[no_of_filebytes];
        newStr.readFully(byteArray);
        newStr.close();
        return byteArray;
    }

    /**
   * Checks if two consectutive bytes can be interpreted as a jpeg
   * @param b1 first byte
   * @param b2 second byte
   * @return true if b1 and b2 are jpeg markers
   */
    private static boolean isJPEG(byte b1, byte b2) {
        return ((b1 & 0xFF) == 0xFF && (b2 & 0xFF) == 0xD8);
    }

    /**
   * Traverse an inputStream and return a thumbnail image if any. We build the thumbnail directly
   * from the inputstream, thus avoiding to run out of memory on very large picture files.
   * @param str the stream
   * @returns Image - created from thumbnail iside jpeg file.
   */
    public static Image getThumbFromFile(InputStream str) throws IOException {
        byte[] tempByteArray = new byte[THUMB_MAX_SIZE];
        byte[] bytefileReader = { 0 };
        byte firstByte, secondByte = 0;
        int currentIndex = 0;
        str.read(bytefileReader);
        firstByte = bytefileReader[0];
        str.read(bytefileReader);
        secondByte = bytefileReader[0];
        int a;
        if (isJPEG(firstByte, secondByte)) {
            byte rByte = 0;
            do {
                while (rByte != -1) {
                    a = str.read(bytefileReader);
                    if (a == -1) {
                        return null;
                    }
                    rByte = bytefileReader[0];
                }
                a = str.read(bytefileReader);
                if (a == -1) {
                    return null;
                }
                rByte = bytefileReader[0];
            } while ((rByte & 0xFF) != 0xD8);
            tempByteArray[currentIndex++] = -1;
            tempByteArray[currentIndex++] = rByte;
            rByte = 0;
            do {
                while (rByte != -1) {
                    a = str.read(bytefileReader);
                    if (a == -1) {
                        return null;
                    }
                    rByte = bytefileReader[0];
                    tempByteArray[currentIndex++] = rByte;
                }
                a = str.read(bytefileReader);
                if (a == -1) {
                    return null;
                }
                rByte = bytefileReader[0];
                tempByteArray[currentIndex++] = rByte;
            } while ((rByte & 0xFF) != 0xD9);
            tempByteArray[currentIndex++] = -1;
            Image im = Image.createImage(tempByteArray, 0, currentIndex - 1);
            tempByteArray = null;
            return im;
        }
        str.close();
        return null;
    }

    /**
   * Created a sized Image from a file
   * @param filename name of the image file
   * @param cW device width
   * @param cH device height
   * @return Image returns scaled image that should fit our screen dimensions
   */
    public static Image createImage(String filename, int cW, int cH) throws IOException, IOException {
        Image rImage = null;
        FileConnection fconn = (FileConnection) Connector.open(filename);
        InputStream inStream = fconn.openInputStream();
        if (fconn.fileSize() > LARGESTBYTESTREAM) {
            rImage = getThumbFromFile(inStream);
        } else {
            byte[] fileBytes = readStream2(inStream, (int) fconn.fileSize());
            inStream.close();
            fconn.close();
            rImage = createImage(fileBytes);
            fileBytes = null;
        }
        Image scaledImage = getScaledImage(rImage, cW, cH);
        return scaledImage;
    }

    /**
   * Get a scaled image directly from inputstream
   * @param is resource input stream
   * @param cW screen width
   * @param cH screen height
   * @return Image if we can create/scale from is otherwise null
   */
    public static Image createImage(InputStream is, int cW, int cH) {
        try {
            return getScaledImage(Image.createImage(is), cW, cH);
        } catch (Exception ex) {
            Logger.warn(ex);
        }
        return null;
    }

    /**
   * Creates an image direct from array of bytes if size permits us.
   * @param encodedImage the image in an array of bytes
   * @return Image
   */
    private static Image createImage(byte[] encodedImage) {
        try {
            Image dispImage = null;
            if (encodedImage.length > LARGESTBYTESTREAM) {
                return getJPEGthumb(encodedImage);
            }
            if (dispImage == null) {
                return Image.createImage(encodedImage, 0, encodedImage.length);
            }
        } catch (Exception e) {
            System.gc();
            try {
                return getJPEGthumb(encodedImage);
            } catch (Exception e2) {
                Logger.warn(e2);
            }
        }
        return null;
    }

    /**
   * Fit the image on our screen size (width, height)
   * @param inImage the image to be resized
   * @param scaleFactor the factor with which we want to scale
   * @param scalePwr shift width/height scalePwr bits
   * @return Image - the resized image
   */
    public static Image resize(Image inImage, int rotation, int scaleFactor, int scalePwr) {
        if (inImage == null) {
            return null;
        }
        int inImWidth = inImage.getWidth();
        int inImHeight = inImage.getHeight();
        int outWidth;
        int outHeight;
        int rgbRowIn[];
        int inImStartW;
        int inImStartH;
        int inImStepW;
        int inImStepH;
        int rgbRowStartIndex;
        int stepRgbRow;
        int getRGBwidth;
        int getRGBheight;
        rgbRowIn = new int[inImWidth];
        outHeight = (inImHeight << scalePwr) / scaleFactor;
        outWidth = (inImWidth << scalePwr) / scaleFactor;
        inImStartW = 0;
        inImStepW = 0;
        inImStartH = 0;
        inImStepH = scaleFactor;
        rgbRowStartIndex = 0;
        stepRgbRow = scaleFactor;
        getRGBwidth = inImWidth;
        getRGBheight = 1;
        int rgbRowOut[] = new int[outWidth];
        Image outImage = Image.createImage(outWidth, outHeight);
        if (outImage == null) return null;
        Graphics imGraphic = outImage.getGraphics();
        if (imGraphic == null || rgbRowIn == null || rgbRowOut == null) {
            outImage = null;
            return null;
        }
        for (int h = 0; h < outHeight; h++) {
            int currW = rgbRowStartIndex;
            int x = Math.max(0, Math.min(inImWidth - 1, inImStartW >> scalePwr));
            int y = Math.max(0, Math.min(inImHeight - 1, inImStartH >> scalePwr));
            inImage.getRGB(rgbRowIn, 0, getRGBwidth, x, y, getRGBwidth, getRGBheight);
            for (int w = 0; w < outWidth; w++) {
                rgbRowOut[w] = rgbRowIn[Math.max(0, Math.min(rgbRowIn.length, currW >> scalePwr))];
                currW += stepRgbRow;
            }
            imGraphic.drawRGB(rgbRowOut, 0, outWidth, 0, h, outWidth, 1, false);
            inImStartW += inImStepW;
            inImStartH += inImStepH;
        }
        return outImage;
    }

    public static void saveImage(Image image, OutputStream outputStream, String format) throws Exception {
        javax.microedition.amms.MediaProcessor mediaProc = javax.microedition.amms.GlobalManager.createMediaProcessor("image/raw");
        javax.microedition.amms.control.ImageFormatControl formatControl = (javax.microedition.amms.control.ImageFormatControl) mediaProc.getControl("javax.microedition.amms.control.ImageFormatControl");
        formatControl.setFormat("image/" + format);
        mediaProc.setInput(image);
        mediaProc.setOutput(outputStream);
        mediaProc.complete();
    }

    public static void saveImage(Image image, OutputStream outputStream) {
        try {
            saveImage(image, outputStream, "jpeg");
        } catch (Exception ex) {
            System.out.println("failed to save jpg, falling back to png");
            try {
                saveImage(image, outputStream, "png");
            } catch (Exception ex2) {
                Logger.warn(ex);
                Logger.warn(ex2);
            }
        }
    }
}
