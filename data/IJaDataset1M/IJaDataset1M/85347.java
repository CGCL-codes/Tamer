package mathgame.common;

import java.util.*;
import java.awt.*;
import javax.swing.ImageIcon;
import java.net.URL;
import java.util.Hashtable;
import java.awt.Image;
import java.awt.image.*;
import java.awt.Toolkit;
import java.io.*;

public class ImageArchive {

    private static Hashtable<String, BufferedImage> archive;

    private static GraphicsConfiguration gconf;

    private ImageArchive() {
    }

    static {
        archive = new Hashtable<String, BufferedImage>(2113);
        gconf = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    }

    /** 
     * Loads a picture from the hashtable archive
     * if there is no picture, calls the method loadImage().
     */
    public static BufferedImage getImage(String filename, boolean translucent) {
        BufferedImage picture = archive.get(filename);
        if (picture == null) {
            picture = loadImage(filename, translucent);
            archive.put(filename, picture);
        }
        return picture;
    }

    public static BufferedImage getImage(URL url, boolean translucent) {
        String urlFilename = url.getFile();
        BufferedImage picture = archive.get(urlFilename);
        if (picture == null) {
            picture = loadImage(url, translucent);
            archive.put(urlFilename, picture);
        }
        return picture;
    }

    public static BufferedImage getImage(MGSv1Frame frame, boolean translucent) {
        try {
            String hashBase = frame.getURL() + "$" + frame.getFrameIdentifier() + "." + frame.getContentType();
            BufferedImage picture = archive.get(hashBase);
            if (picture == null) {
                picture = loadImage(frame.getDataInputStream(), translucent);
                archive.put(hashBase, picture);
            }
            return picture;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }

    private static BufferedImage loadImage(String filename, boolean translucent) {
        URL url = ClassLoader.getSystemResource(filename);
        if (url == null) {
            System.err.println("ImageArchive.loadImage(String): Cannot find image with filename \"" + filename + "\".");
            return null;
        } else return loadImage(url, translucent);
    }

    private static BufferedImage loadImage(URL url, boolean translucent) {
        try {
            return loadImage(url.openStream(), translucent);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }

    /**
     * Another way to load a picture...
     */
    private static BufferedImage loadImage(InputStream indata, boolean translucent) throws IOException {
        if (gconf == null) {
            System.err.println("In ImageArchive: GraphicsConfiguration is null!");
            return null;
        }
        byte[] buffer = new byte[indata.available()];
        indata.read(buffer);
        ImageIcon tempIcon = new ImageIcon(buffer);
        BufferedImage tempImage = gconf.createCompatibleImage(tempIcon.getIconWidth(), tempIcon.getIconHeight(), (translucent ? Transparency.TRANSLUCENT : Transparency.BITMASK));
        tempImage.getGraphics().drawImage(tempIcon.getImage(), 0, 0, null);
        return tempImage;
    }

    public static Image makeImageTransparent(Image img) {
        return makeImageTransparent(img, 0x5F);
    }

    public static Image makeImageTransparent(Image img, int alpha) {
        if (!(img instanceof BufferedImage)) return img;
        BufferedImage newimg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        WritableRaster wr = newimg.getColorModel().createCompatibleWritableRaster(img.getWidth(null), img.getHeight(null));
        int[] olddata = ((DataBufferInt) ((BufferedImage) img).getRaster().getDataBuffer()).getData();
        int[] newdata = ((DataBufferInt) wr.getDataBuffer()).getData();
        int alphamask = alpha << 24;
        for (int i = 0; i < olddata.length; i++) {
            newdata[i] = olddata[i];
            if ((newdata[i] & 0xFF000000) != 0) newdata[i] |= alphamask;
        }
        newimg.setData(wr);
        return newimg;
    }

    public static Image alterImageChannel(Image img, char channel, int value) {
        int byteToAlter;
        if (channel == 'b' || channel == 'B') byteToAlter = 0; else if (channel == 'g' || channel == 'G') byteToAlter = 1; else if (channel == 'r' || channel == 'R') byteToAlter = 2; else if (channel == 'a' || channel == 'A') byteToAlter = 3; else throw new IllegalArgumentException();
        BufferedImage newimg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        newimg.createGraphics().drawImage(img, 0, 0, null);
        WritableRaster wr = newimg.getColorModel().createCompatibleWritableRaster(img.getWidth(null), img.getHeight(null));
        int[] olddata = ((DataBufferInt) newimg.getRaster().getDataBuffer()).getData();
        int[] newdata = ((DataBufferInt) wr.getDataBuffer()).getData();
        int mask = (0x000000FF & value) << byteToAlter * 8;
        for (int i = 0; i < olddata.length; i++) {
            newdata[i] = olddata[i];
            if ((newdata[i] & (0xFF << byteToAlter * 8)) != 0) {
                newdata[i] |= mask;
            }
        }
        newimg.setData(wr);
        return newimg;
    }
}
