package com.itstherules.image.buffered;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public class FlipFilter extends AbstractBufferedImageOp {

    private Flip operation;

    public FlipFilter() {
        this(Flip.HORIZONTALLY_AND_VERTICALLY);
    }

    public FlipFilter(Flip operation) {
        this.operation = operation;
    }

    public void setOperation(Flip operation) {
        this.operation = operation;
    }

    public Flip getOperation() {
        return operation;
    }

    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();
        src.getType();
        src.getRaster();
        int[] inPixels = getRGB(src, 0, 0, width, height, null);
        int w = width;
        int h = height;
        int newW = w;
        int newH = h;
        switch(operation) {
            case HORIZONTALLY:
                break;
            case VERTICALLY:
                break;
            case HORIZONTALLY_AND_VERTICALLY:
                newW = h;
                newH = w;
                break;
            case CLOCKWISE_90_DEGREES:
                newW = h;
                newH = w;
                break;
            case COUNTER_CLOCKWISE_90_DEGREES:
                newW = h;
                newH = w;
                break;
            case OVER_180_DEGREES:
                break;
        }
        int[] newPixels = new int[newW * newH];
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                int index = row * width + col;
                int newRow = row;
                int newCol = col;
                switch(operation) {
                    case HORIZONTALLY:
                        newCol = w - col - 1;
                        break;
                    case VERTICALLY:
                        newRow = h - row - 1;
                        break;
                    case HORIZONTALLY_AND_VERTICALLY:
                        newRow = col;
                        newCol = row;
                        break;
                    case CLOCKWISE_90_DEGREES:
                        newRow = col;
                        newCol = h - row - 1;
                        ;
                        break;
                    case COUNTER_CLOCKWISE_90_DEGREES:
                        newRow = w - col - 1;
                        newCol = row;
                        break;
                    case OVER_180_DEGREES:
                        newRow = h - row - 1;
                        newCol = w - col - 1;
                        break;
                }
                int newIndex = newRow * newW + newCol;
                newPixels[newIndex] = inPixels[index];
            }
        }
        if (dst == null) {
            ColorModel dstCM = src.getColorModel();
            dst = new BufferedImage(dstCM, dstCM.createCompatibleWritableRaster(newW, newH), dstCM.isAlphaPremultiplied(), null);
        }
        dst.getRaster();
        setRGB(dst, 0, 0, newW, newH, newPixels);
        return dst;
    }

    public String toString() {
        switch(operation) {
            case HORIZONTALLY:
                return "Flip Horizontal";
            case VERTICALLY:
                return "Flip Vertical";
            case HORIZONTALLY_AND_VERTICALLY:
                return "Flip Diagonal";
            case CLOCKWISE_90_DEGREES:
                return "Rotate 90";
            case COUNTER_CLOCKWISE_90_DEGREES:
                return "Rotate -90";
            case OVER_180_DEGREES:
                return "Rotate 180";
        }
        return "Flip";
    }
}
