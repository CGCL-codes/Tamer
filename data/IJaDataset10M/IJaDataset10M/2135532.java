package com.mebigfatguy.pixelle;

import java.awt.Color;
import java.awt.image.DataBuffer;

/**
 * an abstract class for evaluation pixels in arbitrary images by enforcing a
 * template pattern that derived classes implement per image format.
 */
public abstract class PixelleEval {

    protected PixelleImage srcImage;

    protected DataBuffer buffer;

    protected int width;

    protected int height;

    protected IndexOutOfBoundsOption ioobOption;

    protected ColorOutOfBoundsOption coobOption;

    private int selectionByte;

    /**
	 * create an evaluator for a specific image and options
	 * 
	 * @param image the image to evaluate
	 * @param iOption the out of bounds pixels option to use
	 * @param cOption TODO
	 */
    public PixelleEval(PixelleImage image, IndexOutOfBoundsOption iOption, ColorOutOfBoundsOption cOption) {
        srcImage = image;
        buffer = image.getBuffer();
        width = image.getWidth();
        height = image.getHeight();
        ioobOption = iOption;
        coobOption = cOption;
    }

    /**
	 * template method to get the red value at a specific x and y
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the pixel red value at the given coordinate
	 */
    public abstract double getRedValue(int x, int y);

    /**
	 * template method to get the green value at a specific x and y
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the pixel green value at the given coordinate
	 */
    public abstract double getGreenValue(int x, int y);

    /**
	 * template method to get the blue value at a specific x and y
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the pixel blue value at the given coordinate
	 */
    public abstract double getBlueValue(int x, int y);

    /**
	 * template method to get the transparency value at a specific x and y
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the pixel transparency value at the given coordinate
	 */
    public abstract double getTransparencyValue(int x, int y);

    /**
	 * retrieves the value at a specific index for a specific color, transparency or selection
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the pixel value at the given coordinate for the pixel specification
	 */
    public double getValue(int x, int y, char pixelSpec) {
        if (((x < 0) || (x >= width)) || ((y < 0) || (y >= height))) {
            switch(ioobOption) {
                case SpecifiedColor:
                    Color c = ioobOption.getColor();
                    switch(pixelSpec) {
                        case 'r':
                            return c.getRed();
                        case 'g':
                            return c.getGreen();
                        case 'b':
                            return c.getBlue();
                        case 't':
                            return 1.0;
                        case 's':
                        default:
                            return 0.0;
                    }
                case BorderColor:
                    if (x < 0) {
                        x = 0;
                    } else if (x >= width) {
                        x = width - 1;
                    }
                    if (y < 0) {
                        y = 0;
                    } else if (y >= height) {
                        y = height - 1;
                    }
                    break;
                case WrapColor:
                    x %= width;
                    y %= height;
                    break;
            }
        }
        switch(pixelSpec) {
            case 'r':
                return getRedValue(x, y);
            case 'g':
                return getGreenValue(x, y);
            case 'b':
                return getBlueValue(x, y);
            case 't':
                return getTransparencyValue(x, y);
            case 's':
                return getSelectionValue(x, y);
            default:
                return 0.0;
        }
    }

    /**
	 * sets the pixel value at the specified x, y
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param pixelSpec the component to set
	 * @param value the value to set
	 */
    public void setValue(int x, int y, char pixelSpec, double value) {
    }

    /**
	 * gets the width of the source image
	 * 
	 * @return the width of the image
	 */
    public int getWidth() {
        return width;
    }

    /**
	 * gets the height of the source image
	 * 
	 * @return the height of the image
	 */
    public int getHeight() {
        return height;
    }

    /**
	 * gets the selection value at specific index
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the selection value either 0 or 1
	 */
    private double getSelectionValue(int x, int y) {
        if ((x & 0x07) == 0) {
            selectionByte = srcImage.getSelectionByte(x >> 3, y);
        }
        int bitOffset = 1 << (x & 0x07);
        return ((selectionByte & bitOffset) == 0) ? 0 : 1;
    }

    /**
	 * sets the selection value at a specific index
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param value the selection value to use where 0 is off
	 */
    protected void setSelectionValue(int x, int y, double value) {
        int bit = x & 0x07;
        if (bit == 0) {
            selectionByte = 0;
        }
        if (value != 0.0) {
            int bitOffset = 1 << (7 - bit);
            selectionByte |= bitOffset;
        }
        if ((bit == 7) || (x == width)) {
            srcImage.setSelectionByte(x >> 3, y, selectionByte);
        }
    }

    /**
	 * adjust out of bounds colors by applying rules of ColorOutOfBoundsOption
	 * @param value the input color value
	 * @return the output color value
	 */
    protected double adjustColor(double value) {
        if ((value >= 0.0) && (value <= 255.0)) {
            return value;
        }
        switch(coobOption) {
            case Roll:
                {
                    int ival = (int) (value + 0.49) % 256;
                    return ival;
                }
            case Wave:
                {
                    int ival = (int) (value + 0.49);
                    int period = ival / 256;
                    if ((period & 0x01) != 0) {
                        if (ival > 0) {
                            ival = 255 - (ival + 1) & 0x00FF;
                        } else {
                            ival = ival & 0x00FF;
                        }
                    } else {
                        if (ival > 0) {
                            ival = ival & 0x00FF;
                        } else {
                            ival = 256 - ival & 0x00FF;
                        }
                    }
                    return ival;
                }
            case Clip:
            default:
                {
                    if (value < 0.0) {
                        return 0.0;
                    } else {
                        return 255.0;
                    }
                }
        }
    }
}
