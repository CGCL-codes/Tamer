package com.bluebrim.image.impl.shared;

import java.awt.color.ColorSpace;
import java.awt.Transparency;
import java.awt.image.*;

/**
 * NOTE: This class is not working and may not even be needed.
 *
 * PENDING: Fix this.
 *
 * @author Markus Persson 1999-11-09
 */
public class CoCMYKColorModel extends PackedColorModel {

    /**
 * Constructs a CoCMYKColorModel from the specified parameters.
 * Color components will be in the specified ColorSpace, which must
 * be of type ColorSpace.TYPE_CMYK.
 * The masks specify which bits in an int pixel representation contain
 * the cyan, magenta, yellow and black color samples. All of the bits
 * in each mask must be contiguous and fit in the specified number of
 * least significant bits of an int pixel representation.
 * The transparency value will always be Transparency.OPAQUE.
 * The transfer type is the type of primitive array used to represent
 * pixel values and must be one of DataBuffer.TYPE_BYTE,
 * DataBuffer.TYPE_USHORT, or DataBuffer.TYPE_INT.
 */
    public CoCMYKColorModel(ColorSpace space, int bits, int cMask, int mMask, int yMask, int kMask, int transferType) {
        super(space, bits, new int[] { cMask, mMask, yMask, kMask }, 0, false, Transparency.OPAQUE, transferType);
    }

    /**
 * We don't support alpha yet.
 */
    public int getAlpha(int pixel) {
        return 255;
    }

    /**
 * getBlue method comment.
 */
    public int getBlue(int pixel) {
        return 0;
    }

    /**
 * Returns an array of unnormalized color/alpha components given a pixel
 * in this ColorModel.  The pixel value is specified as an int.  If the
 * components array is null, a new array will be allocated.  The
 * components array will be returned.  Color/alpha components are
 * stored in the components array starting at offset (even if the
 * array is allocated by this method).  An ArrayIndexOutOfBoundsException
 * is thrown if  the components array is not null and is not large
 * enough to hold all the color and alpha components (starting at offset).
 */
    public final int[] getComponents(int pixel, int[] components, int offset) {
        int numComponents = getNumComponents();
        if (components == null) {
            components = new int[offset + numComponents];
        }
        return components;
    }

    /**
 * getGreen method comment.
 */
    public int getGreen(int pixel) {
        return 0;
    }

    /**
 * getRed method comment.
 */
    public int getRed(int pixel) {
        return 0;
    }
}
