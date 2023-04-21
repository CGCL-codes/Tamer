package org.vous.facelib.filters;

import java.awt.Color;
import org.vous.facelib.bitmap.Bitmap;
import org.vous.facelib.bitmap.PixelUtils;

public class DifferenceFilter implements IFilter {

    private static final int DefaultHighlightRGB = Color.WHITE.getRGB();

    private Bitmap mComparison;

    private int mHighlightRGB;

    public DifferenceFilter(Bitmap comparison) {
        this(comparison, new Color(DefaultHighlightRGB));
    }

    public DifferenceFilter(Bitmap comparison, Color highlightColor) {
        mComparison = comparison;
        mHighlightRGB = highlightColor.getRGB();
    }

    public int getHighlightRGB() {
        return mHighlightRGB;
    }

    public Bitmap apply(Bitmap source) {
        Bitmap dest = source.createCompatible();
        int[] sourcePixels = source.getPixels();
        int[] compPixels = mComparison.getPixels();
        int[] destPixels = sourcePixels;
        for (int i = 0; i < sourcePixels.length; i++) {
            if (PixelUtils.equals(sourcePixels[i], compPixels[i])) destPixels[i] = mHighlightRGB;
        }
        dest.setPixels(destPixels);
        return dest;
    }

    public String toString() {
        return "Difference filter";
    }
}
