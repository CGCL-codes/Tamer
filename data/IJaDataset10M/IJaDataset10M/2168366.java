package org.sulweb.awt;

import java.awt.image.*;

public class TransparentWhiteFilter extends RGBImageFilter {

    public TransparentWhiteFilter() {
        canFilterIndexColorModel = true;
    }

    public int filterRGB(int x, int y, int rgb) {
        if ((rgb & 0x00ffffff) == 0x00ffffff) return 0x00000000;
        return rgb;
    }
}
