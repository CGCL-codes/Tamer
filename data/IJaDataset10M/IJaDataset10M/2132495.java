package i5d.cal;

import java.awt.*;
import java.awt.image.*;
import ij.process.*;

/** "Struct" for storing the display properties of a color channel (e.g. min, max )
 * @author Joachim Walter
 *
 */
public class ChannelDisplayProperties {

    private ColorModel colorModel;

    private double minValue;

    private double maxValue;

    private double minThreshold;

    private double maxThreshold;

    private int lutUpdateMode;

    private boolean displayedGray;

    private boolean displayedInOverlay;

    public ChannelDisplayProperties() {
        byte[] lut = new byte[256];
        for (int i = 0; i < 256; i++) {
            lut[i] = (byte) i;
        }
        colorModel = new IndexColorModel(8, 256, lut, lut, lut);
        minValue = 0d;
        maxValue = 255d;
        minThreshold = ImageProcessor.NO_THRESHOLD;
        maxThreshold = ImageProcessor.NO_THRESHOLD;
        displayedGray = false;
        displayedInOverlay = true;
        lutUpdateMode = ImageProcessor.RED_LUT;
    }

    public ColorModel getColorModel() {
        return colorModel;
    }

    public void setColorModel(ColorModel colorModel) {
        this.colorModel = colorModel;
    }

    public double getMaxThreshold() {
        return maxThreshold;
    }

    public void setMaxThreshold(double maxThreshold) {
        this.maxThreshold = maxThreshold;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public double getMinThreshold() {
        return minThreshold;
    }

    public void setMinThreshold(double minThreshold) {
        this.minThreshold = minThreshold;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public boolean isDisplayedGray() {
        return displayedGray;
    }

    public void setDisplayedGray(boolean displayGray) {
        this.displayedGray = displayGray;
    }

    public boolean isDisplayedInOverlay() {
        return displayedInOverlay;
    }

    public void setDisplayedInOverlay(boolean displayedInOverlay) {
        this.displayedInOverlay = displayedInOverlay;
    }

    public int getLutUpdateMode() {
        return lutUpdateMode;
    }

    public void setLutUpdateMode(int lutUpdateMode) {
        this.lutUpdateMode = lutUpdateMode;
    }

    public static IndexColorModel createModelFromColor(Color color) {
        byte[] rLut = new byte[256];
        byte[] gLut = new byte[256];
        byte[] bLut = new byte[256];
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        double rIncr = ((double) red) / 255d;
        double gIncr = ((double) green) / 255d;
        double bIncr = ((double) blue) / 255d;
        for (int i = 0; i < 256; ++i) {
            rLut[i] = (byte) (i * rIncr);
            gLut[i] = (byte) (i * gIncr);
            bLut[i] = (byte) (i * bIncr);
        }
        return new IndexColorModel(8, 256, rLut, gLut, bLut);
    }

    /** Returns a copy of the colorChannelProperties object.
     * The copy is a deep copy except for the colorModel, where only
     * the reference is copied. 
     * @return
     */
    public ChannelDisplayProperties copy() {
        ChannelDisplayProperties ccp = new ChannelDisplayProperties();
        ccp.setColorModel(getColorModel());
        ccp.setMinValue(getMinValue());
        ccp.setMaxValue(getMaxValue());
        ccp.setMinThreshold(getMinThreshold());
        ccp.setMaxThreshold(getMaxThreshold());
        ccp.setLutUpdateMode(getLutUpdateMode());
        ccp.setDisplayedGray(isDisplayedGray());
        ccp.setDisplayedInOverlay(isDisplayedInOverlay());
        return ccp;
    }
}
