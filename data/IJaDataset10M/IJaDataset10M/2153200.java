package shu.cms.image;

import java.io.*;
import java.awt.image.*;
import shu.math.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author cms.shu.edu.tw
 * @version 1.0
 */
public final class IntegerImage implements ImageInterface {

    public double[] getPixel(int x, int y, double dArray[]) {
        return bufferedImage.getRaster().getPixel(x, y, dArray);
    }

    public void setPixel(int x, int y, double dArray[]) {
        if (autoRationalize) {
            dArray = rationalize(dArray);
        }
        bufferedImage.getRaster().setPixel(x, y, dArray);
    }

    protected final double[] rationalize(double[] values) {
        double[] result = new double[3];
        for (int c = 0; c < 3; c++) {
            result[c] = rationalize(values[c]);
        }
        return result;
    }

    protected final double rationalize(double value) {
        value = value < 0 ? 0 : value;
        value = value > maxValue ? maxValue : value;
        return value;
    }

    protected final int rationalize(int value) {
        value = value < 0 ? 0 : value;
        value = value > maxValue ? maxValue : value;
        return value;
    }

    protected final int[] rationalize(int[] values) {
        int[] result = new int[3];
        for (int c = 0; c < 3; c++) {
            result[c] = rationalize(values[c]);
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        BufferedImage im = ImageUtils.loadImage("Image/WhiteBalance.tif");
        IntegerImage di = new IntegerImage(im);
        di.setAutoRationalize(true);
        di.normalize(new double[] { 58.45, 93.66, 235.66 }, 245);
        BufferedImage im2 = di.getBufferedImage();
        ImageUtils.storeTIFFImage("AfterWB.tif", im2);
    }

    public final Object clone() {
        BufferedImage bi = this.getBufferedImage();
        BufferedImage cloneBI = ImageUtils.cloneBufferedImage(bi);
        IntegerImage cloneDI = new IntegerImage(cloneBI, this.maxValue);
        return cloneDI;
    }

    protected BufferedImage bufferedImage;

    protected int maxValue;

    protected boolean autoRationalize = false;

    public IntegerImage(BufferedImage bufferedImage) {
        this(bufferedImage, 255, false);
    }

    public IntegerImage(int width, int height) {
        this(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
    }

    public void scale(int maxValue) {
        int w = this.getWidth();
        int h = this.getHeight();
        double factor = maxValue / this.maxValue;
        boolean isAutoRationalize = autoRationalize;
        autoRationalize = false;
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                tmp = this.getPixel(x, y, tmp);
                for (int c = 0; c < 3; c++) {
                    tmp[c] *= factor;
                }
                this.setPixel(x, y, tmp);
            }
        }
        autoRationalize = isAutoRationalize;
        this.maxValue = maxValue;
    }

    public IntegerImage(BufferedImage bufferedImage, int maxValue) {
        this(bufferedImage, maxValue, false);
    }

    /**
   *
   * @param bufferedImage BufferedImage
   * @param maxValue int code���̤j��
   * @param autoRationalize boolean �۰ʦX�z��
   */
    public IntegerImage(BufferedImage bufferedImage, int maxValue, boolean autoRationalize) {
        this.maxValue = maxValue;
        this.bufferedImage = bufferedImage;
        this.autoRationalize = autoRationalize;
    }

    public int getWidth() {
        return bufferedImage.getWidth();
    }

    public int getHeight() {
        return bufferedImage.getHeight();
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public boolean isAutoRationalize() {
        return autoRationalize;
    }

    public void setPixel(int x, int y, int iArray[]) {
        if (autoRationalize) {
            iArray = rationalize(iArray);
        }
        bufferedImage.getRaster().setPixel(x, y, iArray);
    }

    public void normalize(double[] normal) {
        double max = Maths.max(normal);
        normalize(normal, max);
    }

    public void normalize(double[] normal, double max) {
        int w = getWidth();
        int h = getHeight();
        double[] tmp = new double[3];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                getPixel(x, y, tmp);
                for (int ch = 0; ch < 3; ch++) {
                    tmp[ch] /= normal[ch];
                    tmp[ch] *= max;
                }
                setPixel(x, y, tmp);
            }
        }
    }

    public void setPixelR(int x, int y, int value) {
        setPixel(x, y, 0, value);
    }

    public void setPixelR(int x, int y, double value) {
        setPixel(x, y, 0, (int) value);
    }

    public void setPixel(int x, int y, int ch, int value) {
        bufferedImage.getRaster().setSample(x, y, ch, value);
    }

    public void setPixelG(int x, int y, int value) {
        setPixel(x, y, 1, value);
    }

    public void setPixelG(int x, int y, double value) {
        setPixel(x, y, 1, (int) value);
    }

    public void setPixelB(int x, int y, int value) {
        setPixel(x, y, 2, value);
    }

    public void setPixelB(int x, int y, double value) {
        setPixel(x, y, 2, (int) value);
    }

    protected int[] tmp = new int[3];

    public int getPixelR(int x, int y) {
        return getPixel(x, y, 0);
    }

    public int getPixelG(int x, int y) {
        return getPixel(x, y, 1);
    }

    public int getPixelB(int x, int y) {
        return getPixel(x, y, 2);
    }

    public int getPixel(int x, int y, int ch) {
        return bufferedImage.getRaster().getSample(x, y, ch);
    }

    public int[] getPixel(int x, int y, int iArray[]) {
        return bufferedImage.getRaster().getPixel(x, y, iArray);
    }

    public void setAutoRationalize(boolean autoRationalize) {
        this.autoRationalize = autoRationalize;
    }
}
