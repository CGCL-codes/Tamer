package org.chemicalcovers.utils;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 * ImageLoader<P/>
 * 
 * A smart image loader that:<BR/>
 * <UL>
 * <LI>wait for the image to be loaded</LI>
 * <LI>correctly handle animated gif</LI>
 * </UL>
 * 
 * @see #loadImage(URL) for more detail
 * 
 * @author Pascal Dal Farra
 */
public class ImageLoader {

    /**
	 * Synchronously loads an image and return when one of the following event occurs<BR/>
	 * <UL>
	 * <LI>the image is completely loaded</LI>
	 * <LI>the first frame of an animated image is completely loaded</LI>
	 * <LI>an error occurs during image loading</LI>
	 * <LI>image loading was aborted</LI>
	 * </UL>
	 * @param theImageUrl url of image to be loaded
	 * @return
	 */
    @SuppressWarnings("synthetic-access")
    public static Image loadImage(URL theImageUrl) {
        if (theImageUrl == null) return null;
        return new ImageLoaderDelegate().loadImage(theImageUrl);
    }

    /**
	 * 
	 * @param theImageUrl
	 * @return
	 */
    @SuppressWarnings("synthetic-access")
    public static ImageIcon loadImageIcon(URL theImageUrl) {
        Image image = loadImage(theImageUrl);
        if (image == null) return null;
        return new ImageIcon(image);
    }

    private static class ImageLoaderDelegate implements ImageObserver {

        /**
		 * Synchronously loads an image and return when one of the following event occurs<BR/>
		 * <UL>
		 * <LI>the image is completely loaded</LI>
		 * <LI>the first frame of an animated image is completely loaded</LI>
		 * <LI>an error occurs during image loading</LI>
		 * <LI>image loading was aborted</LI>
		 * </UL>
		 * @param theImageUrl url of image to be loaded
		 * @return the corresponding Image (or null if something went wrong) 
		 */
        public synchronized Image loadImage(URL theImageUrl) {
            if (theImageUrl == null) return null;
            Image image = null;
            try {
                image = Toolkit.getDefaultToolkit().createImage(theImageUrl);
                image.getWidth(this);
                wait();
            } catch (Throwable theIgnoredThrowable) {
            }
            return image;
        }

        /**
		 * Notify this ImageLoaderDelegate when load is over (may it be successful or not)
		 * @see java.awt.ImageObserver
		 */
        public synchronized boolean imageUpdate(Image theImg, int theInfoflags, int theX, int theY, int theW, int theH) {
            if ((theInfoflags & ImageObserver.ALLBITS) != 0 || (theInfoflags & ImageObserver.FRAMEBITS) != 0 || (theInfoflags & ImageObserver.ABORT) != 0 || (theInfoflags & ImageObserver.ERROR) != 0) {
                notify();
                return false;
            }
            return true;
        }
    }
}
