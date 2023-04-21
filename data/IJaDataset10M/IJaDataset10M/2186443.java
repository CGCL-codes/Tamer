package santa.nice.imaging;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ImagingUtility {

    public static final int IMAGE_TYPE = BufferedImage.TYPE_3BYTE_BGR;

    /**
	 * Clone an image by redrawing it
	 * @param img
	 */
    public static BufferedImage copyImage(BufferedImage img) throws OutOfMemoryError {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage image = new BufferedImage(w, h, IMAGE_TYPE);
        image.createGraphics().drawImage(img, new AffineTransformOp(new AffineTransform(), AffineTransformOp.TYPE_NEAREST_NEIGHBOR), 0, 0);
        return image;
    }

    /**
	 * Get the rgb from image. The {@code col} and {@code row}
	 * could be out of border, this function will return nearest
	 * color in border
	 * @param image
	 * @param row
	 * @param col
	 * @return
	 */
    public static int getRGBExtended(BufferedImage image, int row, int col) {
        int width = image.getWidth();
        int height = image.getHeight();
        row = Math.max(0, Math.min(height - 1, row));
        col = Math.max(0, Math.min(width - 1, col));
        return image.getRGB(col, row);
    }

    /**
	 * Gets the intensity of a given rgb
	 * @param rgb
	 * @return intensity in [0, 255]
	 */
    public static int getColorIntensity(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        return (r * 7471 + g * 38470 + b * 19595) >> 16;
    }
}
