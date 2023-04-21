package com.sun.pdfview;

import java.awt.geom.Rectangle2D;
import java.awt.Color;

public class ImageInfo {

    int width;

    int height;

    Rectangle2D clip;

    Color bgColor;

    public ImageInfo(int width, int height, Rectangle2D clip) {
        this(width, height, clip, Color.WHITE);
    }

    public ImageInfo(int width, int height, Rectangle2D clip, Color bgColor) {
        this.width = width;
        this.height = height;
        this.clip = clip;
        this.bgColor = bgColor;
    }

    @Override
    public int hashCode() {
        int code = (width ^ height << 16);
        if (clip != null) {
            code ^= ((int) clip.getWidth() | (int) clip.getHeight()) << 8;
            code ^= ((int) clip.getMinX() | (int) clip.getMinY());
        }
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ImageInfo)) {
            return false;
        }
        ImageInfo ii = (ImageInfo) o;
        if (width != ii.width || height != ii.height) {
            return false;
        } else if (clip != null && ii.clip != null) {
            return clip.equals(ii.clip);
        } else if (clip == null && ii.clip == null) {
            return true;
        } else {
            return false;
        }
    }
}
