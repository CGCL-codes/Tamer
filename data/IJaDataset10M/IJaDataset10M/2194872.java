package jp.ac.tokyo_ct.asteragy;

import com.nttdocomo.ui.Graphics;
import com.nttdocomo.ui.Image;

class ImagePixels {

    private final int width;

    private final int[] pixels;

    private int position = 0;

    ImagePixels(Image winner) {
        this.width = winner.getWidth();
        Graphics g = winner.getGraphics();
        pixels = g.getRGBPixels(0, 0, width, winner.getHeight(), null, 0);
        g.dispose();
    }

    int getWidth() {
        return width;
    }

    boolean isLeftPixels() {
        if (position != 0) return position % width == 0; else return false;
    }

    boolean hasMoreElements() {
        return position < pixels.length;
    }

    int nextElement() {
        return pixels[position++];
    }
}
