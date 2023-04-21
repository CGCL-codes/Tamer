package com.lovehorsepower.imagemailerapp.workers;

import com.lovehorsepower.imagemailerapp.panels.MainTabPanel;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.stream.ImageInputStream;
import org.openide.util.Exceptions;

/**
 *
 * @author Joseph Obernberger
 */
public class ImageResizerThread extends Thread {

    String filename = "";

    ImageLoader imageLoader = null;

    String cacheDir = null;

    MainTabPanel mp = null;

    public ImageResizerThread(String filename, String cacheDir, ImageLoader imageLoader) {
        this.filename = filename;
        this.cacheDir = cacheDir;
        this.imageLoader = imageLoader;
        this.mp = null;
    }

    public ImageResizerThread(String filename, MainTabPanel mp) {
        this.filename = filename;
        this.mp = mp;
        this.imageLoader = null;
    }

    @Override
    public void run() {
        BufferedImage result = null;
        if ((filename.endsWith(".JPG")) || (filename.endsWith(".jpg")) || (filename.endsWith(".jpeg")) || (filename.endsWith(".JPEG"))) {
            result = resizeAnImage(filename);
        }
        if (imageLoader != null) {
            imageLoader.doneResize(result, filename);
        }
        if (mp != null) {
            mp.doneImageResize(result, filename);
        }
    }

    public BufferedImage resizeAnImage(String filename) {
        long sTime = 0;
        long eTime = 0;
        File file = null;
        File cacheThumb = null;
        String thumbFilename = null;
        file = new File(filename);
        System.out.println("Resize image: " + filename + " with cacheDir: " + cacheDir);
        sTime = System.currentTimeMillis();
        if (cacheDir != null) {
            thumbFilename = cacheDir + filename.substring(filename.lastIndexOf("/"));
            System.out.println("ThumbFilename is: " + thumbFilename);
            cacheThumb = new File(thumbFilename);
            if (cacheThumb.exists()) {
                try {
                    return ImageIO.read(cacheThumb);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
        ImageIO.setCacheDirectory(null);
        ImageIO.setUseCache(true);
        BufferedImage bi = null;
        try {
            if (file.length() > (1024 * 1024)) {
                bi = readImage(file, 8);
            } else {
                bi = readImage(file, 1);
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        eTime = System.currentTimeMillis();
        System.out.println("Load time: " + (eTime - sTime) + "mSec.");
        sTime = System.currentTimeMillis();
        int thumbWidth = 160;
        int thumbHeight = 120;
        double thumbRatio = (double) thumbWidth / (double) thumbHeight;
        int imageWidth = bi.getWidth();
        int imageHeight = bi.getHeight();
        double imageRatio = (double) imageWidth / (double) imageHeight;
        if (thumbRatio < imageRatio) {
            thumbHeight = (int) (thumbWidth / imageRatio);
        } else {
            thumbWidth = (int) (thumbHeight * imageRatio);
        }
        BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = thumbImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(bi, 0, 0, thumbWidth, thumbHeight, null);
        if (cacheThumb != null) {
            try {
                ImageIO.write(thumbImage, "jpg", cacheThumb);
            } catch (IOException ex) {
                System.out.println("Exception during thumbnail write: " + ex);
            }
        }
        eTime = System.currentTimeMillis();
        System.out.println("Resize time: " + (eTime - sTime) + "mSec.");
        return thumbImage;
    }

    public BufferedImage readImage(Object source, int sampleRate) throws IOException {
        ImageInputStream stream = ImageIO.createImageInputStream(source);
        ImageReader reader = (ImageReader) ImageIO.getImageReaders(stream).next();
        reader.setInput(stream);
        ImageReadParam param = reader.getDefaultReadParam();
        param.setSourceSubsampling(sampleRate, sampleRate, 0, 0);
        ImageTypeSpecifier typeToUse = null;
        for (Iterator i = reader.getImageTypes(0); i.hasNext(); ) {
            ImageTypeSpecifier type = (ImageTypeSpecifier) i.next();
            if (type.getColorModel().getColorSpace().isCS_sRGB()) {
                typeToUse = type;
            }
        }
        if (typeToUse != null) {
            param.setDestinationType(typeToUse);
        }
        BufferedImage b = reader.read(0, param);
        reader.dispose();
        stream.close();
        return b;
    }

    public BufferedImage resizeTwo(String imagefile) {
        BufferedImage temp = null;
        int width = 160;
        int height = 120;
        BufferedImage returnImage = null;
        int tempWidth;
        int tempHeight;
        int x = 0;
        int y = 0;
        try {
            temp = javax.imageio.ImageIO.read(new File(imagefile));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        int imageWidth = temp.getWidth(null);
        int imageHeight = temp.getHeight(null);
        if (imageWidth < imageHeight) {
            tempWidth = width;
            tempHeight = (int) (((double) imageHeight * width) / imageWidth);
            y = -(tempHeight - tempWidth) / 2;
        } else {
            tempHeight = height;
            tempWidth = (int) (((double) imageWidth * height) / imageHeight);
            x = -(tempWidth - tempHeight) / 2;
        }
        Image temp1 = temp.getScaledInstance(tempWidth, tempHeight, Image.SCALE_SMOOTH);
        returnImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        returnImage.getGraphics().drawImage(temp1, x, y, null);
        return returnImage;
    }
}
