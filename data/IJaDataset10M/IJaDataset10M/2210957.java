package net.sourceforge.jpotpourri.jpotface.util;

import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.ReplicateScaleFilter;
import java.io.File;
import java.util.Date;
import net.sourceforge.jpotpourri.jpotface.PtImageInfo;
import net.sourceforge.jpotpourri.jpotface.PtWidthHeight;
import org.apache.log4j.Logger;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public final class PtImageUtil {

    private static final Logger LOG = Logger.getLogger(PtImageUtil.class);

    private static final boolean DEBUG = false;

    private PtImageUtil() {
    }

    public static PtImageInfo getResizedImage(final File coverFile, final Component component, final int maxWidth, final int maxHeight) {
        LOG.info("resizing cover image '" + coverFile.getAbsolutePath() + "' to max " + maxWidth + "/" + maxHeight + ".");
        final Image source = Toolkit.getDefaultToolkit().getImage(coverFile.getAbsolutePath());
        return PtImageUtil.getResizedImage(source, component, maxWidth, maxHeight);
    }

    public static PtImageInfo getResizedImage(final Image source, final Component component, final int maxWidth, final int maxHeight) {
        LOG.info("resizing cover image to max " + maxWidth + "/" + maxHeight + ".");
        final Date resizeActionStart = new Date();
        final MediaTracker media = new MediaTracker(component);
        Image resizedImage;
        media.addImage(source, 0);
        PtWidthHeight newDimension = null;
        try {
            media.waitForID(0);
            final int oldWidth = source.getWidth(component);
            final int oldHeight = source.getHeight(component);
            newDimension = recalcMaxWidthHeight(oldWidth, oldHeight, maxWidth, maxHeight);
            final int newWidth = newDimension.getWidth();
            final int newHeight = newDimension.getHeight();
            ImageFilter replicate = new ReplicateScaleFilter(newWidth, newHeight);
            ImageProducer prod = new FilteredImageSource(source.getSource(), replicate);
            resizedImage = Toolkit.getDefaultToolkit().createImage(prod);
            media.addImage(resizedImage, 1);
            media.waitForID(1);
        } catch (final InterruptedException e) {
            LOG.error("interrupted while creating resized cover image!", e);
            throw new RuntimeException("interrupted while creating resized cover image!");
        }
        final double secondsTook = ((double) (new Date(new Date().getTime() - resizeActionStart.getTime())).getTime() / 1000);
        LOG.info("resizing image took " + secondsTook + " seconds.");
        return new PtImageInfo(resizedImage, newDimension);
    }

    @SuppressWarnings("cast")
    private static PtWidthHeight recalcMaxWidthHeight(final int oldWidth, final int oldHeight, final int maxWidth, final int maxHeight) {
        final int newWidth;
        final int newHeight;
        final boolean widthOversize = oldWidth > maxWidth;
        final boolean heightOversize = oldHeight > maxHeight;
        if (DEBUG) {
            System.out.println("old " + oldWidth + "/" + oldHeight + "; max " + maxWidth + "/" + maxHeight + "-- widthOversize=" + widthOversize + ", heightOversize=" + heightOversize);
        }
        if (widthOversize && heightOversize) {
            if (DEBUG) {
                System.out.println("ImageUtil: width+height oversize");
            }
            if (oldWidth < oldHeight) {
                if (DEBUG) {
                    System.out.println("ImageUtil: height oversize dominates");
                }
                newHeight = maxHeight;
                newWidth = (int) (oldWidth * ((double) maxHeight / oldHeight));
            } else {
                if (DEBUG) {
                    System.out.println("ImageUtil: width oversize dominates");
                }
                newWidth = maxWidth;
                newHeight = (int) (oldHeight * ((double) maxWidth / oldWidth));
            }
        } else if (heightOversize) {
            if (DEBUG) {
                System.out.println("ImageUtil: only height oversize");
            }
            newHeight = maxHeight;
            newWidth = (int) (oldWidth * ((double) maxHeight / oldHeight));
        } else if (widthOversize) {
            if (DEBUG) {
                System.out.println("ImageUtil: only width oversize");
            }
            newWidth = maxWidth;
            newHeight = (int) (oldHeight * ((double) maxWidth / oldWidth));
        } else {
            if (DEBUG) {
                System.out.println("ImageUtil: image to small; reusing old width and height");
            }
            newHeight = oldHeight;
            newWidth = oldWidth;
        }
        if (DEBUG) {
            System.out.println("ImageUtil: new " + newWidth + "/" + newHeight);
        }
        return new PtWidthHeight(newWidth, newHeight);
    }
}
