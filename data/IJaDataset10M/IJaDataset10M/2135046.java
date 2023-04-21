package com.itstherules.image.buffered;

import java.awt.*;
import java.awt.image.*;

public class BicubicScaleFilter extends AbstractBufferedImageOp {

    private final int width;

    private final int height;

    public BicubicScaleFilter(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        if (dst == null) {
            ColorModel dstCM = src.getColorModel();
            dst = new BufferedImage(dstCM, dstCM.createCompatibleWritableRaster(width, height), dstCM.isAlphaPremultiplied(), null);
        }
        Graphics2D g = dst.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(src, 0, 0, width, height, null);
        g.dispose();
        return dst;
    }

    public String toString() {
        return "Bicubic Scale";
    }
}
