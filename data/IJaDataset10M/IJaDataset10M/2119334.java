package AccordionLRACDrawer;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * Helper class stores a Java Image object, metadata regarding the image, and an RGBA converted
 * buffer of the image that can be drawn using the glDrawPixels() method.  
 * @author peter
 *
 */
public class rgbaImage {

    Image image;

    Buffer imageRGBA;

    int width;

    int height;

    public rgbaImage(String filename) {
        Image image = Toolkit.getDefaultToolkit().createImage(filename);
        MediaTracker tracker = new MediaTracker(new Canvas());
        tracker.addImage(image, 0);
        try {
            tracker.waitForAll();
        } catch (InterruptedException ie) {
        }
        height = image.getHeight(null);
        width = image.getWidth(null);
        WritableRaster raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, width, height, 4, null);
        ComponentColorModel colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8, 8 }, true, false, ComponentColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE);
        BufferedImage imageBuffer = new BufferedImage(colorModel, raster, false, null);
        Graphics2D g = imageBuffer.createGraphics();
        AffineTransform gt = new AffineTransform();
        gt.translate(0, height);
        gt.scale(1, -1d);
        g.transform(gt);
        g.drawImage(image, null, null);
        DataBufferByte dukeBuf = (DataBufferByte) raster.getDataBuffer();
        this.image = image;
        imageRGBA = ByteBuffer.wrap(dukeBuf.getData());
        g.dispose();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Buffer getImageRGBA() {
        return imageRGBA;
    }

    public void setImageRGBA(Buffer imageRGBA) {
        this.imageRGBA = imageRGBA;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
