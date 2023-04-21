package org.designerator.media.thumbs.two;

import java.io.File;
import org.designerator.image.algo.util.ImageConversion;
import org.designerator.media.image.util.IO;
import org.designerator.media.image.util.IconUtil;
import org.designerator.media.thumbs.ThumbIO;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

public class ThumbIO2 {

    private static final String THUMB_JPG = ".thumb.jpg";

    public static boolean qualityMode = true;

    public static Image createAndSaveThumbnail(File imageFile, File thumbDir, int thumbMaxWidth, int thumbMaxHeight, Display display, int quality, boolean iconthumb) {
        Image thumbNail = null;
        if (thumbDir != null && thumbDir.exists()) {
            File thumbFile = new File(thumbDir, imageFile.getName() + THUMB_JPG);
            if (thumbFile.exists()) {
                thumbNail = IO.loadImage(thumbFile.getAbsolutePath(), display, false, true);
            }
            if (thumbNail == null) {
                thumbNail = createThumbnailImage(imageFile.getAbsolutePath(), thumbMaxWidth, thumbMaxHeight, display, iconthumb);
                if (thumbNail != null) {
                    IO.saveImageSWT(thumbNail.getImageData(), thumbFile.getAbsolutePath(), SWT.IMAGE_JPEG, quality);
                } else {
                    thumbNail = createErrorImage(display);
                }
            }
        } else {
            thumbNail = createThumbnailImage(imageFile.getAbsolutePath(), thumbMaxWidth, thumbMaxHeight, display, iconthumb);
        }
        return thumbNail;
    }

    public static Image createThumbnailImage(String imagePath, int thumbMaxWidth, int thumbMaxHeight, Display display, boolean iconThumb) {
        Image image = IO.loadImage(imagePath, display, false, true);
        Image thumbNail = null;
        if (image != null) {
            Rectangle rect = image.getBounds();
            if (rect.width < thumbMaxWidth && rect.height < thumbMaxHeight) {
                if (iconThumb && rect.width < 17 && rect.height < 17) {
                    thumbNail = IconUtil.createIconThumbnailImage(image, rect, thumbMaxWidth, thumbMaxHeight, display);
                } else {
                    thumbNail = image;
                }
            } else {
                int interpolation = SWT.HIGH;
                if (!qualityMode) {
                    interpolation = (SWT.DEFAULT);
                }
                thumbNail = ImageConversion.createThumbnailImage(image, rect, thumbMaxWidth, thumbMaxHeight, display, interpolation);
                image.dispose();
            }
        }
        if (thumbNail == null) {
            thumbNail = createErrorImage(display);
        }
        return thumbNail;
    }

    public static Image createErrorImage(Display display) {
        return display.getSystemImage(SWT.ICON_ERROR);
    }
}
