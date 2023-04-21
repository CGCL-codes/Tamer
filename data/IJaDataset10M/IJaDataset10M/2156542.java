package claw.coloureyes;

/**
 * Strategy for determining the overall colour of a range of pixels.
 * 
 */
public interface PixelDataToColourStrategy {

    /**
	 * Determines the overall colour of rgbPixels. The method of determining the
	 * colour value is implementation specific.
	 * 
	 * @param rgbPixels
	 *            range of pixels from which the colour will be determined
	 * @return overall colour of the pixel data, returned as RGB
	 */
    int getColourAsRgb(int[] rgbPixels);
}
