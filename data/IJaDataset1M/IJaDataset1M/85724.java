package org.designerator.media.image.preview;

import org.designerator.image.quantize.Wu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class IndexedImage {

    public static Image createIndexedImage(ImageData source, int nColors, Display display) {
        if (nColors < 2 || nColors > 256) {
            SWT.error(SWT.ERROR_INVALID_ARGUMENT);
        }
        ImageData imageData = source;
        RGB[] rgb = new RGB[nColors];
        byte[] indexpixel = new byte[imageData.width * imageData.height];
        Wu wu = new Wu(imageData.data, indexpixel, imageData.width, imageData.height, rgb, imageData.depth, null);
        wu.quantize();
        int i = 0;
        for (i = 0; i < rgb.length; i++) {
            if (rgb[i] == null) {
                break;
            }
        }
        if (i != rgb.length - 1) {
            RGB[] rgb2 = new RGB[i];
            for (int j = 0; j < rgb2.length; j++) {
                rgb2[j] = rgb[j];
            }
            rgb = rgb2;
        }
        ImageData imageData2 = IndexedImage.getIndexedImageData(rgb, indexpixel, imageData.width, imageData.height, false, 8);
        if (display == null) {
            display = Display.getCurrent();
        }
        Image image = new Image(display, imageData2);
        if (image != null) {
            return image;
        }
        return null;
    }

    /**
	 * @param rgb
	 *            max==256
	 * @param height
	 * @param width
	 * @param pixel(indexed)
	 *            usually 8 bit per pixel, can be null(empty image), padding
	 *            will be computed
	 * @param transparency -
	 *            if true, transparency==rgb[0]
	 * @param depth
	 *            usually 8
	 * @return ImageData
	 */
    public static ImageData getIndexedImageData(RGB[] rgb, byte[] pixel, int width, int height, boolean transparency, int depth) {
        if (!(depth == 8)) {
            SWT.error(SWT.ERROR_INVALID_ARGUMENT);
        }
        if (rgb == null || rgb.length > 256) {
            SWT.error(SWT.ERROR_INVALID_ARGUMENT);
        }
        if (pixel != null) {
            if ((depth == 8) && (height * width != pixel.length)) {
                SWT.error(SWT.ERROR_INVALID_ARGUMENT);
            }
        }
        PaletteData palette = new PaletteData(rgb);
        ImageData imageData = new ImageData(width, height, depth, palette);
        int bytesperline = imageData.bytesPerLine;
        if (pixel != null) {
            if (width * height != height * bytesperline) {
                int padding = bytesperline - width;
                byte[] data = imageData.data;
                int pos = 0, pos2 = 0;
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        data[pos] = pixel[pos2];
                        pos++;
                        pos2++;
                    }
                    pos += padding;
                }
            }
        }
        if (transparency) {
            imageData.transparentPixel = imageData.palette.getPixel(rgb[0]);
        }
        return imageData;
    }
}
