package javax.media.ding3d.loaders.lw3d;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * This class resizes an image to be the nearest power of 2 wide and high.
 * This facility now exists inside of the TextureLoader class, so
 * ImageScaler should be eliminated at some point.
 */
class ImageScaler {

    int origW, origH;

    Image origImage;

    ImageScaler(Image image, int w, int h) {
        origImage = image;
        origW = w;
        origH = h;
    }

    ImageScaler(BufferedImage image) {
        origImage = image;
        origW = image.getWidth();
        origH = image.getHeight();
    }

    /**
	* Utility method to return closes poser of 2 to the given integer
	*/
    int getClosestPowerOf2(int value) {
        if (value < 1) return value;
        int powerValue = 1;
        for (int i = 1; i < 20; ++i) {
            powerValue *= 2;
            if (value < powerValue) {
                int minBound = powerValue / 2;
                if ((powerValue - value) > (value - minBound)) return minBound; else return powerValue;
            }
        }
        return 1;
    }

    /**
	* Returns an Image that has been scaled from the original image to
	* the closest power of 2
	*/
    Image getScaledImage() {
        int newWidth = getClosestPowerOf2(origW);
        int newHeight = getClosestPowerOf2(origH);
        if (newWidth == origW && newHeight == origH) return origImage;
        Image scaledImage = null;
        if (origImage instanceof BufferedImage) {
            BufferedImage origImageB = (BufferedImage) origImage;
            scaledImage = new BufferedImage(newWidth, newHeight, origImageB.getType());
            BufferedImage scaledImageB = (BufferedImage) scaledImage;
            float widthScale = (float) origW / (float) newWidth;
            float heightScale = (float) origH / (float) newHeight;
            int origPixels[] = ((DataBufferInt) origImageB.getRaster().getDataBuffer()).getData();
            int newPixels[] = ((DataBufferInt) scaledImageB.getRaster().getDataBuffer()).getData();
            for (int row = 0; row < newHeight; ++row) {
                for (int column = 0; column < newWidth; ++column) {
                    int oldRow = Math.min(origH - 1, (int) ((float) (row) * heightScale + .5f));
                    int oldColumn = Math.min(origW - 1, (int) ((float) column * widthScale + .5f));
                    newPixels[row * newWidth + column] = origPixels[oldRow * origW + oldColumn];
                }
            }
        } else {
            scaledImage = origImage.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
        }
        return scaledImage;
    }
}
