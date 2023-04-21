package _api.alienfactory.javamappy;

import _api.alienfactory.javamappy.util.ParameterChecker;

public class PixelData {

    private int[] rawPixelData;

    private boolean hasTransparentPixels;

    private byte[] cmapPixelData;

    public PixelData(int[] rawPixelData, boolean hasTransparentPixels) {
        this(rawPixelData, new byte[0], hasTransparentPixels);
        cmapPixelData = null;
    }

    public PixelData(int[] rawPixelData, byte[] cmapPixelData, boolean hasTransparentPixels) {
        ParameterChecker.checkNotNull(rawPixelData, "rawPixelData");
        ParameterChecker.checkNotNull(cmapPixelData, "cmapPixelData");
        this.rawPixelData = rawPixelData;
        this.cmapPixelData = cmapPixelData;
        this.hasTransparentPixels = hasTransparentPixels;
    }

    public boolean hasTransparentPixels() {
        return hasTransparentPixels;
    }

    /**
	 * Returns image data as indexes into the ColourMap. <br>
	 * @return colour map pixel data
	 */
    public byte[] getCmapPixelData() {
        if (null == cmapPixelData) throw new IllegalStateException("ColourMap pixel data has not been initialised");
        return cmapPixelData;
    }

    /**
	 * Returns image data in 0xAARRGGBB format. <br>
	 **/
    public int[] getRawPixelData() {
        return rawPixelData;
    }
}
