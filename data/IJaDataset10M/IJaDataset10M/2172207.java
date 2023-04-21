package org.freelords.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Display;
import org.freelords.util.io.loaders.ResourceReference;

/** Collection of various static helper functions to manipulate images.
  *  Flipping images, getting subimages, handling transparency, ...
  *
  * @author James Andrews
  */
public class ImageUtils {

    /** Load an image from an input stream.
	  * 
	  * @param device the device on which to create the image
	  * @param rr a stream wrapper that supplies the image data.
	  * @return the loaded image if loading was successful
	  * @throws IOException if loading was not successful
	  */
    public static Image loadImage(Device device, ResourceReference rr) throws IOException {
        InputStream is = rr.getInputStream();
        try {
            return new Image(device, is);
        } finally {
            is.close();
        }
    }

    /** Flips/Mirrors the image.
	  * 
	  * @param source the original image
	  * @param swtFlipDirection use SWT.HORIZONTAL for horizontal, SWT.VERTICAL for vertical flipping or both
	  * (bitwise) and flip twice
	  * @return the transformed image
	  */
    public static Image flip(Image source, int swtFlipDirection) {
        Image dest = new Image(source.getDevice(), source.getBounds());
        GC gc = new GC(dest);
        gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
        gc.fillRectangle(0, 0, dest.getBounds().width, dest.getBounds().height);
        if (!gc.getAdvanced()) {
            gc.drawImage(source, 0, 0);
            gc.dispose();
            Image flipped = softwareFlip(dest, swtFlipDirection);
            dest.dispose();
            gc.dispose();
            return flipped;
        }
        Transform flip = new Transform(source.getDevice());
        int x = 0;
        int y = 0;
        switch(swtFlipDirection) {
            case SWT.HORIZONTAL:
                flip.scale(-1.0f, 1.0f);
                x = -source.getBounds().width;
                break;
            case SWT.VERTICAL:
                flip.scale(1.0f, -1.0f);
                y = -source.getBounds().height;
                break;
            case (SWT.VERTICAL | SWT.HORIZONTAL):
                flip.scale(-1.0f, -1.0f);
                x = -source.getBounds().width;
                y = -source.getBounds().height;
                break;
            default:
                throw new IllegalArgumentException("Flip direction must be SWT.HORIZONTAL, SWT.VERTICAL or both");
        }
        gc.setTransform(flip);
        gc.drawImage(source, x, y);
        gc.dispose();
        flip.dispose();
        return dest;
    }

    /** Internal redirect to softwareFlip(image, boolean) */
    private static Image softwareFlip(Image source, int swtFlipDirection) {
        switch(swtFlipDirection) {
            case SWT.HORIZONTAL:
                return new Image(source.getDevice(), softwareFlip(source.getImageData(), false));
            case SWT.VERTICAL:
                return new Image(source.getDevice(), softwareFlip(source.getImageData(), true));
            case (SWT.VERTICAL | SWT.HORIZONTAL):
                return new Image(source.getDevice(), softwareFlip(softwareFlip(source.getImageData(), true), false));
            default:
                throw new IllegalArgumentException("Flip direction must be SWT.HORIZONTAL, SWT.VERTICAL or both");
        }
    }

    /**
	 * Flips without need for DirectX/OpenGL
	 * taken from http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/Rotateandflipanimage.htm
	 */
    private static ImageData softwareFlip(ImageData srcData, boolean vertical) {
        int bytesPerPixel = srcData.bytesPerLine / srcData.width;
        int destBytesPerLine = srcData.width * bytesPerPixel;
        byte[] newData = new byte[srcData.data.length];
        for (int srcY = 0; srcY < srcData.height; srcY++) {
            for (int srcX = 0; srcX < srcData.width; srcX++) {
                int destX = 0, destY = 0, destIndex = 0, srcIndex = 0;
                if (vertical) {
                    destX = srcX;
                    destY = srcData.height - srcY - 1;
                } else {
                    destX = srcData.width - srcX - 1;
                    destY = srcY;
                }
                destIndex = (destY * destBytesPerLine) + (destX * bytesPerPixel);
                srcIndex = (srcY * srcData.bytesPerLine) + (srcX * bytesPerPixel);
                System.arraycopy(srcData.data, srcIndex, newData, destIndex, bytesPerPixel);
            }
        }
        return new ImageData(srcData.width, srcData.height, srcData.depth, srcData.palette, destBytesPerLine, newData);
    }

    /** Loads an image from an input stream and handles errors.
	  * 
	  * This function always uses the current device for constructing the image.
	  *
	  * @param rr the input stream wrapper that supplies the data.
	  * @return the loaded image or null on error
	  */
    public static Image getOptionalImage(ResourceReference rr) {
        if (rr == null) {
            return null;
        }
        try {
            InputStream in = new BufferedInputStream(rr.getInputStream());
            try {
                return new Image(Display.getCurrent(), in);
            } finally {
                in.close();
            }
        } catch (IOException ioe) {
            return null;
        }
    }

    /** Cuts out a part of an image.
	  * 
	  * @param sourceImage the original image
	  * @param sourceRectangle the boundaries for the cutting
	  * @return the smaller part of the original image
	  */
    public static Image getSubImage(Image sourceImage, Rectangle sourceRectangle) {
        Image subImage = new Image(null, sourceRectangle.width, sourceRectangle.height);
        GC subImageFC = new GC(subImage);
        subImageFC.drawImage(sourceImage, sourceRectangle.x, sourceRectangle.y, sourceRectangle.width, sourceRectangle.height, 0, 0, sourceRectangle.width, sourceRectangle.height);
        subImageFC.dispose();
        return subImage;
    }

    /** Marks all white pixels as transparent.
	  * 
	  * @param sourceImage some image with white pixels
	  * @return the image with all white pixels replaced by transparent ones
	  */
    public static Image whiteToTransparent(Image sourceImage) {
        ImageData data = sourceImage.getImageData();
        int whitePixel = data.palette.getPixel(new RGB(255, 255, 255));
        data.transparentPixel = whitePixel;
        Image transparentIdeaImage = new Image(null, data);
        return transparentIdeaImage;
    }
}
