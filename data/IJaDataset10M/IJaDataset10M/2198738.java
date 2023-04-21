package wotlas.libs.graphics2D.filter;

import java.awt.Color;
import java.awt.image.BufferedImage;
import wotlas.libs.graphics2D.DynamicImageFilter;
import wotlas.libs.graphics2D.filter.color.Converter;

/** A DynamicImageFilter that can change the brightness of a BufferefImage.
 *
 * @author Petrus
 * @see wotlas.libs.graphics2D.DynamicImageFilter
 */
public class BrightnessFilter implements DynamicImageFilter {

    /** Color Type couples (source & target) for our color change.
     */
    private static byte[][] brightnessMask;

    private static int tilesize;

    private float brightness;

    /** To create a new filtered image from an image source.
     *
     * @param srcIm source BufferedImage we take our data from (not modified).
     * @return new BufferedImage constructed from the given image.
     */
    public BufferedImage filterImage(BufferedImage srcIm) {
        if (srcIm == null) return null;
        if (BrightnessFilter.brightnessMask == null || this.brightness == 0) return srcIm;
        int width = srcIm.getWidth();
        int height = srcIm.getHeight();
        BufferedImage dstIm = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < width; i++) for (int j = 0; j < height; j++) dstIm.setRGB(i, j, filterPixel(srcIm.getRGB(i, j)));
        return dstIm;
    }

    /** To set the brightness mask
     *
     * m_brightnessMask the gray scale brightness mask
     * m_tilesize cell size of the mask (in pixels)
     */
    public static void setBrightnessMask(byte[][] m_brightnessMask, int m_tilesize) {
        BrightnessFilter.brightnessMask = m_brightnessMask;
        BrightnessFilter.tilesize = m_tilesize;
    }

    /** To set the brightness parameter
     *
     * @param x x coordinate of pixel
     * @param y y coordinate of pixel    
     */
    public void setBrightness(float x, float y) {
        if (BrightnessFilter.brightnessMask == null) return;
        int xb = (int) (x / BrightnessFilter.tilesize);
        int yb = (int) (y / BrightnessFilter.tilesize);
        if (xb < 0) xb = 0;
        if (yb < 0) yb = 0;
        if (xb >= BrightnessFilter.brightnessMask.length) xb = BrightnessFilter.brightnessMask.length - 1;
        if (yb >= BrightnessFilter.brightnessMask[0].length) yb = BrightnessFilter.brightnessMask[0].length - 1;
        if (BrightnessFilter.brightnessMask != null) this.brightness = ((float) BrightnessFilter.brightnessMask[xb][yb]) / 255;
    }

    /** To filter a pixel according to our brightness value
     *  @param argb pixel color
     */
    private int filterPixel(int argb) {
        short alpha = Converter.getAlpha(argb);
        if (alpha == 0) return argb;
        short redIndex = Converter.getRed(argb);
        short blueIndex = Converter.getBlue(argb);
        short greenIndex = Converter.getGreen(argb);
        float[] hsbvals = new float[3];
        Color.RGBtoHSB(redIndex, greenIndex, blueIndex, hsbvals);
        float newBrightness = hsbvals[2] + this.brightness;
        if (newBrightness < 0) {
            newBrightness = 0;
        } else {
            if (newBrightness > 1) {
                newBrightness = 1;
            }
        }
        return Color.HSBtoRGB(hsbvals[0], hsbvals[1], newBrightness) | (alpha << 24);
    }
}
