package santa.nice.imaging;

import java.awt.image.BufferedImage;

/**
 * Color mapping: given a color map {@code F}, such that {@code c' = F(c)}, where
 * {@code c'} is the new color, {@code c} is the old color 
 * @author Santa
 */
public class SeparatedChannelColorMapping extends InPlaceFilter {

    int[] redMap = getIdenticalMapping();

    int[] greenMap = getIdenticalMapping();

    int[] blueMap = getIdenticalMapping();

    public SeparatedChannelColorMapping(int[] redMap, int[] greenMap, int[] blueMap) {
        if (redMap != null) this.redMap = redMap;
        if (greenMap != null) this.greenMap = greenMap;
        if (blueMap != null) this.blueMap = blueMap;
    }

    int[] getIdenticalMapping() {
        int[] mapping = new int[256];
        for (int i = 0; i < mapping.length; i++) mapping[i] = i;
        return mapping;
    }

    @Override
    public void applyInPlace(BufferedImage image) {
        runningFlag = true;
        int width = image.getWidth();
        int height = image.getHeight();
        int stride = width;
        int[] rgbArray = image.getRGB(0, 0, width, height, null, 0, stride);
        for (int row = 0; row < height && cancelFlag == false; row++) {
            for (int col = 0; col < width; col++) {
                int r = redMap[(rgbArray[row * stride + col] >> 16) & 0xFF];
                int g = greenMap[(rgbArray[row * stride + col] >> 8) & 0xFF];
                int b = blueMap[rgbArray[row * stride + col] & 0xFF];
                rgbArray[row * stride + col] = (r << 16) | (g << 8) | b;
            }
            progress = 100 * row / height;
        }
        image.setRGB(0, 0, width, height, rgbArray, 0, stride);
        cancelFlag = false;
        runningFlag = false;
    }

    public int[] getRedMap() {
        return redMap;
    }

    public int[] getGreenMap() {
        return greenMap;
    }

    public int[] getBlueMap() {
        return blueMap;
    }
}
