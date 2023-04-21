package com.jgraph.gaeawt.java.awt.image;

import java.util.Hashtable;

public class CropImageFilter extends ImageFilter {

    private final int X, Y, WIDTH, HEIGHT;

    public CropImageFilter(int x, int y, int w, int h) {
        X = x;
        Y = y;
        WIDTH = w;
        HEIGHT = h;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setProperties(Hashtable<?, ?> props) {
        Hashtable<Object, Object> fprops;
        if (props == null) {
            fprops = new Hashtable<Object, Object>();
        } else {
            fprops = (Hashtable<Object, Object>) props.clone();
        }
        String propName = "Crop Filters";
        String prop = "x=" + X + "; y=" + Y + "; width=" + WIDTH + "; height=" + HEIGHT;
        Object o = fprops.get(propName);
        if (o != null) {
            if (o instanceof String) {
                prop = (String) o + "; " + prop;
            } else {
                prop = o.toString() + "; " + prop;
            }
        }
        fprops.put(propName, prop);
        consumer.setProperties(fprops);
    }

    @Override
    public void setPixels(int x, int y, int w, int h, ColorModel model, int[] pixels, int off, int scansize) {
        if (x + w < X || X + WIDTH < x || y + h < Y || Y + HEIGHT < y) {
            return;
        }
        int destX, destY, destWidth, destHeight, endX, endY, srcEndX, srcEndY;
        int newOffset = off;
        endX = X + WIDTH;
        endY = Y + HEIGHT;
        srcEndX = x + w;
        srcEndY = y + h;
        if (x <= X) {
            destX = 0;
            newOffset += X;
            if (endX >= srcEndX) {
                destWidth = srcEndX - X;
            } else {
                destWidth = WIDTH;
            }
        } else {
            destX = x - X;
            if (endX >= srcEndX) {
                destWidth = w;
            } else {
                destWidth = endX - x;
            }
        }
        if (y <= Y) {
            newOffset += scansize * (Y - y);
            destY = 0;
            if (endY >= srcEndY) {
                destHeight = srcEndY - Y;
            } else {
                destHeight = HEIGHT;
            }
        } else {
            destY = y - Y;
            if (endY >= srcEndY) {
                destHeight = h;
            } else {
                destHeight = endY - y;
            }
        }
        consumer.setPixels(destX, destY, destWidth, destHeight, model, pixels, newOffset, scansize);
    }

    @Override
    public void setDimensions(int w, int h) {
        consumer.setDimensions(WIDTH, HEIGHT);
    }
}
